package com.example.dell.expelliarmus;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

//我的药箱
public class MineBoxActivity extends AppCompatActivity {
    private List<MedicineChest> data = new ArrayList<>();
    private List<Integer> ids=new ArrayList<>();
    private MineBoxAdapter adapter;
    private ListView listView;
    private ImageView imageView;
    private ImageButton btnSearch;
    private EditText etSearch;
    private Util util;
    private User LoginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_box);
        util=new Util(this);
        LoginUser=util.getUserInfo();
         imageView= findViewById(R.id.img_return);
         btnSearch=findViewById(R.id.btn_search);
         etSearch=findViewById(R.id.et_search);
        listView = findViewById(R.id.lv_box);

        DisplayMedicineChestAsync displayMedicineChestAsync=new DisplayMedicineChestAsync();
        displayMedicineChestAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"medicine_chest");

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
                        DisplayMedicineChestAsync displayMedicineChestAsync=new DisplayMedicineChestAsync();
                        displayMedicineChestAsync.execute(Constant.URL+"DisplayCollection",LoginUser.getId(),"medicine_chest");
                    }else {
                        DisplaySomeMedAsync displaySomeMedAsync=new DisplaySomeMedAsync();
                        displaySomeMedAsync.execute(Constant.URL+"DisplaySomeMedKit",LoginUser.getId(),etSearch.getText().toString());
                    }
                    break;
            }
        }
    }

    /**
     * 展示收藏的药箱中的所有数据 medicine_chest
     */
    public class DisplayMedicineChestAsync extends AsyncTask {

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
            if (null==o){
                Toast.makeText(MineBoxActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"".equals(o.toString())){
                if (null!=ids){
                    ids.clear();
                }if (null!=data){
                    data.clear();
                }

                String idsMessage=o.toString();
                String[] strIdsMessage=idsMessage.split("&");
                String strIds=strIdsMessage[0];
                String strMessage=strIdsMessage[1];

                Gson gson=new Gson();
                Type type1=new TypeToken<List<Integer>>(){}.getType();
                ids=gson.fromJson(strIds,type1);
                Type type2=new TypeToken<List<MedicineChest>>(){}.getType();
                data=gson.fromJson(strMessage,type2);
            }else {
                Toast.makeText(MineBoxActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            adapter = new MineBoxAdapter(MineBoxActivity.this,data,ids,R.layout.activity_mine_box_item);
            listView.setAdapter(adapter);
        }
    }

    /**
     * 展示收藏的药箱中的部分数据 medicine_chest
     */
    public class DisplaySomeMedAsync extends AsyncTask{

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
                Toast.makeText(MineBoxActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"".equals(o.toString())){
                if (null!=ids){
                    ids.clear();
                }if (null!=data){
                    data.clear();
                }

                String idsMessage=o.toString();
                String[] strIdsMessage=idsMessage.split("&");
                String strIds=strIdsMessage[0];
                String strMessage=strIdsMessage[1];

                Gson gson=new Gson();
                Type type1=new TypeToken<List<Integer>>(){}.getType();
                ids=gson.fromJson(strIds,type1);
                Type type2=new TypeToken<List<MedicineChest>>(){}.getType();
                data=gson.fromJson(strMessage,type2);
//                Log.e("data大小",data.size()+"");
//                Log.e("ids大小",ids.size()+"");
            }else {
                Toast.makeText(MineBoxActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
            }
            adapter = new MineBoxAdapter(MineBoxActivity.this,data,ids,R.layout.activity_mine_box_item);
            listView.setAdapter(adapter);
        }
    }
}

