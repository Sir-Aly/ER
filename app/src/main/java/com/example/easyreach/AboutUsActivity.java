package com.example.easyreach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        ImageButton facebookButton = findViewById(R.id.facebook_text);
        ImageButton instagramButton = findViewById(R.id.instagram_text);
        ImageButton twitterButton = findViewById(R.id.twitter_text);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String facebookUrl = "https://twitter.com/easyreachDev";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String instagramUrl = "https://www.instagram.com/easyreachdev/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String twitterUrl = "https://twitter.com/easyreachDev";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}