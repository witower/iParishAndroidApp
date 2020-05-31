package com.pamo.iparish.register;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pamo.iparish.HomeActivity;
import com.pamo.iparish.R;

public class LoginActivity extends AppCompatActivity {
  EditText emailId, password;
  Button btnSignIn;
  TextView tvSignUp;
  FirebaseAuth mFirebaseAuth;
  private FirebaseAuth.AuthStateListener mAuthStateListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mFirebaseAuth = FirebaseAuth.getInstance();
    emailId = findViewById(R.id.editText);
    password = findViewById(R.id.editText2);
    btnSignIn = findViewById(R.id.button2);
    tvSignUp = findViewById(R.id.textView);

    mAuthStateListener = firebaseAuth -> {
      FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
      if (mFirebaseUser != null) {
        Toast.makeText(LoginActivity.this, getString(R.string.toast_Logged), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
      } else {
        Toast.makeText(LoginActivity.this, getString(R.string.toast_Login), Toast.LENGTH_SHORT).show();
      }
    };

    btnSignIn.setOnClickListener(v -> {
      String email = emailId.getText().toString();
      String pwd = password.getText().toString();
      if (email.isEmpty()) {
        emailId.setError(getString(R.string.enter_email));
        emailId.requestFocus();
      } else if (pwd.isEmpty()) {
        password.setError(getString(R.string.enter_password));
        password.requestFocus();
      } else if (email.isEmpty() && pwd.isEmpty()) {
        Toast.makeText(LoginActivity.this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show();
      } else if (!(email.isEmpty() && pwd.isEmpty())) {
        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (!task.isSuccessful()) {
              Toast.makeText(LoginActivity.this, getString(R.string.error_again), Toast.LENGTH_SHORT).show();
            } else {
              Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
              startActivity(intToHome);
            }
          }
        });
      } else {
        Toast.makeText(LoginActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
      }
    });

    tvSignUp.setOnClickListener(v -> {
      Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(intSignUp);
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
  }
}
