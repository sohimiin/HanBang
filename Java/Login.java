package com.example.hb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

import static java.lang.System.console;
import static java.lang.System.load;


public class Login extends AppCompatActivity {

    // *** 변수 설정
    private CheckBox cb_IdSave, cb_IdSave3;
    private Button bt_Login, bt_Join;
    private EditText et_Id, et_PassWord;
    private boolean saveLoginData;
    private String id,pwd;
    private SharedPreferences appData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("한방 로그인");

        // *** 설정값 불러오기
        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();

        cb_IdSave = (CheckBox)findViewById(R.id.cb_IdSave);
        bt_Login = (Button)findViewById(R.id.bt_Login);
        bt_Join = (Button)findViewById(R.id.bt_Join);
        et_Id = (EditText)findViewById(R.id.et_Id);
        et_PassWord = (EditText)findViewById(R.id.et_PassWord);

        // *** 이전에 로그인 정보 저장 기록이 있다면
        if(saveLoginData){
            et_Id.setText(id);
            et_PassWord.setText(pwd);
            cb_IdSave.setChecked(saveLoginData);
        }
    }


    // *** 설정 값을 저장하는 함수
    private void save() {
        //SharedPreferences 객체만으로 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        //에이터객체.put타입 (저장시킬 이름, 저장시킬 값)
        //저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", cb_IdSave.isChecked());
        editor.putString("ID", et_Id.getText().toString().trim());
        editor.putString("PWD", et_PassWord.getText().toString().trim());

        //apply, commit을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }


    // *** 설정 값을 불러오는 함수
    private void load() {
        //SharedPreferences 객체, get타입(저장된 이름, 기본값)
        //저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        pwd = appData.getString("PWD", "");
    }



    //*** 로그인 버튼 누를 때
    public void LoginButton(View view) {
        //Toast.makeText(getApplicationContext(), "로그인 이동", Toast.LENGTH_SHORT).show();
        new JSONTask().execute("http://210.115.230.153:32430/login");
        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://210.115.230.153:32430/yes";
        StringRequest sr = new StringRequest(
//                Request.Method.POST,
                url,
                responseListener,
                responseErrorListener
        );
        rq.add(sr);
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ID", et_Id.getText().toString());
                jsonObject.put("Password", et_PassWord.getText().toString());

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
                //만약 그 값이 같다면 로그인에 성공한 것입니다.
                if (response.equals("Yes")) {
                    //filter화면에 예시로 id넣어보기
                    Intent intent = new Intent(Login.this, Main.class);
                    intent.putExtra("id", et_Id.getText().toString());
                    //여기서 Intent 뒤에 값만 바꿔주면됭
                    Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
                } else {
                    et_Id.setText("");
                    et_PassWord.setText("");
                }
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
    // 서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
    //*** 회원가입 버튼 누를 때 ( O )
    public void RegisterButton(View view){
        Intent intent = new Intent(this, Join.class);
        startActivity(intent);
    }

}
