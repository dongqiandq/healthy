package com.example.dell.expelliarmus;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MineBodyAdapter extends BaseAdapter{

    private Context context;
    private List<HeartRecord> contents;//需要显示的数据
    private int itemLayoutId;//item对应的布局文件的资源id

    public MineBodyAdapter(Context context, List<HeartRecord> contents, int itemLayoutId) {
        this.context = context;
        this.contents = contents;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        if (null != contents){
            return contents.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != contents){
            return contents.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            //加载item对应的布局文件
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(itemLayoutId,null);
        }

        TextView textView = convertView.findViewById(R.id.tv_dataTime);

        ImageView imageView = convertView.findViewById(R.id.img_heart);
        imageView.setImageResource(R.drawable.mheart);
        TextView heartNum=convertView.findViewById(R.id.heart_num);
        TextView textView1 = convertView.findViewById(R.id.tv_heart);
        TextView tx1 = convertView.findViewById(R.id.tv_text1);

        textView.setText(contents.get(position).getTime()+"");
        heartNum.setText(contents.get(position).getUserHeart()+"");
        int feel = contents.get(position).getFelling();
        if(feel==Constant.HRART_PIANDI){
            textView1.setText("心率测试感受：偏低");
        }else if(feel == Constant.HRART_PIANGAO){
            textView1.setText("心率测试感受：偏高");
        }else{
            textView1.setText("心率测试感受：正常");
        }
        tx1.setText("次/分");
        return convertView;
    }
}
