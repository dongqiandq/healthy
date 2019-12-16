package com.example.dell.expelliarmus;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//我的菜谱
public class MineMenuActivity extends AppCompatActivity {
    private List<CookBook> list=new ArrayList<>();
    private ImageView imageView;
    private ListView lvMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_menu);
        imageView = findViewById(R.id.img_return);
        lvMenu=findViewById(R.id.lv_menu);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineMenuActivity.this.finish();
            }
        });
        DisplayCookBookAsync displayCookBookAsync=new DisplayCookBookAsync();
        displayCookBookAsync.execute(Constant.URL+"DisplayCollection",1,"cookbook");
    }

    /**
     * 展示收藏的食谱中的所有数据
     */
    public class DisplayCookBookAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("tableName",objects[2]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);

                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o){
            if (null!=o){
                if ("".equals(o.toString())){
                    Toast.makeText(MineMenuActivity.this,"没有收藏食谱",Toast.LENGTH_SHORT).show();
                }else {
                    Gson gson=new Gson();
                    Type type=new TypeToken<List<CookBook>>(){}.getType();
                    list=gson.fromJson(o.toString(),type);
                }
            }else {
                Toast.makeText(MineMenuActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }
            MineMenuAdapter adapter=new MineMenuAdapter(MineMenuActivity.this,list,R.layout.activity_mine_menu_item);
            lvMenu.setAdapter(adapter);
        }
    }
}

