package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;
import com.sakkawy.medicare.View.MessageActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter  extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<User> mUserList;
    private Context mContext;

    public DoctorAdapter(List<User> mUserList, Context mContext) {
        this.mUserList = mUserList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_doctor_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUserList.get(i);
        viewHolder.tvName.setText(user.getUserName());
        if(mUserList.get(i).getImageUri() == null || mUserList.get(i).getImageUri().equals("default") ||mUserList.get(i).getImageUri().equals("")){
            viewHolder.profileImageView.setImageResource(R.mipmap.placeholder_acc_pic);
        }else{
            Glide.with(mContext).load(user.getImageUri()).into(viewHolder.profileImageView);
        }
        if(!user.getSpeciality().equals(""))
            viewHolder.tvSpciality.setText(user.getSpeciality());

        viewHolder.ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userId",user.getUserId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImageView;
        TextView tvName, tvSpciality;
        ImageView ivFollow , ivChat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.iv_profile_chat_image);
            tvName = itemView.findViewById(R.id.tv_name_chat);
            tvSpciality = itemView.findViewById(R.id.tv_speciality);
            ivFollow = itemView.findViewById(R.id.iv_follow);
            ivChat = itemView.findViewById(R.id.iv_chat);
        }
    }
}
