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

public class ActivitySix extends AppCompatActivity {
    private Button FiveHome;
    private Button FiveReturn;
    private int count=0;
    private int rightmeasure=0;
    private int CurrentValue=0;
    private int LastValue=0;
    private int ShowValue=0;
    private BarChart ChartFour;
    ArrayList<BarEntry> EntryFour;
    ArrayList<String> Time;
    BarDataSet SetFour;
    BarData DataFour;
    Calendar c;
    Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);

        ChartFour=(BarChart)findViewById(R.id.ChartFour);

        EntryFour=new ArrayList<>();
        Time=new ArrayList<>();

        FiveHome=(Button)findViewById(R.id.FiveHome);
        FiveHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeFour=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(HomeFour);
            }
        });
        AddAll();
        Draw();
        minAdd();

        FiveReturn=(Button)findViewById(R.id.FiveReturn);
        FiveReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ReturnFour=new Intent(getApplicationContext(),ActivityTwo.class);
                startActivity(ReturnFour);
            }
        });
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
        SetFour=new BarDataSet(EntryFour,"TOO_CLOSE TIMES");
        DataFour=new BarData(Time,SetFour);
        SetFour.setColors(ColorTemplate.PASTEL_COLORS);
        SetFour.setBarSpacePercent(20);
        ChartFour.setData(DataFour);
        ChartFour.setVisibleXRangeMaximum(5);
        ChartFour.setDescription("TOO_CLOSE Times");
        ChartFour.setDragEnabled(true);
        ChartFour.setDescription("PostureRight Times");
        ChartFour.setDescriptionPosition(0,3);
        ChartFour.animateY(1000);
        YAxis y=ChartFour.getAxisLeft();
        y.setAxisMaxValue(1000);
        ChartFour.notifyDataSetChanged();
        ChartFour.invalidate();
        //YAxis y=
    }

    public void AddValues()
    {
        SharedPreferences spp = getSharedPreferences("right", 0);
        rightmeasure = spp.getInt("rightnumber", 20);
        CurrentValue=rightmeasure;
        ShowValue=CurrentValue-LastValue;
        EntryFour.add(new BarEntry(ShowValue,count));
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
        ChartFour.notifyDataSetChanged();
        ChartFour.invalidate();
        ChartFour.setVisibleXRangeMaximum(5);
        ChartFour.setDragEnabled(true);
    }
}
