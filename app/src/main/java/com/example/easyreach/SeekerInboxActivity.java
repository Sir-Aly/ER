package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
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

public class SeekerInboxActivity extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    RecyclerView recview;
    TextView message_size;
    ArrayList<model_inbox> datalist_inbox ;
    FirebaseFirestore db;
    myadapter_inbox myadapter_inbox;
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_inbox);
        refreshLayout = findViewById(R.id.refreshlayout);
        message_size = findViewById(R.id.messages_size);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(SeekerInboxActivity.this, SeekerInboxActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist_inbox=new ArrayList<>();
        myadapter_inbox=new myadapter_inbox(datalist_inbox);
        recview.setAdapter(myadapter_inbox);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        db=FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("user");
        String userID =  mAuth.getCurrentUser().getUid();
        users.document(userID).collection("com.example.easyreach.Messages").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            model_inbox obj=d.toObject(model_inbox.class);
                            datalist_inbox.add(obj);
                            String size = String.valueOf(myadapter_inbox.getItemCount());
                            message_size.setText(size +" "+"NEW MESSAGES");
                        }
                        myadapter_inbox.notifyDataSetChanged();
                    }
                });
    }

    public void clear(View view){
        String userID =  mAuth.getCurrentUser().getUid();
        db.collection("user").document(userID).collection("Messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot :task.getResult()){

                    db.collection("user").document(userID).collection("Messages").document(snapshot.getId()).delete();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}