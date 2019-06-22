package com.sakkawy.medicare.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Adapter.MessageAdapter;
import com.sakkawy.medicare.Model.Chat;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.Notifications.Client;
import com.sakkawy.medicare.Notifications.Data;
import com.sakkawy.medicare.Notifications.MyResponse;
import com.sakkawy.medicare.Notifications.Sender;
import com.sakkawy.medicare.Notifications.Token;
import com.sakkawy.medicare.R;
import com.sakkawy.medicare.Utlits.APIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    APIService apiService;
    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userId = getIntent().getStringExtra("userId");
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "onCreate: userId "+userId);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);



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

                notify = true;
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


    private void sendMessage(String senderId , final String reciverId , String message){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Chat");
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("sender",senderId);
        hashMap.put("receiver",reciverId);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        db.push().setValue(hashMap);

        final  String msg = message;
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(senderId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() >0){
                    User user = dataSnapshot.getValue(User.class);
                    if(notify) {
                        sendNotification(reciverId, user.getUserName(), msg);
                    }
                    notify = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String reciverId, final String userName, final String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = tokens.orderByKey().equalTo(reciverId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Token token = snapshot.getValue(Token.class);
                        Data data = new Data(senderId,R.mipmap.ic_launcher,userName+": "+msg,"New Message",userId);
                        Sender sender = new Sender(data,token.getToken());

                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if(response.code() == 200){
                                            if(response.body().success != 1){
                                                Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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


}
