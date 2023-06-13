package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomsActivity extends AppCompatActivity {
    private static final String TAG = "ChatRoomsActivity";

    private ListView listViewChatRooms;
    private List<String> documentIds;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        listViewChatRooms = findViewById(R.id.listViewChatRooms);
        documentIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, documentIds);
        listViewChatRooms.setAdapter(adapter);

        // Retrieve the document IDs from two different collections
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get documents from the first collection
        db.collection("Job Providers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Iterate through the document snapshots and add the document IDs to the list
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentIds.add(documentSnapshot.getId());
                            Log.d(TAG, "Document ID from collection2: " + documentIds);
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting documents from collection1.", e);
                    }
                });

        // Get documents from the second collection
        db.collection("Job Seekers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Iterate through the document snapshots and add the document IDs to the list
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentIds.add(documentSnapshot.getId());
                            Log.d(TAG, "Document ID from collection2: " + documentIds);
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting documents from collection2.", e);
                    }
                });
        listViewChatRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected document ID
                String selectedDocumentId = documentIds.get(position);

                Intent intent = new Intent(ChatRoomsActivity.this, AdminChatActivity.class);
                intent.putExtra("documentId", selectedDocumentId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(ChatRoomsActivity.this, MainActivity.class);
        startActivity(back);
    finish();
    }
}
