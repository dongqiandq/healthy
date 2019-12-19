package com.example.dell.expelliarmus;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class MinePersonNameActivity extends AppCompatActivity {

    private TextView imageView;
    private TextView save;
    private LinearLayout lvnick;
    private EditText nickName;
    private Util util;
    private User LoginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_name);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        imageView= findViewById(R.id.img_return);
        save = findViewById(R.id.tv_save);
        lvnick = findViewById(R.id.lv_nickName);
        nickName = findViewById(R.id.et_nickName);

        MyListener myListener=new MyListener();
        imageView.setOnClickListener(myListener);
        save.setOnClickListener(myListener);
    }

    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    finish();
                    break;
                case R.id.tv_save:
                    if ("".equals(nickName.getText().toString())){
                        Toast.makeText(MinePersonNameActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        String str = nickName.getText().toString();

                        UpdateNameAsync updateNameAsync=new UpdateNameAsync();
                        updateNameAsync.execute(Constant.URL+"UpdateUserName",LoginUser.getId(),str);

                        LoginUser.setUserName(str);
                        Intent intent = new Intent(MinePersonNameActivity.this,MinePersonDataActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    }
    /**
     * 修改用户昵称
     */
    public class UpdateNameAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("id",objects[1]);
                object.put("userName",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                byte[] buffer=new byte[256];
                int len=is.read(buffer);
                String info=new String(buffer,0,len);
                Util.closeIO(is,os);
                return info;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "no";
            } catch (IOException e) {
                e.printStackTrace();
                return "no";
            } catch (JSONException e) {
                e.printStackTrace();
                return "no";
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if ("no".equals((String)o)){
                Toast.makeText(MinePersonNameActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
            }if("ok".equals((String)o)){
                Toast.makeText(MinePersonNameActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
            }if("not".equals((String)o)){
                Toast.makeText(MinePersonNameActivity.this,"没有修改",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
