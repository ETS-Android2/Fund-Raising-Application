package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FundActivity extends AppCompatActivity {
    CardView education,humanity,medical,climate,other,raiseFund;
    String category = "",status = "";
    ImageView image;
    EditText title,description,goal;
    Switch aSwitch;
    Uri selectedImage;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        education = findViewById(R.id.education_add);
        humanity = findViewById(R.id.humanity_add);
        medical = findViewById(R.id.medical_add);
        climate = findViewById(R.id.climate_add);
        other = findViewById(R.id.other_add);
        aSwitch = findViewById(R.id.switch_status);
        raiseFund = findViewById(R.id.raise_fund);
        title = findViewById(R.id.title_f);
        description = findViewById(R.id.description);
        image = findViewById(R.id.title_fund);
        goal = findViewById(R.id.goal);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    status = "Active";
                }else{
                    status = "Disable";
                }
            }
        });

        raiseFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImage!=null && status.equals("Active") && !category.equals("")){
                    StorageReference reference = storage.getReference().child("Funds").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUri = uri.toString();
                                        String tit = title.getText().toString();
                                        String desc = description.getText().toString();
                                        String goa = goal.getText().toString();
                                        String uid = auth.getUid();
                                        String randomKey = database.getReference().push().getKey();
                                        Funds funds = new Funds(category,tit,status,desc,imageUri,goa,randomKey,uid);

                                        database.getReference()
                                                .child("funds")
                                                .push()
                                                .setValue(funds)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(FundActivity.this, "Fund Raised Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(FundActivity.this,HomeActivity.class);
                                                        startActivity(intent);

                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(FundActivity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();

                }
            }
        });
        System.out.println(status);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);

            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(education.getAlpha()==1)
                {
                    education.setAlpha((float) 0.4);
                    category = "education";
                }

                else{
                    education.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        humanity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humanity.getAlpha()==1)
                {
                    humanity.setAlpha((float) 0.4);
                    category = "humanity";
                }

                else{
                    humanity.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        climate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(climate.getAlpha()==1)
                {
                    climate.setAlpha((float) 0.4);
                    category = "climate";
                }

                else{
                    climate.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medical.getAlpha()==1)
                {
                    medical.setAlpha((float) 0.4);
                    category = "medical";
                }

                else{
                    medical.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(other.getAlpha()==1)
                {
                    other.setAlpha((float) 0.4);
                    category = "other";
                }

                else{
                    other.setAlpha((float) 1);
                    category = "";
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==25) {
            if (data != null) {
                if (data.getData() != null) {
                    selectedImage = data.getData();
                    image.setImageURI(selectedImage);
                }
            }
        }
    }
}