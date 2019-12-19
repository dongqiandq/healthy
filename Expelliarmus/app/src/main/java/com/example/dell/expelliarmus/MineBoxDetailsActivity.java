package com.example.dell.expelliarmus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class MineBoxDetailsActivity extends AppCompatActivity {
    private TextView imageView;
    private TextView tvCancel;
    private TextView tv1Tong;
    private TextView tv3English;
    private TextView tv4Pinyin;
    private TextView tvTraits;//性状
    private TextView tvIndication;//适应症
    private TextView tvDosage;//用法用量
    private TextView tvPrecautions;//注意事项
    private int id;
    private int medKitId;
    private Util util;
    private User LoginUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_box_details);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        id=getIntent().getIntExtra("id",0);
        medKitId=getIntent().getIntExtra("medKitId",0);
        findViews();

        MyListener myListener=new MyListener();
        imageView.setOnClickListener(myListener);
        tvCancel.setOnClickListener(myListener);
    }

    private void findViews(){
        imageView= findViewById(R.id.img_return);

        tvCancel=findViewById(R.id.tv_cancel);

        tv1Tong=findViewById(R.id.tv1);
        tv1Tong.setText(getIntent().getStringExtra("name"));

        tv3English=findViewById(R.id.tv3);
        tv3English.setText(getIntent().getStringExtra("englishName"));

        tv4Pinyin=findViewById(R.id.tv4);
        tv4Pinyin.setText(getIntent().getStringExtra("chineseName"));

        tvTraits=findViewById(R.id.tv_mine_traits);
        tvTraits.setText(getIntent().getStringExtra("traits"));

        tvIndication=findViewById(R.id.tv_mine_indication);
        tvIndication.setText(getIntent().getStringExtra("indication"));

        tvDosage=findViewById(R.id.tv_mine_dosage);
        tvDosage.setText(getIntent().getStringExtra("dosage"));

        tvPrecautions=findViewById(R.id.tv_mine_precautions);
        tvPrecautions.setText(getIntent().getStringExtra("precautions"));
    }

    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    Intent intent=new Intent(MineBoxDetailsActivity.this,MineBoxActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tv_cancel:
                    if ("取消收藏".equals(tvCancel.getText().toString())){
                        CancelMedKitAsync cancelMedKitAsync=new CancelMedKitAsync();
                        cancelMedKitAsync.execute(Constant.URL+"DetailMedKit",0,medKitId);
                    }else {
                        MedKitCollAsync medKitCollAsync=new MedKitCollAsync();
                        medKitCollAsync.execute(Constant.URL+"DetailMedKit",1,LoginUser.getId(),id);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MineBoxDetailsActivity.this,MineBoxActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    /**
     * 点击“取消收藏”时，取消收藏
     */
    public class CancelMedKitAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("medKitId",objects[2]);
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
                Toast.makeText(MineBoxDetailsActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else{
                tvCancel.setText("加入药箱");
            }
        }
    }

    /**
     * 点击取消收藏后再点击加入药箱按钮
     */
    public class MedKitCollAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("userId",objects[2]);
                object.put("medicineChestId",objects[3]);
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
                Toast.makeText(MineBoxDetailsActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else{
                Integer medKitId0=Integer.parseInt(o.toString());
//                Log.e("medKitId0大小",medKitId0+"");
                if (medKitId0==0){
                    Toast.makeText(MineBoxDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
                }else {
                    medKitId=medKitId0;
                    tvCancel.setText("取消收藏");
                }
            }
        }
    }
}
