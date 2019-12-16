package com.example.dell.expelliarmus;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

//最近阅读
public class MinePersonReadActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private ListView listView ;
    private MineCollectAdapter collectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_read);
        ImageView imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinePersonReadActivity.this.finish();
            }
        });

        listView = findViewById(R.id.lv_read);
        list.add("肌肉拉伸动作大全，跑步、健身、久坐都适用");
        list.add("肌肉拉伸动作大全，跑步、健身、久坐都适用");

        collectAdapter = new MineCollectAdapter(list,this,R.layout.activity_mine_collect_item);
        listView.setAdapter(collectAdapter);
    }
}

