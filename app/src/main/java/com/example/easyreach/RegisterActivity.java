package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail, mPassword, mName, mConfirmPassword;
    RadioGroup userTypeRadioGroup;
    private FirebaseAuth mAuth;
    private RadioButton mJobSeekerRadioButton, mJobProviderRadioButton;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private ImageView passwordVisibilityToggle, passwordConfirmVisibilityToggle;


    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        mJobSeekerRadioButton = findViewById(R.id.jobSeekerRadioButton);
        mJobProviderRadioButton = findViewById(R.id.jobProviderRadioButton);

        TextView existing = (TextView) findViewById(R.id.existing);
        mAuth = FirebaseAuth.getInstance();

        passwordVisibilityToggle = findViewById(R.id.passwordVisibilityToggle);

        passwordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
        passwordConfirmVisibilityToggle = findViewById(R.id.passwordConfirmVisibilityToggle);

        passwordConfirmVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConfirmPasswordVisibility();
            }
        });
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
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        // User is a job seeker
                                        Intent i = new Intent(RegisterActivity.this, SeekerMainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error checking user type
                                }
                            });
                }
            }
        };

        existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        mRegister = (Button) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
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
                final String pEmail = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                final Boolean tnc = checkBox.isChecked();

                if ( checkInputs(pEmail, name, password, tnc)&& validatePasswords()){

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
                                            Map<String, Object> userInfo = new HashMap<>();
                                            userInfo.put("name", name);
                                            userInfo.put("profileImageUrl", "default");
                                            currentUserDb.updateChildren(userInfo);
                                            mEmail.setText("");
                                            mName.setText("");
                                            mPassword.setText("");
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("pEmail", pEmail);
                                            user.put("pName", name);
                                            user.put("jobProvider", isJobProvider());

                                            Map<String, Object> seekerUser = new HashMap<>();
                                            seekerUser.put("sEmail", pEmail);
                                            seekerUser.put("sName", name);
                                            seekerUser.put("jobProvider", isJobProvider());
                                            // Store the user's information in the Firestore database
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            String userID = mAuth.getCurrentUser().getUid();
                                            if (isJobProvider()) {
                                                db.collection("Job Providers").document(userID).set(user);
                                                Toast.makeText(RegisterActivity.this, "Please Don't forget to fill your data when you login.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                db.collection("Job Seekers").document(userID).set(seekerUser);
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

    private boolean isJobProvider() {
        int checkedRadioButtonId = userTypeRadioGroup.getCheckedRadioButtonId();
        return checkedRadioButtonId == R.id.jobProviderRadioButton;
    }
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Hide password
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityToggle.setImageResource(R.drawable.baseline_visibility_off_24);
        } else {
            // Show password
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityToggle.setImageResource(R.drawable.baseline_visibility_24);
        }

        passwordVisible = !passwordVisible;

        // Move cursor to the end of the password field
        mPassword.setSelection(mPassword.getText().length());
    }

    private void toggleConfirmPasswordVisibility() {
        if (confirmPasswordVisible) {
            // Hide password
            mConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordConfirmVisibilityToggle.setImageResource(R.drawable.baseline_visibility_off_24);
        } else {
            // Show password
            mConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordConfirmVisibilityToggle.setImageResource(R.drawable.baseline_visibility_24);
        }

        confirmPasswordVisible = !confirmPasswordVisible;

        // Move cursor to the end of the password field
        mConfirmPassword.setSelection(mConfirmPassword.getText().length());
    }

    private boolean validatePasswords() {
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Password fields are empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords Doesn't match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Passwords are valid
        return true;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, Choose_Login_And_Reg.class);
        startActivity(i);
        finish();
    }
}