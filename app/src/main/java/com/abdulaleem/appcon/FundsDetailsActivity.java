package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abdulaleem.appcon.databinding.ActivityFundsDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FundsDetailsActivity extends AppCompatActivity {
    String title,descr,photo,status,goal,category,key;
    ArrayList<Progress> progressList;
    ActivityFundsDetailsBinding binding;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ProgressAdapter1 adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFundsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        progressList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        adapter = new ProgressAdapter1(getApplicationContext(),progressList);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        title = getIntent().getStringExtra("title");
        descr = getIntent().getStringExtra("descr");
        photo = getIntent().getStringExtra("photo");
        goal = getIntent().getStringExtra("goal");
        status = getIntent().getStringExtra("status");
        category = getIntent().getStringExtra("category");
        key = getIntent().getStringExtra("key");

        binding.statusDetails.setText(status);
        binding.titleDetails.setText(title);
        binding.descrDetails.setText(descr);
        binding.categoryDetails.setText(category);
        Glide.with(getApplicationContext()).load(photo)
                .placeholder(R.drawable.ic_baseline_photo_camera_24)
                .into(binding.imageDetails);
        recyclerView.setAdapter(adapter);
        database.getReference().child("progress").orderByChild("key").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Progress arrayList= snapshot1.getValue(Progress.class);
                    progressList.add(arrayList);

                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FundsDetailsActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        binding.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FundsDetailsActivity.this,PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}