package com.andreea.ewa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by andreeagb.
 */

public class RegisterActivity extends AppCompatActivity {

    // UI.
    private EditText email;
    private EditText password;
    private EditText password2;

    // Firebase auth.
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        email = ((TextInputLayout)findViewById(R.id.eEmail1)).getEditText();
        password =  ((TextInputLayout)findViewById(R.id.ePass1)).getEditText();
        password2 = ((TextInputLayout)findViewById(R.id.ePass2)).getEditText();

        // init firebase authentication.
        firebaseAuth = FirebaseAuth.getInstance();
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
    public void click(View v) {
        switch (v.getId()) {
            case R.id.bRegisterNew:
                String e = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
               if(!validateForm())
                   break;

                if(!pass.equals(password2.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    break;
                }
                firebaseAuth.createUserWithEmailAndPassword(e, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_SHORT).show();

                            // Make user sign in
                            firebaseAuth.signOut();

                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                break;
        }
    }
}
