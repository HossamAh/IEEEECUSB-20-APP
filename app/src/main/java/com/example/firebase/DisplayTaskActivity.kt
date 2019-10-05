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
        TOPIC.text = intent.getStringExtra("Topic")
        DETAILS.text = intent.getStringExtra("Details")
        Deadline.text = intent.getStringExtra("DeadLine")
        Target.text = intent.getStringExtra("Target")

    }
}