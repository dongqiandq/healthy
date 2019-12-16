package com.example.dell.expelliarmus;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

//重置密码
public class MineResetActivity extends AppCompatActivity {

    private EditText pwd1;
    private EditText pwd2;
    private Button submmit;
    private Util util = new Util(this);
    private String phoneNumber;
    private ImageView eye;
    private ImageView eyeOK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_reset);

        pwd1 = findViewById(R.id.et_reset_word);
        pwd2 = findViewById(R.id.et_reset_rewordOK);
        submmit = findViewById(R.id.btn_reset_submit);
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("tel");
        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPwd = pwd1.getText().toString();
                String surePwd = pwd2.getText().toString();
                if(inputPwd.length()==0){
                    Toast.makeText(MineResetActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else if(surePwd.length()==0){
                    Toast.makeText(MineResetActivity.this,"请确认密码",Toast.LENGTH_SHORT).show();
                }else if(!inputPwd.equals(surePwd)){
                    Toast.makeText(MineResetActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    ResetUserPwd task = new ResetUserPwd();
                    task.execute(Constant.URL+"ResetUserPwd");
                }
            }
        });

//        imageView = findViewById(R.id.img_return);
//        word = findViewById(R.id.et_reword);
//        wordOK = findViewById(R.id.et_rewordOK);
        eye = findViewById(R.id.img_eye);
        eyeOK = findViewById(R.id.img_eyeOK);
//        submit = findViewById(R.id.btn_submit);

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MineResetActivity.this.finish();
//            }
//        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEye(eye,pwd1);
            }
        });
        eyeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEye(eyeOK,pwd2);
            }
        });

//        //具体实现后台判断密码是否一致
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isSame(word,wordOK) == true){
//                    showDialog();
//                }else {
//                    showDialogs();
//                }
//            }
//        });

    }

    private class ResetUserPwd extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String result = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                JSONObject object = new JSONObject();
                object.put("phoneNumber",phoneNumber);
                object.put("newPwd",pwd1.getText().toString());
                os.write(object.toString().getBytes());
                InputStream is = connection.getInputStream();
                result = util.readInputStreamToString(is);
                util.closeIO(is,os);

            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o.equals("ok")){
               showDialog();
            }else{
                Toast.makeText(MineResetActivity.this,"修改失败，请重试",Toast.LENGTH_SHORT);
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
        builder.setMessage("您的密码已重置，请重新登录！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MineResetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    public void showDialogs(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("温馨提示");
//        builder.setMessage("您的密码不统一，请重新设置！");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    //判断密码统一与否
    public Boolean isSame(EditText editText,EditText editTextOK){
        String et = editText.getText().toString();
        String etOK = editTextOK.getText().toString();
        if (et.equals(etOK)){
            //将修改后的数据提交到数据库
            return true;
        }else {
            editText.setText("");
            editTextOK.setText("");
            return false;
        }
    }
}

