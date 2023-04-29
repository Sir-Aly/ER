package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class Choose_between_2options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_between2options);
    }

    public void href_to_about_us(View view){
        Intent intent = new Intent(this,AboutUsActivity.class);
        startActivity(intent);
    }


    //change AboutUSActivity To Your Job Seeker Page
    public void href_to_job_seeker(View view) {
        Intent intent = new Intent(this, SeekerLoginActivity.class);
        startActivity(intent);
    }


    //change AboutUSActivity To Your Job Offerer Page
    public void href_to_job_offerer(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}