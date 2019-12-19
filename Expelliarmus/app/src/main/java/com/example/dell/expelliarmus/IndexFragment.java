package com.example.dell.expelliarmus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.MinePersonBodyActivity.finalBIM;

public class IndexFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private List<KeepFit> list = new ArrayList<>();
    private ListView lvIndex;
    private IndexShouyeAdapter shouyeAdapter;
    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private String[] contentDescs;
    private TextView tv_desc;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;
    private ImageButton button;
    private int i=0;
    private ListView lvindex;
    private Activity myContext;
    private LinearLayout llSearch;
    private EditText etSearch;
    private Util util;
    private User LoginUser;

    LinearLayout l1;
    LinearLayout l2;
    LinearLayout l3;
    LinearLayout l4;
    LinearLayout l5;
    LinearLayout l6;
    LinearLayout l7;
    LinearLayout l8;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myContext=getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.index,container,false);
        util=new Util(getContext());
        LoginUser=util.getUserInfo();
        lvIndex =view.findViewById(R.id.lv_index);
        button = view.findViewById(R.id.btn_search);
        etSearch = view.findViewById(R.id.et_search);

        if (LoginUser.getPhoneNumber().length()>0 && finalBIM!=0.0){
            HeadRecommedAsync headRecommedAsync=new HeadRecommedAsync();
            headRecommedAsync.execute(Constant.URL+"HeadRecommed",1,LoginUser.getPhoneNumber(),finalBIM);
        }else if (LoginUser.getPhoneNumber().length()>0){
            HeadRecommedAsync headRecommedAsync=new HeadRecommedAsync();
            headRecommedAsync.execute(Constant.URL+"HeadRecommed",2,LoginUser.getPhoneNumber(),0.0);
        }else {
            HeadRecommedAsync headRecommedAsync=new HeadRecommedAsync();
            headRecommedAsync.execute(Constant.URL+"HeadRecommed",3,"",0.0);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                Intent intent = new Intent(getContext(),NoMessageActivity.class);
                startActivity(intent);
            }
        });

//        lvIndex.setSelection(ListView.FOCUS_DOWN);

//        return  view;

        // 初始化布局 View视图
        initViews(view);

        // Model数据
        initData();

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
                    myContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + viewPager.getCurrentItem());
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            int j=(i-1)%4;
                            LinearLayout.LayoutParams params1 =  new LinearLayout.LayoutParams(20,20);
                            LinearLayout.LayoutParams params2 =  new LinearLayout.LayoutParams(12,12);
                            params1.leftMargin = 15;
                            params2.leftMargin = 15;
                            ll_point_container.getChildAt(j).setLayoutParams(params1);
                            ll_point_container.getChildAt((j+1)%4).setLayoutParams(params2);
                            ll_point_container.getChildAt((j+2)%4).setLayoutParams(params2);
                            ll_point_container.getChildAt((j+3)%4).setLayoutParams(params2);
//                            ll_point_container.getChildAt(j).setBackgroundResource(R.drawable.white);
//                            ll_point_container.getChildAt((j+3)%4).setBackgroundResource(R.drawable.whitecircle);
                        }
                    });
                }
            }

            ;
        }.start();
        l1 = view.findViewById(R.id.l1);
        l2 = view.findViewById(R.id.l2);
        l3 = view.findViewById(R.id.l3);
        l4 = view.findViewById(R.id.l4);
        l5 = view.findViewById(R.id.l5);
        l6 = view.findViewById(R.id.l6);
        l7 = view.findViewById(R.id.l7);
        l8 = view.findViewById(R.id.l8);
        MyListener listener = new MyListener();
        l1.setOnClickListener(listener);
        l2.setOnClickListener(listener);
        l3.setOnClickListener(listener);
        l4.setOnClickListener(listener);
        l5.setOnClickListener(listener);
        l6.setOnClickListener(listener);
        l7.setOnClickListener(listener);
        l8.setOnClickListener(listener);

        return view;
    }

    /**
     * 身体记录和BIM值
     */
    public class HeadRecommedAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("phoneNumber",objects[2]);
                object.put("bim",objects[3]);
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
                Toast.makeText(getContext(),"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<KeepFit>>(){}.getType();
                list=gson.fromJson(o.toString(),type);
            }else {
                Toast.makeText(getContext(),"暂无数据",Toast.LENGTH_SHORT).show();
            }
//            Log.e("长度为",list.size()+"");
            shouyeAdapter = new IndexShouyeAdapter(list,getContext(),R.layout.index_item);
            lvIndex.setAdapter(shouyeAdapter);
            shouyeAdapter.notifyDataSetChanged();
            setListViewHeightBaseOnChildren(lvIndex);
        }
    }

    public class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),ListViewActivity.class);
            switch (v.getId()){
                case R.id.l1:
                    intent.putExtra("flag", com.example.dell.expelliarmus.Context.ITEM1);
                    startActivity(intent);
                    break;
                case R.id.l2:
                    Intent foodIntent = new Intent(getActivity(),ShipuActivity.class);
                    startActivity(foodIntent);
                    break;
                case R.id.l3:
                    intent.putExtra("flag",com.example.dell.expelliarmus.Context.ITEM2);
                    startActivity(intent);
                    break;
                case R.id.l4:
                    intent.putExtra("flag",com.example.dell.expelliarmus.Context.ITEM3);
                    startActivity(intent);
                    break;
                case R.id.l5:
                    Intent MedicineIntent = new Intent(getActivity(),MedicineActivity.class);
                    startActivity(MedicineIntent);
                    break;
                case R.id.l6:
                    intent.putExtra("flag",com.example.dell.expelliarmus.Context.ITEM4);
                    startActivity(intent);
                    break;
                case R.id.l7:
                    intent.putExtra("flag",com.example.dell.expelliarmus.Context.ITEM5);
                    startActivity(intent);
                    break;
                case R.id.l8:
                    intent.putExtra("flag",com.example.dell.expelliarmus.Context.ITEM6);
                    startActivity(intent);
                    break;
            }
        }
    }

    public void setListViewHeightBaseOnChildren(ListView listView){
        IndexShouyeAdapter adapter = (IndexShouyeAdapter) listView.getAdapter();
        if (adapter == null){
            Log.e("状态","null");
            return;
        }

        int totalHeight = 0;
        for (int i=0;i<adapter.getCount();i++){
            View listItem = adapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount()-1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);// 设置页面更新监听
//		viewPager.setOffscreenPageLimit(1);// 左右各保留几个对象
        ll_point_container = view.findViewById(R.id.ll_point_container);

        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        llSearch=view.findViewById(R.id.ll_search);
        llSearch.bringToFront();
    }

    /**
     * 初始化要显示的数据
     */
    private void initData() {
        // 图片资源id数组
        imageResIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};

        // 文本描述
        contentDescs = new String[]{
                "生命不息，运动不止",
                "好吃的莲藕排骨汤来啦，快来学习怎么做吧",
                "神秘的基因工程",
                "用爱呵护您的眼睛",

        };

        // 初始化要展示的4个ImageView
        imageViewList = new ArrayList<ImageView>();
        View pointView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            ImageView imageView1 = new ImageView(getActivity());
            imageView1.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView1);

            // 加小白点, 指示器
            pointView = new View(getActivity());
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
//        ll_point_container.getChildAt(0).setBackgroundResource(R.drawable.white);
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
    class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {
        // 滚动时调用
    }

    @Override
    public void onPageSelected(int i) {
        // 新的条目被选中时调用
        System.out.println("onPageSelected: " + i);
        int newPosition = i % imageViewList.size();

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
    public void onPageScrollStateChanged(int i) {
        // 滚动状态变化时调用
    }
}
