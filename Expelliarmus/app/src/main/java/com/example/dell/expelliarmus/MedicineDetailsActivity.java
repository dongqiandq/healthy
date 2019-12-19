package com.example.dell.expelliarmus;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class MedicineDetailsActivity extends AppCompatActivity {
    private int ITEM=0;
    private TextView back;
    private Button btnBName;
    private TextView tvName;
    private TextView tvEnglishName;
    private TextView tvChineseName;
    private TextView tvTraits;
    private TextView tvIndication;
    private TextView tvDosage;
    private TextView tvPrecautions;
    private int id;
    private Util util;
    private User LoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        util=new Util(this);
        LoginUser=util.getUserInfo();
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineDetailsActivity.this.finish();
            }
        });

        final Button addBox = findViewById(R.id.btn_addBox);
        addBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUser.getPhoneNumber().length()>0){
                    if (ITEM==0){
                        AddMedicineAsync addMedicineAsync=new AddMedicineAsync();
                        addMedicineAsync.execute(Constant.URL+"MedicineCollection",LoginUser.getId(),id);
                        addBox.setText("已加入");
                        addBox.setTextColor(Color.BLACK);
                        ITEM = 1;
                    }
                }else {
                    Toast.makeText(MedicineDetailsActivity.this,"请先登录，再加入药箱",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(){
        back= findViewById(R.id.btn_backm);
        btnBName=findViewById(R.id.btn_bname);
        tvName=findViewById(R.id.tv_name);
        tvEnglishName=findViewById(R.id.tv_EnglishName);
        tvChineseName=findViewById(R.id.tv_ChineseName);
        tvTraits=findViewById(R.id.tv_traits);
        tvIndication=findViewById(R.id.tv_indication);
        tvDosage=findViewById(R.id.tv_dosage);
        tvPrecautions=findViewById(R.id.tv_precautions);

        id=getIntent().getIntExtra("id",0);
        btnBName.setText(getIntent().getStringExtra("name"));
        tvName.setText(getIntent().getStringExtra("name"));
        tvEnglishName.setText(getIntent().getStringExtra("englishName"));
        tvChineseName.setText(getIntent().getStringExtra("chineseName"));
        tvTraits.setText(getIntent().getStringExtra("traits"));
        tvIndication.setText(getIntent().getStringExtra("indication"));
        tvDosage.setText(getIntent().getStringExtra("dosage"));
        tvPrecautions.setText(getIntent().getStringExtra("precautions"));
    }

    /**
     * 加入药箱
     */
    public class AddMedicineAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("medId",objects[2]);
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
    }
}
