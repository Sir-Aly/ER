package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<MyUser> userList;


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String adminUserId; // Admin user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.userRecyclerView);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        adminUserId = mAuth.getCurrentUser().getUid();

        // Fetch the list of users who messaged the admin
        fetchUserList();

        // Set click listener for user items
        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MyUser user = userList.get(position);
                String chatRoomId = user.getChatRoomId();
                openChatRoom(chatRoomId);
            }
        });
    }


    private void fetchUserList() {
        db.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> userIds = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String userId = document.getId();
                            userIds.add(userId);
                        }
                        setupUserRecyclerView(userIds);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setupUserRecyclerView(List<String> userIds) {
        UserAdapter userAdapter = new UserAdapter(userIds, new UserAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);
    }
    private void startChat(String userId) {
        // Implement the logic to start a chat between the current user and the selected user
        // You can open a chat room activity and pass the necessary information (e.g., user IDs) to start the chat
    }



    private void openChatRoom(String chatRoomId) {
        // Start the chat room activity and pass the chat room ID
        Intent intent = new Intent(AdminActivity.this, ChatRoomActivity.class);
        intent.putExtra("chatRoomId", chatRoomId);
        startActivity(intent);
    }
}
