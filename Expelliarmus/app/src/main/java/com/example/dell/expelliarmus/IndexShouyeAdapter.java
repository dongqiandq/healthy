package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IndexShouyeAdapter extends BaseAdapter {
    private List<KeepFit> list;
    private Context context;
    private int itemId;

    public IndexShouyeAdapter(List<KeepFit> list, Context context, int itemId) {
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
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(itemId,null);
            viewHolder = new ViewHolder();
            viewHolder.imgBground = convertView.findViewById(R.id.iv_bground);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_shou_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout item = convertView.findViewById(R.id.ll_shouye_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,IndexDetailsActivity.class);
                intent.putExtra("titles",list.get(position).getTitles());
                intent.putExtra("content",list.get(position).getContent());
                context.startActivity(intent);
            }
        });
        String strImages=list.get(position).getImages();
        String[] images=strImages.split(",");
        String url=Constant.URL+"img/"+images[0];
        Util.getDBImage(context,url,viewHolder.imgBground);
        if (Util.ChineseCount(list.get(position).getTitles())>10){
            String main0=list.get(position).getTitles().substring(0,10);
            viewHolder.tvTitle.setText(main0+"......");
        }else {
            viewHolder.tvTitle.setText(list.get(position).getTitles());
        }
        return convertView;
    }

    private class ViewHolder{
        public ImageView imgBground;
        public TextView tvTitle;
    }

}
