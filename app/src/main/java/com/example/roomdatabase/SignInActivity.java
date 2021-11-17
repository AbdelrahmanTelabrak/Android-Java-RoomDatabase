package com.example.roomdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.roomdatabase.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        binding.signInBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.signInTieEmail.getText().toString();
                if(email.isEmpty()){
                    binding.signInTilEmail.setError("Email is required");
                    return;
                }
                String password = binding.signInTiePassword.getText().toString();
                if (password.isEmpty()){
                    binding.signInTilPassword.setError("Password is required");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                finish();
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = e.getLocalizedMessage();
                        Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.signInBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}