package com.example.dell.expelliarmus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HospitalAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private List<Integer> letterCharList;
    private String[] title;
    private int layout_item;
    private String address;

    public HospitalAdapter(Context context, List<String> data, List<Integer> letterCharList, String[] title,int layout_item,String address) {
        this.context=context;
        this.data=data;
        this.letterCharList=letterCharList;
        this.title=title;
        this.layout_item=layout_item;
        this.address=address;
    }

    @Override
    public int getCount() {
        if (null!=data){
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null!=data){
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout_item,null);
        }
        TextView tvItem=convertView.findViewById(R.id.mainlist_item_tv01);
        if (position==0){
            tvItem.setText(address);
            Log.e("当前",address+"");
        }else{
            tvItem.setText(data.get(position-1));
        }
        return convertView;
    }
}
