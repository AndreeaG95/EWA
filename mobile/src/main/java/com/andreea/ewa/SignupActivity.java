package com.andreea.ewa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN";

    // UI.
    private EditText email;
    private EditText password;

    private TextView tStatus;
    private TextView tDetail;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        email = (EditText) findViewById(R.id.eEmail);
        password = (EditText) findViewById(R.id.ePassword);
        tStatus = (TextView) findViewById(R.id.tStatus);
        tDetail = (TextView) findViewById(R.id.tDetail);

        mAuth = FirebaseAuth.getInstance();

    }

    private void registerUser(){
        Intent intent = new Intent(this, RegisterActivity.class);
        SignupActivity.this.startActivity(intent);

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
            password.setError("Required.");
            valid = false;
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            tStatus.setText("Email user: " + user.getEmail());
            tDetail.setText("Firebase user: " + user.getUid());

            //findViewById(R.id.lSignIn).setVisibility(View.GONE);
            //findViewById(R.id.email_password_fields).setVisibility(View.GONE);
        } else {
            tStatus.setText("Signed out");
            tDetail.setText(null);

            //findViewById(R.id.lSignIn).setVisibility(View.VISIBLE);
            //findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
        }
    }

    public void clicked(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                registerUser();
                break;
            case R.id.bLogin:
                signIn(email.getText().toString(), password.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", email.getText().toString());
                intent.putExtra("pass", password.getText().toString());
                setResult(RESULT_OK);
                finish();

                break;
        }
    }
}
