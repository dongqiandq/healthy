package com.example.dell.expelliarmus;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class CommunicateFragment extends Fragment {
    private List<CommunicateQuestions> listQuestions = new ArrayList<>();
    private ListView listView;
    private CommunicateAdapter adapter;
    private int i=0;
    private EditText etQuestions;
    private Util util;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_communicate,container,false);
        etQuestions = view.findViewById(R.id.et_questions);
        Button send = view.findViewById(R.id.btn_communicate_submit);
        util = new Util(getContext());
        LoginUser = util.getUserInfo();

        //发送问题点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUser.getPhoneNumber().length()>0){
                    if(etQuestions.getText().length()!=0){
                        CommunicateQuestions questions = new CommunicateQuestions();
                        questions.question = etQuestions.getText().toString();
                        listQuestions.add(questions);
                        adapter.notifyDataSetChanged();
                        AddQuestion task = new AddQuestion();
                        task.execute(Constant.URL+"AddQuestion");

                    }else{
                        Toast.makeText(getContext(),"请填写问题！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showDialog();
                }


            }
        });
        listView = view.findViewById(R.id.lv_communicate_listview);
        //数据库查询所有问题
        ShowView showTask = new ShowView();
        showTask.execute(Constant.URL+"ShowView");
        return  view;
    }

    //显示所有问题，初始化
    private class ShowView extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter = new CommunicateAdapter(listQuestions,getContext(),R.layout.communicate_item);
            listView.setAdapter(adapter);

        }
    }

    //添加问题
    private class AddQuestion extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            String result = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                CommunicateQuestions question = new CommunicateQuestions();
                question.setHeadImg(LoginUser.getUserImage());
                question.setQuestion(etQuestions.getText().toString());
                question.setUserId(LoginUser.getId());
                String json = new Gson().toJson(question);
                InputStream is = connection.getInputStream();
                result = util.readInputStreamToString(is);
                util.closeIO(is,os);

            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            etQuestions.setText("");
        }
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");
        builder.setMessage("请先登录！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
