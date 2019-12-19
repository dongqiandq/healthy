package com.example.dell.expelliarmus;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class FoodDetailsActivity extends AppCompatActivity {
    private int id;
    private int ITEM=0;
    private int count;
    private int ITEM1=0;
    private TextView back;
    private ImageView ivDetailPhoto;
    private ImageView ivZan;
    private TextView zanCount;
    private TextView tvName;
    private TextView tvMessage;
    private Button btnAddFood;
    private LinearLayout llMaterials;
    private LinearLayout llProcedure;
    private String[] materials1;
    private String[] materials2;
    private String[] steps;
    private String[] images;
    private User LoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Util util=new Util(FoodDetailsActivity.this);
        LoginUser=util.getUserInfo();
        findViews();
        id=getIntent().getIntExtra("id",0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodDetailsActivity.this.finish();
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUser.getPhoneNumber().length()>0){
                    if (ITEM==0){
                        ITEM=1;
                        UpdateMenuAsync updateMenuAsync=new UpdateMenuAsync();
                        updateMenuAsync.execute(Constant.URL+"MenuCollection",LoginUser.getId(),id);
                    }
                }else {
                    Toast.makeText(FoodDetailsActivity.this,"请先登录，再收藏",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUser.getPhoneNumber().length()>0){
                    if (ITEM1 == 0){
                        ivZan.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zanxuanzhong));
                        zanCount.setText(Integer.parseInt(zanCount.getText().toString())+1+"");
                        ITEM1 = 1;
                        UpdateLikeNumAsync updateLikeNumAsync1=new UpdateLikeNumAsync();
                        updateLikeNumAsync1.execute(Constant.URL+"UpdateLikeNumber",id,"cookbook",1);
                    }else {
                        ivZan.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.zan));
                        zanCount.setText(Integer.parseInt(zanCount.getText().toString())-1+"");
                        ITEM1 = 0;
                        UpdateLikeNumAsync updateLikeNumAsync2=new UpdateLikeNumAsync();
                        updateLikeNumAsync2.execute(Constant.URL+"UpdateLikeNumber",id,"cookbook",-1);
                    }
                }else {
                    Toast.makeText(FoodDetailsActivity.this,"请先登录，再点赞",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findViews(){
        back= findViewById(R.id.btn_backm);
        ivDetailPhoto=findViewById(R.id.iv_detailPhoto);
        String strImages=getIntent().getStringExtra("images");
        images=strImages.split(",");
        String image=images[0];
        Util.getDBImage(this,Constant.URL+"img/"+image,ivDetailPhoto);

        ivZan = findViewById(R.id.iv_zan);
        zanCount = findViewById(R.id.zanCount);
        zanCount.setText(getIntent().getIntExtra("likeNumber",0)+"");

        tvName=findViewById(R.id.tv_name);
        tvName.setText(getIntent().getStringExtra("name"));

        tvMessage=findViewById(R.id.tv_message);
        tvMessage.setText(getIntent().getStringExtra("description"));

        btnAddFood = findViewById(R.id.btn_addFood);
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

    //添加烹饪步骤
    public void addProduce(){
        for (int i=0;i<steps.length;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(this);
            params.setMargins(0,10,0,10);
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
        if (!(i>images.length-2)){
            Util.getDBImage(FoodDetailsActivity.this,Constant.URL+"img/"+images[i+1],imageView);
        }else {
            Util.getDBImage(FoodDetailsActivity.this,Constant.URL+"img/none.png",imageView);
        }
        linearLayout.addView(imageView);
    }
    //步骤中的文字描述
    private void addMessage(LinearLayout linearLayout,int i){
        final TextView meText = new TextView(this);
        meText.setTextSize(18);
        meText.setText(steps[i]);
        LinearLayout.LayoutParams paramsMe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsMe.setMargins(0,5,0,10);
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
    private void addView(final LinearLayout linearLayout,int i){
        final TextView material1Text = new TextView(this);
        material1Text.setTextSize(20);
        material1Text.setText(materials1[i]);
//        //set 文本大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=5;
        //set 四周距离
        params.setMargins(0,5,0,5);
        material1Text.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(material1Text);
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
        params.setMargins(0,5,0,5);
        countText.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(countText);
        return countText;
    }

    /**
     * 点赞和取消点赞
     */
    public class UpdateLikeNumAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("id",objects[1]);
                object.put("tableName",objects[2]);
                object.put("status",objects[3]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                byte[] buffer=new byte[256];
                int len=is.read(buffer);
                String info=new String(buffer,0,len);

                is.close();
                os.close();
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
                Toast.makeText(FoodDetailsActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
            }if("ok".equals((String)o)){
                Toast.makeText(FoodDetailsActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
            }if("not".equals((String)o)){
                Toast.makeText(FoodDetailsActivity.this,"没有修改点赞数",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 收藏入菜谱
     */
    public class UpdateMenuAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("cookBookId",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                return content;
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
            if ("ok".equals(o.toString())){
                btnAddFood.setText("已加入");
                btnAddFood.setTextColor(Color.BLACK);
            }else if ("no".equals(o.toString())){
                Toast.makeText(FoodDetailsActivity.this,"出错了，请稍后再试",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(FoodDetailsActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

