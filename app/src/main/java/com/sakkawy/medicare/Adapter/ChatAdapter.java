package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<User> mUserList;
    private Context mContext;
    boolean isChat;

    public ChatAdapter(Context mContext , List<User> mUserList , boolean isChat) {
        this.mUserList = mUserList;
        this.mContext = mContext;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = mUserList.get(i);
        viewHolder.nameTextView.setText(user.getUserName());
        if(mUserList.get(i).getImageUri() == null || mUserList.get(i).getImageUri().equals("default") ||mUserList.get(i).getImageUri().equals("")){
            viewHolder.profileImageView.setImageResource(R.mipmap.placeholder_acc_pic);
        }else{
            Glide.with(mContext).load(user.getImageUri()).into(viewHolder.profileImageView);
        }

        if(isChat){
            if(user.getStatus().equals("online")){
                viewHolder.imgOn.setVisibility(View.VISIBLE);
                viewHolder.imgOff.setVisibility(View.GONE);
            }else{
                viewHolder.imgOff.setVisibility(View.VISIBLE);
                viewHolder.imgOn.setVisibility(View.GONE);
            }
        }else{
            viewHolder.imgOff.setVisibility(View.GONE);
            viewHolder.imgOn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImageView ,imgOn,imgOff;
        TextView nameTextView , msgTextView , dateTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.iv_profile_chat_image );
            nameTextView = itemView.findViewById(R.id.tv_name_chat);
            msgTextView = itemView.findViewById(R.id.tv_msg_chat);
            dateTextView = itemView.findViewById(R.id.tv_time);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);
        }
    }
}
