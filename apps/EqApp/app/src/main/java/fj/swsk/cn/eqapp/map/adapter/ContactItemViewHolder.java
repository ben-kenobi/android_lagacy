package fj.swsk.cn.eqapp.map.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.subs.setting.M.ContactInfo;
import fj.swsk.cn.eqapp.subs.setting.M.ContactSearchArgs;

/**
 * Created by apple on 16/6/16.
 */
public class ContactItemViewHolder {
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mobilePhone;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            mobilePhone = (TextView) itemView.findViewById(R.id.phone);
        }

        public void update(ContactInfo mContactInfo) {
            name.setText(mContactInfo.Name);
            mobilePhone.setText(mContactInfo.Phone);
            name.setTag(mContactInfo);
        }
    }

    public static class contactSearchViewHolder extends RecyclerView.ViewHolder {
        TextView department_name;
        TextView city_name;
        TextView job_post_name;
        TextView district_name;
        public contactSearchViewHolder(View itemView) {
            super(itemView);
            department_name = (TextView) itemView.findViewById(R.id.department_name);
            city_name = (TextView) itemView.findViewById(R.id.city_name);
            job_post_name = (TextView) itemView.findViewById(R.id.job_post_name);
            district_name = (TextView) itemView.findViewById(R.id.district_name);
        }

        public void update(ContactSearchArgs mContactSearchArgs) {
            district_name.setText(mContactSearchArgs.SubordinatedDistrict);
            city_name.setText(mContactSearchArgs.SubordinatedCity);
            job_post_name.setText(mContactSearchArgs.SubordinatedJobPost);
            department_name.setText(mContactSearchArgs.SubordinatedDepartment);
        }
    }

    public static class ItemWithTitleViewHolder extends RecyclerView.ViewHolder {
        TextView phone;
        TextView list_item_title;
        TextView name;

        public ItemWithTitleViewHolder(View itemView) {
            super(itemView);
            phone = (TextView) itemView.findViewById(R.id.phone);
            list_item_title = (TextView) itemView.findViewById(R.id.list_item_title);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void update(ContactInfo mContactInfo) {
            this.phone.setText(mContactInfo.Phone);
            this.name.setText(mContactInfo.Name);
            this.list_item_title.setText(mContactInfo.sork_key);
            name.setTag(mContactInfo);
        }
    }
}
