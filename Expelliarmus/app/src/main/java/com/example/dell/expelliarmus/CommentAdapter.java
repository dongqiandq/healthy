package com.example.dell.expelliarmus;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class CommentAdapter extends BaseAdapter {
    public List<CommunicateQuestions.Comment> list ;
    private CommunicateQuestions.Comment addComm;
    private Context context;
    private int itemId;
    private int ITEM=0;
    private int count=0;

    public CommentAdapter(ArrayList<CommunicateQuestions.Comment> list, Context context, int itemId){
        this.list = list;
        this.context = context;
        this.itemId = itemId;
    }

    @Override
    public int getCount() {
        if (null != list){
            Log.e("commentAdapterSize",list.size()+"");
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
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvResponse = convertView.findViewById(R.id.comment_response);
            viewHolder.ivZan = convertView.findViewById(R.id.iv_zan);
            viewHolder.tvZanCount = convertView.findViewById(R.id.tv_zanCount);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).getSendPersonName());
        viewHolder.tvResponse.setText(list.get(position).getContent());
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

    public void notifyList(CommunicateQuestions.Comment comment){
        addComm = new CommunicateQuestions.Comment();
        addComm.setSendPersonName(comment.getSendPersonName());
        addComm.setSendPersonId(comment.getSendPersonId());
        addComm.setMessageId(comment.getMessageId());
        addComm.setContent(comment.getContent());
        list.add(addComm);
        this.notifyDataSetChanged();
        AddComment task = new AddComment();
        task.execute(Constant.URL+"AddComment");
    }

    private class AddComment extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String result = "";
            try {
                HttpURLConnection connection = Util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                String gson = new Gson().toJson(addComm);
                os.write(gson.getBytes());
                InputStream is = connection.getInputStream();
                result = Util.readInputStreamToString(is);
                Util.closeIO(is,os);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o!=null && ((String)o).length()>0){
                int result = Integer.parseInt((String)o);
                if(result>0){
                    addComm.setId(result);
//                    list.add(addComm);
//                    notifyDataSetChanged();
                }
            }
        }
    }

    private class ViewHolder{
        private TextView tvName;
        public TextView tvResponse;
        public ImageView ivZan;
        public TextView tvZanCount;
    }
}
