package com.abdulaleem.appcon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Thread thread = new Thread()
        {
            public void run()
            {
                try {
                    sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null)
                    {
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };thread.start();
}}