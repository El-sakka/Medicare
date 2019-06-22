package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sakkawy.medicare.Model.Chat;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    private List<Chat> mChatList;
    private Context mContext;

    FirebaseUser fUser;

    public MessageAdapter(List<Chat> mChatList, Context mContext) {
        this.mChatList = mChatList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right_layout, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left_layout, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        Chat chat = mChatList.get(position);
        viewHolder.showMessage.setText(chat.getMessage());

        if(position == mChatList.size() -1 ){
            if(chat.isIsseen()){
                viewHolder.txt_seen.setText("Seen");
            }else{
                viewHolder.txt_seen.setText("Delivered");
            }
        }else{
            viewHolder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount()
    {
        return mChatList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView showMessage ,showDate , txt_seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showDate = itemView.findViewById(R.id.tv_time);
            showMessage = itemView.findViewById(R.id.text_msg);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
