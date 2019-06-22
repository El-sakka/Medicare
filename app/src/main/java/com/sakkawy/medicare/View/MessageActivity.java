package com.sakkawy.medicare.View;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.MessageAdapter;
import com.sakkawy.medicare.Model.Chat;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    String userId;
    View view;
    ImageView ivBackArrow ,ivAttachment,ivSendMsg;
    CircleImageView ivProfileView;
    TextView tvProfileName;
    EditText etMsg;
    DatabaseReference ref ;
    String senderId;


    MessageAdapter mMessageAdapter;
    List<Chat> mChatList;
    RecyclerView mRecyclerView;

    ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userId = getIntent().getStringExtra("userId");
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "onCreate: userId "+userId);

        mRecyclerView = findViewById(R.id.rv_message);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        View view = findViewById(R.id.header_chat_layout);
        ivBackArrow = view.findViewById(R.id.iv_back_arrow);
        ivProfileView =  view.findViewById(R.id.iv_header_profile_image);
        tvProfileName = view.findViewById(R.id.tv_name_header_chat);

        ivAttachment = findViewById(R.id.iv_attachment);
        ivSendMsg = findViewById(R.id.iv_send);
        etMsg = findViewById(R.id.et_message);





        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0 ){
                    User user = dataSnapshot.getValue(User.class);
                    if(!user.getImageUri().equals(""))
                        Glide.with(MessageActivity.this).load(user.getImageUri()).into(ivProfileView);
                    if(!user.getUserName().equals(""))
                        tvProfileName.setText(user.getUserName());

                    readMessage(senderId,userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ivSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMsg.getText().toString();
                if(!msg.equals("")){
                    sendMessage(senderId,userId,msg);
                }else{
                    etMsg.setError("Can't be Empty");
                }

                etMsg.setText("");
            }
        });

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        seenMessage(userId);

    }


    private void sendMessage(String senderId , String reciverId , String msg){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Chat");
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("sender",senderId);
        hashMap.put("receiver",reciverId);
        hashMap.put("message",msg);
        hashMap.put("isseen",false);

        db.push().setValue(hashMap);
    }


    private void readMessage(final String myId , final String userId){
        mChatList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("Chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatList.clear();
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                        chat.getSender().equals(myId) && chat.getReceiver().equals(userId)){
                            mChatList.add(chat);
                        }


                        mMessageAdapter = new MessageAdapter(mChatList,MessageActivity.this);
                        mRecyclerView.setAdapter(mMessageAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessage(final String userId){
        ref = FirebaseDatabase.getInstance().getReference().child("Chat");
        seenListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getReceiver().equals(senderId) && chat.getSender().equals(userId)){
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("isseen",true);
                            snapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("Status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(seenListener);
        status("offline");
    }
}
