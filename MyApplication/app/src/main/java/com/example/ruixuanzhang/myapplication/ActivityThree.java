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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityThree extends AppCompatActivity {

    Calendar c;
    private LineGraphSeries<DataPoint> abnormalL;
    private int count;
    private Handler handler;
    private Button Return;
    private Button HomePage;
    private Button GetPlot;
    private BarChart AbChart;
    ArrayList<BarEntry> Abvalue;
    ArrayList<String> Time;
    BarDataSet Bardataset;
    BarData BARDATA;
    int ABabnormalnumber = 0;
    int BeforeValue = 0;
    int CurrentValue = 0;
    int ShowValue=0;
    String answer="";
    String abnormalString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
//        SharedPreferences spp = getSharedPreferences("abnormal", 0);
//        ABabnormalnumber = spp.getInt("abnormalnumber", 100);

        AbChart = (BarChart) findViewById(R.id.Abchart);
        Return = (Button) findViewById(R.id.returnAbnormal);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnAbnormal = new Intent(getApplicationContext(), ActivityTwo.class);
                startActivity(returnAbnormal);
            }
        });

        HomePage = (Button) findViewById(R.id.HomeAbnormal);
        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnHome);
            }
        });
        Abvalue = new ArrayList<>();
        Time = new ArrayList<String>();
        BeforeValue = ABabnormalnumber;
        CurrentValue = ABabnormalnumber;
        AddAll();
        Draw();
        minAdd();

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

    private void Draw() {
        Bardataset = new BarDataSet(Abvalue, "AbnormalTimes");
        BARDATA = new BarData(Time, Bardataset);
        Bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        Bardataset.setBarSpacePercent(20);
        AbChart.setData(BARDATA);
        AbChart.setVisibleXRangeMaximum(5);
        AbChart.setDescription("HUMPBACK Times");
        AbChart.setDescriptionPosition(0,3);
        AbChart.setDragEnabled(true);
        AbChart.animateY(1000);
        YAxis y=AbChart.getAxisLeft();
        y.setAxisMaxValue(100);
        AbChart.notifyDataSetChanged();
        AbChart.invalidate();
    }

    public void AddValues() {
        SharedPreferences abn = getSharedPreferences("abnormal", 0);
        ABabnormalnumber = abn.getInt("abnormalnumber", 80);
        CurrentValue=ABabnormalnumber;
        ShowValue=CurrentValue-BeforeValue;
        Abvalue.add(new BarEntry(ShowValue, count));
        count++;
        BeforeValue=CurrentValue;

    }
    public void AddLabel(String label) {

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
        AbChart.notifyDataSetChanged();
        AbChart.invalidate();
        AbChart.setVisibleXRangeMaximum(5);
        AbChart.setDragEnabled(true);
    }

}