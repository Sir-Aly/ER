package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobsInterestedListActivity extends AppCompatActivity {

    RecyclerView JobsRecyclerView;
    ArrayList<model_jobs> JobsDataList;

    FirebaseFirestore db;
    myadapter_jobs jobAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_interested_list);

        mAuth = FirebaseAuth.getInstance();
        JobsRecyclerView=(RecyclerView)findViewById(R.id.recycleView);
        JobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        JobsDataList=new ArrayList<>();
        jobAdapter=new myadapter_jobs(JobsDataList);
        JobsRecyclerView.setAdapter(jobAdapter);

        db=FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("user");
        String userID =  mAuth.getCurrentUser().getUid();
        users.document(userID).collection("Likes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            model_jobs obj=d.toObject(model_jobs.class);
                            JobsDataList.add(obj);
                        }
                        jobAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(JobsInterestedListActivity.this, SeekerMainActivity.class);
        startActivity(i);
    }
}