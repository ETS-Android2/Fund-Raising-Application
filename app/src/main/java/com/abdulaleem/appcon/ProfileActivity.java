package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    FirebaseDatabase database;
    TextView profileName, profileEmail;
    FirebaseUser user;
    FloatingActionButton floatingActionButton;
    ImageView profilePhoto, back;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    String name,email;
    Uri selectedImage;
    Button logOut,myFunds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        database = FirebaseDatabase.getInstance();
        profileEmail = findViewById(R.id.email_profile);
        profileName = findViewById(R.id.name_profile);
        floatingActionButton = findViewById(R.id.add_photo);
        profilePhoto = findViewById(R.id.profile_photo);
        user = FirebaseAuth.getInstance().getCurrentUser();
        back = findViewById(R.id.back_profile);
        logOut = findViewById(R.id.logout);
        myFunds = findViewById(R.id.myFunds);

        myFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,MyFundsActivity.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(ProfileActivity.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                 name = profile.getDisplayName();
                 email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                profileName.setText(name);
                profileEmail.setText(email);


            }

        }
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    User user1 = snapshot1.getValue(User.class);
                    if(user1.getUid().equals(user.getUid())){
                        Glide.with(getApplicationContext()).load(user1.getProfileImage())
                                .placeholder(R.drawable.ic_baseline_photo_camera_24)
                                .into(profilePhoto);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);

            }
        });

        if(selectedImage!=null){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==25){
            if(data!=null)
            {
                if(data.getData()!=null){
                    selectedImage = data.getData();

                    user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(selectedImage)
                            .build();


                    assert user != null;
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this,"Photo Saved",Toast.LENGTH_LONG).show();
                                    }
                                    else{

                                    }
                                }
                            });
                    createUser(name,selectedImage,email);
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                profileName.setText(name);
                profileEmail.setText(email);
                profilePhoto.setImageURI(photoUrl);

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    User user1 = snapshot1.getValue(User.class);
                    if(user1.getUid().equals(user.getUid())){
                        Glide.with(getApplicationContext()).load(user1.getProfileImage())
                                .placeholder(R.drawable.ic_baseline_photo_camera_24)
                                .into(profilePhoto);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                profileName.setText(name);
                profileEmail.setText(email);
                profilePhoto.setImageURI(photoUrl);

            }

        }
    }



    private void createUser(String name,Uri image, String email) {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth =FirebaseAuth.getInstance();



        StorageReference reference = storage.getReference().child("Profiles").child(Objects.requireNonNull(mAuth.getUid()));
        reference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            String uid = mAuth.getUid();
                            String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                            User user = new User(uid,name, email,imageUri);

                            database.getReference()
                                    .child("users")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {


                                        }
                                    });
                        }
                    });
                }
            }
        });
    }
}