package com.example.easyreach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminMessagesActivity extends AppCompatActivity {

    private ListView listView;
    private AdminMessageAdapter adapter;
    private List<Users> userList;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_messages);

        // Initialize Firebase Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        listView = findViewById(R.id.listView);

        // Set up the list view
        userList = new ArrayList<>();
        adapter = new AdminMessageAdapter(this, userList);
        listView.setAdapter(adapter);

        // Set click listener for list view items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users selectedUser = userList.get(position);
                startChatActivity(selectedUser.getId(), selectedUser.getSenderName());
            }
        });

        // Retrieve users who sent messages to the admin
        getUsersWithMessages();
    }

    private void getUsersWithMessages() {
        CollectionReference messagesRef = firestore.collection("chats");
        Query query = messagesRef.whereEqualTo("adminUID", mAuth.getCurrentUser().getUid());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot) {
                        Users user = document.toObject(Users.class);
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                // Handle error
            }
        });
    }

    private void startChatActivity(String userID, String userName) {
        Intent intent = new Intent(AdminMessagesActivity.this, AdminChatActivity.class);
        intent.putExtra("adminUID", userID);
        intent.putExtra("senderName", userName);
        startActivity(intent);
    }
}
