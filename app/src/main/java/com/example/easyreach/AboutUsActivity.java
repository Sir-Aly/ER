package com.example.easyreach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        ImageButton facebookButton = findViewById(R.id.facebook_text);
        ImageButton instagramButton = findViewById(R.id.instagram_text);
        ImageButton twitterButton = findViewById(R.id.twitter_text);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String facebookUrl = "https://www.facebook.com/aliabdelrahmanjr";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String instagramUrl = "https://www.instagram.com/mostofaly/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(instagramUrl));
                startActivity(intent);
            }
        });
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Facebook page in a browser
                String twitterUrl = "https://www.instagram.com/mostofaly/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });

    }
}