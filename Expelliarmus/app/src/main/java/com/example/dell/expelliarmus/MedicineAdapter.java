package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends BaseAdapter {
    private List<MedicineChest> list = new ArrayList<>();
    private int itemResId;
    private Context context;

    public MedicineAdapter (Context context, List<MedicineChest> lists, int itemResId){
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
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LinearLayout item = convertView.findViewById(R.id.item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MedicineDetailsActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("englishName",list.get(position).getEnglishName());
                intent.putExtra("chineseName",list.get(position).getPinYin());
                intent.putExtra("traits",list.get(position).getCharacters());
                intent.putExtra("indication",list.get(position).getIndications());
                intent.putExtra("dosage",list.get(position).getDosage());
                intent.putExtra("precautions",list.get(position).getNote());
                context.startActivity(intent);
            }
        });
        viewHolder.tvName.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        public TextView tvName;
    }
}
