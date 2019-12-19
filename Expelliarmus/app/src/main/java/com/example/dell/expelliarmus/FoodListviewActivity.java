package com.example.dell.expelliarmus;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class FoodListviewActivity extends AppCompatActivity {
    private List<CookBook> list = new ArrayList<>();
    private ListView listView;
    private FoodAdapter myAdapter;
    private int flag;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_listview);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);
        judgeMenu();
        initData();


        TextView back = findViewById(R.id.btn_backm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodListviewActivity.this.finish();
            }
        });
    }

    private void findViews(){
        listView = findViewById(R.id.lv_jianpikaiwei);
        myAdapter = new FoodAdapter(FoodListviewActivity.this,list,R.layout.list_food_item);
        listView.setAdapter(myAdapter);
    }
    private void initData(){
        DisplayOneTypeAsync displayOneTypeAsync=new DisplayOneTypeAsync();
        displayOneTypeAsync.execute(Constant.URL+"DisplayOneType",category);
    }

    public void judgeMenu(){
        switch (flag){
            case FoodConstent.MENU1:
                category="健脾开胃";
                break;
            case FoodConstent.MENU2:
                category="补血益气";
                break;
            case FoodConstent.MENU3:
                category="温补驱寒";
//                initData();
//                findViews();
                break;
            case FoodConstent.MENU4:
                category="清肺止咳";
                break;
            case FoodConstent.MENU5:
                category="减脂健身";
                break;
            case FoodConstent.MENU6:
                category="经期食谱";
                break;
            case FoodConstent.MENU7:
                category="美容养颜";
                break;
            case FoodConstent.MENU8:
                category="滋阴补肾";
                break;
            case FoodConstent.MENU9:
                category="美食";
                break;
            case FoodConstent.MENU10:
                category="特色";
                break;
            case FoodConstent.MENU11:
                category="独家菜谱";
                break;
            case FoodConstent.MENU12:
                category="新鲜菜";
                break;
        }
    }

    /**
     * 查询特定类型的菜单
     */
    public class DisplayOneTypeAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os= connection.getOutputStream();
                os.write(objects[1].toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if (null==o){
                Toast.makeText(FoodListviewActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"not".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<CookBook>>(){}.getType();
                list=gson.fromJson(o.toString(),type);
            }
            else {
                Toast.makeText(FoodListviewActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            findViews();
        }
    }
}
