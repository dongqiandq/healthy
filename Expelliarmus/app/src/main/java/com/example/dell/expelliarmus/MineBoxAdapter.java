package com.example.dell.expelliarmus;

import android.app.Activity;
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
    private List<MedicineChest> contents;//需要显示的数据
    private List<Integer> ids;
    private int itemLayoutId;//item对应的布局文件的资源id

    public MineBoxAdapter(Context context, List<MedicineChest> contents, List<Integer> ids,int itemLayoutId) {
        this.context = context;
        this.contents = contents;
        this.ids=ids;
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
        if (Util.ChineseCount(contents.get(position).getName())>10){
            String main0=contents.get(position).getName().substring(0,10);
            textView.setText(main0+"......");
        }else {
            textView.setText(contents.get(position).getName());
        }

        LinearLayout item = convertView.findViewById(R.id.rl_box);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineBoxDetailsActivity.class);
                intent.putExtra("medKitId",ids.get(position));
                intent.putExtra("id",contents.get(position).getId());
                intent.putExtra("name",contents.get(position).getName());
                intent.putExtra("englishName",contents.get(position).getEnglishName());
                intent.putExtra("chineseName",contents.get(position).getPinYin());
                intent.putExtra("traits",contents.get(position).getCharacters());
                intent.putExtra("indication",contents.get(position).getIndications());
                intent.putExtra("dosage",contents.get(position).getDosage());
                intent.putExtra("precautions",contents.get(position).getNote());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        return convertView;
    }
}
