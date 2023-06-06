package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class jobs_viewer extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;


    RecyclerView recview;
    TextView message_size;
    ArrayList<JobOffer> datalist_jobs ;
    FirebaseFirestore db;
    JobsAdapter myadapter_jobs;
    private FirebaseAuth mAuth;
    private ImageButton btnMain, btnInterest;
    private ImageButton btnAddJob;
    private ImageButton btnInterestedList;
    ImageView  backBtn;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs_viewer);
//        message_size = findViewById(R.id.messages_size);
//       message_size.setText(getItemCounttt());


        btnInterest = findViewById(R.id.seeker_interest_list);
        btnInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interest = new Intent(jobs_viewer.this, JobsInterestedListActivity.class);
                startActivity(interest);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist_jobs=new ArrayList<>();
        myadapter_jobs =new JobsAdapter(datalist_jobs);
        recview.setAdapter(myadapter_jobs);

        btnMain = findViewById(R.id.btnMain);
//        btnAddJob = findViewById(R.id.btnAddJob);
        btnInterestedList = findViewById(R.id.btnListedJobs);


        backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backs = new Intent(jobs_viewer.this, SeekerMainActivity.class);
                startActivity(backs);
                CustomIntent.customType(jobs_viewer.this,"right-to-left");
            }
        });


        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(jobs_viewer.this, SeekerMainActivity.class);
                startActivity(i);
                CustomIntent.customType(jobs_viewer.this,"right-to-left");

            }
        });

//        btnAddJob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(jobs_viewer.this, JobPostingActivity.class);
//                startActivity(i);
//                CustomIntent.customType(jobs_viewer.this,"bottom-to-up");
//
//            }
//        });
        btnInterestedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(jobs_viewer.this, "You are currently at the Listed Jobs!", Toast.LENGTH_SHORT).show();
            }
        });

        //collections

        db=FirebaseFirestore.getInstance();
        CollectionReference Jobs = db.collection("Jobs");
        String userID =  mAuth.getCurrentUser().getUid();
        Jobs.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            JobOffer obj=d.toObject(JobOffer.class);
                            datalist_jobs.add(obj);
                        }
                        myadapter_jobs.notifyDataSetChanged();
                    }
                });

//        message_size.setText(datalist_jobs.size() +" "+"NEW MESSAGES");
//        message_size.setText(myadapter_jobs.getItemCount() +" "+"NEW MESSAGES");
    }

    public void clear(View view){
        String userID =  mAuth.getCurrentUser().getUid();
        db.collection("user").document(userID).collection("com.example.easyreach.Messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot :task.getResult()){

//                    db.collection("user/" + userID + "com.example.easyreach.Messages/").document(snapshot.getId()).delete();
                    db.collection("user").document(userID).collection("com.example.easyreach.Messages").document(snapshot.getId()).delete();
                }
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(jobs_viewer.this, MainActivity.class);
        startActivity(i);
    }
}