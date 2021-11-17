package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.roomdatabase.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    ActivityRegisterBinding binding;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    Uri imageUri;

    String imageUrl, email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        binding.registerBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri==null){
                    Toast.makeText(RegisterActivity.this, "Image is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                name = binding.registerTieName.getText().toString();
                if(name.isEmpty()){
                    binding.registerTilName.setError("Name is required");
                    return;
                }
                email = binding.registerTieEmail.getText().toString();
                if(email.isEmpty()){
                    binding.registerTilEmail.setError("Email is required");
                    return;
                }
                password = binding.registerTiePassword.getText().toString();
                if(password.isEmpty()){
                    binding.registerTilPassword.setError("Password is requires");
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        uploadImage();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        String errorMessage = e.getLocalizedMessage();
                        Log.i(TAG, "createUser onFailure: "+ errorMessage);
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.registerIvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            binding.registerIvPic.setImageURI(imageUri);
        }
    }

    private void uploadImage(){
        String uid = firebaseAuth.getCurrentUser().getUid();

        storageReference.child("Images").child("profileImages").child(uid).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getImageUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        String errorMessage = e.getLocalizedMessage();
                        Log.i(TAG, "upload image onFailure: "+errorMessage);
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getImageUrl(){
        String uid = firebaseAuth.getCurrentUser().getUid();

        String randomId =  uid + String.valueOf(System.currentTimeMillis());

        storageReference.child("Images").child("profileImages").child(uid).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = randomId.toString();
                        Log.i(TAG, "onSuccess: "+ imageUrl);
                        uploadUserDataToFireStore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage = e.getLocalizedMessage();
                Log.i(TAG, "getImage onFailure: "+ errorMessage);
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadUserDataToFireStore(){
        User user = new User(name, email, imageUrl);
        String uid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("users").document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getLocalizedMessage();
                        Log.i(TAG, "upload user data onFailure: "+ errorMessage);
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}