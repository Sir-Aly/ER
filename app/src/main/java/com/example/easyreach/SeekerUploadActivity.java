package com.example.easyreach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SeekerUploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload;
    private Button selectImageButton;
    private ImageView imagePreview;
    private static final String TAG = "SeekerUploadActivity";
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_upload);
        selectImageButton = findViewById(R.id.seeker_select_image_button);
        imagePreview = findViewById(R.id.seeker_image_preview);
        btnUpload = findViewById(R.id.seeker_btn_Upload);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePreview.setImageURI(imageUri);
        }
    }
    private void uploadImage() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imagesRef = storageRef.child( userId + "/profile_picture");

        imagesRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                String imageUrl = downloadUrl.toString();
                                // Save the image URL to Firestore
                                saveImageUrlToFirestore(imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast
                                .makeText(SeekerUploadActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    private void saveImageUrlToFirestore(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        CollectionReference jobSeekersRef = db.collection("JS");
        Query query = jobSeekersRef.whereEqualTo("uid", userId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference currentUserRef = jobSeekersRef.document(document.getId());
                        Map<String, Object> data = new HashMap<>();
                        data.put("profileUrl", imageUrl);
                        currentUserRef.set(data, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Profile URL added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding profile URL", e);
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });




//        Map<String, Object> data = new HashMap<>();
//        data.put("profilePictureUrl", imageUrl);
//
//        db.collection("JS").document(userId)
//                .set(data, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast
//                                .makeText(SeekerUploadActivity.this,
//                                        "Image Uploaded!!",
//                                        Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast
//                                .makeText(SeekerUploadActivity.this,
//                                        "Failed " + e.getMessage(),
//                                        Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent b = new Intent(this, SeekerFillActivity.class);
        startActivity(b);
        finish();
    }
}
