package com.example.easyreach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class inbox_viewer extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<model_inbox> datalist_inbox;
    FirebaseFirestore db;
    myadapter_inbox myadapter_inbox;
    private FirebaseAuth mAuth;





    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_viewer);
        mAuth = FirebaseAuth.getInstance();
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist_inbox=new ArrayList<>();
        myadapter_inbox=new myadapter_inbox(datalist_inbox);
        recview.setAdapter(myadapter_inbox);
        //collections

        db=FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("user");
        String userID =  mAuth.getCurrentUser().getUid();
        users.document(userID).collection("Messages").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            model_inbox obj=d.toObject(model_inbox.class);
                            datalist_inbox.add(obj);
                        }
                        myadapter_inbox.notifyDataSetChanged();
                    }
                });
    }
}