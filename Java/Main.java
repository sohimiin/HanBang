package com.example.hb;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("한방");

        findViewById(R.id.bt_list2).setOnClickListener(view -> {
            Intent intent1 = new Intent(Main.this, HanBangList.class);
            startActivity(intent1);
        });

        findViewById(R.id.bt_search).setOnClickListener(view -> {
            Intent intent1 = new Intent(Main.this, Filter.class);
            startActivity(intent1);
        });

        findViewById(R.id.button).setOnClickListener(view -> {
            Intent intent1 = new Intent(Main.this, Registration.class);
            startActivity(intent1);
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
            Intent intent1 = new Intent(Main.this, Main.class);
            startActivity(intent1);
        });
    }

    private void playBtn1() {
        findViewById(R.id.action_myinformation).setOnClickListener(view -> {
            Intent intent1 = new Intent(Main.this, MyInformation.class);
            startActivity(intent1);
        });
    }

}
