package com.example.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.firebase.models.FreindlyMessage;
import com.example.firebase.models.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_SIGN_IN = 123;
    private static final int RC_PHOTO_PICKER = 2;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private android.support.v7.widget.Toolbar toolbar;

    private String mUsername;
    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mFirebaseDatabaseReference;
    private ChildEventListener childEventListener;
    private String mFirebaseUserName;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private String ProfileUID;
    private int chatType;
    private String CommitteeName;
    private String ChatKey;
    private String ProfileUserName;
    private ImageView mUser_Image;
    private TextView mNameTextView;
    private String Title;
    private Users user;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUsername = ANONYMOUS;

        mFirebaseUserName = mUsername;
        mFireBaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();
        mFirebaseDatabaseReference = mFireBaseDataBase.getReference();
        mFireBaseAuth = FirebaseAuth.getInstance();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUser_Image = (ImageView)findViewById(R.id.User_imageView);
        mNameTextView = (TextView)findViewById(R.id.UserNameText);
        final List<FreindlyMessage> friendlyMessages = new ArrayList<>();

        mMessageAdapter= new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        //check for what is the chat  type is

        ProfileUID = getIntent().getExtras().get("ProfileUID").toString(); //if the auth profile will be empty string.
        chatType = getIntent().getExtras().getInt("chatType");//0 for private , 1 for committee , 2 for club
        CommitteeName = getIntent().getExtras().get("CommitteeName").toString(); //if the private chat or club  will be empty string.
        ProfileUserName = getIntent().getExtras().get("ProfileUserName").toString();

        switch (chatType){
            case 0://private chat.
                ChatKey = setOneToOneChat(ProfileUID,mFireBaseAuth.getCurrentUser().getUid());
                mFirebaseDatabaseReference = mFireBaseDataBase.getReference().child("PrivateChatting").child(ChatKey);
    //          mChatHeadingTextView.setText(ProfileUserName);
                Title = ProfileUserName;
                setTitle(ProfileUserName);
                break;

            case 1:
                ChatKey = CommitteeName+"_Chat";
                mFirebaseDatabaseReference = mFireBaseDataBase.getReference().child("Committees chatting").child(ChatKey);
      //          mChatHeadingTextView.setText(CommitteeName);
                setTitle(CommitteeName);
                Title = CommitteeName;
                break;
            case 2:
                ChatKey = "IEEECUSB_Chat";
                mFirebaseDatabaseReference = mFireBaseDataBase.getReference().child("Committees chatting").child(ChatKey);
        //        mChatHeadingTextView.setText("IEEECUSB");
                setTitle("IEEECUSB");
                Title = "IEEECUSB";
                break;
        }





        mFireBaseDataBase.getReference().child("users").child(mFireBaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user  = dataSnapshot.getValue(Users.class);
                mFirebaseUserName = user.getUser_Name();
                mNameTextView.setVisibility(View.GONE);
                toolbar.setTitle(Title);
                //mNameTextView.setText(Title);
                if(chatType ==0) {
                    if (user.getUrl() != null)
                        Picasso.get().load(user.getUrl()).transform(new CircleTransform()).into(mUser_Image);
                    else {
                        mUser_Image.setImageResource(R.drawable.defaultprofile);
                    }
                }
                else
                    {
                        mUser_Image.setVisibility(View.GONE);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//action type is get content
                intent.setType("image/jpeg");// type of data to determine the component that receive the intent
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//to make the component be the data on the device.
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                FreindlyMessage message = new FreindlyMessage(mMessageEditText.getText().toString(),mFirebaseUserName,null,getTimestamp(),mFireBaseAuth.getCurrentUser().getUid(),user.getUrl());
                mFirebaseDatabaseReference.child("Messages").push().setValue(message);
                checkIsExist(ChatKey,chatType);
                mFirebaseDatabaseReference.child("ChatNode").child("Last Message").setValue(message);
                if(chatType ==0)
                {
                    mFirebaseDatabaseReference.child("ChatNode").child("ReceiverName").setValue(ProfileUserName);
                    mFirebaseDatabaseReference.child("ChatNode").child("ReceiverImageUrl").setValue(user.getUrl());
                }
                else
                    {
                        mFirebaseDatabaseReference.child("ChatNode").child("ReceiverName").setValue(ChatKey);
                        //
                    }
                // Clear input box
                mMessageEditText.setText("");
            }
        });


        onSignedInInit(mFirebaseUserName);
    }

    private void checkIsExist(final String chatKey, final int chatType) {
        final boolean[] check = new boolean[1];
        check[0] = false;
        mFireBaseDataBase.getReference().child("users").child(mFireBaseAuth.getCurrentUser().getUid()).child("ChattingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Log.e("HomeActivity","in is exist logic");
                    if(snapshot.getValue().toString().equals(chatKey))
                    {
                        check[0] = true;
                    }
                }
                if(!check[0])
                {
                    Log.e("HomeActivity","the chat is not exist before");
                    mFireBaseDataBase.getReference().child("users").child(mFireBaseAuth.getCurrentUser().getUid()).child("ChattingList").push().setValue(ChatKey);
                    if(chatType ==0)
                    {
                        mFireBaseDataBase.getReference().child("users").child(ProfileUID).child("ChattingList").push().setValue(ChatKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void onSignedInInit(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListner();

    }
    private void attachDatabaseReadListner() {
        Log.e("Reader", "Reader initialization is completed correctly");
        if (childEventListener == null) {
            Log.e("ReaderInside", "childEventListener is completed correctly");
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FreindlyMessage freindlyMessage = dataSnapshot.getValue(FreindlyMessage.class);
                    mMessageAdapter.add(freindlyMessage);
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
            };
            mFirebaseDatabaseReference.child("Messages").addChildEventListener(childEventListener);
        }
    }
        private void detachDatabaseReadListener(){
        if(childEventListener != null)
            mFirebaseDatabaseReference.removeEventListener(childEventListener);
        childEventListener=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R. menu.nav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.draw_committees_menu:
                Intent intent = new Intent(HomeActivity.this,CommitteesListActivity.class);
                startActivity(intent);
                break;
            case R.id.draw_Logout_menu:
                mFireBaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                break;
        }

        return true;

    }


    @Override
    public void onResume()
    {
        super.onResume();
        attachDatabaseReadListner();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }


    private String setOneToOneChat(String uid1, String uid2) //key of the private chat of these two users . auth user and the profile user.
    {
        //Check if user1’s id is less than user2's
        if(uid1.compareToIgnoreCase(uid2) <= -1)
        {
            return uid1+uid2;
        }
        else{
            return uid2+uid1;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {
            Uri selectedImage = data.getData();
            final StorageReference imageRef = mStorageReference.child(ChatKey).child("images").child(selectedImage.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(selectedImage);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        FreindlyMessage message = new FreindlyMessage(null,mFirebaseUserName,downloadUri.toString(),getTimestamp(),mFireBaseAuth.getCurrentUser().getUid(),user.getUrl());
                        mFirebaseDatabaseReference.push().setValue(message);
                        mMessageAdapter.notifyDataSetChanged();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Egypt"));
        return sdf.format(new Date());
    }
}
