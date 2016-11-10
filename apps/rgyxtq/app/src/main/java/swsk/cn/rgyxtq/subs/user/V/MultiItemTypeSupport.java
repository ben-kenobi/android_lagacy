package swsk.cn.rgyxtq.subs.user.V;

/**
 * Created by apple on 16/3/11.
 */
public interface MultiItemTypeSupport<T>{
    int getLayoutId(int position,T t);
    int getViewTypeCount();
    int getItemViewType(int position,T t);
}
