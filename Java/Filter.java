package com.example.hb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Filter extends AppCompatActivity {
    private EditText txtDepositMax,txtMonthlyMax;
    int[] list = new int[3]; //DB에 보낼 배열

    //앱바에 메뉴 이동
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_main:
                playBtn();
                return true;

            case R.id.action_myinformation:
                playBtn1();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void playBtn() {
        findViewById(R.id.action_main).setOnClickListener(view -> {
            Intent intent1 = new Intent(Filter.this, Main.class);
            startActivity(intent1);
        });
    }


    private void playBtn1() {
        findViewById(R.id.action_myinformation).setOnClickListener(view -> {
            Intent intent1 = new Intent(Filter.this, MyInformation.class);
            startActivity(intent1);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RadioButton rdoYear,rdoMonth;
        RadioButton rdoRoom1,rdoRoom2;
        RadioGroup grpType,grpRoom;

        txtDepositMax = (EditText)findViewById(R.id.txtDepositMax);
        txtMonthlyMax = (EditText)findViewById(R.id.txtMonthlyMax);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setTitle("조건 검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grpType = (RadioGroup) findViewById(R.id.grpType);
        grpRoom = (RadioGroup) findViewById(R.id.grpRoom);
        grpType.setOnCheckedChangeListener(radioGroupButtonChangeListener1);
        grpRoom.setOnCheckedChangeListener(radioGroupButtonChangeListener2);

        CheckBox checkBox = (CheckBox) findViewById(R.id.chkFloor) ;
        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    list[1]=1;
                } else {
                    list[1]=0;
                }
            }
        }) ;
    }

    //월세,전세 선택하는 라디오버튼
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            txtDepositMax = (EditText) findViewById(R.id.txtDepositMax);
            txtMonthlyMax = (EditText) findViewById(R.id.txtMonthlyMax);

            if (i == R.id.rdoYear) {
                Toast.makeText(Filter.this, "전세가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                list[0] = 1; //유형
                txtMonthlyMax.setText("0");
            } else if (i == R.id.rdoMonth) {
                Toast.makeText(Filter.this, "월세가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                list[0] = 0; //유형
                txtDepositMax.setText("");
                txtMonthlyMax.setText("");
            }
        }
    };



    //원룸,투룸 선택하는 라디오버튼
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rdoRoom1){
                Toast.makeText(com.example.hb.Filter.this, "원룸이 선택되었습니다.",Toast.LENGTH_SHORT).show();
                list[2]=1; //원룸
            }
            else if(i == R.id.rdoRoom2){
                Toast.makeText(com.example.hb.Filter.this, "투룸이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                list[2]=0; //투룸
            }
        }
    };

    public void onButtonClk(View v) {

        new JSONTask().execute("http://210.115.230.153:32430/filter");
        
        findViewById(R.id.btnSearch).setOnClickListener(view->{
            Intent intent=new Intent(Filter.this,FilterList.class);
            startActivity(intent);
        });
    }

    //복사
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("Deposit", txtDepositMax.getText());
                //txtDepositMax.getText().toString()
                jsonObject.accumulate("MonthlyRent", txtMonthlyMax.getText());
                //txtMonthlyMax.getText().toString()
                jsonObject.accumulate("YearRent", list[0]);
                jsonObject.accumulate("Floor", list[1]);
                jsonObject.accumulate("Rooms", list[2]);


                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outStream = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuilder buffer = new StringBuilder();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
//                JSONObject json = new JSONObject(result);
                JSONArray ja = new JSONArray(result);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject j = ja.getJSONObject(i);
                    txtDepositMax.setText(j.getString("Location"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
