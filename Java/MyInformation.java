package com.example.hb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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


public class MyInformation extends AppCompatActivity {

    private String id, name;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinformation);
        getSupportActionBar().setTitle("내 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        findViewById(R.id.bt_MyInformation).setOnClickListener(view -> {
            Intent intent1 = new Intent(MyInformation.this, MyInformationDetails.class);
            startActivity(intent1);
        });

        findViewById(R.id.bt_MyWriteList).setOnClickListener(view -> {
            Intent intent1 = new Intent(MyInformation.this, MyHouseList.class);
            startActivity(intent1);
        });

        findViewById(R.id.bt_MyReviewList).setOnClickListener(view -> {
            Intent intent1 = new Intent(MyInformation.this, MyReviewList.class);
            startActivity(intent1);
        });

        findViewById(R.id.bt_LikeList).setOnClickListener(view -> {
            Intent intent1 = new Intent(MyInformation.this, MyLikeList.class);
            startActivity(intent1);
        });

        findViewById(R.id.bt_Logout).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyInformation.this);
            dialog = builder.setTitle("경고")
                    .setMessage("확인 버튼을 누르면 로그아웃됩니다.")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(MyInformation.this, Login.class);
                            startActivity(intent1);
                        }
                    })
                    .create();
            dialog.show();
        });
    }

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
            Intent intent1 = new Intent(MyInformation.this, Main.class);
            startActivity(intent1);
        });
    }


    private void playBtn1() {
        findViewById(R.id.action_myinformation).setOnClickListener(view -> {
            Intent intent1 = new Intent(MyInformation.this, MyInformation.class);
            startActivity(intent1);
        });
    }
    public void myinfomation(View view){
        new JSONTask().execute("http://210.115.230.153:32430/info");

        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://210.115.230.153:32430/name";
        StringRequest sr = new StringRequest(
                Request.Method.POST,
                url,
                responseListener,
                responseErrorListener
        );
        rq.add(sr);
    }

    public void mywritelist(View view){
//        Intent intent = new Intent(this, MyHouseList.class);
//        startActivity(intent);
    }

    public void myreviewlist(View view){
//        Intent intent = new Intent(this, MyReviewList.class);
//        startActivity(intent);
    }

    public void likelist(View view){
//        Intent intent = new Intent(this, MyLikeList.class);
//        startActivity(intent);
    }

    public void logout(View view){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ID", id);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    // con.setRequestProperty("Accept", "text/html");
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

                    StringBuffer buffer = new StringBuffer();

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
            //super.onPostExecute(result);
        }
    }
    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                Intent intent = new Intent(MyInformation.this, MyInformationDetails.class);
                intent.putExtra("id", id);
                intent.putExtra("Name", response);
                startActivity(intent);
                //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };


}
