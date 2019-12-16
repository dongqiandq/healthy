package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class CommunicateAdapter extends BaseAdapter {
    private List<CommunicateQuestions> listQuestion = new ArrayList<>();
    private String response="";
    private Context context;
    private int itemId;
    private int count=0;
    private int ITEM;
    private int listSize = listQuestion.size() - 1;

    public CommunicateAdapter(List<CommunicateQuestions> list,Context context,int itemId){
        this.listQuestion = list;
        this.context = context;
        this.itemId = itemId;
    }

    @Override
    public int getCount() {
        if (null != listQuestion){
            return listQuestion.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != listQuestion){
            return listQuestion.get(position);
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
            viewHolder.imgHeader = convertView.findViewById(R.id.iv_Avatar);
            viewHolder.name = convertView.findViewById(R.id.tv_communicate_name);

            viewHolder.question = convertView.findViewById(R.id.tv_question);
            viewHolder.etResponse = convertView.findViewById(R.id.et_response);
            viewHolder.responseSend = convertView.findViewById(R.id.btn_response_send);
            viewHolder.llResponse = convertView.findViewById(R.id.ll_response);
            viewHolder.listComment = convertView.findViewById(R.id.lv_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(LoginUser.getUserImage().contains("/img/header.png")){
            Glide.with(context).load(Constant.URL+"img/header.png").into(viewHolder.imgHeader);
        }else{
            Glide.with(context).load(LoginUser.getUserImage()).into(viewHolder.imgHeader);
        }
//        viewHolder.imgHeader.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.qing1));
        viewHolder.name.setText(LoginUser.getUserName());
        viewHolder.question.setText(listQuestion.get(position).question);
        final CommentAdapter commentAdapter = new CommentAdapter(listQuestion.get(position).listResponseContent,context,R.layout.comment_item);
        viewHolder.listComment.setAdapter(commentAdapter);

        final ViewHolder finalViewHolder = viewHolder;
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.responseSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUser.getPhoneNumber().length()>0){
                    if(finalViewHolder.etResponse.getText().toString().length()!=0){
                        commentAdapter.notifyList(finalViewHolder.etResponse.getText().toString());
                        setListViewHeightBaseOnChildren(finalViewHolder1.listComment);
                        finalViewHolder.etResponse.setText("");
                    }else{
                        Toast.makeText(context,"请填写评论！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showDialog();
                }

            }
        });
        return convertView;
    }

    private class ViewHolder{
        public ImageView imgHeader;
        public TextView name;
        public TextView question;
        public EditText etResponse;
        public Button responseSend;
        public LinearLayout llResponse;
        public ListView listComment;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage("请先登录！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void setListViewHeightBaseOnChildren(ListView listView){
        CommentAdapter commentAdapter = (CommentAdapter) listView.getAdapter();
        if (commentAdapter == null){
            return;
        }

        int totalHeight = 0;
        for (int i=0;i<commentAdapter.getCount();i++){
            View listItem = commentAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (commentAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }
}
