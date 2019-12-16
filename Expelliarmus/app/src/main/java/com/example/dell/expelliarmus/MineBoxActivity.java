package com.example.dell.expelliarmus;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//我的药箱
public class MineBoxActivity extends AppCompatActivity {
    private List<String> data = new ArrayList<>();
    private MineBoxAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_box);
        ImageView imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineBoxActivity.this.finish();
            }
        });

        data.add("感冒药");
        data.add("感冒药");
        data.add("感冒药");
        data.add("感冒药");
        data.add("感冒药");
        data.add("感冒药");
        listView = findViewById(R.id.lv_box);
        adapter = new MineBoxAdapter(this,data,R.layout.activity_mine_box_item);
        listView.setAdapter(adapter);
    }
}

