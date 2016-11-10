package fj.swsk.cn.eqapp.map.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.subs.setting.M.ContactInfo;
import fj.swsk.cn.eqapp.subs.setting.M.ContactSearchArgs;

/**
 * Created by apple on 16/6/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ContactInfo> mContacts;
    private ContactSearchArgs mContactSearchArgs;
    public static interface OnContactItemClickListener{
        void onItemClick(View v,Object contactInfo);
    }
    public static interface  OnSearchItemClickListener{
        void onItemClick(View v,int position);
    }
    private OnContactItemClickListener mOnContactItemClickListener = null;
    private OnSearchItemClickListener mOnSearchItemClickListener   = null;
    public ContactsAdapter(Context context, List<ContactInfo> mContacts, ContactSearchArgs mContactSearchArgs) {
        this(context);
        this.mContacts = mContacts;
        this.mContactSearchArgs = mContactSearchArgs;
    }

    public ContactsAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            ((ContactItemViewHolder.contactSearchViewHolder) viewHolder).update(mContactSearchArgs);
        } else {
            ContactInfo item = mContacts.get(position - 1);
            switch (item.viewType) {
                case 1:
                    ((ContactItemViewHolder.ItemViewHolder) viewHolder).update(item);
                     break;
                case 2:
                    ((ContactItemViewHolder.ItemWithTitleViewHolder) viewHolder).update(item);
                    break;
                default:
                    return;
            }
        }
    }

    //@Override
    public int getItemCount() {
        return mContacts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return mContacts.get(position - 1).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = mLayoutInflater.inflate(R.layout.contract_search_view, viewGroup, false);
                view.findViewById(R.id.city_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSearchItemClickListener.onItemClick(v,0);
                    }
                });
                view.findViewById(R.id.district_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSearchItemClickListener.onItemClick(v,1);
                    }
                });
                view.findViewById(R.id.department_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSearchItemClickListener.onItemClick(v,2);
                    }
                });
                view.findViewById(R.id.job_post_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnSearchItemClickListener.onItemClick(v,3);
                    }
                });
                return new ContactItemViewHolder.contactSearchViewHolder(view);
            case 1:
                view = mLayoutInflater.inflate(R.layout.contract_item, viewGroup, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnContactItemClickListener.onItemClick(v,v.findViewById(R.id.name).getTag());
                    }
                });
                return new ContactItemViewHolder.ItemViewHolder(view);
            case 2:
                view = mLayoutInflater.inflate(R.layout.contract_item_withtitle, viewGroup, false);
                view.findViewById(R.id.contactItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mOnContactItemClickListener.onItemClick(v,
                                v.findViewById(R.id.name).getTag());
                    }
                });
                return new ContactItemViewHolder.ItemWithTitleViewHolder(view);
            default:
                return null;
        }
    }

    //@Override
    public long getItemId(int position) {
        if (position > 0) {
            return mContacts.get(position).Id;
        }
        return -1;
    }
    public void setOnContactItemClickListener(OnContactItemClickListener listener){
        mOnContactItemClickListener = listener;
    }
    public void setOnSearchItemClickListener(OnSearchItemClickListener listener){
        mOnSearchItemClickListener = listener;
    }
}
