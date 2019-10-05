package com.example.firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_display_event.*
import kotlinx.android.synthetic.main.activity_display_task.*

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
        deadlinedate = intent.getStringExtra("DeadLine")
        target = intent.getStringExtra("Target")
        Log.e("DisplayActivity","topic is :$topic")
        if(topic != null)
            Topic.text = topic
        Deatails.text = details
        Deadline.text = deadlinedate
        Target.text = target
    }
}