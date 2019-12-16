package com.example.dell.expelliarmus;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {
    public List<String> list ;
    private Context context;
    private int itemId;
    private int ITEM=0;
    private int count=0;

    public CommentAdapter(List<String> list, Context context, int itemId){
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
            viewHolder.ivHeader = convertView.findViewById(R.id.iv_header);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvResponse = convertView.findViewById(R.id.comment_response);
            viewHolder.ivZan = convertView.findViewById(R.id.iv_zan);
            viewHolder.tvZanCount = convertView.findViewById(R.id.tv_zanCount);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivHeader.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.qing1));
        viewHolder.tvName.setText("小猴同学");
        viewHolder.tvResponse.setText(list.get(position));
        viewHolder.tvZanCount.setText(count+"");
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.ivZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ITEM == 0){
                    finalViewHolder.ivZan.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.czanxuanzhong));
                    count = count+1;
                    finalViewHolder.tvZanCount.setText(count+"");
                    ITEM = 1;
                }else {
                    finalViewHolder.ivZan.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.czan));
                    count = count - 1;
                    finalViewHolder.tvZanCount.setText(count+"");
                    ITEM = 0;
                }
            }
        });
        return convertView;
    }

    public void notifyList(String comment){
        list.add(comment);
        this.notifyDataSetChanged();
    }

    private class ViewHolder{
        private ImageView ivHeader;
        private TextView tvName;
        public TextView tvResponse;
        public ImageView ivZan;
        public TextView tvZanCount;
    }
}
