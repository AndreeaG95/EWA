package com.andreea.ewa;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private Button register;

    // Firebase auth.
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.bRegisterNew);
        email = findViewById(R.id.eEmail1);
        password =  findViewById(R.id.ePass1);
        password2 = findViewById(R.id.ePass2);

        // init firebase authentication.
        firebaseAuth = firebaseAuth.getInstance();
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.bRegisterNew:
                String e = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(e.isEmpty()) {
                    email.setError("Required.");
                    break;
                }

                if(pass.isEmpty()){
                    password.setError("Required.");
                    break;
                }

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
