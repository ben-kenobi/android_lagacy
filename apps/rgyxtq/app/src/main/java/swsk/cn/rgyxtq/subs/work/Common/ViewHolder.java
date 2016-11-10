package swsk.cn.rgyxtq.subs.work.Common;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import swsk.cn.rgyxtq.util.ResUtil;

/**
 * Created by apple on 16/3/1.
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    public int mPosition;
    public View mConvertView;
    public int resId;

    public ViewHolder(int position,int resid,LayoutInflater inflater){
        this.mViews=new SparseArray<View>();
        this.mPosition=position;
        mConvertView=inflater.inflate(resid,null);
        this.resId=resid;
        mConvertView.setTag(this);
    }

    public static ViewHolder get(View convertView,int layoutId,int position,LayoutInflater inflater){
        if(convertView==null){
            return new ViewHolder(position,layoutId,inflater);
        }else{
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.mPosition=position;
            return holder;

        }
    }


    public <T extends  View> T getView(int id){
        View view = mViews.get(id);
        if(view==null){
            view = mConvertView.findViewById(id);
            mViews.put(id,view);
        }
        return (T)view;
    }

    public ViewHolder setText(int id,String text){
        TextView tv=getView(id);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int id,int resid){
        ImageView view = getView(id);
        view.setImageResource(resid);
        return this;
    }
    public ViewHolder setImageBitmap(int id,Bitmap bm){
        ImageView view = getView(id);
        view.setImageBitmap(bm);
        return this;
    }
    public ViewHolder setImageDrawable(int id,Drawable dra){
        ImageView view = getView(id);
        view.setImageDrawable(dra);
        return this;
    }

    public ViewHolder setBackgroundColor(int id,int color){
        View view = getView(id);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int id,int bgres){
        View view = getView(id);
        view.setBackgroundResource(bgres);
        return this;
    }

    public ViewHolder setTextColor(int id,int txcolor){
        TextView view = getView(id);
        view.setTextColor(txcolor);
        return this;
    }
    public ViewHolder setTextColorRes(int id,int resid){
        TextView view = getView(id);
        view.setTextColor(ResUtil.getColor(resid));
        return this;
    }

    public ViewHolder setAlpha(int id,float value){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            getView(id).setAlpha(value);
        }else{
            AlphaAnimation alpha = new AlphaAnimation(value,value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(id).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int id,boolean visible){
        View view = getView(id);
        view.setVisibility(visible?View.VISIBLE: View.GONE);
        return this;
    }

    public ViewHolder linkify(int id){
        TextView view = getView(id);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface,int... ids){
        for(int id :ids){
            TextView view = getView(id);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags()| Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }


    public ViewHolder setProgress(int id,int progress){
        ProgressBar view = getView(id);
        view.setProgress(progress);
        return this;
    }


    public ViewHolder setProgress(int id,int pro,int max){
        ProgressBar view = getView(id);
        view.setProgress(pro);
        view.setMax(max);
        return this;
    }

    public ViewHolder setMax(int id,int max){
        ProgressBar view = getView(id);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int id,float rate){
        RatingBar view = getView(id);
        view.setRating(rate);
        return this;
    }

    public ViewHolder setRating(int id,float rating,int max){
        RatingBar view = getView(id);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }


    public ViewHolder setTag(int id,Object tag){
        View view = getView(id);
        view.setTag(tag);
        return this;
    }


    public ViewHolder setTag(int id,int key,Object tag){
        View view = getView(id);
        view.setTag(key,tag);
        return this;
    }
    public ViewHolder setChecked(int id,boolean checked){
        Checkable view =(Checkable)getView(id);
        view.setChecked(checked);
        return this;
    }



    public ViewHolder setOnClickListener(int id,View.OnClickListener listener){
        View view = getView(id);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int id,View.OnTouchListener listener){
        View view = getView(id);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int id,View.OnLongClickListener listener){
        View view = getView(id);
        view.setOnLongClickListener(listener);
        return this;
    }


}
