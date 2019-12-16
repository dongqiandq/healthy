package com.example.dell.expelliarmus;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.List;

public class MedicineListviewActivity extends AppCompatActivity {
    private List<MedicineChest> medicineChests = new ArrayList<>();

    private ListView listView;
    private MedicineAdapter myAdapter;
    private int flag;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_listview);
        listView = findViewById(R.id.lv_list);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);
        judgeMenu();
        DisplayOneCategoryAsync displayOneCategoryAsync=new DisplayOneCategoryAsync();
        displayOneCategoryAsync.execute(Constant.URL+"DisplayOneTable","medicine_chest",category);

        ImageButton back = findViewById(R.id.btn_title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineListviewActivity.this.finish();
            }
        });
    }

    private void judgeMenu(){
        switch (flag){
            case MedicineContent.ITEM1:
                category="感冒";
                break;
            case MedicineContent.ITEM2:
                category="肠胃";
                break;
            case MedicineContent.ITEM3:
                category="急救";
                break;
            case MedicineContent.ITEM4:
                category="妇科";
                break;
            case MedicineContent.ITEM5:
                category="男科";
                break;
            case MedicineContent.ITEM6:
                category="儿童";
                break;
            case MedicineContent.ITEM7:
                category="老人";
                break;
            case MedicineContent.ITEM8:
                category="其他";
                break;
            case MedicineContent.ITEM9:
                category="糖尿病";
                break;
            case MedicineContent.ITEM10:
                category="高血压";
                break;
            case MedicineContent.ITEM11:
                category="乙肝";
                break;
            case MedicineContent.ITEM12:
                category="胃溃疡";
                break;
            case MedicineContent.ITEM13:
                category="脑血栓";
                break;
            case MedicineContent.ITEM14:
                category="心脏病";
                break;
            case MedicineContent.ITEM15:
                category="中风";
                break;
            case MedicineContent.ITEM16:
                category="肺结核";
                break;
        }
    }

    /**
     * 查询某一类药品
     */
    public class DisplayOneCategoryAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("tableName",objects[1]);
                object.put("category",category);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                String content=Util.readInputStreamToString(is);
                Util.closeIO(is,os);
                Gson gson=new Gson();
                Type type=new TypeToken<List<MedicineChest>>(){}.getType();
                List<MedicineChest> medicineChests=gson.fromJson(content,type);
                if (medicineChests.size()==0){
                    return "not";
                }else {
                    return content;
                }
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
                Toast.makeText(MedicineListviewActivity.this,"网络连接失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }else if (!"not".equals(o.toString())){
                Gson gson=new Gson();
                Type type=new TypeToken<List<MedicineChest>>(){}.getType();
                medicineChests=gson.fromJson(o.toString(),type);
                myAdapter = new MedicineAdapter(MedicineListviewActivity.this,medicineChests,R.layout.medicine_list_item);
                listView.setAdapter(myAdapter);
            }
            else {
                Toast.makeText(MedicineListviewActivity.this,"暂无内容",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
