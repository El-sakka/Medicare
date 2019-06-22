package com.sakkawy.medicare.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Model.Chat;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;
import com.sakkawy.medicare.View.MessageActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    boolean isChat;
    String theLastMsg;
    private List<User> mUserList;
    private Context mContext;

    public ChatAdapter(Context mContext, List<User> mUserList, boolean isChat) {
        this.mUserList = mUserList;
        this.mContext = mContext;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUserList.get(i);
        viewHolder.nameTextView.setText(user.getUserName());
        if (mUserList.get(i).getImageUri() == null || mUserList.get(i).getImageUri().equals("default") || mUserList.get(i).getImageUri().equals("")) {
            viewHolder.profileImageView.setImageResource(R.mipmap.placeholder_acc_pic);
        } else {
            Glide.with(mContext).load(user.getImageUri()).into(viewHolder.profileImageView);
        }

        if(isChat){
            lastMessage(user.getUserId(),viewHolder.msgTextView);
        }else{
            viewHolder.msgTextView.setVisibility(View.GONE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                viewHolder.imgOn.setVisibility(View.VISIBLE);
                viewHolder.imgOff.setVisibility(View.GONE);
            } else {
                viewHolder.imgOff.setVisibility(View.VISIBLE);
                viewHolder.imgOn.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgOff.setVisibility(View.GONE);
            viewHolder.imgOn.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

    private void lastMessage(final String userId, final TextView last_msg) {
        theLastMsg = "default";
        final String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(senderId) && chat.getSender().equals(userId) ||
                                chat.getReceiver().equals(userId) && chat.getSender().equals(senderId)) {
                            theLastMsg = chat.getMessage();
                        }
                    }

                    switch (theLastMsg) {
                        case "default":
                            last_msg.setText("No Message");
                            break;
                        default:
                            last_msg.setText(theLastMsg);
                            break;
                    }

                    theLastMsg = "default";

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImageView, imgOn, imgOff;
        TextView nameTextView, msgTextView, dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.iv_profile_chat_image);
            nameTextView = itemView.findViewById(R.id.tv_name_chat);
            msgTextView = itemView.findViewById(R.id.tv_msg_chat);
            dateTextView = itemView.findViewById(R.id.tv_time);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);
        }
    }
}
