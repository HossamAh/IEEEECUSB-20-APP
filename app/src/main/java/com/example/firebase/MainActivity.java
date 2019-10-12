package com.example.firebase;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.models.FreindlyMessage;
import com.example.firebase.models.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private static final int RC_Profile_Picker = 1;
    EditText EmailId,Password,UserNameEditText;
    TextView SigninText;
    Button SignUp,ProfilePicker;
    String Email,pass,UserName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Spinner CommitteeSpinner,PostitonSpinner;
    DatabaseReference databaseReference;
    Users user;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    List<String> finalPositions1;
    boolean imagePicked;
    String CommitteeName,PositionName;
    Uri profilePicUri;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseCommitteesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailId= (EditText)findViewById(R.id.EmailEditTextLogIn);
        Password= (EditText)findViewById(R.id.Password_EditTextLogIn);
        UserNameEditText= (EditText)findViewById(R.id.UserName_EditText);
        SignUp = (Button)findViewById(R.id.SignUp_ButtonLogIn);
        SigninText = (TextView) findViewById(R.id.signup_TextViewLogIn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        CommitteeSpinner = (Spinner)findViewById(R.id.Committee_spinner);
        PostitonSpinner = (Spinner)findViewById(R.id.Postition_spinner);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        ProfilePicker = (Button)findViewById(R.id.ProfileImagePickerbutton);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
        mDatabaseCommitteesReference = firebaseDatabase.getReference().child("Committees");
        imagePicked = false;
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String[] Committees
        ={"None",
        "High Board",
        "Technical Committee",
        "Embedded Committee",
        "IT Committee",
        "HR Committee",
        "Marketing Committee",
        "Media Committee",
        "Biomedical Committee",
        "Electronics Committee",
        "Coaching Committee",
        "Organization Committee",
        "Magazine Committee",
        "Computer Committee",
        "Power Committee",
        "PR Committee",
        "FR Committee"};

        ArrayAdapter ArrAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Committees);
        ArrAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        CommitteeSpinner.setAdapter(ArrAdapter);


        CommitteeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"Committee is : "+Committees[position]);
                CommitteeName = Committees[position];
                ArrayAdapter ArrAdapter2;
                finalPositions1 = new ArrayList<>();
                if(CommitteeName.equals("High Board")) {

                    finalPositions1.add( "Chairman");
                    finalPositions1.add("Chairman Vice");
                     ArrAdapter2= new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,finalPositions1);
                }
                else if(CommitteeName.equals("Technical Committee")) {
                    finalPositions1.add("Technical Manager");
                    finalPositions1.add("Technical Vice");
                    ArrAdapter2 = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,finalPositions1);
                }
                else
                    {
                        finalPositions1.add("Head");
                        finalPositions1.add("Vice");
                        finalPositions1.add("Member");
                        ArrAdapter2 = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item,finalPositions1);
                    }

                ArrAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                PostitonSpinner.setAdapter(ArrAdapter2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                CommitteeName = Committees[0];
                Log.e(TAG,"Committee not selected");
            }
        });



        PostitonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PositionName = finalPositions1.get(position);
                Log.e(TAG,"position is : "+finalPositions1.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PositionName = finalPositions1.get(0);
                Log.e(TAG,"Position not selected");
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = EmailId.getText().toString();
                pass = Password.getText().toString();
                UserName = UserNameEditText.getText().toString();

                if(!Email.isEmpty() && !pass.isEmpty() && !UserName.isEmpty())
                    firebaseAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful()) {
                               Log.e(TAG,CommitteeName);
                               Log.e(TAG,PositionName);
                               if(imagePicked)
                               user = new Users(UserName,CommitteeName,PositionName,profilePicUri.toString());
                               else
                                   user = new Users(UserName,CommitteeName,PositionName,null);
                               databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);


                               if(PositionName == "Head" ||PositionName == "Vice")
                               {
                                   if(CommitteeName .equals( "Embedded Committee") || CommitteeName.equals("Computer Committee")
                                           || CommitteeName.equals("Power Committee")
                                   ||CommitteeName.equals("Biomedical Committee")
                                   ||CommitteeName.equals("Electronics Committee"))
                                   {
                                       mDatabaseCommitteesReference.child("Technical Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                                   mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   if(PositionName.equals( "Head"))
                                   {
                                       mDatabaseCommitteesReference.child(CommitteeName).child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                                   else if(PositionName.equals("Vice"))
                                   {
                                       mDatabaseCommitteesReference.child(CommitteeName).child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                               }
                               else if(PositionName.equals("Chairman") ||PositionName.equals("Chairman Vice"))
                               {
                                   if(PositionName.equals("Chairman"))
                                   {
                                       mDatabaseCommitteesReference.child("High Board").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                                   else if(PositionName .equals("Chairman Vice"))
                                   {
                                       mDatabaseCommitteesReference.child("High Board").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                               }
                               else if(PositionName.equals("Technical Manager") ||PositionName.equals("Technical Vice"))
                               {
                                   mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   if(PositionName == "Technical Manager")
                                   {
                                       mDatabaseCommitteesReference.child("Technical Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                                   else if(PositionName == "Chairman Vice")
                                   {
                                       mDatabaseCommitteesReference.child("Technical Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                   }
                               }


                               Toast.makeText(getBaseContext(), "account creation is completed"+UserName, Toast.LENGTH_LONG).show();
                               startActivity(new Intent(getBaseContext(),ProfileActivity.class));
                              }
                           else
                               Toast.makeText(getBaseContext(),"account creation is uncompleted",Toast.LENGTH_LONG).show();
                        }
                    });
            }
        });
        SigninText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLogIn = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intentToLogIn);
            }
        });

        ProfilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp.setClickable(false);
                //TODO add progreessbar to indicate loading picture.
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//action type is get content
                intent.setType("image/jpeg");// type of data to determine the component that receive the intent
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//to make the component be the data on the device.
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_Profile_Picker);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity",RC_Profile_Picker+"");
        if(requestCode == RC_Profile_Picker && resultCode == RESULT_OK)
        {
            Log.e("MainActivity","Request success");
            Uri selectedImage = data.getData();
            final StorageReference imageRef = mStorageReference.child("ProfileImages").child(selectedImage.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(selectedImage);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("MainActivity","Task success");
                     imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                         @Override
                         public void onComplete(@NonNull Task<Uri> task) {
                             Log.e("MainActivity","download url success");
                             SignUp.setClickable(true);
                             //TODO disappear progress bar.
                             profilePicUri = task.getResult();
                             imagePicked = true;
                             Toast.makeText(getApplicationContext(),"Uploading Profile Picture Successfully",Toast.LENGTH_LONG).show();
                             Log.e("MainActivity",profilePicUri.toString());

                         }
                     });
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        firebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

        firebaseAuth.addAuthStateListener(mAuthListener);

    }
}
