package com.andreea.ewa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by andreeagb.
 */


public class MainActivity extends AppCompatActivity {

    // Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int REQ_SIGNIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup authentication
        mAuth = FirebaseAuth.getInstance();
        /*
        * Listen to any changes in the state of the user.
        * The listener  will fire when it is attached, and then every time a user logs in, out or his token expires.
        * The triggered method returns the currently logged in user (or null).
        */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    TextView tLoginDetail = (TextView) findViewById(R.id.tLoginDetail);
                    TextView tUser = (TextView) findViewById(R.id.tUser);
                    tLoginDetail.setText("Firebase ID: " + user.getUid());
                    tUser.setText("Email: " + user.getEmail());


                   // AppState.get().setUserId(user.getUid());
                    //attachDBListener(user.getUid());
                } else {
                    // If user not signed in, start new activity.
                    startActivityForResult(new Intent(getApplicationContext(), SignupActivity.class), REQ_SIGNIN);
                }
            }
        };

    }


}
