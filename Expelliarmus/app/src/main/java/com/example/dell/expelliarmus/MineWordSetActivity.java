package com.example.dell.expelliarmus;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class MineWordSetActivity extends AppCompatActivity {

    private TextView oldPwd;
    private EditText newPwd;
    private EditText makeSurePwd;
    private Button btn;
    private Util util = new Util(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_word_set);


        //*************修改用户密码******************//
        oldPwd = findViewById(R.id.et_oldPwd);
        newPwd = findViewById(R.id.et_newPwd);
        makeSurePwd = findViewById(R.id.et_makeSurePwd);
        btn = findViewById(R.id.btn_fixPwd);
        //*************修改用户密码******************//
        TextView imageView = findViewById(R.id.img_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        TextView textView = findViewById(R.id.tv_ok);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        //修改用户密码点击事件
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPwd.getText().toString();
                String makeSurePassword = makeSurePwd.getText().toString();
                if(newPassword.equals("") && newPassword==null){
                    Toast.makeText(MineWordSetActivity.this,"请输入修改密码！",Toast.LENGTH_SHORT).show();
                }else if(makeSurePassword.equals("") && makeSurePassword==null){
                    Toast.makeText(MineWordSetActivity.this,"请输入确认密码！",Toast.LENGTH_SHORT).show();
                }else if(!newPassword.equals(makeSurePassword)){
                    Toast.makeText(MineWordSetActivity.this,"两次输入的密码不一致！",Toast.LENGTH_SHORT).show();
                }else{
                    FixUserPwd task = new FixUserPwd();
                    task.execute(Constant.URL+"FixUserPwd",newPassword);
                }
            }
        });
    }

    //用户修改密码的异步任务
    public class FixUserPwd extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String judge = "false";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                JSONObject object = new JSONObject();
                object.put("userId",LoginUser.getId());
                object.put("newPwd",(String)objects[1]);
                os.write(object.toString().getBytes());
                InputStream is = connection.getInputStream();
                judge = util.readInputStreamToString(is);
                Log.e("test",judge);
                util.closeIO(is,os);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return judge;
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            if(o.equals("ok")){
                Toast.makeText(MineWordSetActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MineWordSetActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
