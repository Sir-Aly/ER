package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

        private Button mRegister;
        private ProgressBar spinner;
        private EditText mEmail, mPassword, mName, mBudget;


        private RadioGroup mRadioGroup;
        private boolean isJobProvider;
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//        private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);
        TextView existing = (TextView) findViewById(R.id.existing);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                spinner.setVisibility(View.VISIBLE);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent i = new Intent( RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    return;
                }
                spinner.setVisibility(View.GONE);
            }
        };

        existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });

        CheckBox jobProviderCheckbox = findViewById(R.id.job_provider_checkbox);
        jobProviderCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isJobProvider = isChecked;
            }
        });
        mRegister = (Button) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox1);
        TextView textView = (TextView) findViewById(R.id.TextView2);

        checkBox.setText("");
        textView.setText(Html.fromHtml("I have read and  agree to the " + " <a href = 'https://easyreach0.blogspot.com/p/conditions-by-downloading-or-using-app.html'> Terms & Conditions</a>"));
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);

                final String pEmail = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                final Boolean tnc = checkBox.isChecked();

                if ( checkInputs(pEmail, name, password, tnc)){

                    mAuth.createUserWithEmailAndPassword(pEmail, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())  {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Registered Successfully." + " Please Check your email for Verification. ", Toast.LENGTH_SHORT).show();
                                            String userId = mAuth.getCurrentUser().getUid();
                                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                                            Map userInfo = new HashMap<>();
                                            userInfo.put("name", name);
                                            userInfo.put("profileImageUrl", "default");
                                            currentUserDb.updateChildren(userInfo);

                                            mEmail.setText("");
                                            mName.setText("");
                                            mPassword.setText("");

                                            User user = new User(name, pEmail, isJobProvider);

                                            // Store the user's information in the Firestore database
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            String userID = mAuth.getCurrentUser().getUid();
                                            if (isJobProvider) {
                                                db.collection("Job Providers").document(userID).set(user);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Please Don't forget to fill your data when you login.", Toast.LENGTH_SHORT).show();
                                            }

                                            Intent i = new Intent(RegisterActivity.this, Choose_Login_And_Reg.class);
                                            startActivity(i);
                                            finish();
                                            return;

                                        } else {

                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                }



                    spinner.setVisibility(View.GONE);
            }
        });

    }

    private boolean checkInputs(String email, String username, String password, Boolean tnc) {
        if (email.equals("") || username.equals("")|| password.equals("")) {
            Toast.makeText(this, "All fields must be filled out properly", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Invalid Email address, please enter a valid email and click on confirm ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!tnc) {
            Toast.makeText(this, "Please accept our Terms and Conditions if you wish to Register ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, Choose_Login_And_Reg.class);
        startActivity(i);
        finish();

    }
}