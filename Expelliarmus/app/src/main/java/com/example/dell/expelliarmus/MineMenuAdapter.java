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

public class MineMenuAdapter extends BaseAdapter{
    private List<CookBook> list;
    private int itemResId;
    private Context context;

    public MineMenuAdapter(Context context, List<CookBook> lists, int itemResId){
        this.context = context;
        this.list = lists;
        this.itemResId = itemResId;
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
            convertView = LayoutInflater.from(context).inflate(itemResId,null);
            viewHolder = new ViewHolder();
            viewHolder.ivPhoto = convertView.findViewById(R.id.iv_photo);
            viewHolder.tvPhotoName = convertView.findViewById(R.id.tv_photoName);
            viewHolder.tvPhotoMessage = convertView.findViewById(R.id.tv_photoMessage);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout item = convertView.findViewById(R.id.photoItem);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineMenuDetailsActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("description",list.get(position).getDescription());
                intent.putExtra("images",list.get(position).getImages());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("material",list.get(position).getMaterial());
                intent.putExtra("steps",list.get(position).getSteps());
                intent.putExtra("likeNumber",list.get(position).getLikeNumber());
                context.startActivity(intent);
            }
        });
        String strImages=list.get(position).getImages();
        String[] images=strImages.split(",");
        String image=images[0];
        Util.getDBImage(context,Constant.URL+"img/"+image,viewHolder.ivPhoto);
        viewHolder.tvPhotoName.setText(list.get(position).getName());
        if (Util.ChineseCount(list.get(position).getDescription())>10){
            String str=list.get(position).getDescription().substring(0,10);
            viewHolder.tvPhotoMessage.setText(str+"......");
        }else {
            viewHolder.tvPhotoMessage.setText(list.get(position).getDescription());
        }
        return convertView;
    }

    private class ViewHolder{
        public ImageView ivPhoto;
        public TextView tvPhotoName;
        public TextView tvPhotoMessage;

    }
}

