package com.example.dell.expelliarmus;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    private ListView listView;
    private IndexMyAdapter myAdapter;
    private TextView title;
    private int flag;
    private String tableName;
    private ImageButton back;
    private ImageButton ibSearch;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        title = findViewById(R.id.tv_title);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);
        judgeItem();
        findViewsJianshen();

        DisplayTableDataAsync displayTableDataAsync=new DisplayTableDataAsync();
        displayTableDataAsync.execute(Constant.URL+"DisplayOneTable",tableName);

        back = findViewById(R.id.btn_title_back);
        MyListener myListener=new MyListener();
        back.setOnClickListener(myListener);
        ibSearch.setOnClickListener(myListener);
    }

    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_title_back:
                    ListViewActivity.this.finish();
                    break;
                case R.id.btn_search:
                    if (!etSearch.getText().toString().equals("")){
                        SearchSomeDataAsync searchSomeDataAsync=new SearchSomeDataAsync();
                        searchSomeDataAsync.execute(Constant.URL+"SearchSomeData",tableName,etSearch.getText().toString());
                    }else {
                        DisplayTableDataAsync displayTableDataAsync=new DisplayTableDataAsync();
                        displayTableDataAsync.execute(Constant.URL+"DisplayOneTable",tableName);
                    }
                    break;
            }
        }
    }

    /**
     * 从数据库中查询某张表的某些数据
     */
    public class SearchSomeDataAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("tableName",objects[1]);
                object.put("search",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            List<KeepFit> lists=null;
            if (null==o){
                Toast.makeText(ListViewActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"not".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<KeepFit>>(){}.getType();
                lists=gson.fromJson(o.toString(),type);
            }
            else {
                Toast.makeText(ListViewActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            myAdapter = new IndexMyAdapter(lists,ListViewActivity.this,R.layout.index_detail_item);
            listView.setAdapter(myAdapter);
        }
    }

    /**
     * 从数据库中查询某张表的数据
     */
    public class DisplayTableDataAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("tableName",objects[1]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                Gson gson=new Gson();
                Type type=new TypeToken<List<KeepFit>>(){}.getType();
                List<KeepFit> keepFits=gson.fromJson(content,type);
                if (keepFits.size()==0){
                    return "not";
                }else {
                    return content;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            List<KeepFit> lists=null;
            if (null==o){
                Toast.makeText(ListViewActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"not".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<KeepFit>>(){}.getType();
                lists=gson.fromJson(o.toString(),type);
            }
            else {
                Toast.makeText(ListViewActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            myAdapter = new IndexMyAdapter(lists,ListViewActivity.this,R.layout.index_detail_item);
            listView.setAdapter(myAdapter);
        }
    }

    private void findViewsJianshen(){
        listView = findViewById(R.id.lv_view);
        ibSearch=findViewById(R.id.btn_search);
        etSearch=findViewById(R.id.et_search);
    }

    public void judgeItem(){
        switch (flag){
            case Context.ITEM1:
                title.setText("健身");
                tableName="keep_fit";
                break;
            case Context.ITEM2:
                title.setText("养生");
                tableName="health";
                break;
            case Context.ITEM3:
                title.setText("疾病");
                tableName="disease";
                break;
            case Context.ITEM4:
                title.setText("护眼");
                tableName="eye_protection";
                break;
            case Context.ITEM5:
                title.setText("心理");
                tableName="psychological";
                break;
            case Context.ITEM6:
                title.setText("保健");
                tableName="health_care";
                break;
        }
    }
}

