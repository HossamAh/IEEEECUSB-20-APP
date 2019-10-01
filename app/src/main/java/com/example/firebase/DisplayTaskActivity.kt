package com.example.firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class DisplayTaskActivity : AppCompatActivity() {


    private lateinit var topic: String
    private lateinit var target: String
    private lateinit var details: String
    private lateinit var deadlinedate: String

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_task)
           topic = intent.getStringExtra("Topic")
           details = intent.getStringExtra("Details")
           deadlinedate = intent.getStringExtra("Deadline")
           target = intent.getStringExtra("Target")
           val topicTxt = findViewById<TextView>(R.id.Topic)
           val detailsTxt = findViewById<TextView>(R.id.Deatails)
           val deadlineTxt = findViewById<TextView>(R.id.Deadline)
           val  targetTxt = findViewById<TextView>(R.id.Target)
           topicTxt.text = topic
           detailsTxt.text = details
           deadlineTxt.text = deadlinedate
           targetTxt.text = target
    }
}
