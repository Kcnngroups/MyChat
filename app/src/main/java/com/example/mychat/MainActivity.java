package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mychat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                  String uid=dataSnapshot.getKey();
                  if(!uid.equals(FirebaseAuth.getInstance().getUid())){
                      UserModel userModel=dataSnapshot.child(uid).getValue(UserModel.class);
                  }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}