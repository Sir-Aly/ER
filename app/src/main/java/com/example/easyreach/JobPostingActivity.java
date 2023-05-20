package com.example.easyreach;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobPostingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText jTitle, jDesc, jReq, jSalary, jLoc;
    MaterialButton postJobBtn;

    Long MaxJobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);

        jTitle = findViewById(R.id.job_title_edit_text);
        jDesc = findViewById(R.id.job_description_edit_text);
        jReq = findViewById(R.id.job_requirements_edit_text);
        jSalary = findViewById(R.id.job_salary_edit_text);
        jLoc = findViewById(R.id.job_location_edit_text);
        postJobBtn = findViewById(R.id.post_job_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CollectionReference JobsRef = db.collection("Jobs");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String jobProviderUid = user.getUid();
        DocumentReference JobsDocRef =  JobsRef.document(jobProviderUid + "  Jobs");


        postJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the code for creating a new job ID and adding a new job to the "Jobs" collection here
                // For example:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference jobsRef = db.collection("Jobs");
                jobsRef.document("job_ids").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            long lastJobId = document.getLong("last_job_id");
                            long newJobId = lastJobId + 1;
                            String userID =  mAuth.getCurrentUser().getUid();
                            String Title = jTitle.getText().toString();
                            String Description = jDesc.getText().toString();
                            String Requirements = jReq.getText().toString();
                            String Location = jLoc.getText().toString();
                            String Salary = jSalary.getText().toString();
                            Map<String, Object> job = new HashMap<>();
                            job.put("job_id", newJobId);
                            job.put("jTitle",Title);
                            job.put("pUid", userID);
                            job.put("jDescription",Description);
                            job.put("jLocation", Location);
                            job.put("jRequirements", Requirements);
                            job.put("jSalary", Salary);
                            jobsRef.document(String.valueOf(newJobId)).set(job);
                            jobsRef.document("job_ids").update("last_job_id", newJobId);
                        }
                    }
                });
                // End of the code for creating a new job ID and adding a new job to the "Jobs" collection
            }
        });


    }
}