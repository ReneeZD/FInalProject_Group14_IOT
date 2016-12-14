package com.example.ruixuanzhang.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView reminder;
    private Button record;
    static int response;
    String answer="";
    String classification="";
    String numberresult="";
    int abnormalmeasure=0;
    int goodmeasure=0;
    int leftmeasure=0;
    int rightmeasure=0;
    static String DEBUG_TAG="debug";
    String good;
    String humpback;
    String left;
    String right;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        reminder=(TextView)findViewById(R.id.reminder);
        record=(Button)findViewById(R.id.record);


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),ActivityTwo.class);
                startActivity(intent);

            }
        });
        CallPeriodically();
       // Log.d(DEBUG_TAG,Integer.toString(goodmeasure));
    }
    private TimerTask task;
    private void CallPeriodically()
    {
        final Handler handler = new Handler();
        Timer timer =new Timer();
        task = new TimerTask(){
            public void run()
            {
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        new TryGet().execute();
                        Log.d(DEBUG_TAG,Integer.toString(rightmeasure));

                    }
                });
            }
        };
        timer.schedule(task,0,5000);

    }
    private class TryGet extends AsyncTask<Void,Void,String>
    {
        protected void onPreExecute()
        {
            //reminder.setText("hi");
        }
        protected String doInBackground(Void... Params)
        {
            answer="";
            try
            {
                URL url=new URL("http://ec2-54-149-75-206.us-west-2.compute.amazonaws.com/posture/times");
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                JSONObject temp=new JSONObject();
                String t;
                temp.put("abnormal",0);
                t=temp.toString();
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Length", ""+ t.length() );
                con.setRequestMethod("POST");
                OutputStreamWriter w = new OutputStreamWriter(con.getOutputStream());
                w.write(temp.toString());
                w.flush();
                w.close();
//                System.out.println("after connecting");
                response = con.getResponseCode();
//                System.out.println("response:"+response);
//                Log.d(DEBUG_TAG,"The response is: "+response);
                if(response==200)
                {
                    String line;
                    BufferedReader reader = new BufferedReader((new InputStreamReader(con.getInputStream())));
                    while ((line=reader.readLine())!=null)
                    {
                        answer+=line;
                    }
                }
                else{
                    answer="";
                }
                Log.d(DEBUG_TAG,answer);
                if(answer=="")
                    return "No data";
                else {
                    Log.d(DEBUG_TAG,"answer exists");
                    String output[]=answer.split(",");
                    good=output[3];
                    humpback=output[0];
                    left=output[2];
                    right=output[1];
                    //good=answer.substring(answer.indexOf("\"posturezero\""),answer.indexOf("\"posturezero\"")+25);
                    //humpback=answer.substring(answer.indexOf("\"postureone\""),answer.indexOf("\"postureone\"")+25);
                    //left=answer.substring(answer.indexOf("\"posturetwo\""),answer.indexOf("\"posturetwo\"")+25);
                    //right=answer.substring(answer.indexOf("\"posturethree\""),answer.indexOf("\"posturezero\"")+25);
                    //System.out.println();
                    goodmeasure=Integer.parseInt(good.replaceAll("[^0-9?!\\.]",""));
                    Log.d(DEBUG_TAG,Integer.toString(goodmeasure));
                    abnormalmeasure=Integer.parseInt(humpback.replaceAll("[^0-9?!\\.]",""));
                    leftmeasure=Integer.parseInt(left.replaceAll("[^0-9?!\\.]",""));
                    rightmeasure=Integer.parseInt(right.replaceAll("[^0-9?!\\.]",""));
                    SharedPreferences goo=getSharedPreferences("good",0);
                    SharedPreferences.Editor editors=goo.edit();
                    editors.putInt("goodnumber",goodmeasure);
                    editors.commit();

                    SharedPreferences abn=getSharedPreferences("abnormal",0);
                    SharedPreferences.Editor editorsA=abn.edit();
                    editorsA.putInt("abnormalnumber",abnormalmeasure);
                    editorsA.commit();

                    SharedPreferences lef=getSharedPreferences("left",0);
                    SharedPreferences.Editor editorsL=lef.edit();
                    editorsL.putInt("leftnumber",leftmeasure);
                    editorsL.commit();

                    SharedPreferences rig=getSharedPreferences("right",0);
                    SharedPreferences.Editor editorsR=rig.edit();
                    editorsR.putInt("rightnumber",rightmeasure);
                    editorsR.commit();
                    return good;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if(answer=="")
                return "No data";
            else {
                return good;
            }
        }

        protected void onPostExecute(String result)
        {
            //Log.d(DEBUG_TAG,"onpost");
            reminder.setText(result);
        }
    }
}
