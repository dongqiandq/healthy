package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MineCollectAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();
    private List<Integer> imageViewList = new ArrayList<>();
    private Context context;
    private int itemId;

    public MineCollectAdapter(List<String> list,Context context,int itemId){
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        LinearLayout item = convertView.findViewById(R.id.collect_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineCollectDetailsActivity.class);
//                intent.putExtra("list_location",1);
                context.startActivity(intent);
            }
        });
        viewHolder.img1.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.jianshen2));
        viewHolder.img2.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.jianshen2));
        viewHolder.img3.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.jianshen3));
        viewHolder.tvText.setText(list.get(position));
        return convertView;
    }

    private class ViewHolder{
        public ImageView img1;
        public ImageView img2;
        public ImageView img3;
        public TextView tvText;
    }
}

