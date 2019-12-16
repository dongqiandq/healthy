package com.example.dell.expelliarmus;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineSafeSetActivity extends AppCompatActivity {

    private TextView imageView;
    private RelativeLayout num;
    private RelativeLayout password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_safe_set);

        imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineSafeSetActivity.this.finish();
            }
        });

        num = findViewById(R.id.rl_num);//账号随系统更改
        password = findViewById(R.id.rl_password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineSafeSetActivity.this,MineWordSetActivity.class);
                startActivity(intent);
            }
        });

    }
}

