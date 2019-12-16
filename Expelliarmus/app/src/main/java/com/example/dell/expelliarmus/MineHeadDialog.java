package com.example.dell.expelliarmus;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MineHeadDialog extends Dialog {
    private TextView tvAlbum;
    private TextView tvPhotograph;
    private TextView tvCancel;
    private Context context;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doGetPic();//相册选择

        public void doGetCamera();//拍照

        public void doCancel();

    }

    public MineHeadDialog(@NonNull Context context) {
        super(context,R.style.Theme_Transparent);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mine_dialog, null);
        setContentView(view);

        tvPhotograph = view.findViewById(R.id.tv_photograph);
        tvAlbum = view.findViewById(R.id.tv_album);
        tvCancel = view.findViewById(R.id.tv_cancel);

        tvAlbum.setOnClickListener(new clickListener());
        tvPhotograph.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // 获取屏幕宽高
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        // 设置宽高
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_photograph:
                    clickListenerInterface.doGetCamera();
                    break;
                case R.id.tv_album:
                    clickListenerInterface.doGetPic();
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
}


