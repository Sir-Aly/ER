package com.example.easyreach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class jobs_viewer extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;


    RecyclerView recview;
    TextView message_size;
    ArrayList<model_jobs> datalist_jobs ;
    FirebaseFirestore db;
    myadapter_jobs myadapter_jobs;
    private FirebaseAuth mAuth;









    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobs_viewer);
//        message_size = findViewById(R.id.messages_size);
//       message_size.setText(getItemCounttt());


        mAuth = FirebaseAuth.getInstance();
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist_jobs=new ArrayList<>();
        myadapter_jobs =new myadapter_jobs(datalist_jobs);
        recview.setAdapter(myadapter_jobs);



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
                            model_jobs obj=d.toObject(model_jobs.class);
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
        db.collection("user").document(userID).collection("Messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot :task.getResult()){

//                    db.collection("user/" + userID + "Messages/").document(snapshot.getId()).delete();
                    db.collection("user").document(userID).collection("Messages").document(snapshot.getId()).delete();
                }
            }
        });





    }


}