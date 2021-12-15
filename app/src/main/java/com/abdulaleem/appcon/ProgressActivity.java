package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.abdulaleem.appcon.databinding.ActivityProgressBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProgressActivity extends AppCompatActivity {
    ActivityProgressBinding binding;
    private ArrayList<Uri> imageUris;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    private static final int PICK_IMAGE_CODE = 0;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageUris = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        String key = getIntent().getStringExtra("key");
        database.getReference().child("funds").orderByChild("key").equalTo("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<imageUris.size()-1){
                    position++;
                    binding.is.setImageURI(imageUris.get(position));
                }
                else {
                    Toast.makeText(ProgressActivity.this,"No More Images...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>0){
                    position--;
                    binding.is.setImageURI(imageUris.get(position));

                }
                else {
                    Toast.makeText(ProgressActivity.this,"No Previous Images...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=0;
                ArrayList<String> img = new ArrayList<>();
                int count=imageUris.size();

                for(int j=0;j<count;j++){
                   StorageReference reference = storage.getReference().child(System.currentTimeMillis() + "." + getFileExtension(imageUris.get(j))).child(auth.getUid());
                    reference.putFile(imageUris.get(j)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri = uri.toString();
                                        System.out.println(imageUri);
                                        String uid = auth.getUid();
                                        String email = auth.getCurrentUser().getEmail();
                                        Progress progress = new Progress(imageUri,key,uid);

                                        database.getReference()
                                                .child("progress")
                                                .push()
                                                .setValue(progress)


                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ProgressActivity.this,"Progress saved successfully",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                    System.out.println(j);
                }

            }
        });

        binding.is.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });

        binding.is.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });
    }

    public void pickImagesIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image(s)"),PICK_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    for(int i=0; i<count; i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    binding.is.setImageURI(imageUris.get(0));
                    position = 0;
                }
                else {
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    binding.is.setImageURI(imageUris.get(0));
                    position = 0;
                }
            }
        }
    }
    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
}