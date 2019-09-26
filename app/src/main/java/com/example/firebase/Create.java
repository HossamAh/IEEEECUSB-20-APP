
package com.example.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Create extends AppCompatActivity {
    private EditText Topic,Details,Date,Location;
    private Spinner TargetSpinner;
    private Button CreateEvent,CreateTask,Submit;
    private TextView TopicTextView,DetailsTextView,DateTextView,LocationTextView,TargetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Topic = (EditText) findViewById(R.id.TopicEditText);
        Details =(EditText)findViewById(R.id.DetailsEditText);
        Date = (EditText)findViewById(R.id.DateEditText);
        Location = (EditText)findViewById(R.id.LocationEditText);
        TargetSpinner = (Spinner)findViewById(R.id.TargetSpinner);
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
        Submit.setVisibility(View.GONE);

        CreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Submit.setVisibility(View.VISIBLE);

            }
        });
        CreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicTextView.setVisibility(View.VISIBLE);
                DetailsTextView.setVisibility(View.VISIBLE);
                DateTextView.setText("DeadLine");
                DateTextView.setVisibility(View.VISIBLE);
                LocationTextView.setVisibility(View.VISIBLE);
                TargetTextView.setVisibility(View.VISIBLE);
                Topic.setVisibility(View.VISIBLE);
                Details.setVisibility(View.VISIBLE);
                Date.setVisibility(View.VISIBLE);
                Location.setVisibility(View.GONE);
                TargetSpinner.setVisibility(View.VISIBLE);
                Submit.setVisibility(View.VISIBLE);
            }
        });



        Spinner spinner  = findViewById(R.id.TargetSpinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("General");
        arrayList.add("Committee");
        arrayList.add("Member");
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
    }
}
