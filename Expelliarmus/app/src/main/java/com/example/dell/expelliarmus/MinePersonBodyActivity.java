package com.example.dell.expelliarmus;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

//身体数据
public class MinePersonBodyActivity extends AppCompatActivity {
    private Button btn_BIM;
    private TextView BIM;
    private TextView tvBodyStatus;

    private List<HeartRecord> lists;
    private SwipeMenuListView lvText;
    private TextView imageView;
    private MineBodyAdapter adapter;
    private Util util;
    private User user;
    private EditText height;
    private EditText weight;
    public static double finalBIM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_body);
        height = findViewById(R.id.et_height);
        weight = findViewById(R.id.et_weight);
        btn_BIM = findViewById(R.id.btn_getBIM);
        BIM = findViewById(R.id.body_digital);
        tvBodyStatus = findViewById(R.id.tv_body_status);

        //添加用户的身高、体重、BIM
        btn_BIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否登录，如果登录，则通过用户ID查找出身体指标ID,将该条数据更改
//                SaveHeightAndWeight task1 = new SaveHeightAndWeight();
//                task1.execute(Constant.URL+"SaveHeightAndWeight");
                String str = "0.00";
                String h = height.getText().toString();
                String w = weight.getText().toString();
                if(h.equals("")||h==null||w.equals("")||w==null){
                    Toast.makeText(MinePersonBodyActivity.this,"请填写数据！",Toast.LENGTH_SHORT).show();
                }else {
                    double hh = Double.parseDouble(h);
                    double ww = Double.parseDouble(w);
                    str = String.format("%.2f",  ww/(hh*hh));
                    finalBIM=ww/(hh*hh);
                    BIM.setText(str);
                    double data = Double.parseDouble((String)str);
                    if (data<=18.4){
                        tvBodyStatus.setText("偏瘦");
                    }else if (18.4<data && data<=23.9){
                        tvBodyStatus.setText("正常");
                    }else if (23.9<data && data<=27.9){
                        tvBodyStatus.setText("过重");
                    }else {
                        tvBodyStatus.setText("肥胖");
                    }

                }

            }
        });

        util = new Util(this);
        user = util.getUserInfo();
        imageView = findViewById(R.id.img_return);
        lvText=findViewById(R.id.view_text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinePersonBodyActivity.this.finish();
            }
        });

        //查询用户心率信息
        FindUserHearts task = new FindUserHearts();
        task.execute(Constant.URL+"FindHeartData");

    }

    //用户存储身高体重数据的异步任务
//    public class SaveHeightAndWeight extends AsyncTask{
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            String judge = "";
//            String str = "0.00";
//            try {
//                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
//                OutputStream os = connection.getOutputStream();
//                String h = height.getText().toString();
//                String w = weight.getText().toString();
//                if(h.equals("")||h==null||w.equals("")||w==null){
//                    Toast.makeText(MinePersonBodyActivity.this,"请填写数据！",Toast.LENGTH_SHORT).show();
//                }else {
//                    double hh = Double.parseDouble(h);
//                    double ww = Double.parseDouble(w);
//                    str = String.format("%.2f",  ww/(hh*hh));
////                    JSONObject object = new JSONObject();
////                    object.put("userTel",user.getPhoneNumber());
////                    object.put("height",hh);
////                    object.put("weight",ww);
////                    object.put("bim",str);
////                    os.write(object.toString().getBytes());
//                }
//                InputStream is = connection.getInputStream();
//                judge = util.readInputStreamToString(is);
//                Log.e("test",judge);
//                util.closeIO(is,os);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            return str;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//            BIM.setText(o.toString());
//            double data = Double.parseDouble((String) o);
//            if (data<=18.4){
//                tvBodyStatus.setText("偏瘦");
//            }else if (18.4<data && data<=23.9){
//                tvBodyStatus.setText("正常");
//            }else if (23.9<data && data<=27.9){
//                tvBodyStatus.setText("过重");
//            }else {
//                tvBodyStatus.setText("肥胖");
//            }
//
//        }
//    }


    //查询用户心率信息
    private class FindUserHearts extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String result = "";
            try {
                HttpURLConnection connection = Util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                os.write(user.getPhoneNumber().getBytes());
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
            Type type = new TypeToken<List<HeartRecord>>(){}.getType();
            lists = new Gson().fromJson((String )o,type);
//            Log.e("test",lists.get(1).toString());
            adapter = new MineBodyAdapter(MinePersonBodyActivity.this,lists,R.layout.activity_mine_body_item);
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    // create "delete" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                    // set item background
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(150);
                    // set a icon
                    deleteItem.setIcon(R.drawable.trashbin);
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
            lvText.setMenuCreator(creator);
            lvText.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
                {
                    switch (index)
                    {
                        case 0:
                            HeartRecord record = lists.get(position);
                            lists.remove(position);
                            adapter.notifyDataSetChanged();
                            DeleteRecord task = new DeleteRecord();
                            task.execute(Constant.URL+"DeleteRecord",record.getId()+"");
                            break;
                    }
                    return false;
                }
            });

            lvText.setAdapter(adapter);

        }
    }

    private class DeleteRecord extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String result = "";
            try {
                HttpURLConnection connection = Util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                os.write(((String)objects[1]).getBytes());
                InputStream is = connection.getInputStream();
                result = Util.readInputStreamToString(is);
                Util.closeIO(is,os);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

}
