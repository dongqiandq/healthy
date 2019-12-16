package com.example.dell.expelliarmus;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;
//我的收藏

public class MineCollectActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private SwipeMenuListView listView ;
    private MineCollectAdapter collectAdapter;
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect);
        imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MineCollectActivity.this.finish();
//                Intent intent2 = new Intent(MineCollectActivity.this,MainActivity.class);
//                startActivity(intent2);
                finish();
            }
        });


        listView = findViewById(R.id.lv_shoucang);
        list.add("肌肉拉伸动作大全，跑步、健身、久坐都适用");
        list.add("肌肉拉伸动作大全，跑步、健身、久坐都适用");

        collectAdapter = new MineCollectAdapter(list,this,R.layout.activity_mine_collect_item);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(150);
                // set a icon
                deleteItem.setIcon(R.drawable.trashbin);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                switch (index)
                {
                    case 0:
                        list.remove(position);
                        collectAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        listView.setAdapter(collectAdapter);
    }
}

