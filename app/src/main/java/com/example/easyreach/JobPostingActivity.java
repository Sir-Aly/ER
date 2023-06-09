package com.example.easyreach;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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
    TextView field;
    String jField;
    ImageView backBtn;
    EditText jTitle, jDesc, jReq, jSalary, jLoc;
    MaterialButton postJobBtn;
    String[] item = {"Web Developer", "AI Developer", "Data Scientist", "Accountant", "Graphic Designer"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private static final String TAG = "Job Posting Activity";

    Long MaxJobs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting);
        autoCompleteTextView = findViewById(R.id.job_auto_complete_textview);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        AutoCompleteTextView citySpinner = findViewById(R.id.cityAutoCompleteTextView);

        // Retrieve the list of Egyptian cities from resources
        String[] egyptianCities = getResources().getStringArray(R.array.egyptian_cities);

        // Create an ArrayAdapter with the Egyptian cities
        final ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.list_item, egyptianCities);

        // Set the ArrayAdapter as the adapter for the AutoCompleteTextView
        citySpinner.setAdapter(cityAdapter);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        citySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the city list based on the user's input
                String filter = s.toString().toLowerCase();
                cityAdapter.getFilter().filter(filter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed in this case
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                field = findViewById(R.id.field);
                field.setText(item);
                jField = field.getText().toString();
            }
        });

        jTitle = findViewById(R.id.job_title_edit_text);
        jDesc = findViewById(R.id.job_description_edit_text);
        jReq = findViewById(R.id.job_requirements_edit_text);
        jSalary = findViewById(R.id.job_salary_edit_text);
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


                String Address = citySpinner.getText().toString();
                String jTitleValue = jTitle.getText().toString();
                String jDescValue = jDesc.getText().toString();
                String jReqValue = jReq.getText().toString();
                String jSalaryValue = jSalary.getText().toString();
                String jLocValue = citySpinner.getText().toString();

                // Check if any of the mandatory fields are empty
                if (TextUtils.isEmpty(jTitleValue) || TextUtils.isEmpty(jDescValue) ||
                        TextUtils.isEmpty(jReqValue) || TextUtils.isEmpty(jSalaryValue) ||
                        TextUtils.isEmpty(jLocValue)) {
                    // Display an error message indicating the missing fields
                    Toast.makeText(JobPostingActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Prevent further action
                }
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
                            String Location = citySpinner.getText().toString();
                            String pEmail = mAuth.getCurrentUser().getEmail();
                            String ImageUrl = "https://firebasestorage.googleapis.com/v0/b/easyreach-1.appspot.com/o/jobsbg.jpg?alt=media&token=90f0f4b5-d7c0-451f-9a19-75b98da26c9c";
                            String Salary = jSalary.getText().toString();
                            Map<String, Object> job = new HashMap<>();
                            job.put("job_id", newJobId);
                            job.put("jTitle",Title);
                            job.put("jField", jField);
                            job.put("pUid", userID);
                            job.put("jDescription",Description);
                            job.put("jLocation", Location);
                            job.put("jRequirements", Requirements);
                            job.put("jSalary", Salary);
                            job.put("pEmail",pEmail);
                            job.put("JobImage", ImageUrl);
                            jobsRef.document(String.valueOf(newJobId)).set(job);
                            jobsRef.document("job_ids").update("last_job_id", newJobId);
                            Toast.makeText(JobPostingActivity.this, "Job offer Added successfully", Toast.LENGTH_SHORT).show();
                            jTitle.setText("");
                            jDesc.setText("");
                            jReq.setText("");
                            jSalary.setText("");
                            citySpinner.setText("");
                        }
                    }
                });
                // End of the code for creating a new job ID and adding a new job to the "Jobs" collection
                }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}