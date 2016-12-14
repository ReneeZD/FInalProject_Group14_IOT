package com.example.ruixuanzhang.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.Date;

public class ActivityTwo extends AppCompatActivity {

    private Button Return;
    private Button ABNORMAL;
    private Button ClassThree;
    private Button ClassFour;
    private Button ClassTwo;
    int abnormalmeasure=0;
    int totalmeasure=0;
    Date Start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        //abnormalmeasure=getIntent().getIntExtra("abnormalnumber",-1);
        //totalmeasure=getIntent().getIntExtra("totalnumber",-1);
        Return=(Button)findViewById(R.id.returnrecord);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnintent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(returnintent);
            }
        });
        ABNORMAL=(Button)findViewById(R.id.abnormal);
        ABNORMAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToAbnormal=new Intent(getApplicationContext(),ActivityThree.class);
                //ToAbnormal.putExtra("abnormalnumber",abnormalmeasure);
                startActivity(ToAbnormal);
            }
        });
        //Go to activityFive page
        ClassThree=(Button)findViewById(R.id.ClassThree);
        ClassThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GoThree=new Intent(getApplicationContext(),ActivityFive.class);
                startActivity(GoThree);
            }
        });
        //Go to activitySix Page
        ClassFour=(Button)findViewById(R.id.ClassFour);
        ClassFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GoFour=new Intent(getApplicationContext(),ActivitySix.class);
                startActivity(GoFour);
            }
        });
        //Go to ActivityFour Page
        ClassTwo=(Button)findViewById(R.id.ClassTwo);
        ClassTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GoTwo=new Intent(getApplicationContext(),ActivityFour.class);
                startActivity(GoTwo);
            }
        });
    }
}
