package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Job Providers").whereEqualTo("pEmail", user.getEmail()).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // User is a job provider
                                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                        return;
                                    } else {
                                        // User is a job seeker
                                        Intent i = new Intent(SplashScreenActivity.this, SeekerMainActivity.class);
                                        startActivity(i);
                                        finish();
                                        return;
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error checking user type
                                }
                            });

                } else {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashScreenActivity.this, Choose_Login_And_Reg.class);
                            startActivity(i);
                            finish();
                        }
                    }, 3000);
                }

            }
        };



    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthStateListener != null) {
            mAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }
}