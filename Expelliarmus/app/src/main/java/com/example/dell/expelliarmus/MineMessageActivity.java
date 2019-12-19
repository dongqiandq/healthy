package com.example.dell.expelliarmus;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class MineMessageActivity extends AppCompatActivity {
    private List<CommunicateQuestions> Questions = new ArrayList<CommunicateQuestions>();
    private ListView listView;
    private CommunicateAdapter adapter;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message);
        back = findViewById(R.id.btn_tutle_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = findViewById(R.id.lv_communicate_listview);
        ShowView task = new ShowView();
        task.execute(Constant.URL+"ShowMyQuestion");
    }

    //显示自己发送的所有问题，初始化
    private class ShowView extends AsyncTask {
        String result = "";
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection = Util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                os.write((LoginUser.getId()+"").getBytes());
                InputStream is = connection.getInputStream();
                result = Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                Log.e("list",result);

            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && ((String)o).length()>0){
                Type type = new TypeToken<ArrayList<CommunicateQuestions>>() {}.getType();
                Questions = new Gson().fromJson((String)o,type);
//                Log.e("size",Questions.get(0).getListResponse().size()+"");
            }
            adapter = new CommunicateAdapter(Questions,MineMessageActivity.this,R.layout.communicate_item);
            listView.setAdapter(adapter);
        }
    }

}
