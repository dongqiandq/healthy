package com.example.dell.expelliarmus;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MineMenuDetailsActivity extends AppCompatActivity{

//    private List<FoodMaterrils> list = new ArrayList<>();
//    private List<FoodProduce> listProduce = new ArrayList<>();
//    private Button btnInsert;
    private int id;
    private TextView back;
    private ImageView ivDetailPhoto;
//    private ImageView ivZan;
//    private TextView zanCount;
    private TextView tvName;
    private TextView tvMessage;
//    private Button btnAddFood;
    private LinearLayout llMaterials;
    private LinearLayout llProcedure;
    private String[] materials1;
    private String[] materials2;
    private String[] steps;
    private String[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_menu_details);
        findViews();
        id=getIntent().getIntExtra("id",0);

         back= findViewById(R.id.btn_backm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineMenuDetailsActivity.this.finish();
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

//        ivZan = findViewById(R.id.iv_zan);
//        zanCount = findViewById(R.id.zanCount);
//        zanCount.setText(getIntent().getIntExtra("likeNumber",0)+"");

        tvName=findViewById(R.id.tv_name);
        tvName.setText(getIntent().getStringExtra("name"));

        tvMessage=findViewById(R.id.tv_message);
        tvMessage.setText(getIntent().getStringExtra("description"));

//        btnAddFood = findViewById(R.id.btn_addFood);
        llMaterials = findViewById(R.id.ll_materials);
        String strMaterials=getIntent().getStringExtra("material");
        String[] materials=strMaterials.split(",");
        materials1=new String[materials.length/2];
        materials2=new String[materials.length/2];
        int m=0,n=0;
        for (int i=0;i<materials.length/2;i++){
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
        pcText.setTextSize(27);
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
        params.setMargins(0,5,0,5);
        materialText.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(materialText);
        return materialText;
    }

    //材料数量
    private TextView addViewCount(final LinearLayout linearLayout,int i){
        final TextView countText = new TextView(this);
        countText.setTextSize(20);
        countText.setText(materials1.length+"");
        //set 文本大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight=2;
        //set 四周距离
        params.setMargins(0,20,0,20);
        countText.setLayoutParams(params);
        //添加文本到主布局
        linearLayout.addView(countText);
        return countText;
    }

}

