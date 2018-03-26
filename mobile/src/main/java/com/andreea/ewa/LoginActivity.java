package com.andreea.ewa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN";

    // UI.
    private EditText email;
    private EditText password;

    //private TextView tStatus;
    //private TextView tDetail;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        // TODO() Offline persistency.
        //mDatabaseUsers = AppState.get().getDatabaseReference();
       // mDatabaseUsers.child("Patients").keepSynced(true);


        email = ((TextInputLayout)findViewById(R.id.eEmail)).getEditText();
        password =  ((TextInputLayout)findViewById(R.id.ePassword)).getEditText();
       // tStatus =  findViewById(R.id.tStatus);
        //tDetail =  findViewById(R.id.tDetail);

        mAuth = FirebaseAuth.getInstance();

    }

    private void registerUser(){
        Intent intent = new Intent(this, RegisterActivity.class);
        this.startActivity(intent);
    }
    public boolean validateForm(){
        boolean valid = true;

        String e = email.getText().toString();
        if (TextUtils.isEmpty(e)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }


        String pass = password.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            valid = false;
            password.setError("Required.");
        } else {
            password.setError(null);
        }

        return valid;
    }

    public void signIn(String user, String pass){
        Log.d("Logging", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            // Pass signed in successfully and user email to listening activity
                            Intent intent = new Intent();
                            intent.putExtra("user", email.getText().toString());
                            setResult(RESULT_OK, intent);

                            // Finish activity
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            password.setText("");
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(LoginActivity.this, "Signed in.",
                    Toast.LENGTH_SHORT).show();
           // tStatus.setText("Email user: " + user.getEmail());
            //tDetail.setText("Firebase user: " + user.getUid());

            //findViewById(R.id.lSignIn).setVisibility(View.GONE);
            //findViewById(R.id.email_password_fields).setVisibility(View.GONE);
        } else {

           // tStatus.setText("Signed out");
           // tDetail.setText(null);

            //findViewById(R.id.lSignIn).setVisibility(View.VISIBLE);
            //findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
        }
    }

    public void forgotPassword() {

        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        this.startActivity(intent);
    }

    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                registerUser();
                break;
            case R.id.bLogin:
                signIn(email.getText().toString(), password.getText().toString());
                break;

            case R.id.tForgot:
                forgotPassword();
                break;
        }
    }
}
