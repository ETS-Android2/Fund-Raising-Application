package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    FirebaseAuth mAuth;
    TextView nameText;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<Funds> fundsList;
    FundsAdapter adapter;
    FirebaseDatabase database;
    CardView education,humanity,medical,climate,other;
    String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bw_home);
        database = FirebaseDatabase.getInstance();
        fundsList = new ArrayList<>();
        adapter = new FundsAdapter(getApplicationContext(),fundsList);
        recyclerView=findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        education = findViewById(R.id.education);
        humanity = findViewById(R.id.humanity);
        medical = findViewById(R.id.medical);
        climate = findViewById(R.id.climate);
        other = findViewById(R.id.other);




        nameText = findViewById(R.id.name_home);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();


                nameText.setText(name);



            }

        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profile:
                        Intent profileIntent = new Intent(HomeActivity.this,ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.add:
                        Intent addIntent = new Intent(HomeActivity.this,FundActivity.class);
                        startActivity(addIntent);
                        break;


                }
                return true;
            }
        });


        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(education.getAlpha()==1)
                {
                    education.setAlpha((float) 0.4);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "education";
                    onResume();
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 0.4);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "humanity";
                    recreate();
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 0.4);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "climate";
                    recreate();
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 0.4);
                    category = "medical";
                    recreate();
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 0.4);
                    medical.setAlpha((float) 1);
                    category = "other";
                    recreate();
                }

                else{
                    other.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        System.out.println(category);


            recyclerView.setAdapter(adapter);
            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();


                nameText.setText(name);



            }

        }

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(education.getAlpha()==1)
                {
                    education.setAlpha((float) 0.4);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "education";
                    onResume();
                }

                else{
                    education.setAlpha((float) 1);
                    category = "";
                    onResume();
                }

            }
        });

        humanity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(humanity.getAlpha()==1)
                {
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 0.4);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "humanity";
                    onResume();
                }

                else{
                    humanity.setAlpha((float) 1);
                    category = "";
                    onResume();
                }

            }
        });

        climate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(climate.getAlpha()==1)
                {
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 0.4);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
                    category = "climate";
                    onResume();
                }

                else{
                    climate.setAlpha((float) 1);
                    category = "";
                    onResume();
                }

            }
        });

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medical.getAlpha()==1)
                {
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 0.4);
                    category = "medical";
                    onResume();
                }

                else{
                    medical.setAlpha((float) 1);
                    category = "";
                    onResume();
                }

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(other.getAlpha()==1)
                {
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 0.4);
                    medical.setAlpha((float) 1);
                    category = "other";
                    onResume();
                }

                else{
                    other.setAlpha((float) 1);
                    category = "";
                    onResume();
                }

            }
        });

        System.out.println(category);
        recyclerView.setAdapter(adapter);
        if(category.equals("")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("education")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("education").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("climate")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("climate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("humanity")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("humanity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("medical")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("medical").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("other")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("other").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();


                nameText.setText(name);



            }

        }
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(education.getAlpha()==1)
                {
                    education.setAlpha((float) 0.4);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 0.4);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 0.4);
                    other.setAlpha((float) 1);
                    medical.setAlpha((float) 1);
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 1);
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
                    education.setAlpha((float) 1);
                    humanity.setAlpha((float) 1);
                    climate.setAlpha((float) 1);
                    other.setAlpha((float) 0.4);
                    medical.setAlpha((float) 1);
                    category = "other";
                }

                else{
                    other.setAlpha((float) 1);
                    category = "";
                }

            }
        });

        System.out.println(category);
        recyclerView.setAdapter(adapter);
        if(category.equals("")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("education")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("education").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("climate")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("climate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("humanity")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("humanity").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("medical")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("medical").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(category.equals("other")){

            String randomKey = database.getReference().push().getKey();
            database.getReference().child("funds").orderByChild("category").equalTo("other").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    fundsList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        Funds fund = snapshot1.getValue(Funds.class);
                        fundsList.add(fund);

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}