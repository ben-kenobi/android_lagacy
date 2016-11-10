package swsk.cn.rgyxtq.subs.user.V;

import android.content.Context;

import java.util.List;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.subs.user.M.ChatMessage;
import swsk.cn.rgyxtq.subs.work.Common.ViewHolder;

/**
 * Created by apple on 16/3/11.
 */
public class ChatAdapter extends MultiItemCommonAdapter<ChatMessage> {
    public ChatAdapter(Context context, List<ChatMessage> datas) {
        super(context, datas, new MultiItemTypeSupport<ChatMessage>() {
            @Override
            public int getLayoutId(int position, ChatMessage chatMessage) {
                if(chatMessage.isComMeg()){
                    return R.layout.main_chat_from_msg;
                }else{
                    return R.layout.main_chat_send_msg;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getItemViewType(int position, ChatMessage chatMessage) {
                if(chatMessage.isComMeg())
                    return ChatMessage.RECIEVE_MSG;
                return ChatMessage.SEND_MSG;
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage) {
        if(holder.resId==R.layout.main_chat_from_msg){
           holder.setText(R.id.chat_from_content,chatMessage.getContent()).
                   setText(R.id.chat_from_name,chatMessage.getName())
                   .setImageResource(R.id.chat_from_icon,chatMessage.getIcon());
        }else if(holder.resId==R.layout.main_chat_send_msg){
            holder.setText(R.id.chat_send_content,chatMessage.getContent())
                    .setText(R.id.chat_send_name,chatMessage.getName())
                    .setImageResource(R.id.chat_send_icon,chatMessage.getIcon());
        }
    }
}
