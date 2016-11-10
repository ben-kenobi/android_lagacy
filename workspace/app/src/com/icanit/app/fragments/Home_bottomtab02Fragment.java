package com.icanit.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.icanit.app.R;
import com.icanit.app.adapter.MyArrayAdapter;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;

public class Home_bottomtab02Fragment extends AbstractRadioBindFragment {
	private View self;
	private int resId=R.layout.fragment4home_02_searchpage;
	private EditText editText;
	private GridView gridView;
	private MyArrayAdapter maa;
	private DataService dataService;
	private String[] searchCategory;
	private ImageButton textDisposer,backButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			init();
			bindListeners();
			initGv();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return self;
	}

	private void init() throws AppException {
		self=getActivity().getLayoutInflater().inflate(resId,null,false);
		backButton = (ImageButton) self.findViewById(R.id.imageButton2);
		editText = (EditText) self.findViewById(R.id.editText1);
		gridView = (GridView) self.findViewById(R.id.gridView1);
		dataService = AppUtil.getServiceFactory().getDataServiceInstance(getActivity());
		textDisposer = (ImageButton) self.findViewById(R.id.imageButton1);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);

	}

	private void initGv() throws AppException {
		gridView.setAdapter(maa = new MyArrayAdapter(getActivity(),
				R.layout.item4gv_merchandize_cate_search, R.id.button1,
				searchCategory = dataService.getSearchCategory()));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				Toast.makeText(getActivity(),
						searchCategory[position], Toast.LENGTH_SHORT).show();
			}
		});
	}
}
