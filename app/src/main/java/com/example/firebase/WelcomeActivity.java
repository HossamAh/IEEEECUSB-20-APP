package com.example.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.firebase.R.layout.activity_welcome;

public class WelcomeActivity extends AppCompatActivity {


    Button startButton;
    Animation fromBottom , fromTop;
    ImageView ballon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_welcome);

        startButton = (Button) findViewById(R.id.startBtn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            }
        });
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        startButton.setAnimation(fromBottom);

        ballon = (ImageView) findViewById(R.id.ballon);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        ballon.setAnimation(fromTop);
    }
}



