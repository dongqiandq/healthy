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

//我的菜谱
public class MineMenuActivity extends AppCompatActivity {
    private List<CookBook> list=new ArrayList<>();
    private List<Integer> ids=new ArrayList<>();
    private ImageView imageView;
    private ListView lvMenu;
    private ImageButton btnSearch;
    private EditText etSearch;
    private Util util;
    private User LoginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_menu);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        imageView = findViewById(R.id.img_return);
        lvMenu=findViewById(R.id.lv_menu);
        btnSearch=findViewById(R.id.btn_search);
        etSearch=findViewById(R.id.et_search);

        MyListener myListener=new MyListener();
        imageView.setOnClickListener(myListener);
        btnSearch.setOnClickListener(myListener);

        DisplayCookBookAsync displayCookBookAsync=new DisplayCookBookAsync();
        displayCookBookAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"cookbook");
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    finish();
                    break;
                case R.id.btn_search:
                    if ("".equals(etSearch.getText().toString())){
                        DisplayCookBookAsync displayCookBookAsync=new DisplayCookBookAsync();
                        displayCookBookAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"cookbook");
                    }else {
                        DisplaySomeCookBookAsync displaySomeCookBookAsync=new DisplaySomeCookBookAsync();
                        displaySomeCookBookAsync.execute(Constant.URL+"DisplaySomeCookBook",LoginUser.getId(),etSearch.getText().toString());
                    }
                    break;
            }
        }
    }

    /**
     * 展示收藏的食谱中的所有数据
     */
    public class DisplayCookBookAsync extends AsyncTask {

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
        protected void onPostExecute(Object o){
            if (null!=o){
                if ("".equals(o.toString())){
                    Toast.makeText(MineMenuActivity.this,"没有收藏食谱",Toast.LENGTH_SHORT).show();
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
                    Type type2=new TypeToken<List<CookBook>>(){}.getType();
                    list=gson.fromJson(strMessage,type2);
                }
            }else {
                Toast.makeText(MineMenuActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }
            MineMenuAdapter adapter=new MineMenuAdapter(MineMenuActivity.this,list,ids,R.layout.activity_mine_menu_item);
            lvMenu.setAdapter(adapter);
        }
    }

    /**
     * 展示收藏的食谱中的部分数据
     */
    public class DisplaySomeCookBookAsync extends AsyncTask{

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
            if (null==o){
                Toast.makeText(MineMenuActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else {
                if ("".equals(o.toString())){
                    Toast.makeText(MineMenuActivity.this,"没有收藏食谱",Toast.LENGTH_SHORT).show();
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
                    Type type2=new TypeToken<List<CookBook>>(){}.getType();
                    list=gson.fromJson(strMessage,type2);
                }
            }
            MineMenuAdapter adapter=new MineMenuAdapter(MineMenuActivity.this,list,ids,R.layout.activity_mine_menu_item);
            lvMenu.setAdapter(adapter);
        }
    }
}

