package fj.swsk.cn.eqapp.subs.setting.C;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.map.VectorInfo;
import fj.swsk.cn.eqapp.map.adapter.VectorDownloadAdapter;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;

public class OfflineMapDownload extends BaseTopbarActivity
        implements AdapterView.OnItemClickListener {
    ListView mListView;
    List<VectorInfo> vectorData;
    VectorDownloadAdapter vectorDownloadAdapter;
    View current_expand = null;
    int current_expand_pos = -1;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                vectorDownloadAdapter = new VectorDownloadAdapter(OfflineMapDownload.this, vectorData);

                mListView.setAdapter(vectorDownloadAdapter);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_offline_map_download;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        mListView = (ListView) findViewById(R.id.pinnedlv);
        mListView.setOnItemClickListener(this);
        vectorData = new ArrayList<>();
        initView();
    }

    private void initView() {
        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                String[] vectorNameArray = new String[]{
                        getResources().getString(R.string.gas_station_name),
                        getResources().getString(R.string.hydr_station_name),
                        getResources().getString(R.string.natgas_station_name),
                        getResources().getString(R.string.school_name),
                        getResources().getString(R.string.hospital_name),
                        getResources().getString(R.string.city_site_name),
                        getResources().getString(R.string.resettlement_region_name)
                };
                String[] vectorPathArray = new String[]{
                        getResources().getString(R.string.gas_station),
                        getResources().getString(R.string.hydr_station),
                        getResources().getString(R.string.natgas_station),
                        getResources().getString(R.string.school),
                        getResources().getString(R.string.hospital),
                        getResources().getString(R.string.city_site),
                        getResources().getString(R.string.resettlement_region)
                };
                String state1 = getResources().getString(R.string.download_state1);
                String state2 = getResources().getString(R.string.download_state2);
                if (HttpUtils.isNetworkAvailable(OfflineMapDownload.this)) {
                    initInternetVectorInfo(vectorNameArray,
                            vectorPathArray, state1, state2);
                } else {
                    initLocalVectorInfo(vectorNameArray,
                            vectorPathArray, state1, state2);
                }
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 从网络上获取生命线数据信息
     *
     * @param vectorNameArray
     * @param vectorPathArray
     * @param state1
     * @param state2
     * @return
     */
    private void initInternetVectorInfo(String[] vectorNameArray
            , String[] vectorPathArray, String state1, String state2) {
        Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(IConstants.lifelineLayerInfoUrl));
        int status = (int) map.get("status");
        if (status == 400) {
            String msg = (String) map.get("msg");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (status == 200) {
            ArrayList<LinkedHashMap> result = (ArrayList<LinkedHashMap>) map.get("data");
            String SDPath = FileUtils.getSDPath();
            for (int i = 0; i < result.size(); i++) {
                LinkedHashMap hashMap = result.get(i);
                String name = (String) hashMap.get("name");
                String url = (String) hashMap.get("url");
                for (int j = 0; j < vectorNameArray.length; j++) {
                    String name2 = vectorNameArray[j];
                    if (name2 == name) {
                        String filePath = SDPath + vectorPathArray[i];
                        addVectorItem(filePath, name, url, state1, state2);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 从本地获取生命线数据信息
     *
     * @param vectorNameArray
     * @param vectorPathArray
     * @param state1
     * @param state2
     * @return
     */
    private void initLocalVectorInfo(String[] vectorNameArray
            , String[] vectorPathArray, String state1, String state2) {
        String SDPath = FileUtils.getSDPath();
        for (int i = 0; i < vectorPathArray.length; i++) {
            String filePath = SDPath + vectorPathArray[i];
            String name = vectorNameArray[i];
            addVectorItem(filePath, name, null, state1, state2);
        }
    }

    private void addVectorItem(String filePath, String name, String url, String state1, String state2) {
        String state;
        if (FileUtils.isFileExists(filePath)) {
            state = state1;
        } else {
            state = state2;
        }
        vectorData.add(
                new VectorInfo(name, state, url, filePath)
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        View container = view.findViewById(R.id.vector_download_btn_con);
        if (container.getVisibility() == View.GONE) {
            container.setVisibility(View.VISIBLE);
        } else {
            container.setVisibility(View.GONE);
        }
        if (current_expand != null && current_expand != view) {
            current_expand.findViewById(R.id.vector_download_btn_con).setVisibility(View.GONE);
        }

        current_expand = view;
        current_expand_pos = position;
    }

    public void onDownloadVectorData(View view) {
        String url = (String) view.getTag();
    }

    public void onDeleteVectorData(View view) {
        String filePath = (String) view.getTag();
        File file = new File(filePath);
        FileUtils.deleteFileRecursively(file, true);
        vectorData.remove(current_expand_pos);
        vectorDownloadAdapter.setVecResInfo(vectorData);
        vectorDownloadAdapter.notifyDataSetChanged();
    }
}
