package com.example.easyreach;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfilePage extends AppCompatActivity {
    TextView pNameTv, nameTv, addressTv, ageTv, fieldTv, emailTv;
    private Button mForgetPassword;
    private boolean loginBtnClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        final String TAG = "problem";
//

        pNameTv= (TextView) findViewById(R.id.name);
        nameTv= (TextView) findViewById(R.id.uname);
        addressTv= (TextView) findViewById(R.id.address);
        ageTv= (TextView) findViewById(R.id.age);
        fieldTv= (TextView) findViewById(R.id.field);
        emailTv= (TextView) findViewById(R.id.email);
        mForgetPassword = (Button) findViewById(R.id.forgetPasswordButton);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, ForgetPasswordActivity.class  );
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("user").document(currentid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final String TAG = "problem";
                if (task.getResult().exists()){
                    String fullName= task.getResult().getString("Fname");
                    String uaddress = task.getResult().getString("Address");
                    String uage = task.getResult().getString("Age");
                    String ufield = task.getResult().getString("Field");
                    String umail = task.getResult().getString("Semail");

                    pNameTv.setText(fullName);
                    nameTv.setText(fullName);
                    addressTv.setText(uaddress);
                    ageTv.setText(uage);
                    fieldTv.setText(ufield);
                    emailTv.setText(umail);
                }
                else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(ProfilePage.this, MainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }


}
