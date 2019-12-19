package com.example.dell.expelliarmus;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//最近阅读
public class MinePersonReadActivity extends AppCompatActivity {

    private List<KeepFit> list = new ArrayList<>();
    private ListView listView ;
    private IndexMyAdapter adapter;
    private ImageView imageView;
    private ImageButton btnSearch;
    private EditText etSearch;
    private Util util;
    private User LoginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_read);
        util=new Util(this);
        LoginUser=util.getUserInfo();

         imageView= findViewById(R.id.img_return);
        listView = findViewById(R.id.lv_read);
        btnSearch=findViewById(R.id.btn_search);
        etSearch=findViewById(R.id.et_search);

        DisplayReadAsync displayReadAsync=new DisplayReadAsync();
        displayReadAsync.execute(Constant.URL+"DisplayRecentRead",LoginUser.getId(),"all",etSearch.getText().toString());

        MyListener myListener=new MyListener();
        imageView.setOnClickListener(myListener);
        btnSearch.setOnClickListener(myListener);
    }
    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    finish();
                    break;
                case R.id.btn_search:
                    if ("".equals(etSearch.getText().toString())){
                        DisplayReadAsync displayReadAsync=new DisplayReadAsync();
                        displayReadAsync.execute(Constant.URL+"DisplayRecentRead",LoginUser.getId(),"all",etSearch.getText().toString());
                    }else {
                        DisplayReadAsync displayReadAsync=new DisplayReadAsync();
                        displayReadAsync.execute(Constant.URL+"DisplayRecentRead",LoginUser.getId(),"some",etSearch.getText().toString());
                    }
                    break;
            }
        }
    }

    /**
     * 最近阅读
     */
    public class DisplayReadAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("operation",objects[2]);
                object.put("content",objects[3]);
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
            if (null==o){
                Toast.makeText(MinePersonReadActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<KeepFit>>(){}.getType();
                list=gson.fromJson(o.toString(),type);
            }else {
                Toast.makeText(MinePersonReadActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            adapter = new IndexMyAdapter(0,list,MinePersonReadActivity.this,R.layout.index_detail_item);
            listView.setAdapter(adapter);
        }
    }
}

