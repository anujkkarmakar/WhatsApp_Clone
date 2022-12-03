package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.txt_signup).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);
    }

    public void signInUser() {
        EditText temp = findViewById(R.id.login_email);
        String email = temp.getText().toString().trim();
        String password = ((EditText)findViewById(R.id.login_password)).getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your credentials", Toast.LENGTH_SHORT).show();
        } else if(!email.matches(emailPattern)) {
            temp.setError("Invalid email");
            Toast.makeText(this, "Please enter a valid email id", Toast.LENGTH_SHORT).show();
        } else {
            //when everything is correct, we proceed with sign in of the user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Successfully signed in
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_signup:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.btn_signin:
                signInUser();
                break;
        }
    }
}