package com.example.dell.expelliarmus;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class MineFragment extends Fragment {
    private LinearLayout loginPerson;//头像昵称
    private TextView user;//昵称
    private ImageView imgView;//头像

    private LinearLayout login;//登录
    private RelativeLayout collect;//收藏
    private RelativeLayout menu;//菜谱
    private RelativeLayout box;//药箱
    private RelativeLayout message;//消息
    private RelativeLayout data;//个人信息
    private RelativeLayout body;//身体数据
    private RelativeLayout read;//最近阅读
    private RelativeLayout set;//设置
    private ImageView imgHead;
    private TextView tvClick;
    private Util util;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine,container,false);
        /**
         * 获取视图控件对象
         */
        login = view.findViewById(R.id.lv_login);
        collect = view.findViewById(R.id.rl_shoucang);
        menu = view.findViewById(R.id.rl_caipu);
        box = view.findViewById(R.id.rl_yaoxiang);
        message = view.findViewById(R.id.rl_xiaoxi);
        data = view.findViewById(R.id.rl_personData);
        body = view.findViewById(R.id.rl_body);
        read = view.findViewById(R.id.rl_read);
        set = view.findViewById(R.id.rl_shezhi);
        imgHead=view.findViewById(R.id.img_head);
        tvClick=view.findViewById(R.id.tv_click);
        setListeners();//设置事件鉴定器

        util = new Util(getContext());
        LoginUser = util.getUserInfo();
        if (LoginUser.getPhoneNumber().length()>0){
            Log.e("user",LoginUser.toString());
            if(LoginUser.getUserImage().contains("/img/header.png")){
                Glide.with(this).load(Constant.URL+"img/header.png").into(imgHead);
            }else{
                Glide.with(this).load(LoginUser.getUserImage()).into(imgHead);
            }
//            imgHead.setImageDrawable(getResources().getDrawable(R.drawable.yangzi));
            tvClick.setText(LoginUser.getUserName().toString());
        }else{

        }

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==2){
            LoginUser = util.getUserInfo();
            if(LoginUser.getPhoneNumber().length()>0){
                if(LoginUser.getUserImage().contains("/img/header.png")){
                    Glide.with(this).load(Constant.URL+"img/header.png").into(imgHead);
                }else{
                    Glide.with(this).load(LoginUser.getUserImage()).into(imgHead);
                }
                tvClick.setText(LoginUser.getUserName());
            }
        }
    }

    /**
     * 设置视图控件的事件监听器
     */
    public void setListeners(){
        MyClickListener myClickListener = new MyClickListener();
        login.setOnClickListener(myClickListener);
        collect.setOnClickListener(myClickListener);
        menu.setOnClickListener(myClickListener);
        box.setOnClickListener(myClickListener);
        message.setOnClickListener(myClickListener);
        data.setOnClickListener(myClickListener);
        body.setOnClickListener(myClickListener);
        read.setOnClickListener(myClickListener);
        set.setOnClickListener(myClickListener);
    }

    private class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.lv_login:
                    if (LoginUser.getPhoneNumber().length()==0){
                        Intent intent1 = new Intent(getActivity(),LoginActivity.class);
                        startActivityForResult(intent1,1);
                    }else {
                        Intent intent=new Intent(getActivity(),MinePersonDataActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.rl_shoucang:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
                        Intent intent2 = new Intent(getActivity(),MineCollectActivity.class);
                        startActivity(intent2);
                    }
                    break;
                case R.id.rl_caipu:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
                        Intent intent3 = new Intent(getActivity(),MineMenuActivity.class);
                        startActivity(intent3);
                    }
                    break;
                case R.id.rl_yaoxiang:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
                        Intent intent4 = new Intent(getActivity(),MineBoxActivity.class);
                        startActivity(intent4);
                    }
                    break;
                case R.id.rl_xiaoxi:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
//                        Intent intent5 = new Intent(getActivity(),MineMessageActivity.class);
//                        startActivity(intent5);
                    }
                    break;
                case R.id.rl_personData:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
                        Intent intent6 = new Intent(getActivity(),MinePersonDataActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.rl_body:
                    if (LoginUser.getPhoneNumber().length()==0){
                        showDialog();
                    }else {
                        Intent intent7 = new Intent(getActivity(),MinePersonBodyActivity.class);
                        startActivity(intent7);
                    }
                    break;
                case R.id.rl_read:
                    Intent intent8 = new Intent(getActivity(),MinePersonReadActivity.class);
                    startActivity(intent8);
//                    showDialog();
                    break;
                case R.id.rl_shezhi:
                    Intent intent9 = new Intent(getActivity(),MinePersonSettingsActivity.class);
                    startActivity(intent9);
//                    showDialog();
                    break;
            }
        }
    }


    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");
        builder.setMessage("请登录以享受更多功能！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
