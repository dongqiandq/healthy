package com.example.dell.expelliarmus;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//收藏详情页
public class MineCollectDetailsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect_details);
        TextView back = findViewById(R.id.btn_backm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(checkColor() == false){
//                    Intent intent = new Intent(CollectDetailsActivity.this,CollectActivity.class);
//
//                }
                finish();
            }
        });

        LinearLayout collect = findViewById(R.id.ll_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivSollect = findViewById(R.id.iv_sollect);
                TextView tvSollect = findViewById(R.id.tv_sollect);
                if (checkColor()){
                    ivSollect.setImageResource(R.drawable.shoucang1);
                    tvSollect.setTextColor(Color.parseColor("#D3D3D3"));

                }
                else {
                    ivSollect.setImageResource(R.drawable.shoucangxuanhzong);
                    tvSollect.setTextColor(Color.parseColor("#8bc34a"));
                }
            }
        });
    }

    protected Boolean checkColor(){
        TextView tvSollect = findViewById(R.id.tv_sollect);
        int color = tvSollect.getCurrentTextColor();
        int check = Color.parseColor("#8bc34a");
        if (color == check){
            return true;
        }else {
            return false;
        }
    }

}

