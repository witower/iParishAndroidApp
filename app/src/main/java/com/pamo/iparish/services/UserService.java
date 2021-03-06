package com.pamo.iparish.services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pamo.iparish.R;
import com.pamo.iparish.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Service for user registration and login
 *
 * This service is validating form and determining which action call
 * @see com.pamo.iparish.MainActivity
 */
public class UserService extends Service {

  FirebaseAuth mFirebaseAuth;
  FirebaseFirestore fStore;
  String userID;

  public UserService() {
    mFirebaseAuth = FirebaseAuth.getInstance();
    fStore = FirebaseFirestore.getInstance();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  /**
   * User registration method, called from main activity.
   * @param email User e-mail
   * @param pwd  User password
   * @param activity Activity that called action
   */
  public void createUser(String email, String pwd, Activity activity) {

    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        documentReference.set(user).addOnSuccessListener(aVoid -> {
          Log.d(TAG, "onSuccess: " + userID);
          activity.startActivity(new Intent(activity, HomeActivity.class));
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
      }
    });
  }

  /**
   * User authentication method, called from main activity.
   * @param email User e-mail
   * @param pwd  User password
   * @param activity Activity that called action
   */
  public void signInUser(String email, String pwd, Activity activity) {

    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(activity, task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(activity, activity.getString(R.string.error_again), Toast.LENGTH_SHORT).show();
      } else {
        Intent intToHome = new Intent(activity, HomeActivity.class);
        intToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intToHome);
        activity.finish();
      }
    });
  }
}
