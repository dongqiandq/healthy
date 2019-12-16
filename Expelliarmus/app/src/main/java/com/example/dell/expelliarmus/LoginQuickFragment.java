package com.example.dell.expelliarmus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mob.MobSDK;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginQuickFragment extends Fragment {

    private View thisView;
    private Context context;
    private Boolean flag = true;
    private Util util ;
    private EditText phone;
    private int i = 30;
    private User user;
    private EditText code;
    private Button getCode;
    private Button fastLogin;
    private EventHandler eh;
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
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){//提交验证码成功
                        FindUserByPhoneNumber task = new FindUserByPhoneNumber();
                        task.execute(Constant.URL+"FindUserByPhoneNumber");
                    }
                } else {//其他出错情况
                    util.mainHandlerSetToGetCodeError(flag,getCode,context,phone);
                }
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_quick,container,false);
        context = getContext();
        thisView = view;
        util = new Util(context);

        MobSDK.init(context, "2d6cbb3e871aa", "bbb7d5e422e4d7aedb1f9f44515920ee");
        findView();
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
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
                if(util.judPhone(context,phone))//去掉左右空格获取字符串，是正确的手机号
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

        fastLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(util.judCord(context,code,phone)){//判断验证码
                    String phoneNum = phone.getText().toString();
                    String codeNum = code.getText().toString();
                    SMSSDK.submitVerificationCode("86", phoneNum, codeNum);
                }
            }
        });


//        Button login = view.findViewById(R.id.btn_fastlogin);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent1);
//                getActivity().finish();
//            }
//        });
        return view;
    }

    public void findView(){
        //**快速登录**//
        phone = thisView.findViewById(R.id.et_fastlogin_phone);
        code = thisView.findViewById(R.id.et_fastlogin_yanzheng);
        getCode = thisView.findViewById(R.id.btn_fastlogin_getcode);
        fastLogin = thisView.findViewById(R.id.btn_fastlogin);
    }

    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    private class FindUserByPhoneNumber extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String judge = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                String telePhone = phone.getText().toString();
                JSONObject object = new JSONObject();
                object.put("phoneNumber",telePhone);
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
                Toast.makeText(context,"请先注册！",Toast.LENGTH_SHORT).show();
            }else{
                flag = false;
                //保存数据到本地
                user = new Gson().fromJson((String)o,User.class);
                util.saveUserInfo(user);
                Log.e("user",util.getUserInfo().toString());
                getActivity().setResult(2);
                getActivity().finish();
            }
        }

    }
}

