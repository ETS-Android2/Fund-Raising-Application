package com.abdulaleem.appcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private CallbackManager mCallBackManager;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginButton fbBtn;


    private static final String EMAIL = "email";
    private static final String TAG = "FacebookAuthentication";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fbBtn = (LoginButton) findViewById(R.id.fb_login);
        fbBtn.setReadPermissions("email","public_profile");
        bottomNavigationView = findViewById(R.id.bw);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        mCallBackManager = CallbackManager.Factory.create();


        fbBtn.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess" + loginResult);



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Insert your code here
                                handleFacebookToken(loginResult.getAccessToken());
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d(TAG,"onError" + e);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }

            }
        };




        FragmentTransaction loginTrans = getSupportFragmentManager().beginTransaction();
        loginTrans.replace(R.id.content,new LoginFragment());
        loginTrans.commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.login:
                        FragmentTransaction chatTrans = getSupportFragmentManager().beginTransaction();
                        chatTrans.replace(R.id.content,new LoginFragment());
                        chatTrans.commit();
                        break;
                    case R.id.signup:
                        FragmentTransaction contactTrans = getSupportFragmentManager().beginTransaction();
                        contactTrans.replace(R.id.content,new SignupFragment());
                        contactTrans.commit();
                        break;

                }
                return true;
            }
        });

    }

    private void handleFacebookToken(AccessToken accessToken) {

        Log.d(TAG,"onSuccess" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"sign in with credential: Successful" + accessToken);
                    Toast.makeText(LoginActivity.this,"Authentication Success",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null){
                        //createUser();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Log.d(TAG,"sign in with credential: Failure", task.getException());
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void createUser() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth =FirebaseAuth.getInstance();


        Uri uri = Uri.parse("android.resource://com.abdulaleem.appcon/drawable/ic_outline_person_24");
        StorageReference reference = storage.getReference().child("Profiles").child(Objects.requireNonNull(mAuth.getUid()));
        reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                            User user = new User(uid,"", email,imageUri);

                            database.getReference()
                                    .child("users")
                                    .child(uid)
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(LoginActivity.this,"User Created",Toast.LENGTH_LONG).show();

                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

}