package com.abdulaleem.appcon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    EditText email,password,userName,confPass;
    FloatingActionButton signupBtn;
    FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    FirebaseStorage storage;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        email = view.findViewById(R.id.emailSignup);
        password = view.findViewById(R.id.passSignup);
        confPass = view.findViewById(R.id.confPass);
        userName = view.findViewById(R.id.nameSingup);
        signupBtn = view.findViewById(R.id.signupBtn);
        mAuth = FirebaseAuth.getInstance();




        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(emails);
                String emails = email.getText().toString();
                String pass = password.getText().toString();
                String conpas = confPass.getText().toString();
                String name = userName.getText().toString();
                if(emails.equals("")||pass.equals("")||conpas.equals("")||name.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if(pass.equals(conpas)){
                    mAuth.createUserWithEmailAndPassword(emails,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .setPhotoUri(Uri.parse("android.resource://com.abdulaleem.appcon/drawable/ic_outline_person_24"))
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });

                                        Intent intent = new Intent(getActivity().getApplicationContext(),HomeActivity.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity().getApplicationContext(),"Failed To Create User",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"Confirm Password must match",Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }


}