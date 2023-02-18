package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.databinding.ActivityAuthenticationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity {
    ActivityAuthenticationBinding binding;
    String name, email, password;
    DatabaseReference  databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();
                Toast.makeText(AuthenticationActivity.this, "Clicked Login", Toast.LENGTH_SHORT).show();
                Login();
            }
        });
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.name.getText().toString();
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();

                Signup();
            }
        });
    }

    private void Login() {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("nikzdevz",e.toString());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Signup() {
            FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseUser.updateProfile(userProfileChangeRequest);
                            UserModel userModel=new UserModel(FirebaseAuth.getInstance().getUid(),name,email,password);
                            databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(userModel);
                            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                            finish();
                        }
                    });
//            use to check why onclick listener is not working
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("nikzdevz",e.toString());
//                        }
//                    });
        }
    }
