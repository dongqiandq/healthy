package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MineBoxAdapter extends BaseAdapter {
    private Context context;
    private List<String> contents = new ArrayList<>();//需要显示的数据
    private int itemLayoutId;//item对应的布局文件的资源id

    public MineBoxAdapter(Context context, List<String> contents, int itemLayoutId) {
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

        TextView textView = convertView.findViewById(R.id.tv_medicine);
        textView.setText(contents.get(position));

        LinearLayout item = convertView.findViewById(R.id.rl_box);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineBoxDetailsActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
