package com.example.easyreach;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class OrgFillActivity extends AppCompatActivity {

    TextView orgField;
    String skills;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload;
    private Button selectImageButton;
    private ImageView imagePreview;
    private Uri imageUri;
    String imageUrl;
    EditText firstName, foundation, location, eField, userMail,description;
    MaterialButton RegisterBtn;
    FirebaseFirestore db;
private FirebaseAuth mAuth;

    String[] item = {"Web Developer", "AI Developer", "Data Scientist", "Accountant"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;
    String TAG = "OrgFillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_fill);



        autoCompleteTextView = findViewById(R.id.auto_complete_textview);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                orgField = findViewById(R.id.orgField);
                orgField.setText(item);
                skills = orgField.getText().toString();
            }
        });

        db = FirebaseFirestore.getInstance();
        firstName = findViewById(R.id.firstName);
        foundation = findViewById(R.id.pFoundationYear);
        description = findViewById(R.id.pDescription);
        location = findViewById(R.id.pLocation);
        RegisterBtn = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();

        selectImageButton = findViewById(R.id.seeker_select_image_button);
        imagePreview = findViewById(R.id.provider_image_preview);
        btnUpload = findViewById(R.id.seeker_btn_Upload);




        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = mAuth.getCurrentUser().getUid();
                String userMail = mAuth.getCurrentUser().getEmail();
                CollectionReference ProvidersRef = db.collection("Job Providers");
                DocumentReference ProviderDocRef = ProvidersRef.document(userID);

                // Create a map to hold the updated data
                Map<String, Object> updatedData = new HashMap<>();

                // Retrieve the existing data from the database
                ProviderDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Check if the document exists
                        if (documentSnapshot.exists()) {
                            // Retrieve the existing data
                            String existingName = documentSnapshot.getString("pName");
                            String existingFoundation = documentSnapshot.getString("pFoundationYear");
                            String existingDescription = documentSnapshot.getString("pDescription");
                            String existingLocation = documentSnapshot.getString("pLocation");
                            String existingField = documentSnapshot.getString("pField");

                            // Update the fields if the corresponding EditText fields are not empty
                            if (!firstName.getText().toString().isEmpty()) {
                                updatedData.put("pName", firstName.getText().toString());
                            } else {
                                updatedData.put("pName", existingName); // Keep the existing value
                            }
                            if (!foundation.getText().toString().isEmpty()) {
                                updatedData.put("pFoundationYear", foundation.getText().toString());
                            } else {
                                updatedData.put("pFoundationYear", existingFoundation); // Keep the existing value
                            }
                            if (!description.getText().toString().isEmpty()) {
                                updatedData.put("pDescription", description.getText().toString());
                            } else {
                                updatedData.put("pDescription", existingDescription); // Keep the existing value
                            }
                            if (!location.getText().toString().isEmpty()) {
                                updatedData.put("pLocation", location.getText().toString());
                            } else {
                                updatedData.put("pLocation", existingLocation); // Keep the existing value
                            }
                            if (skills != null && !orgField.getText().toString().isEmpty()) {
                                updatedData.put("pField", skills);
                            } else {
                                updatedData.put("pField", existingField); // Keep the existing value
                            }
                            updatedData.put("pUID",userID);
                            updatedData.put("pEmail", userMail);
                            // Perform the update operation
                            ProviderDocRef.update(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Fields updated successfully
                                    Toast.makeText(OrgFillActivity.this, "Fields updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error updating fields
                                    Toast.makeText(OrgFillActivity.this, "Failed to update fields", Toast.LENGTH_SHORT).show();
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
                                .makeText(OrgFillActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    private void saveImageUrlToFirestore(String imageUrl) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference jobSeekersRef = db.collection("Job Providers");
        Query query = jobSeekersRef.whereEqualTo("pUID", userId);

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
                                        Toast.makeText(OrgFillActivity.this, "Profile URL added successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(OrgFillActivity.this, "Image Upload Fialed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(OrgFillActivity.this, "Something Wrong Happened", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent btnClick = new Intent(OrgFillActivity.this, MainActivity.class);
        startActivity(btnClick);
        super.onBackPressed();
        finish();
    }
}