package com.example.dell.expelliarmus;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class LoginCommonFragment extends Fragment {
    private TextView textView;
    private Button login;
    private Button register;
    private ImageView eye;
    private EditText editText;
    public static User LoginUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_common,container,false);
        textView = view.findViewById(R.id.tv_forgetWord);
        login = view.findViewById(R.id.btn_login);
        register = view.findViewById(R.id.btn_register);
        eye = view.findViewById(R.id.img_eye);
        editText = view.findViewById(R.id.et_word);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MineWordForgetActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                LoginUser=new User();
                LoginUser.setUserName("小猴子");
                LoginUser.setSex("男");
                Intent intent1 = new Intent();
                intent1.putExtra("id",3);
                getActivity().setResult(2,intent1);
                getActivity().finish();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), MineRegisterActivity.class);
                startActivity(intent2);
                getActivity().finish();
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPasswordEye(eye,editText);
            }
        });
        return view;
    }

    //设置密码可见和不可见
    private void setPasswordEye(ImageView imageView,EditText editText) {
        if (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == editText.getInputType()) {
            //如果可见就设置为不可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //修改眼睛图片
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eye_close));
        } else {
            //如果不可见就设置为可见
            editText.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //修改图片
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.eye_open));
        }
        //执行上面的代码后光标会处于输入框的最前方,所以把光标位置挪到文字的最后面
        editText.setSelection(editText.getText().toString().length());
    }

}

