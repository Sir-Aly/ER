package com.example.easyreach;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class JobDetailActivity extends AppCompatActivity {

    private ImageView jobImageView;
    private TextView jobTitleTextView;
    private TextView jobRequirementsTextView;
    private TextView jobLocationTextView;
    private TextView jobDescriptionTextView;
    private TextView jobSalaryTextView, providerEmail;
    ImageButton pInfo;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        jobImageView = findViewById(R.id.jobImageView);
        jobTitleTextView = findViewById(R.id.jobTitleTextView);
        jobRequirementsTextView = findViewById(R.id.jobRequirementsTextView);
        jobLocationTextView = findViewById(R.id.jobLocationTextView);
        jobDescriptionTextView = findViewById(R.id.jobDescriptionTextView);
        jobSalaryTextView = findViewById(R.id.jobSalaryTextView);
        providerEmail = findViewById(R.id.providerEmailTextView);
        pInfo = findViewById(R.id.providerInfo);

        pInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String proMail = providerEmail.getText().toString();
                Intent intent = new Intent(JobDetailActivity.this, ProviderInfoActivity.class);
                intent.putExtra("providerInfo", proMail);
                startActivity(intent);
            }
        });

        providerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverEmail = providerEmail.getText().toString();
                composeEmail(receiverEmail);
            }
        });

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backAnimationView.playAnimation();
                onBackPressed();
            }
        });

        db = FirebaseFirestore.getInstance();

        // Retrieve the jobId from the intent extras
        String jobId = getIntent().getStringExtra("jobID");

        // Retrieve the job details from Firestore
        db.collection("Jobs")
                .document(jobId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Get the job data
                            String jobImage = document.getString("jobImage");
                            String jobTitle = document.getString("jTitle");
                            String jobRequirements = document.getString("jRequirements");
                            String jobLocation = document.getString("jLocation");
                            String jobDescription = document.getString("jDescription");
                            String jobSalary = document.getString("jSalary");
                            String providerMail = document.getString("pEmail");

                            // Set the job data to the views
                            Glide.with(getApplicationContext())
                                    .load(jobImage).placeholder(R.drawable.jobsbg)
                                    .into(jobImageView);
                            jobTitleTextView.setText(jobTitle);
                            jobRequirementsTextView.setText(jobRequirements);
                            jobLocationTextView.setText(jobLocation);
                            jobDescriptionTextView.setText(jobDescription);
                            jobSalaryTextView.setText(jobSalary);
                            providerEmail.setText(providerMail);
                        }
                    } else {
                        // Error handling
                    }
                });
    }
    private void composeEmail(String receiverEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + receiverEmail));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiverEmail});

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> emailApps = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (emailApps.isEmpty()) {
            // No email client found
            Toast.makeText(this, "No email client found on the device", Toast.LENGTH_SHORT).show();
        } else {
            // At least one email client found, open the email client
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
