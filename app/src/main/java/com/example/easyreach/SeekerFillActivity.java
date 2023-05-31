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

    EditText firstName, age, eAddress, eField, sDescription, sYoE;
    TextView field;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload;
    private Button selectImageButton;
    private ImageView imagePreview;
    private Uri imageUri;
    String skills;
    String PhotoUrl;
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
        sDescription = findViewById(R.id.sDescription);
        sYoE = findViewById(R.id.YoE);
        age = findViewById(R.id.age);
        eAddress = findViewById(R.id.address);
//        eField = findViewById(R.id.field);
        RegisterBtn = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        selectImageButton = findViewById(R.id.seeker_select_image_button);
        imagePreview = findViewById(R.id.seeker_image_preview);
        btnUpload = findViewById(R.id.seeker_btn_Upload);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String userID =  mAuth.getCurrentUser().getUid();
                String Firstname = firstName.getText().toString();
                String Email = mAuth.getCurrentUser().getEmail();
                String Age = age.getText().toString();
                String YoE = sYoE.getText().toString();
                String Description = sDescription.getText().toString();


                String Address = eAddress.getText().toString();
//                String Field = eField.getText().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("sName",Firstname);
                user.put("sEmail", Email);
                user.put("sAge",Age);
                user.put("sLocation", Address);
                user.put("sYoE", YoE);
                user.put("sDescription", Description);
                user.put("sField", skills);
                user.put("UID", userID);

                CollectionReference SeekersRef = db.collection("Job Seekers");
                DocumentReference SeekerDocRef = SeekersRef.document(userID);

                CollectionReference SelectedCategoryRef = db.collection("JS").document("Field").collection(skills);

                SelectedCategoryRef.whereEqualTo("UID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) {

                                        SelectedCategoryRef.document("Field_Id").get().addOnCompleteListener(tasks -> {
                                            if (tasks.isSuccessful()) {
                                                DocumentSnapshot document = tasks.getResult();
                                                if (document.exists()) {
                                                    long lastSeekerId = document.getLong("last_id");
                                                    long newSeekerId = lastSeekerId + 1;

                                                    Map<String, Object> Seeker = new HashMap<>();
                                                    Seeker.put("seeker_id", newSeekerId);
                                                    Seeker.put("sName",Firstname);
                                                    Seeker.put("UID", userID);
                                                    Seeker.put("sEmail", Email);
                                                    Seeker.put("sAge",Age);
                                                    Seeker.put("sYoE", YoE);
                                                    Seeker.put("sDescription", Description);
                                                    Seeker.put("sLocation", Address);
                                                    Seeker.put("sField", skills);
                                                    SelectedCategoryRef.document(String.valueOf(newSeekerId)).set(Seeker);
                                                    SelectedCategoryRef.document("Field_Id").update("last_id", newSeekerId);
                                                }
                                            }
                                        });
                                    } else {
                                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                        String documentId = documentSnapshot.getId();
                                        SelectedCategoryRef.document(documentId)
                                                .set(user, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot updated with ID: " + documentId);
                                                        Toast.makeText(SeekerFillActivity.this, "Your Data is Updated Successfully", Toast.LENGTH_SHORT).show();
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


                // Create a map to hold the updated data
                Map<String, Object> updatedData = new HashMap<>();

                // Retrieve the existing data from the database
                SeekerDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Check if the document exists
                        if (documentSnapshot.exists()) {
                            // Retrieve the existing data
                            String existingName = documentSnapshot.getString("sName");
                            String existingAge = documentSnapshot.getString("sAge");
                            String existingDescription = documentSnapshot.getString("sDescription");
                            String existingLocation = documentSnapshot.getString("sLocation");
                            String existingField = documentSnapshot.getString("sField");
                            String existingYoE = documentSnapshot.getString("sYoE");

                            // Update the fields if the corresponding EditText fields are not empty
                            if (!firstName.getText().toString().isEmpty()) {
                                updatedData.put("sName", firstName.getText().toString());
                            } else {
                                updatedData.put("sName", existingName); // Keep the existing value
                            }
                            if (!age.getText().toString().isEmpty()) {
                                updatedData.put("sAge", age.getText().toString());
                            } else {
                                updatedData.put("sAge", existingAge); // Keep the existing value
                            }
                            if (!sDescription.getText().toString().isEmpty()) {
                                updatedData.put("sDescription", sDescription.getText().toString());
                            } else {
                                updatedData.put("sDescription", existingDescription); // Keep the existing value
                            }
                            if (!sYoE.getText().toString().isEmpty()) {
                                updatedData.put("sYoE", sYoE.getText().toString());
                            } else {
                                updatedData.put("sYoE", existingYoE); // Keep the existing value
                            }
                            if (!eAddress.getText().toString().isEmpty()) {
                                updatedData.put("sLocation", eAddress.getText().toString());
                            } else {
                                updatedData.put("sLocation", existingLocation); // Keep the existing value
                            }
                            if (skills != null && !field.getText().toString().isEmpty()) {
                                updatedData.put("sField", skills);
                            } else {
                                updatedData.put("sField", existingField); // Keep the existing value
                            }
                            updatedData.put("UID",userID);
                            updatedData.put("sEmail", Email);
                            updatedData.put("sImageUrl", PhotoUrl);
                            // Perform the update operation
                            SeekerDocRef.set(updatedData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Fields updated successfully
                                    Toast.makeText(SeekerFillActivity.this, "Fields updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error updating fields
                                    Toast.makeText(SeekerFillActivity.this, "Failed to update fields", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            updatedData.put("sYoE", YoE);
                            updatedData.put("sName",Firstname);
                            updatedData.put("UID", userID);
                            updatedData.put("sEmail", Email);
                            updatedData.put("sAge",Age);
                            updatedData.put("sLocation", Address);
                            updatedData.put("sField", skills);
                            updatedData.put("sImageUrl", PhotoUrl);
                            SeekerDocRef.set(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SeekerFillActivity.this, "Your data is added successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SeekerFillActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
            }
        });
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
                                PhotoUrl = downloadUrl.toString();
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
                        data.put("sImageUrl", imageUrl);
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
        //Up


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