package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.models.Event;
import com.example.firebase.models.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class Create extends AppCompatActivity {
    private EditText Topic,Details,Date,Location;
    private Spinner TargetSpinner;
    private Spinner spinner2;
    private Button CreateEvent,CreateTask,Submit;
    private TextView TopicTextView,DetailsTextView,DateTextView,LocationTextView,TargetTextView;
    private FirebaseDatabase mFireBaseDataBase;
    private DatabaseReference mFirebaseDataBaseReference;
    private DatabaseReference mCommitteeTaskReference;
    private DatabaseReference mCommitteeEventReference;
    private DatabaseReference mGeneralEventReference;
    //private DatabaseReference mIndividualTaskReference;
    private int TaskOrEvent = 0;  // 1 for task 2 for event


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        mFireBaseDataBase = FirebaseDatabase.getInstance();
        mFirebaseDataBaseReference = mFireBaseDataBase.getReference();
        //mCommitteeEventReference = mFirebaseDataBaseReference.child("Committees");//.child(committeeName).child("Event").push().setValue(CommitteeEvent Object);



        Topic = (EditText)findViewById(R.id.TopicEditText);
        Details =(EditText)findViewById(R.id.DetailsEditText);
        Date = (EditText)findViewById(R.id.DateEditText);
        Location = (EditText)findViewById(R.id.LocationEditText);
        TargetSpinner = (Spinner)findViewById(R.id.TargetSpinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        CreateEvent = (Button) findViewById(R.id.CreateAnnouncment);
        CreateTask = (Button)findViewById(R.id.CreateTask);
        Submit = (Button)findViewById(R.id.Submit_button);
        TopicTextView = (TextView)findViewById(R.id.TopicTextView);
        DetailsTextView = (TextView)findViewById(R.id.DetailsTextView);
        DateTextView =(TextView)findViewById(R.id.DateTextView);
        LocationTextView = (TextView)findViewById(R.id.LocationTextView);
        TargetTextView = (TextView)findViewById(R.id.TargetTextView);


        TopicTextView.setVisibility(View.GONE);
        DetailsTextView.setVisibility(View.GONE);
        DateTextView.setVisibility(View.GONE);
        LocationTextView.setVisibility(View.GONE);
        TargetTextView.setVisibility(View.GONE);
        Topic.setVisibility(View.GONE);
        Details.setVisibility(View.GONE);
        Date.setVisibility(View.GONE);
        Location.setVisibility(View.GONE);
        TargetSpinner.setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);
        Submit.setVisibility(View.GONE);

        CreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskOrEvent = 2;
                TopicTextView.setVisibility(View.VISIBLE);
                DetailsTextView.setVisibility(View.VISIBLE);
                DateTextView.setVisibility(View.VISIBLE);
                LocationTextView.setVisibility(View.VISIBLE);
                TargetTextView.setVisibility(View.VISIBLE);
                Topic.setVisibility(View.VISIBLE);
                Details.setVisibility(View.VISIBLE);
                Date.setVisibility(View.VISIBLE);
                Location.setVisibility(View.VISIBLE);
                TargetSpinner.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);

            }
        });
        CreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskOrEvent = 1;
                TopicTextView.setVisibility(View.VISIBLE);
                DetailsTextView.setVisibility(View.VISIBLE);
                DateTextView.setText("DeadLine");
                DateTextView.setVisibility(View.VISIBLE);

                TargetTextView.setVisibility(View.VISIBLE);
                Topic.setVisibility(View.VISIBLE);
                Details.setVisibility(View.VISIBLE);
                Date.setVisibility(View.VISIBLE);

                TargetSpinner.setVisibility(View.VISIBLE);
                spinner2.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });



        Spinner spinner  = TargetSpinner;
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("General");
        arrayList.add("Committee");
        // arrayList.add("Member");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Target = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner spinner1 = spinner2;
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("High Board");
        arrayList1.add("Technical Committee");
        arrayList1.add("Embedded Committee");
        arrayList1.add("IT Committee");
        arrayList1.add("HR Committee");
        arrayList1.add("Marketing Committee");
        arrayList1.add("Media Committee");
        arrayList1.add("HR Committee");
        arrayList1.add("PR Committee");
        arrayList1.add("FR Committee");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String TargetCommittee = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TaskOrEvent == 1) //Task
                {
                    Task task = new Task(Topic.getText().toString(), TargetSpinner.getSelectedItem().toString(),Details.getText().toString(),Date.getText().toString(),null);
                    mFirebaseDataBaseReference.child("Committees").child(spinner2.getSelectedItem().toString()).child("Tasks").push().setValue(task);
                    Toast.makeText(getBaseContext(), "Successful Creation of an announcement", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Create.this, DisplayTaskActivity.class);
                    intent.putExtra("Topic",task.getTopic());
                    intent.putExtra("Details",task.getDetails());
                    intent.putExtra("DeadLine",task.getDeadlineDate());
                    intent.putExtra("Target",task.getTarget());
                    intent.putExtra("TaskID",task.getTaskID());
                    startActivity(intent);
                }
                if (TaskOrEvent == 2) //Event
                {
                    Event event = new Event(Topic.getText().toString(),Details.getText().toString(),TargetSpinner.getSelectedItem().toString(),Date.getText().toString(),Location.getText().toString(),null,null);
                    if (TargetSpinner.getSelectedItem().toString()=="General")
                    {
                        mFirebaseDataBaseReference.child("IEEECUSB").child("Events").push().setValue(event);
                    }
                    if (TargetSpinner.getSelectedItem().toString()=="Committee")
                    {
                        mFirebaseDataBaseReference.child("Committees").child(spinner2.getSelectedItem().toString()).child("Events").push().setValue(event);
                    }
                    Toast.makeText(getBaseContext(), "Successful Creation of an announcement", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Create.this, DisplayEventActivity.class);
                    intent.putExtra("Topic",event.getTopic());
                    intent.putExtra("Details",event.getDetails());
                    intent.putExtra("Date",event.getDate());
                    intent.putExtra("Location",event.getLocation());
                    if(event.getImageUrl() !=null)
                        intent.putExtra("ImageUrl",event.getImageUrl());
                    else
                        intent.putExtra("ImageUrl","");
                    intent.putExtra("Target",event.getTarget());
                    intent.putExtra("EventID",event.getEventID());
                    startActivity(intent);
                }

            }
        });


    }
}