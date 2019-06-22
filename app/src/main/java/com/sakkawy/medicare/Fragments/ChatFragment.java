package com.sakkawy.medicare.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sakkawy.medicare.Adapter.ChatAdapter;
import com.sakkawy.medicare.Model.Chat;
import com.sakkawy.medicare.Model.User;
import com.sakkawy.medicare.Notifications.Token;
import com.sakkawy.medicare.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private RecyclerView mRecyclerView;
    private ChatAdapter chatAdapter;
    private List<User> mUsers;
    private List<String> usersList;

    FirebaseUser fUser;
    DatabaseReference reference;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = getActivity().findViewById(R.id.rv_chat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList= new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    usersList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if(chat.getSender().equals(fUser.getUid())){
                            usersList.add(chat.getReceiver());
                        }
                        if(chat.getReceiver().equals(fUser.getUid())){
                            usersList.add(chat.getSender());
                        }
                    }

                    readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }


    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Token token1 = new Token(token);
        reference.child(fUser.getUid()).setValue(token1);
    }

    private void readChats() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    mUsers.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        for(String id : usersList){
                            if(user.getUserId().equals(id)){
                                if(mUsers.size() != 0){
                                    for(User user1 : mUsers){
                                        if(!user.getUserId().equals(user1.getUserId())){
                                            mUsers.add(user);
                                        }
                                    }
                                }else{
                                    mUsers.add(user);
                                }
                            }
                        }
                    }

                    chatAdapter = new ChatAdapter(getContext(),mUsers,true);
                    mRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
