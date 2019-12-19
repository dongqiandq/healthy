package com.example.dell.expelliarmus;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

public class MineMenuDetailsActivity extends AppCompatActivity{

    private int id;
    private int menuId;
    private TextView back;
    private ImageView ivDetailPhoto;
//    private ImageView ivZan;
//    private TextView zanCount;
    private TextView tvName;
    private TextView tvMessage;
    private Button btnAddFood;
    private LinearLayout llMaterials;
    private LinearLayout llProcedure;
    private String[] materials1;
    private String[] materials2;
    private String[] steps;
    private String[] images;
    private Util util;
    private User LoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_menu_details);
        util=new Util(this);
        LoginUser=util.getUserInfo();

        findViews();
        id=getIntent().getIntExtra("id",0);
        menuId=getIntent().getIntExtra("menuId",0);

        MyListener myListener=new MyListener();
        back.setOnClickListener(myListener);
        btnAddFood.setOnClickListener(myListener);
    }

    private void findViews(){
        back= findViewById(R.id.btn_backm);
        ivDetailPhoto=findViewById(R.id.iv_detailPhoto);
        String strImages=getIntent().getStringExtra("images");
        images=strImages.split(",");
        String image=images[0];
        Util.getDBImage(this,Constant.URL+"img/"+image,ivDetailPhoto);

//        ivZan = findViewById(R.id.iv_zan);
//        zanCount = findViewById(R.id.zanCount);
//        zanCount.setText(getIntent().getIntExtra("likeNumber",0)+"");

        tvName=findViewById(R.id.tv_name);
        tvName.setText(getIntent().getStringExtra("name"));

        tvMessage=findViewById(R.id.tv_message);
        tvMessage.setText(getIntent().getStringExtra("description"));

        btnAddFood = findViewById(R.id.btn_insert);
        llMaterials = findViewById(R.id.ll_materials);
        String strMaterials=getIntent().getStringExtra("material");
        String[] materials=strMaterials.split(",");
        materials1=new String[materials.length/2];
        materials2=new String[materials.length/2];
        int m=0,n=0;
        for (int i=0;i<materials.length;i++){
            if (i%2==0){
                materials1[m]=materials[i];
                m++;
            }else {
                materials2[n]=materials[i];
                n++;
            }
        }
        addItem();
        llProcedure = findViewById(R.id.ll_procedure);
        String strSteps=getIntent().getStringExtra("steps");
        steps=strSteps.split("!");
        addProduce();
    }

    public class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_return:
                    Intent intent=new Intent(MineMenuDetailsActivity.this,MineMenuActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_insert:
                    if ("取消收藏".equals(btnAddFood.getText().toString())){
                        CancelMenuAsync cancelMenuAsync=new CancelMenuAsync();
                        cancelMenuAsync.execute(Constant.URL+"DetailMenu",0,menuId);
                    }else {
                        MenuCollAsync menuCollAsync=new MenuCollAsync();
                        menuCollAsync.execute(Constant.URL+"DetailMenu",1,LoginUser.getId(),id);
                    }
                    break;
            }
        }
    }

    /**
     * 取消食谱中某个item的收藏
     */
    public class CancelMenuAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("menuId",objects[2]);
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
                Toast.makeText(MineMenuDetailsActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if ("ok".equals(o.toString())){
                btnAddFood.setText("加入菜谱");
            }else {
                Toast.makeText(MineMenuDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 点击取消收藏后再点击加入菜谱按钮
     */
    public class MenuCollAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("userId",objects[2]);
                object.put("cookBookId",objects[3]);
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
                Toast.makeText(MineMenuDetailsActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else{
                Integer menuId0=Integer.parseInt(o.toString());
                if (menuId0==0){
                    Toast.makeText(MineMenuDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
                }else {
                    menuId=menuId0;
                    btnAddFood.setText("取消收藏");
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MineMenuDetailsActivity.this,MineMenuActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }

    //添加烹饪步骤
    public void addProduce(){
        for (int i=0;i<steps.length;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(this);
            params.setMargins(0,10,0,0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(params);
            addProduceCount(linearLayout,i);
            addProduceMessage(linearLayout,i);
            llProcedure.addView(linearLayout);
        }
    }

    //烹饪步骤的数字
    private void addProduceCount(LinearLayout linearLayout,int i){
        final TextView pcText = new TextView(this);
        pcText.setTextSize(24);
        pcText.setText(i+1+"");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(35,0,0,0);
        pcText.setLayoutParams(params);
        linearLayout.addView(pcText);
    }
    //添加烹饪步骤中的图片及文字描述
    private void addProduceMessage(LinearLayout linearLayout,int i){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setLayoutParams(params);
        addPhoto(linearLayout1,i);
        addMessage(linearLayout1,i);
        linearLayout.addView(linearLayout1);
    }
    //步骤中的图片
    private void addPhoto(LinearLayout linearLayout,int i){
        final ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams paramsPhoto = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (!(images.length-2<i)){
            Util.getDBImage(MineMenuDetailsActivity.this,Constant.URL+"img/"+images[i+1],imageView);
        }else {
            Util.getDBImage(MineMenuDetailsActivity.this,Constant.URL+"img/none.png",imageView);
        }
        linearLayout.addView(imageView);
    }
    //步骤中的文字描述
    private void addMessage(LinearLayout linearLayout,int i){
        final TextView meText = new TextView(this);
        meText.setTextSize(22);
        meText.setText(steps[i]);
        LinearLayout.LayoutParams paramsMe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsMe.setMargins(0,5,0,5);
        meText.setLayoutParams(paramsMe);
        linearLayout.addView(meText);
    }

    //添加材料
    public void addItem(){
        for (int i=0;i<materials1.length;i++){
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(params);
            addView(linearLayout,i);
            addViewCount(linearLayout,i);
            //偶数行设置背景颜色为灰色
            if (i%2!=0){
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colroMatreial));
            }
            llMaterials.addView(linearLayout);
        }

    }

    //材料
    private TextView addView(final LinearLayout linearLayout, int i){
        final TextView materialText = new TextView(this);
        materialText.setTextSize(20);
        materialText.setText(materials1[i]);
//        //set 文本大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=5;
        //set 四周距离
        params.setMargins(0,7,0,7);
        materialText.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(materialText);
        return materialText;
    }

    //材料数量
    private TextView addViewCount(final LinearLayout linearLayout,int i){
        final TextView countText = new TextView(this);
        countText.setTextSize(20);
        countText.setText(materials2[i]);
        //set 文本大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=2;
        //set 四周距离
        params.setMargins(0,7,0,7);
        countText.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(countText);
        return countText;
    }

}

