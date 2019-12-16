package com.example.dell.expelliarmus;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//设置
public class MinePersonSettingsActivity extends AppCompatActivity {
    private TextView imageView;
    private RelativeLayout set1;
    private RelativeLayout set2;
    private Button exit;
    private Util util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_settings);

        util = new Util(this);
        imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinePersonSettingsActivity.this.finish();
            }
        });

        set1 = findViewById(R.id.rl_set1);
        set2 = findViewById(R.id.rl_set2);
        exit = findViewById(R.id.btn_exit);

        set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MinePersonSettingsActivity.this,MineSafeSetActivity.class);
                startActivity(intent);
            }
        });
        set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MinePersonSettingsActivity.this,MineAboutActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.clearUserInfo();
                Intent intent2 = new Intent(MinePersonSettingsActivity.this,MainActivity.class);
                startActivity(intent2);
            }
        });
    }
}

