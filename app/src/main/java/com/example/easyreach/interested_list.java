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

public class interested_list extends AppCompatActivity {

    RecyclerView recview;
    ArrayList<model> datalist;

    FirebaseFirestore db;
    myadapter adapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_list);
        mAuth = FirebaseAuth.getInstance();
        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adapter=new myadapter(datalist);
        recview.setAdapter(adapter);
        //collections

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
                            model obj=d.toObject(model.class);
                            datalist.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(interested_list.this, MainActivity.class);
        startActivity(i);
    }
}






























//    public void loadNote(View v) {
//        Likes.document("1IPH1gf15GddrBImlub2").get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        if (documentSnapshot.exists()) {
//                            String title = documentSnapshot.getString(KEY_TITLE);
//                            String description = documentSnapshot.getString(KEY_DESCRIPTION);
//                            TextView text1 = findViewById(R.id.text1);
//                            text1.setText("Title: " + title + "\n" + "Description: " + description);
//                        }
//                    }
//                });
//    }
//}

