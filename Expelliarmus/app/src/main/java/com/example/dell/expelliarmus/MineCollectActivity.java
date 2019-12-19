package com.example.dell.expelliarmus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
//我的收藏

public class MineCollectActivity extends AppCompatActivity {
    private List<KeepFit> list = new ArrayList<>();
    private SwipeMenuListView listView ;
    private MineCollectAdapter collectAdapter;
    private ImageView imageView;
    private EditText etSearch;
    private ImageButton btnSearch;
    private List<Integer> ids;
    private int operation;
    private Util util;
    private User LoginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        operation=getIntent().getIntExtra("operation",0);
        imageView = findViewById(R.id.img_return);
        etSearch=findViewById(R.id.et_search);
        listView = findViewById(R.id.lv_shoucang);
        btnSearch=findViewById(R.id.btn_search);

        MyListener myListener=new MyListener();
        imageView.setOnClickListener(myListener);
        btnSearch.setOnClickListener(myListener);

        DisplayAllCollAsync displayAllCollAsync=new DisplayAllCollAsync();
        displayAllCollAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"six");


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
    }

    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    MineCollectActivity.this.finish();
                    break;
                case R.id.btn_search:
                    if (!etSearch.getText().toString().equals("")){
                        SearchSomeDataAsync searchSomeDataAsync=new SearchSomeDataAsync();
                        searchSomeDataAsync.execute(Constant.URL+"DisplaySomeCollection",LoginUser.getId(),etSearch.getText().toString());
                    }else {
                        DisplayAllCollAsync displayAllCollAsync=new DisplayAllCollAsync();
                        displayAllCollAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"six");
                    }
                    break;
            }
        }
    }

    /**
     * 展示收藏中的所有数据
     */
    public class DisplayAllCollAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("tableName",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);

                Util.closeIO(is,os);
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
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
            if (null!=o){
                if ("".equals(o.toString())){
                    Toast.makeText(MineCollectActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                }else {
                    if (null!=ids){
                        ids.clear();
                    }if (null!=list){
                        list.clear();
                    }

                    String idsMessage=o.toString();
                    String[] strIdsMessage=idsMessage.split("&");
                    String strIds=strIdsMessage[0];
                    String strMessage=strIdsMessage[1];

                    Gson gson=new Gson();
                    Type type1=new TypeToken<List<Integer>>(){}.getType();
                    ids=gson.fromJson(strIds,type1);
                    Type type2=new TypeToken<List<KeepFit>>(){}.getType();
                    list=gson.fromJson(strMessage,type2);
                }
            }else {
                Toast.makeText(MineCollectActivity.this,"没有收藏",Toast.LENGTH_SHORT).show();
            }
            collectAdapter = new MineCollectAdapter(list,ids,MineCollectActivity.this,R.layout.activity_mine_collect_item);
            listView.setAdapter(collectAdapter);
            collectAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 展示收藏中的部分数据
     */
    public class SearchSomeDataAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("content",objects[2]);
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
            Log.e("数据",o.toString());
            if (null==o){
                Toast.makeText(MineCollectActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if ("".equals(o.toString())){
                Toast.makeText(MineCollectActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
            }else {
                if (null!=ids){
                    ids.clear();
                }if (null!=list){
                    list.clear();
                }

                String idsMessage=o.toString();
                String[] strIdsMessage=idsMessage.split("&");
                String strIds=strIdsMessage[0];
                String strMessage=strIdsMessage[1];

                Gson gson=new Gson();
                Type type1=new TypeToken<List<Integer>>(){}.getType();
                ids=gson.fromJson(strIds,type1);
                Type type2=new TypeToken<List<KeepFit>>(){}.getType();
                list=gson.fromJson(strMessage,type2);
            }
            collectAdapter = new MineCollectAdapter(list,ids,MineCollectActivity.this,R.layout.activity_mine_collect_item);
            listView.setAdapter(collectAdapter);
            collectAdapter.notifyDataSetChanged();
        }
    }
}

