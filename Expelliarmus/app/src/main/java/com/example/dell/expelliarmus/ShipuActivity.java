package com.example.dell.expelliarmus;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShipuActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener{
    List<CookBook> list = new ArrayList<>();
    private ImageButton back;
    private int i = 0;
    private int height;
    private int width;
    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private String[] contentDescs;
    private TextView tv_desc;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;
    private EditText etShipuSearch;
    private ImageButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipu);
        button = findViewById(R.id.shipu_search);
        etShipuSearch = findViewById(R.id.et_shipu_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etShipuSearch.setText("");
                Intent intent = new Intent(ShipuActivity.this,NoMessageActivity.class);
                startActivity(intent);
            }
        });
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height=metrics.heightPixels;
        width=metrics.widthPixels;
        initDate();

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipuActivity.this.finish();
            }
        });

        //初始化布局 View视图
        initViews();

        // Model数据
        initDatas();

        // Controller 控制器
        initAdapter();

        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    i++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 往下跳一位
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + viewPager.getCurrentItem());
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            int j=(i-1)%5;
                            LinearLayout.LayoutParams params1 =  new LinearLayout.LayoutParams(20,20);
                            LinearLayout.LayoutParams params2 =  new LinearLayout.LayoutParams(12,12);
                            params1.leftMargin = 15;
                            params2.leftMargin = 15;
                            ll_point_container.getChildAt(j).setLayoutParams(params1);
                            ll_point_container.getChildAt((j+1)%5).setLayoutParams(params2);
                            ll_point_container.getChildAt((j+2)%5).setLayoutParams(params2);
                            ll_point_container.getChildAt((j+3)%5).setLayoutParams(params2);
                            ll_point_container.getChildAt((j+4)%5).setLayoutParams(params2);
                        }
                    });
                }
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(ShipuActivity.this);// 设置页面更新监听
//		viewPager.setOffscreenPageLimit(1);// 左右各保留几个对象
        ll_point_container = (LinearLayout) findViewById(R.id.ll_point_container);

        tv_desc = (TextView) findViewById(R.id.tv_desc);
    }

    /**
     * 初始化要显示的数据
     */
    private void initDatas() {
        // 图片资源id数组
        imageResIds = new int[]{R.drawable.food_a, R.drawable.food_b, R.drawable.food_c, R.drawable.food_d, R.drawable.food_e};

        // 文本描述
        contentDescs = new String[]{
                "健脾开胃",
                "补血益气",
                "温补驱寒",
                "清肺止咳",
                "减脂健身"
        };

        // 初始化要展示的5个ImageView
        imageViewList = new ArrayList<ImageView>();

        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);

            // 加小白点, 指示器
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.whitecircle);
            layoutParams = new LinearLayout.LayoutParams(12, 12);
//            if (i != 0)
                layoutParams.leftMargin = 10;
            // 设置默认所有都不可用
            pointView.setEnabled(false);
            ll_point_container.addView(pointView, layoutParams);
        }
    }

    private void initAdapter() {
        ll_point_container.getChildAt(0).setEnabled(true);
        ll_point_container.getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(20,20));
        tv_desc.setText(contentDescs[0]);
        previousSelectedPosition = 0;

        // 设置适配器
        viewPager.setAdapter(new MyAdapter());

        // 默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        viewPager.setCurrentItem(5000000); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
//			System.out.println("isViewFromObject: "+(view == object));
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem初始化: " + position);
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

//			newPosition = position % 5
            int newPosition = position % imageViewList.size();

            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常
        }

        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
            System.out.println("destroyItem销毁: " + position);
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // 滚动时调用
    }

    @Override
    public void onPageSelected(int position) {
        // 新的条目被选中时调用
        System.out.println("onPageSelected: " + position);
        int newPosition = position % imageViewList.size();

        //设置文本
        tv_desc.setText(contentDescs[newPosition]);

//		for (int i = 0; i < ll_point_container.getChildCount(); i++) {
//			View childAt = ll_point_container.getChildAt(position);
//			childAt.setEnabled(position == i);
//		}
        // 把之前的禁用, 把最新的启用, 更新指示器
        ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
        ll_point_container.getChildAt(newPosition).setEnabled(true);

        // 记录之前的位置
        previousSelectedPosition = newPosition;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 滚动状态变化时调用
    }



    //初始化数据(显示点赞量最多的5个菜谱)
    private void initDate(){
        DisplayTop5Async displayTop5Async=new DisplayTop5Async();
        displayTop5Async.execute(Constant.URL+"DiaplayTop5");
    }

    //添加推荐
    public void addRecommend(){
        LinearLayout llCommend = findViewById(R.id.ll_recommend);
        for (int i=0;i<list.size();i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    (int)(height*0.5));
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setId(i);
            final int j=i;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShipuActivity.this,FoodDetailsActivity.class);
                    intent.putExtra("id",list.get(j).getId());
                    intent.putExtra("description",list.get(j).getDescription());
                    intent.putExtra("images",list.get(j).getImages());
                    intent.putExtra("name",list.get(j).getName());
                    intent.putExtra("material",list.get(j).getMaterial());
                    intent.putExtra("steps",list.get(j).getSteps());
                    intent.putExtra("likeNumber",list.get(j).getLikeNumber());
                    startActivity(intent);
                }
            });
            linearLayout.setLayoutParams(params);
            String strImages=list.get(i).getImages();
            String[] images=strImages.split(",");
            String image=images[0];
            addPhoto(linearLayout,image);
            addText(linearLayout,i);
            llCommend.addView(linearLayout);
        }
    }

    //在LinearLayout中添加图片
    public void addPhoto(LinearLayout linearLayout,String image){
        final ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams paramsPhoto = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int)(height*0.35));
        Util.getDBImage(ShipuActivity.this,Constant.URL+"img/"+image,imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(paramsPhoto);
        linearLayout.addView(imageView);
    }
    //添加文字
    public void addText(LinearLayout linearLayout,int i){
        final TextView rText = new TextView(this);
        rText.setTextSize(23);
        rText.setText(list.get(i).getName());
        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsText.setMargins(7,0,7,0);
        rText.setLayoutParams(paramsText);
        linearLayout.addView(rText);

        final TextView dText=new TextView(this);
        dText.setTextSize(18);
        if (Util.ChineseCount(list.get(i).getDescription())>10){
            String str=list.get(i).getDescription().substring(0,10);
            dText.setText(str+"......");
        }else {
            dText.setText(list.get(i).getDescription());
        }
        LinearLayout.LayoutParams paramdText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramdText.setMargins(7,0,7,0);
        dText.setLayoutParams(paramdText);
        linearLayout.addView(dText);
    }

    public void onClicked(View view) {
        Intent intent = new Intent(ShipuActivity.this,FoodListviewActivity.class);
        switch (view.getId()){
//            case R.id.ib_jianpikaiwei:
//                intent.putExtra("flag",FoodConstent.MENU1);
//                startActivity(intent);
//                break;
//            case R.id.ib_buxueyiqi:
//                intent.putExtra("flag",FoodConstent.MENU2);
//                startActivity(intent);
//                break;
//            case R.id.ib_wenbuquhan:
//                intent.putExtra("flag",FoodConstent.MENU3);
//                startActivity(intent);
//                break;
//            case R.id.ib_qingfeizhike:
//                intent.putExtra("flag",FoodConstent.MENU4);
//                startActivity(intent);
//                break;
//            case R.id.ib_jjianzhijianshen:
//                intent.putExtra("flag",FoodConstent.MENU5);
//                startActivity(intent);
//                break;
//            case R.id.ib_jingqishipu:
//                intent.putExtra("flag",FoodConstent.MENU6);
//                startActivity(intent);
//                break;
//            case R.id.ib_meirongyangyan:
//                intent.putExtra("flag",FoodConstent.MENU7);
//                startActivity(intent);
//                break;
//            case R.id.ib_ziyinbushen:
//                intent.putExtra("flag",FoodConstent.MENU8);
//                startActivity(intent);
//                break;
            case R.id.ll_darencaipu:
                intent.putExtra("flag",FoodConstent.MENU9);
                startActivity(intent);
                break;
            case R.id.ll_zhouzuire:
                intent.putExtra("flag",FoodConstent.MENU10);
                startActivity(intent);
                break;
            case R.id.ll_dujiacaipu:
                intent.putExtra("flag",FoodConstent.MENU11);
                startActivity(intent);
                break;
            case R.id.ll_xinxiancai:
                intent.putExtra("flag",FoodConstent.MENU12);
                startActivity(intent);
                break;
        }
    }

    /**
     * 查询菜谱中点赞量最高的5个菜谱
     */
    public class DisplayTop5Async extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            if (null==o){
                Toast.makeText(ShipuActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"not".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<CookBook>>(){}.getType();
                list=gson.fromJson(o.toString(),type);
            }
            else {
                Toast.makeText(ShipuActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
            addRecommend();
        }
    }
}
