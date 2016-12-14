package com.example.ruixuanzhang.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityFour extends AppCompatActivity {

    private Button ThreeHome;
    private Button ThreeReturn;
    private int count=0;
    private int goodmeasure=0;
    private int CurrentValue=0;
    private int LastValue=0;
    private int ShowValue=0;

    private BarChart ChartTwo;
    ArrayList<BarEntry> EntryTwo;
    ArrayList<String> Time;
    BarDataSet SetTwo;
    BarData DataTwo;

    Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        EntryTwo=new ArrayList<>();
        Time=new ArrayList<>();
        ChartTwo=(BarChart)findViewById(R.id.ChartTwo);

        ThreeHome=(Button)findViewById(R.id.ThreeHome);
        ThreeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeThree=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(HomeThree);
            }
        });

        ThreeReturn=(Button)findViewById(R.id.ThreeReturn);
        ThreeReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReturnThree=new Intent(getApplicationContext(),ActivityTwo.class);
                startActivity(ReturnThree);
            }
        });
        AddAll();
        Draw();
        minAdd();
    }


    private void Draw()
    {
        SetTwo=new BarDataSet(EntryTwo,"GOOD_POSTURE TIMES");
        DataTwo=new BarData(Time,SetTwo);
        SetTwo.setColors(ColorTemplate.PASTEL_COLORS);
        SetTwo.setBarSpacePercent(20);
        ChartTwo.setData(DataTwo);
        ChartTwo.setVisibleXRangeMaximum(5);
        ChartTwo.setDescription("GoodPosture Times");
        ChartTwo.setDescriptionPosition(0,3);
        ChartTwo.setDragEnabled(true);
        ChartTwo.animateY(1000);
        YAxis y=ChartTwo.getAxisLeft();
        y.setAxisMaxValue(1000);
        ChartTwo.notifyDataSetChanged();
        ChartTwo.invalidate();


    }

    private TimerTask minTask;
    private void minAdd()
    {
        final Handler handler = new Handler();
        Timer timer =new Timer();
        minTask = new TimerTask(){
            public void run()
            {
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        AddAll();
                    }
                });
            }
        };
        timer.schedule(minTask,0,5000);
    }
    public void AddValues()
    {
        SharedPreferences spp = getSharedPreferences("good", 0);
        goodmeasure = spp.getInt("goodnumber", 20);
        CurrentValue=goodmeasure;
        System.out.println(CurrentValue);
        ShowValue=CurrentValue-LastValue;
        EntryTwo.add(new BarEntry(ShowValue,count));
        count++;
        LastValue=CurrentValue;
    }

    public void AddLabel(String label)
    {
        Time.add(label);
    }

    public void AddAll()
    {
        AddValues();
        c=Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedData = df.format(c.getTime());
        System.out.println("formatted date: " + formattedData);
        String AllTime = formattedData.substring(10, 19);
        AddLabel(AllTime);
        ChartTwo.notifyDataSetChanged();
        ChartTwo.invalidate();
        ChartTwo.setVisibleXRangeMaximum(5);
        ChartTwo.setDragEnabled(true);
    }
}
