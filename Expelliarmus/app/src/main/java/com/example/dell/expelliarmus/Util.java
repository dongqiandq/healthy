package com.example.dell.expelliarmus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    private Context context;
    OkHttpClient client = new OkHttpClient();
    public Util(Context context){
        this.context = context;
    }
    private Util(){}

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)        context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }


    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

    /**
     * 得到HTTPURLconnection的连接，使用post请求
     * @param string
     * @return
     */
    public static HttpURLConnection getURLConnection(String string){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(string);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭输入输出流
     * @param is
     * @param os
     */
    public static void closeIO(InputStream is, OutputStream os){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取输入流并将其转换为字符串
     * @param is 输入流
     * @return
     */
    public static String readInputStreamToString(InputStream is){
        String string = null;
        try {
            int len = 0;
            byte[] bytes = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while((len=(is.read(bytes)))!=-1){
                stringBuffer.append(new String(bytes,0,len));
            }
            string = stringBuffer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 判断手机号是否合法
     * @param context
     * @param phone
     * @return
     */
    public static boolean judPhone(Context context, EditText phone) {//判断手机号是否正确
        //不正确的情况
        if(TextUtils.isEmpty(phone.getText().toString().trim()))
        //对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(context,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(context,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            return false;
        }
        //正确的情况
        else
        {
            return true;
        }
    }


    /**
     * 判断验证码是否合法
     * @param context
     * @param code
     * @param phone
     * @return
     */
    public static boolean judCord(Context context,EditText code,EditText phone) {//判断验证码是否正确
        judPhone(context,phone);//先执行验证手机号码正确与否
        if(TextUtils.isEmpty(code.getText().toString().trim()))//验证码
        {
            Toast.makeText(context,"请输入您的验证码",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(code.getText().toString().trim().length()!=4)
        {
            Toast.makeText(context,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            String cord_number=code.getText().toString().trim();
            return true;
        }
    }

    /**
     * 设置重发按钮的倒计时
     * @param mainHandler
     * @param i
     */
    public static void listenerToGetCode(Handler mainHandler, int i){
        for (; i > 0; i--) {
            Message message = new Message();
            message.what = Constant.RESEND_VERIFICATION_CODE_FLAG;
            message.arg1 = i;
            mainHandler.sendMessage(message);
            if (i <= 0) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = new Message();
        message.what =Constant.GET_VERIFICATION_CODE_FLAG;
        message.arg1 = i;
        mainHandler.sendMessage(message);
    }

    /**
     * 在mainHandler中的错误处理
     * @param flag
     * @param getCode
     * @param context
     * @param phone
     */
    public void mainHandlerSetToGetCodeError(boolean flag, Button getCode, Context context, EditText phone){
        if (flag) {
            getCode.setVisibility(View.VISIBLE);
            Toast.makeText(context.getApplicationContext(), "验证码输入错误", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
        } else {
            Toast.makeText(context.getApplicationContext(), "验证码获取失败请重新获取", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将用户信息存入sharedPrefereaces
     * @param user
     */
    public void saveUserInfo(User user) {
        SharedPreferences prefenences = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefenences.edit();
        ed.putInt("id",user.getId());
        ed.putString("userName",user.getUserName());
        ed.putString("userPassword",user.getUserPassword());
        ed.putString("userPhoneNumber",user.getPhoneNumber());
        if(user.getUserImage()==null || user.getUserImage().length()==0){
            ed.putString("userImg",Constant.URL+"img/header.png");
        }else{
            ed.putString("userImg",user.getUserImage());
        }
        ed.commit();
    }


    /**
     * 将用户信息从sharePreferences中取出来
     * @return
     */
    public User getUserInfo(){
        SharedPreferences prefenences = context.getSharedPreferences("user", MODE_PRIVATE);
        User u = new User();
        if(prefenences!=null){
            u.setId(prefenences.getInt("id",0));
            u.setUserName(prefenences.getString("userName",""));
            u.setUserPassword(prefenences.getString("userPassword",""));
            u.setPhoneNumber(prefenences.getString("userPhoneNumber",""));
            u.setUserImage(prefenences.getString("userImg",""));
        }
        return u;
    }

    public void clearUserInfo(){
        SharedPreferences prefenences = context.getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefenences.edit();
        editor.clear();
        editor.commit();
    }


    //根据url下载图片保存到本地,图片文件名称imgName
    public void downLoadImg(String url, final String imgName){
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException{
                InputStream is = response.body().byteStream();
                File file = new File(context.getFilesDir()+"/"+imgName+".png");
                Log.e("path",context.getFilesDir()+"");
                FileOutputStream outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] bytes = new byte[1024];
                while ((len = is.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }
                is.close();
                outputStream.close();
            }
        });
    }

    /**
     * 上传图片文件到服务器
     * @param path:保存在本地的绝对路径：如：context.getFilesDir()+"/a.png"
     * @param url:上传到服务器的路径
     */
    public void upLoadImage(String path,String url){
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),file);
        final Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("test:","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("test:","ok");
            }
        });
    }

    /**
     * 加载数据库图片
     */
    public static void getDBImage(Context context,String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 计算某段文字中汉字的数目
     */
    public static int ChineseCount(String ss){
//        String regex = "[\u4e00-\u9fa5]";
//        String ss = "java中求字符串中汉字的个数。";
        if(ss!=null){
            String[]s=ss.split("");
            for(int i=0;i<s.length;i++){
                System.out.println(s[i]);
            }
            return s.length-1;
        }else{
            return 0;
        }

    }

}
