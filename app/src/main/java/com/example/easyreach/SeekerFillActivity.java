package com.example.easyreach;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
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
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SeekerFillActivity extends AppCompatActivity {

    EditText firstName, age, eAddress, eField, sDescription, sYoE;
    TextView field;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnUpload, btnChooseCV, btnUploadCV;
    private Button selectImageButton;

    private Uri selectedImageUri;
    private Uri croppedImageUri;
    private ImageView imagePreview;
    private Uri imageUri;
    private static final int PICK_PDF_REQUEST = 2;
    private Uri cvUri;
    private StorageReference cvStorageRef;
    private DocumentReference userRef; // Unique integer value for CV selection request
    String skills;
    private static final int REQUEST_SELECT_IMAGE = 1;
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

        cvStorageRef = FirebaseStorage.getInstance().getReference().child("cv");

// Initialize Firestore document reference for the current user
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = db.collection("Job Seekers").document(currentUserId);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        AutoCompleteTextView citySpinner = findViewById(R.id.cityAutoCompleteTextView);

        // Retrieve the list of Egyptian cities from resources
        String[] egyptianCities = getResources().getStringArray(R.array.egyptian_cities);

        // Create an ArrayAdapter with the Egyptian cities
        final ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.list_item, egyptianCities);

        // Set the ArrayAdapter as the adapter for the AutoCompleteTextView
        citySpinner.setAdapter(cityAdapter);

        LottieAnimationView backAnimationView = findViewById(R.id.backAnimationView);

        backAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });


        // Set a TextWatcher to filter the city list as the user types
        citySpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed in this case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the city list based on the user's input
                String filter = s.toString().toLowerCase();
                cityAdapter.getFilter().filter(filter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed in this case
            }
        });
        autoCompleteTextView.setAdapter(adapterItems);
        btnUpload = findViewById(R.id.seeker_btn_Upload);
        btnUploadCV = findViewById(R.id.btnUploadCV);
        btnUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cvUri != null) {
                    uploadPDFToStorage(cvUri);
                } else {
                    Toast.makeText(SeekerFillActivity.this, "Please select a CV file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChooseCV = findViewById(R.id.btnChooseCV);
        btnChooseCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();
            }
        });
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


                String Address = citySpinner.getText().toString();
//                String Field = eField.getText().toString();



                // Create a map to hold the updated data
                Map<String, Object> updatedData = new HashMap<>();

                // Retrieve the existing data from the database
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                            String existingImageUrl = documentSnapshot.getString("sImageUrl");

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
                            if (!citySpinner.getText().toString().isEmpty()) {
                                updatedData.put("sLocation", citySpinner.getText().toString());
                            } else {
                                updatedData.put("sLocation", existingLocation); // Keep the existing value
                            }
                            if (skills != null && !field.getText().toString().isEmpty()) {
                                updatedData.put("sField", skills);
                            } else {
                                updatedData.put("sField", existingField); // Keep the existing value
                            }
                            if (!(imagePreview.getDrawable() == null)){
                                updatedData.put("sImageUrl", PhotoUrl);
                            }else {
                                updatedData.put("sImageUrl", existingImageUrl);
                            }
                            updatedData.put("UID",userID);
                            updatedData.put("sEmail", Email);
                            // Perform the update operation
                            userRef.set(updatedData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            userRef.set(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                openGallery();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (imagePreview.getDrawable() ==null) {
                    Toast.makeText(SeekerFillActivity.this, "please Select an Image First!.", Toast.LENGTH_SHORT).show();
                }else {
                    uploadImage();
                }

            }
        });
    }


    private void uploadImage() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imagesRef = storageRef.child( userId + "/profile_picture");

        imagesRef.putFile(croppedImageUri)
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
        String Email = mAuth.getCurrentUser().getEmail();

        CollectionReference jobSeekersRef = db.collection("Job Seekers");
        Query query = jobSeekersRef.whereEqualTo("sEmail", Email);

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

    }
    private void saveCVUrlToFirestore(String downloadUrl) {
        userRef.update("cvUrl", downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // CV URL saved successfully
                    Toast.makeText(SeekerFillActivity.this, "CV uploaded successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred while updating the document
                    Toast.makeText(SeekerFillActivity.this, "Failed to upload CV: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void selectPDFFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }
    private void uploadPDFToStorage(Uri fileUri) {
        if (fileUri != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Define the storage reference for the file
            StorageReference fileRef = cvStorageRef.child( userId + "/CV");

            // Start the upload process
            fileRef.putFile(fileUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Retrieve the download URL of the uploaded file
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    // Save the download URL to Firestore
                                    saveCVUrlToFirestore(downloadUrl);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle any errors that occurred while retrieving the download URL
                                    Toast.makeText(SeekerFillActivity.this, "Failed to upload CV: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors that occurred during the upload process
                        Toast.makeText(SeekerFillActivity.this, "Failed to upload CV: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            startCropActivity(selectedImageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCropResult(data);
        }
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            cvUri = data.getData();
            // Set the selected file name to a TextView or perform any other action

        }
    }

    private void startCropActivity(Uri sourceUri) {
        String destinationFileName = "cropped_image.jpg";
        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);

        uCrop.withOptions(options);

        uCrop.start(SeekerFillActivity.this);
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);

        if (resultUri != null) {
            croppedImageUri = resultUri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                imagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Failed to crop image", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}