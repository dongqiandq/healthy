package com.example.dell.expelliarmus;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mob.MobSDK;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//注册
public class MineRegisterActivity extends AppCompatActivity {

    private User user;
    private EditText phone;
    private Util util = new Util(this);
    private EditText pwd;
    private EditText surePwd;
    private EditText code;
    private Button getCode;
    private Button register;
    private boolean flag = true;
    private int i=30;
    private CheckBox box;
    private EventHandler eh;
    private ImageView eye;
    private ImageView eyeOK;
    private Handler mainHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.RESEND_VERIFICATION_CODE_FLAG) {
                getCode.setText("重新发送(" + msg.arg1 + ")");
            } else if (msg.what == Constant.GET_VERIFICATION_CODE_FLAG){
                getCode.setText("获取验证码");
                getCode.setClickable(true);
                i = 30;
            } else {
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                //回调完成
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        RegisterUser task = new RegisterUser();
                        task.execute(Constant.URL+"RegisterUser");
                    }
                } else {//其他出错情况
                    util.mainHandlerSetToGetCodeError(flag,getCode,MineRegisterActivity.this,phone);
                }
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_register);

        MobSDK.init(MineRegisterActivity.this, "2d6cbb3e871aa", "bbb7d5e422e4d7aedb1f9f44515920ee");
        ActivityCompat.requestPermissions(MineRegisterActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        SMSSDK.setAskPermisionOnReadContact(true);
        eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();//创建了一个对象
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                mainHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
        findView();
        setListener();

        ImageView imageView = findViewById(R.id.img_return);
        eye = findViewById(R.id.img_eye);
        eyeOK = findViewById(R.id.img_eyeOK);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineRegisterActivity.this.finish();
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEye(eye,pwd);
            }
        });
        eyeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEye(eyeOK,surePwd);
            }
        });


    }

    public void findView(){
        phone = findViewById(R.id.et_register_phone);
        pwd = findViewById(R.id.et_register_word);
        surePwd = findViewById(R.id.et_register_wordOK);
        code = findViewById(R.id.et_register_yanzheng);
        getCode = findViewById(R.id.btn_register_getcode);
        register = findViewById(R.id.btn_register);
        box = findViewById(R.id.checkbox);
    }

    public void setListener(){
        //注册设置点击事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().length()==0){
                    Toast.makeText(MineRegisterActivity.this,"请填写电话号码",Toast.LENGTH_SHORT).show();
                }else if(pwd.getText().length()==0){
                    Toast.makeText(MineRegisterActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else if(surePwd.getText().length()==0){
                    Toast.makeText(MineRegisterActivity.this,"请确认密码",Toast.LENGTH_SHORT).show();
                }else if(!pwd.getText().toString().equals(surePwd.getText().toString())){
                    Toast.makeText(MineRegisterActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }else if(!box.isChecked()){
                    Toast.makeText(MineRegisterActivity.this,"请先阅读协议",Toast.LENGTH_SHORT).show();
                }else{
                    if(util.judCord(MineRegisterActivity.this,code,phone)){//判断验证码
                        String phoneNum = phone.getText().toString();
                        String codeNum = code.getText().toString();
                        SMSSDK.submitVerificationCode("86", phoneNum, codeNum);
                    }
                }

            }
        });


        //获取验证码点击事件
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 通过规则判断手机号
                if(util.judPhone(MineRegisterActivity.this,phone))//去掉左右空格获取字符串，是正确的手机号
                {
                    String phoneNum = phone.getText().toString();
                    // 2. 通过sdk发送短信验证
                    SMSSDK.getVerificationCode("86", phoneNum);
                    // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                    getCode.setClickable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            util.listenerToGetCode(mainHandler,i);
                        }
                    }).start();
                }
            }
        });

    }

    private class RegisterUser extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String judge = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                String telePhone = phone.getText().toString();
                String password = pwd.getText().toString();
                user = new User();
                user.setPhoneNumber(telePhone);
                user.setUserPassword(password);
                JSONObject object = new JSONObject();
                object.put("tel",telePhone);
                object.put("pwd",password);
                os.write(object.toString().getBytes());
                InputStream is = connection.getInputStream();
                judge = util.readInputStreamToString(is);
                util.closeIO(is,os);

            }catch (Exception e){
                e.printStackTrace();
            }
            return judge;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o.equals("no")){
//                Toast.makeText(MineRegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
            }else{
                Log.e("user",o.toString());
                showDialog();
            }
        }
    }

    //设置密码可见和不可见
    private void setPasswordEye(ImageView imageView,EditText editText) {

        if (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == editText.getInputType()) {
            //如果可见就设置为不可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //修改眼睛图片
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eye_close));
        } else {
            //如果不可见就设置为可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //修改图片
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eye_open));
        }
        //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
        editText.setSelection(editText.getText().toString().length());
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("注册成功，请登录！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(MineRegisterActivity.this,LoginActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
