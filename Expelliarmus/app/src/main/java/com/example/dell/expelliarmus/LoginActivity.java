package com.example.dell.expelliarmus;


import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//用户登录，分两种fragment
public class LoginActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;
    private List<TextView> textViewList = new ArrayList<>();
    private String TAG = this.getClass().getSimpleName();
    private ImageView qq ;
    private  ImageView weixin;
    Util util;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        util = new Util(this);
        //动态申请权限
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }

        qq = findViewById(R.id.youmeng_img_qq);
        weixin = findViewById(R.id.youmeng_img_weixin);

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin(qq);
            }
        });

        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weiXinLogin(weixin);
            }
        });

        int id=getIntent().getIntExtra("id",0);
        initFragment();
        setTabChangedListener();
        ImageView imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

    }

    private void initFragment(){
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tag1");
        tabSpec1.setIndicator(getTabSpecView("普通登录"));
        tabHost.addTab(tabSpec1,LoginCommonFragment.class,null);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tag2");
        tabSpec2.setIndicator(getTabSpecView("快速登录"));
        tabHost.addTab(tabSpec2, LoginQuickFragment.class,null);
        textViewList.get(0).setTextColor(Color.parseColor("#8bc34a"));
    }

    private View getTabSpecView(String title){
        View view = getLayoutInflater().inflate(R.layout.mine_tabspec,null);
        TextView textView = view.findViewById(R.id.tv_title);
        textView.setText(title);
        textViewList.add(textView);

        return view;
    }

    private void setTabChangedListener(){
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "tag1":
                        textViewList.get(0).setTextColor(Color.parseColor("#8bc34a"));
                        textViewList.get(1).setTextColor(getResources().getColor(android.R.color.black));
                        break;
                    case "tag2":
                        textViewList.get(0).setTextColor(getResources().getColor(android.R.color.black));
                        textViewList.get(1).setTextColor(Color.parseColor("#8bc34a"));
                        break;
                }
            }
        });
    }

    //友盟
    public void qqLogin(View view) {
        authorization(SHARE_MEDIA.QQ);
    }

    public void weiXinLogin(View view) {
        authorization(SHARE_MEDIA.WEIXIN);
    }


    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");//性别
                String iconurl = map.get("iconurl");

                Log.e("info","expires_in"+expires_in);

                //openid作为手机号；name作为userName
//                Toast.makeText(getApplicationContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();
                user = new User(name,uid,gender,iconurl);
//                util.downLoadImg(iconurl,uid);
//                util.upLoadImage(getFilesDir()+"/"+uid+".png",Constant.URL+"img/"+uid+".png");//保存头像到服务器

                //拿到信息去请求登录接口。。。
                QQFindUserByUid task = new QQFindUserByUid();
                task.execute(Constant.URL+"QQFindUserByUid");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
            }
        });
    }


        private class QQFindUserByUid extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String judge = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                if(user!=null){
                    String object = new Gson().toJson(user);
                    os.write(object.toString().getBytes());
                    InputStream is = connection.getInputStream();
                    judge = util.readInputStreamToString(is);
                    util.closeIO(is,os);
                }else{
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return judge;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //保存数据到本地
            int userId = Integer.parseInt((String)o);
            if(userId!=-1){
                user.setId(userId);
                util.saveUserInfo(user);
                String info = util.getUserInfo().toString();
                Log.e("user",info);
                setResult(2);
                finish();
            }else{
                Toast.makeText(LoginActivity.this,"登录失败!",Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }

}

