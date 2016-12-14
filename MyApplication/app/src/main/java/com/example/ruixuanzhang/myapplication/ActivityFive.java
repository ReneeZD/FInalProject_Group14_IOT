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

public class ActivityFive extends AppCompatActivity {
    private Button FourHome;
    private Button FourReturn;
    private int count=0;
    private int leftmeasure=0;
    private int CurrentValue=0;
    private int LastValue=0;
    private int ShowValue=0;
    private BarChart ChartThree;
    ArrayList<BarEntry> EntryThree;
    ArrayList<String> Time;
    BarDataSet SetThree;
    BarData DataThree;
    Calendar c;
    Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);

        ChartThree=(BarChart)findViewById(R.id.ChartThree);
        SharedPreferences spp = getSharedPreferences("left", 0);
        leftmeasure = spp.getInt("leftnumber", 100);

        EntryThree=new ArrayList<>();
        Time=new ArrayList<>();

        FourHome=(Button)findViewById(R.id.FourHome);
        FourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeFour=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(HomeFour);
            }
        });
        FourReturn=(Button)findViewById(R.id.FourReturn);
        FourReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReturnFour=new Intent(getApplicationContext(),ActivityTwo.class);
                startActivity(ReturnFour);
            }
        });

        AddAll();
        Draw();

        minAdd();


//        Draw();

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
    private void Draw()
    {
        SetThree=new BarDataSet(EntryThree,"LEAN_BACK TIMES");
        DataThree=new BarData(Time,SetThree);
        SetThree.setColors(ColorTemplate.PASTEL_COLORS);
        SetThree.setBarSpacePercent(20);
        ChartThree.setData(DataThree);
        ChartThree.setVisibleXRangeMaximum(5);
        ChartThree.setDescription("LEAN_BACK Times");
        ChartThree.setDescription("PostureLeft Times");
        ChartThree.setDescriptionPosition(0,3);
        ChartThree.setDragEnabled(true);
        ChartThree.animateY(1000);
        YAxis y=ChartThree.getAxisLeft();
        y.setAxisMaxValue(1000);
        ChartThree.notifyDataSetChanged();
        ChartThree.invalidate();
        //YAxis y=
    }

    public void AddValues()
    {
        SharedPreferences spp = getSharedPreferences("left", 0);
        leftmeasure = spp.getInt("leftnumber", 100);
        CurrentValue=leftmeasure;
        ShowValue=CurrentValue-LastValue;
        EntryThree.add(new BarEntry(ShowValue,count));
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
        ChartThree.notifyDataSetChanged();
        ChartThree.invalidate();
        ChartThree.setVisibleXRangeMaximum(5);
        ChartThree.setDragEnabled(true);
    }
}
