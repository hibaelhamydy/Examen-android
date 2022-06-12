package com.hiba.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private TextView occupationTxtView, nameTxtView, workTxtView,passwordtxt;
    private Button deleteacc,updatepsd,logout;
    private TextView emailTxtView, phoneTxtView, videoTxtView, facebookTxtView, twitterTxtView;
    private ImageView userImageView, emailImageView, phoneImageView, videoImageView;
    private ImageView facebookImageView, twitterImageView;
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Map<String, String> userMap;
    private String email;
    private User utilisateur;
    private String userid;
    private static final String USERS = "users";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //receive data from login screen
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS);
        Log.v("USERID", userRef.getKey());

        occupationTxtView = findViewById(R.id.occupation_textview);
        nameTxtView = findViewById(R.id.name_textview);
        workTxtView = findViewById(R.id.workplace_textview);
        emailTxtView = findViewById(R.id.email_textview);
        phoneTxtView = findViewById(R.id.phone_textview);

        /*userImageView = findViewById(R.id.user_imageview);*/
        emailImageView = findViewById(R.id.email_imageview);
        phoneImageView = findViewById(R.id.phone_imageview);
        videoImageView = findViewById(R.id.phone_imageview);
        passwordtxt = findViewById(R.id.psd);
        logout=findViewById(R.id.logout);
        deleteacc=findViewById(R.id.deleteacc);
        updatepsd=findViewById(R.id.updatepsd);

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            String fname, mail, profession, workplace, phone;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        fname = keyId.child("fullName").getValue(String.class);
                        profession = keyId.child("profession").getValue(String.class);
                        workplace = keyId.child("workplace").getValue(String.class);
                        phone = keyId.child("phone").getValue(String.class);
                        break;
                    }
                }
                nameTxtView.setText(fname);
                emailTxtView.setText(email);
                occupationTxtView.setText(profession);
                workTxtView.setText(workplace);
                phoneTxtView.setText(phone);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



    public void logout(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();
        } else {
            // No user is signed in
            Log.w(TAG, "Failed to logout.");
    }
}

    public void delete(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User account deleted.");
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));

                }
            }
        });
        }

    public void update(View view) {
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(passwordtxt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Log.d(TAG, "User password updated.");
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                    Log.d(TAG, "login using new password.");
                    finish();

                }
            }
        });

    }}