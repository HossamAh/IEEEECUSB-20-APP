package com.example.firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_event.*

class DisplayEventActivity : AppCompatActivity() {

    private lateinit var topic: String
    private lateinit var date: String
    private lateinit var details: String
    private lateinit var location: String
    private lateinit var imgurl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_event)
        topic = intent.getStringExtra("Topic")
        details = intent.getStringExtra("Details")
        date = intent.getStringExtra("Date")
        location = intent.getStringExtra("Location")
        val topicTxt = findViewById<TextView>(R.id.TOPIC)
        val detailsTxt = findViewById<TextView>(R.id.DETAILS)
        val dateTxt = findViewById<TextView>(R.id.DATE)
        val  LocationTxt = findViewById<TextView>(R.id.LOCATION)
        val  image = findViewById<ImageView>(R.id.ImageURL)
        topicTxt.text = topic
        detailsTxt.text = details
        dateTxt.text = date
        LocationTxt.text = location
        /*if(imgurl == "")
            image.setImageResource(R.drawable.ieeeblue)
        else
            Picasso.get().load(imgurl).transform(CircleTransform()).into(ImageURL)
*/
    }
}