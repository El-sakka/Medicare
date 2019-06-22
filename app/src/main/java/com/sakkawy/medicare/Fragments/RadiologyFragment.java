package com.sakkawy.medicare.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sakkawy.medicare.Adapter.ImageAdapter;
import com.sakkawy.medicare.Model.Image;
import com.sakkawy.medicare.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadiologyFragment extends Fragment {

    private boolean fabExpanded = false;
    private FloatingActionButton fabAdd;
    private LinearLayout layoutOpenCamera;
    private LinearLayout layoutFabSaveImage;


    RecyclerView mRecyleView;
    ImageAdapter adapter;

    String parentFolder ;

    private Uri resultUri;
    StorageReference filePath;
    DatabaseReference dbImages;
    String imageUri = "default";

    String userId;

    List<Image> imageList = new ArrayList<>();


    public RadiologyFragment() {
        // Required empty public constructor
    }
    public RadiologyFragment(String parentFolder) {
        // Required empty public constructor
        this.parentFolder = parentFolder;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radiology, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fabAdd = getActivity().findViewById(R.id.fabMain);
        layoutOpenCamera = getActivity().findViewById(R.id.layoutFabSaveFolder);
        layoutFabSaveImage = getActivity().findViewById(R.id.layoutFabSaveImage);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRecyleView = getActivity().findViewById(R.id.rv_prescription);
        mRecyleView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        adapter = new ImageAdapter(getActivity(),imageList);
        mRecyleView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabExpanded == true){
                    closeSubMenusFab();
                }else{
                    openSubMenusFab();
                }
            }
        });

        closeSubMenusFab();

        layoutFabSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        layoutOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        readRadiology();

    }
    private void readRadiology() {

        dbImages = FirebaseDatabase.getInstance().getReference().child("Radiology");
        dbImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    imageList.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Image image = snapshot.getValue(Image.class);
                        if(image.getParentId().equals(parentFolder) && image.getUserId().equals(userId)){
                            imageList.add(image);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            saveImage();
        }
    }
    private void saveImage(){
        if(resultUri != null){
            filePath = FirebaseStorage.getInstance().getReference().child("Radiology");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

            byte[] data = baos.toByteArray();
            //upload image to database
            UploadTask uploadTask = filePath.putBytes(data);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        imageUri = downloadUri.toString();
                        Image image = new Image(userId,parentFolder,imageUri);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Radiology");
                        db.push().setValue(image).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"Done",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }
    }

    private void closeSubMenusFab(){
        layoutOpenCamera.setVisibility(View.INVISIBLE);
        layoutFabSaveImage.setVisibility(View.INVISIBLE);
        fabAdd.setImageResource(R.drawable.ic_add_24dp);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        layoutOpenCamera.setVisibility(View.VISIBLE);
        layoutFabSaveImage.setVisibility(View.VISIBLE);
        fabAdd.setImageResource(R.drawable.ic_done_24dp);
        fabExpanded = true;
    }
}
