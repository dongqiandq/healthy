package com.example.dell.expelliarmus;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class IndexMyAdapter extends BaseAdapter {
    private List<KeepFit> list = new ArrayList<>();
    private Context context;
    private int itemId;
    private String tableName;
    private int category;
    private Util util;
    private User LoginUser;

    public IndexMyAdapter(int category,List<KeepFit> list,Context context,int itemId){
        this.category=category;
        this.list = list;
        this.context = context;
        this.itemId = itemId;
    }

    public IndexMyAdapter(Integer category,String tableName,List<KeepFit> list,Context context,int itemId){
        this.category=category;
        this.tableName=tableName;
        this.list = list;
        this.context = context;
        this.itemId = itemId;
    }

    @Override
    public int getCount() {
        if (null != list){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != list){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        util=new Util(context);
        LoginUser=util.getUserInfo();
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(itemId,null);
            viewHolder = new ViewHolder();
            viewHolder.img1 = convertView.findViewById(R.id.img1);
            viewHolder.img2 = convertView.findViewById(R.id.img2);
            viewHolder.img3 = convertView.findViewById(R.id.img3);
            viewHolder.tvText = convertView.findViewById(R.id.tv_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout item = convertView.findViewById(R.id.item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,IndexDetailsActivity.class);
                if (LoginUser.getPhoneNumber().length()>0){
                    if (category==1){
                        RecentReadAsync recentReadAsync=new RecentReadAsync();
                        recentReadAsync.execute(Constant.URL+"RecentReadServlet",LoginUser.getId(),tableName,list.get(position).getId());
                        intent.putExtra("tableName",tableName);
                    }
                }
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("titles",list.get(position).getTitles());
                intent.putExtra("content",list.get(position).getContent());
                context.startActivity(intent);
            }
        });
        String strImages=list.get(position).getImages();
        String[] images=strImages.split(",");
        for(int i=0;i<3;i++){
            String url=Constant.URL+"img/"+images[i];
            if (i==0){
                Util.getDBImage(context,url,viewHolder.img1);
            }else if(i==1){
                Util.getDBImage(context,url,viewHolder.img2);
            }else {
                Util.getDBImage(context,url,viewHolder.img3);
            }
        }
        if (Util.ChineseCount(list.get(position).getTitles())>20){
            String main0=list.get(position).getTitles().substring(0,20);
            viewHolder.tvText.setText(main0+"......");
        }else {
            viewHolder.tvText.setText(list.get(position).getTitles());
        }
        return convertView;
    }

    private class ViewHolder{
        public ImageView img1;
        public ImageView img2;
        public ImageView img3;
        public TextView tvText;
    }

    /**
     * 功能：最近阅读
     *描述：点击过的文章加入到最近阅读中
     */
    public class RecentReadAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HttpURLConnection connection=Util.getURLConnection((String)objects[0]);
                OutputStream os=connection.getOutputStream();
                JSONObject object=new JSONObject();
                object.put("userId",objects[1]);
                object.put("tableName",objects[2]);
                object.put("tableId",objects[3]);
                os.write(object.toString().getBytes());

                InputStream is=connection.getInputStream();
                byte[] buffer=new byte[256];
                int len=is.read(buffer);
                String info=new String(buffer,0,len);

                os.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

