package com.example.easyreach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Choose_Login_And_Reg extends AppCompatActivity {
private Button mLogin, mRegister,mLogout;
private FirebaseAuth mAuth;

SwitchCompat switchMode;
boolean nightMode;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_and_reg);

        switchMode = findViewById(R.id.switchMode);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);
        if (nightMode) {
        switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }
                editor.apply();
            }
        });

        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        mLogout = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();



        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Choose_Login_And_Reg.this, LoginActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Choose_Login_And_Reg.this, RegisterActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

            }
        });
    }
}