package com.example.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText EmailId,Password;
    TextView SignUpText;
    Button SignIn;
    String Email,pass;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase;
    String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailId= (EditText)findViewById(R.id.EmailEditTextLogIn);
        Password= (EditText)findViewById(R.id.Password_EditTextLogIn);
        SignIn = (Button)findViewById(R.id.SignUp_ButtonLogIn);
        SignUpText = (TextView) findViewById(R.id.signup_TextViewLogIn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(getBaseContext(), "Signed in completed "+UserName, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getBaseContext(), ProfileActivity.class));

                } else {
                    Toast.makeText(getBaseContext(), "Log In", Toast.LENGTH_LONG).show();
                }
            }
        };





        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.e("LoginAtivity","in Login button listener");
                Toast.makeText(getBaseContext(),"in Login button listener",Toast.LENGTH_LONG).show();
                Email = EmailId.getText().toString();
                pass = Password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener( LoginActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Signed in completed", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getBaseContext(),ProfileActivity.class));
                        }
                        else
                            Toast.makeText(getBaseContext(),"incorrect Email Or Password",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        SignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLogUp = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intentToLogUp);
            }
        });
    }
    @Override
    protected  void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }
}
