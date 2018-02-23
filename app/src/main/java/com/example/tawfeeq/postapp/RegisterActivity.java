package com.example.tawfeeq.postapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText user_name, user_email, user_pass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user_name = (EditText) findViewById(R.id.user_name);
        user_email = (EditText) findViewById(R.id.user_email);
        user_pass = (EditText) findViewById(R.id.user_pass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerButtonClicked(View view){
        final String name = user_name.getText().toString().trim();
        final String email = user_email.getText().toString().trim();
        final String pass = user_pass.getText().toString().trim();

        if(TextUtils.isEmpty(name)&& TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {
            user_name.setError("This field is required");
            user_email.setError("This field is required");
            user_pass.setError("This field is required");
        }
        else if(TextUtils.isEmpty(name)) {
            user_name.setError("This field is required");
        }
        else if(TextUtils.isEmpty(email)) {
            user_email.setError("This field is required");
        }
        else if(TextUtils.isEmpty(pass)){
            user_pass.setError("This field is required");
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("Name").setValue(name);
                        current_user_db.child("Email").setValue(email);
                        current_user_db.child("Password").setValue(pass);
                        current_user_db.child("image").setValue("default");

                        Intent mainIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                }
            });
        }

    }
}
