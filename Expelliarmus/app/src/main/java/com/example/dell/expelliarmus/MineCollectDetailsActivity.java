package com.example.dell.expelliarmus;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.net.MalformedURLException;
import java.util.List;

//收藏详情页
public class MineCollectDetailsActivity extends AppCompatActivity {
    private TextView back;
    private TextView tvCaption;
    private TextView tvVersion;
    private int id;
    private int collectionId;
    private  LinearLayout collect;
    private String tableName;
    private ImageView ivSollect;
    private TextView tvSollect;
    private Util util;
    private User LoginUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect_details);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        back= findViewById(R.id.btn_backm);
        tvCaption=findViewById(R.id.tv_caption);
        tvVersion=findViewById(R.id.tv_version);
        collect= findViewById(R.id.ll_collect);
        ivSollect= findViewById(R.id.iv_sollect);
        tvSollect= findViewById(R.id.tv_sollect);

        id=getIntent().getIntExtra("id",0);
        collectionId=getIntent().getIntExtra("collectionId",0);

        tvCaption.setText(getIntent().getStringExtra("titles"));
        tvVersion.setText(getIntent().getStringExtra("content"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MineCollectDetailsActivity.this,MineCollectActivity.class);
                startActivity(intent);
                finish();
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkColor()){//取消收藏
                    ivSollect.setImageResource(R.drawable.mine_shoucang);
                    tvSollect.setTextColor(Color.parseColor("#D3D3D3"));
                    SixCancelCollAsync sixCancelCollAsync=new SixCancelCollAsync();
                    sixCancelCollAsync.execute(Constant.URL+"DetailSixCollection",0,collectionId);
                }
                else {//收藏
                    if (null!=tableName){
                        ivSollect.setImageResource(R.drawable.shoucangxuanhzong);
                        tvSollect.setTextColor(Color.parseColor("#8bc34a"));
                        SixCollAsync sixCollAsync=new SixCollAsync();
                        sixCollAsync.execute(Constant.URL+"DetailSixCollection",1,LoginUser.getId(),tableName,id);
                    }else {
                        Toast.makeText(MineCollectDetailsActivity.this,"请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MineCollectDetailsActivity.this,MineCollectActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
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

    /**
     * 点击取消收藏
     */
    public class SixCancelCollAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("id",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                tableName=content;

                Util.closeIO(is,os);
                return tableName;
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
                Toast.makeText(MineCollectDetailsActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MineCollectDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 再次点击收藏，可以进行收藏操作
     */
    public class SixCollAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("userId",objects[2]);
                object.put("tableName",objects[3]);
                object.put("tableId",objects[4]);
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
                Toast.makeText(MineCollectDetailsActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (Integer.parseInt(o.toString())!=0){
                collectionId=Integer.parseInt(o.toString());
//                Log.e("值",collectionId+"");
                Toast.makeText(MineCollectDetailsActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MineCollectDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

