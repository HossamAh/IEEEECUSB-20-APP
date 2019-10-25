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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    private static final int RC_Profile_Picker = 1;
    EditText EmailId,Password,UserNameEditText,CodeEditText;
    TextView SigninText;
    Button SignUp,ProfilePicker;
    String Email,pass,UserName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    //Spinner CommitteeSpinner,PostitonSpinner;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    Users user;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    List<String> finalPositions1;
    boolean imagePicked;
    String CommitteeName,PositionName,UserCode;
    Uri profilePicUri;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseCommitteesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailId = (EditText) findViewById(R.id.EmailEditTextLogIn);
        Password = (EditText) findViewById(R.id.Password_EditTextLogIn);
        UserNameEditText = (EditText) findViewById(R.id.UserName_EditText);
        SignUp = (Button) findViewById(R.id.SignUp_ButtonLogIn);
        SigninText = (TextView) findViewById(R.id.signup_TextViewLogIn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        CodeEditText = (EditText) findViewById(R.id.Code);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference2 = firebaseDatabase.getReference();
        ProfilePicker = (Button)findViewById(R.id.ProfileImagePickerbutton);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
        mDatabaseCommitteesReference = firebaseDatabase.getReference().child("Committees");
        imagePicked = false;
        mFirebaseAuth = FirebaseAuth.getInstance();
   /*     final String[] Committees
        ={"None",
        "High Board",
        "Technical Committee",
        "Embedded Committee",
        "IT Committee",
        "HR Committee",
        "Marketing Committee",
        "Media Committee",
        "HR Committee",
        "PR Committee",
        "FR Committee"};
*/

        //TODO: link committee name and position name to codes
        //TODO: Map Codes in the following Ref and it shall be OK
        final Map<String, ArrayList<String>> localCodes = new HashMap<String, ArrayList<String>>();
        final ArrayList<String> names = new ArrayList<String>();
        DatabaseReference codes = databaseReference2.child("Codes");
        final DatabaseReference NamesRef = databaseReference2.child("Names");
        NamesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                names.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        codes.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                localCodes.put(dataSnapshot.getKey(), (ArrayList<String>) dataSnapshot.getValue());
                Log.d(TAG, "onCreate: local codes = "+dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "onCreate: local codes = "+localCodes);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = EmailId.getText().toString();
                pass = Password.getText().toString();
                UserName = UserNameEditText.getText().toString();

                for (int i = 0; i < localCodes.size(); i++) {
                    if (!CodeEditText.getText().toString().isEmpty())
                        if (localCodes.get(CodeEditText.getText().toString()) != null){
                            CommitteeName = localCodes.get(CodeEditText.getText().toString()).get(0);
                            PositionName = localCodes.get(CodeEditText.getText().toString()).get(1);}
                }
                if (names.contains(UserName)) {
                    Log.d(TAG, "onClick: BLIN");
                    Toast.makeText(MainActivity.this, "UserName is Taken", Toast.LENGTH_SHORT).show();

                } else {
                    if (!Email.isEmpty() && !pass.isEmpty() && !UserName.isEmpty() && CommitteeName != null)
                        firebaseAuth.createUserWithEmailAndPassword(Email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.e(TAG, CommitteeName);
                                    Log.e(TAG, PositionName);
                                    if (imagePicked)
                                        user = new Users(UserName, CommitteeName, PositionName, profilePicUri.toString());
                                    else
                                        user = new Users(UserName, CommitteeName, PositionName, null);
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);



                                    if (PositionName == "Chairman" || PositionName=="Chairman Vice")
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
                                    else if(PositionName == "Head" && CommitteeName == "IT Committee")
                                    {
                                        mDatabaseCommitteesReference.child("IT Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "IT Committee")
                                    {
                                        mDatabaseCommitteesReference.child("IT Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "IT Committee")
                                    {
                                        mDatabaseCommitteesReference.child("IT Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Secretary")
                                    {
                                        mDatabaseCommitteesReference.child("Coaching Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Head"  && CommitteeName == "Coaching Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Coaching Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Vice"  && CommitteeName == "Coaching Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Coaching Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Coaching Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Coaching Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "HR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("HR Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "HR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("HR Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "HR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("HR Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Branding Manager")
                                    {
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Branding Section").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Multimedia Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Multimedia Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Multimedia Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Multimedia Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Multimedia Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Multimedia Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Magazine Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Magazine Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Magazine Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Magazine Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Magazine Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Magazine Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Marketing Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Marketing Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Marketing Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Branding Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Marketing Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Marketing Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Marketing Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Technical Manager")
                                    {
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Technical Section").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Technical Vice")
                                    {
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Technical Section").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Electronics Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Electronics Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Electronics Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Electronics Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Electronics Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Electronics Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Embedded Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Embedded Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Embedded Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Embedded Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Embedded Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Embedded Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Computer Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Computer Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Computer Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Computer Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Computer Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Computer Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Power Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Power Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Power Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Power Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Power Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Power Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Biomedical Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Biomedical Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Biomedical Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Technical Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Biomedical Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Biomedical Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Biomedical Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if (PositionName == "Relation Manager")
                                    {
                                        mDatabaseCommitteesReference.child("High Board").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Relation Section").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "PR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("PR Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "PR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("PR Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "PR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("PR Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "FR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("FR Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "FR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("FR Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "FR Committee")
                                    {
                                        mDatabaseCommitteesReference.child("FR Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Head" && CommitteeName == "Organization Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Organization Committee").child("Committee_Heads_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Vice" && CommitteeName == "Organization Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Relation Section").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                        mDatabaseCommitteesReference.child("Organization Committee").child("Committee_Vice_ID").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }
                                    else if(PositionName == "Member" && CommitteeName == "Organization Committee")
                                    {
                                        mDatabaseCommitteesReference.child("Organization Committee").child("members_IDs").push().setValue(firebaseAuth.getCurrentUser().getUid());
                                    }


                                    Toast.makeText(getBaseContext(), "account creation is completed"+UserName, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getBaseContext(),ProfileActivity.class));
                                }
                                else
                                    Toast.makeText(getBaseContext(),"account creation is uncompleted",Toast.LENGTH_LONG).show();
                            }
                        });
                    else
                        Toast.makeText(getBaseContext(), "Invaild Data", Toast.LENGTH_SHORT).show();
                }
            }});
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
                            profilePicUri = task.getResult();
                            imagePicked = true;
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
