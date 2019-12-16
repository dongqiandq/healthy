package com.example.dell.expelliarmus;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class IndexDetailsActivity extends AppCompatActivity {
    private LinearLayout sollect;
    private ImageView ivSollect;
    private TextView tvSollect;
    private TextView tvCaption;
    private TextView tvVersion;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_details);
        id=getIntent().getIntExtra("id",0);
        sollect= findViewById(R.id.ll_collect);
        ivSollect= findViewById(R.id.iv_sollect);
        tvSollect= findViewById(R.id.tv_sollect);

        tvCaption=findViewById(R.id.tv_caption);
        tvVersion=findViewById(R.id.tv_version);
        tvCaption.setText(getIntent().getStringExtra("titles"));
        tvVersion.setText(getIntent().getStringExtra("content"));

        TextView back = findViewById(R.id.btn_backm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexDetailsActivity.this.finish();
            }
        });

        sollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivSollect.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.hll_mine_shoucang).getConstantState())){
                    if (null!=LoginUser){
                        ivSollect.setImageResource(R.drawable.shoucangxuanhzong);
                        tvSollect.setTextColor(getResources().getColor(R.color.colorPrimary));
                        SixCollAsync sixCollAsync=new SixCollAsync();
                        sixCollAsync.execute(Constant.URL+"SixCollection",1,1,"keep_fit",1);
                    }else {
                        Toast mToast=Toast.makeText(IndexDetailsActivity.this,null,Toast.LENGTH_SHORT);
                        LinearLayout toastView=(LinearLayout)mToast.getView();
                        TextView textView=new TextView(IndexDetailsActivity.this);
                        textView.setTextSize(20);
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        textView.setLayoutParams(params);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.setPadding(15,0,15,20);
                        mToast.setView(toastView);
                        toastView.addView(textView);
                        textView.setText("请先登录,再收藏");
                        mToast.show();
                    }
                }else {
                    ivSollect.setImageResource(R.drawable.hll_mine_shoucang);
                    tvSollect.setTextColor(getResources().getColor(R.color.gray));
                    SixCollAsync sixCollAsync=new SixCollAsync();
                    sixCollAsync.execute(Constant.URL+"SixCollection",0,1,"keep_fit",1);
                }
            }
        });
    }

    /**
     * 点击收藏或取消收藏
     */
    public class SixCollAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("operation",objects[1]);
                object.put("userId",objects[2]);
                object.put("tableName",objects[3]);
                object.put("tableId",objects[4]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                byte[] buffer=new byte[256];
                int len=is.read(buffer);
                String info=new String(buffer,0,len);
                os.close();
                is.close();
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
                Toast.makeText(IndexDetailsActivity.this,"操作失败，请稍后再试",Toast.LENGTH_SHORT).show();
            }if("ok".equals((String)o)){
                Toast.makeText(IndexDetailsActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
