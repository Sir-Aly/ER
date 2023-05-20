package com.example.easyreach;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class SeekerFillActivity extends AppCompatActivity {

    EditText firstName, age, eAddress, eField, userMail;
    TextView field;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload;
    private Button selectImageButton;
    private ImageView imagePreview;
    private Uri imageUri;
    String skills;
    MaterialButton RegisterBtn, SeekerUploadBtn;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String[] item = {"Web Developer", "AI Developer", "Data Scientist", "Accountant", "Graphic Designer"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;
    private static final String TAG = "SeekerFillActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_fill);
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                field = findViewById(R.id.field);
                field.setText(item);
                skills = field.getText().toString();
            }
        });
        db = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        userMail = findViewById(R.id.userMail);
        age = findViewById(R.id.age);
        eAddress = findViewById(R.id.address);
//        eField = findViewById(R.id.field);
        RegisterBtn = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

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
                                .makeText(SeekerFillActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    private void saveImageUrlToFirestore(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        CollectionReference jobSeekersRef = db.collection("JS").document("Field").collection(skills);
        Query query = jobSeekersRef.whereEqualTo("UID", userId);

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
                                        Toast.makeText(SeekerFillActivity.this, "Profile URL added successfully!", Toast.LENGTH_SHORT).show();
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

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID =  mAuth.getCurrentUser().getUid();
                String Firstname = firstName.getText().toString();
                String Email = userMail.getText().toString();
                String Age = age.getText().toString();
                String Address = eAddress.getText().toString();
//                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("name",Firstname);
                user.put("email", Email);
                user.put("age",Age);
                user.put("location", Address);
                user.put("skills", skills);
                user.put("UID", userID);
                CollectionReference graphicDesignerRef = db.collection("JS").document("Field").collection(skills);

                graphicDesignerRef.whereEqualTo("UID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) {

                                        graphicDesignerRef.document("Field_Id").get().addOnCompleteListener(tasks -> {
                                            if (tasks.isSuccessful()) {
                                                DocumentSnapshot document = tasks.getResult();
                                                if (document.exists()) {
                                                    long lastSeekerId = document.getLong("last_id");
                                                    long newSeekerId = lastSeekerId + 1;

                                                    Map<String, Object> Seeker = new HashMap<>();
                                                    Seeker.put("seeker_id", newSeekerId);
                                                    Seeker.put("name",Firstname);
                                                    Seeker.put("UID", userID);
                                                    Seeker.put("email", Email);
                                                    Seeker.put("age",Age);
                                                    Seeker.put("location", Address);
                                                    Seeker.put("skills", skills);
                                                    graphicDesignerRef.document(String.valueOf(newSeekerId)).set(Seeker);
                                                    graphicDesignerRef.document("Field_Id").update("last_id", newSeekerId);
                                                }
                                            }
                                        });
                                    } else {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        String documentId = documentSnapshot.getId();
                                        graphicDesignerRef.document(documentId)
                                                .set(user, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot updated with ID: " + documentId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

//                graphicDesignerRef.document(userID)
//                        .set(user)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(SeekerFillActivity.this,"Successful",Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull @NotNull Exception e) {
//
//                                Toast.makeText(SeekerFillActivity.this, e.getMessage() ,Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(SeekerFillActivity.this, SeekerMainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }
}