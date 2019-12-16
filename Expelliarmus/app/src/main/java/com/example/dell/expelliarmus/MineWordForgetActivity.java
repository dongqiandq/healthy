package com.example.dell.expelliarmus;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//忘记密码
public class MineWordForgetActivity extends AppCompatActivity{

    private Boolean flag = true;
    private Util util = new Util(this);
    private EditText phone;
    private int i = 30;
    private User user;
    private EditText code;
    private Button getCode;
    private Button submmit;
    private EventHandler eh;
    private Handler mainHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.RESEND_VERIFICATION_CODE_FLAG){
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
                        Intent intent = new Intent(MineWordForgetActivity.this,MineResetActivity.class);
                        intent.putExtra("tel",phone.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                } else {//其他出错情况
                    util.mainHandlerSetToGetCodeError(flag,getCode,MineWordForgetActivity.this,phone);
                }
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_word_forget);
        findView();
        MobSDK.init(this, "2d6cbb3e871aa", "bbb7d5e422e4d7aedb1f9f44515920ee");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
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

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 通过规则判断手机号
                if(util.judPhone(MineWordForgetActivity.this,phone))//去掉左右空格获取字符串，是正确的手机号
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

        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(util.judCord(MineWordForgetActivity.this,code,phone)){//判断验证码
                    String phoneNum = phone.getText().toString();
                    String codeNum = code.getText().toString();
                    SMSSDK.submitVerificationCode("86", phoneNum, codeNum);
                }
            }
        });

//        ImageView imageView = findViewById(R.id.img_return);
//        Button get = findViewById(R.id.btn_get);
//        Button submit = findViewById(R.id.btn_submit);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MineWordForgetActivity.this,LoginActivity.class);
//                startActivity(intent);
//                MineWordForgetActivity.this.finish();
//            }
//        });

//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(MineWordForgetActivity.this,MineResetActivity.class);
//                startActivity(intent1);
//                finish();
//            }
//        });
    }

    public void findView(){
        //**快速登录**//
        phone = findViewById(R.id.et_forget_phone);
        code = findViewById(R.id.et_forget_yanzheng);
        getCode = findViewById(R.id.btn_forget_getcode);
       submmit = findViewById(R.id.btn_forget_submit);
        //**快速登录**//
    }

    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}

