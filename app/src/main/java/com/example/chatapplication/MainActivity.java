package com.example.chatapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());


       ImageView iv =  findViewById(R.id.googleSignInButton);
        Picasso.get().load("https://onymos.com/wp-content/uploads/2020/10/google-signin-button-1024x260.png").into(iv);
        iv.setOnClickListener(view -> {
            // Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null) {
                Toast.makeText(getApplicationContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,ChatActivity.class));
           }
        } else {
            if(response != null) {
                Toast.makeText(this, "There was a problem logging in, please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }
}