package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PICK_IMAGE = 1;
    //private final String namePattern = "^([A-Za-z]{1}[A-Za-z\\d_]*\\.)+[A-Za-z][A-Za-z\\d_]*$";
    Uri imageUri;
    private final String defaultStatus = "Namaste. I'm using WhatsApp Clone";
    private final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    CircleImageView profileImage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        findViewById(R.id.txt_signin).setOnClickListener(this);
        findViewById(R.id.txt_signup).setOnClickListener(this);
        profileImage = findViewById(R.id.profile_image);
    }

    public void signUpNewUser() {
        EditText etEmail = findViewById(R.id.reg_email);
        EditText etName = findViewById(R.id.reg_name);
        EditText etPass = findViewById(R.id.reg_password);
        EditText etConPass = findViewById(R.id.reg_con_password);

        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String con_password = etConPass.getText().toString().trim();
        String name = etName.getText().toString().trim();

        progressDialog.show();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(con_password)) {
            progressDialog.dismiss();
            Toast.makeText(this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)){
            progressDialog.dismiss();
            etEmail.setError("Enter valid email");
        // } else if(!name.matches(namePattern)) {
            //progressDialog.dismiss();
            //etName.setError("Enter valid name");
        } else if(!password.equals(con_password)) {
            progressDialog.dismiss();
            etPass.setError("Password did not match");
            etConPass.setError("Password did not match");
        } else if(password.length() < 6) {
            progressDialog.dismiss();
            Toast.makeText(this, "Password must be at-least 6 characters", Toast.LENGTH_SHORT).show();
            etPass.setError("Password did not match");
            etConPass.setError("", null);
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                //TODO: TO BE UPDATED
                                assert auth != null;
                                DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if(imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users user = new Users(name, email, imageURI, auth.getUid(), defaultStatus);
                                                        reference.setValue(user)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(RegistrationActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                                        }
                                                                        else {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(RegistrationActivity.this, "Error created profile", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegistrationActivity.this, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/random-a2331.appspot.com/o/profile_image.png?alt=media&token=4457d398-a7ac-4908-931f-3c0dfb4343ea";
                                    Users user = new Users(name, email, imageURI, auth.getUid(), defaultStatus);
                                    reference.setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(RegistrationActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                    }
                                                    else {
                                                        Toast.makeText(RegistrationActivity.this, "Error created profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                            else {
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE) {
            if(data != null) {
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_signin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.txt_signup:
                signUpNewUser();
                break;
        }
    }
}