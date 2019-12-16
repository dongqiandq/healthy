package com.example.dell.expelliarmus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.example.dell.expelliarmus.LoginCommonFragment.LoginUser;

public class HeartMeasure extends AppCompatActivity {
    //记录点击次数
    private int k=0;
    private int mTotalProgress = 150;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;
    private TextView tip;
    private static String heartRate;
    private int i=30;
    private Timer timer = new Timer();
    private TimerTask task;
    private static int gx;
    private static int j;
    private static double flag=1;
    private Handler handler;
    private Button start;
    private String title = "pulse";
    private XYSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private int addX = -1;
    double addY;
    int[] xv = new int[300];
    int[] yv = new int[300];
    int[] hua=new int[]{9,10,11,12,13,14,13,12,11,10,9,8,7,6,7,8,9,10,11,10,10};
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static MySurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static TextView text = null;
    private static TextView text1 = null;
    private static TextView text2 = null;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];

    public static enum TYPE {
        GREEN, RED
    };

    private boolean tag = false;
    private static TYPE currentType = TYPE.GREEN;
    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private Camera mcamera;
    private CameraPreview mPreview = null;
    ViewGroup con;
    private Util util;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_heart_measure);
        util = new Util(this);
        onCreatedq();

        TextView back = findViewById(R.id.btn_heart_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeartMeasure.this.finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreatedq(){
        //进度条
        mTasksView = (CompletedView) this.findViewById(R.id.tasks_view);

        tip = this.findViewById(R.id.tv_heart_tip);
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加到这个数据集中
        mDataset.addSeries(series);
        //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //        		刷新图表
                if(series!=null){
                    updateChart();

                }
                if (msg.what == Constant.HEART_COUNTDOWN_START) {
                    start.setText("倒计时(" + msg.arg1 + ")");
                } else if (msg.what == Constant.HEART_COUNTDOWN_STOP){
                    start.setText("开始测量");
                    start.setClickable(true);
                    i = 30;
                    tip.setText("");
                    start.setClickable(true);
                    try{
                        Camera.Parameters mParameters;
                        mParameters = mcamera.getParameters();
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mcamera.setParameters(mParameters);
                        mcamera.setPreviewCallback(null);
                        mcamera.stopPreview();
                        mcamera.release();
                        mcamera = null;
                        //手机振动
                        Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(getContext().VIBRATOR_SERVICE);
                        vibrator.vibrate(500);

                        //判断是否有心率数据，若有显示提示
//                        if (heartRate != null){
                        showDialog();
//                        }

                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        preview = (MySurfaceView) this.findViewById(R.id.preview);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        text = (TextView) this.findViewById(R.id.text);
        text1 = (TextView) this.findViewById(R.id.text1);
        text2 = (TextView) this.findViewById(R.id.text2);
        start = this.findViewById(R.id.dq_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermission(HeartMeasure.this);
                mcamera = getCameraInstance();
                if(mcamera!=null){
                    start.setClickable(false);
                    mPreview = new CameraPreview(HeartMeasure.this,mcamera);
                    tip.setText("开始检测,请保持静止...");
                    //进度条
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (mCurrentProgress < mTotalProgress) {
                                mCurrentProgress += 1;
                                mTasksView.setProgress(mCurrentProgress);
                                try {
                                    Thread.sleep(300);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            listenerToGetCode(handler,i);
                        }
                    }).start();
                }

            }
        });
    }

    //显示保存的对话框
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否保存测量结果到我的身体数据？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginUser = util.getUserInfo();
                if (LoginUser.getPhoneNumber().length()==0){
                    showDialogSave();
                }else {
                    //数据保存到数据库，在MinePersonBodyActivity添加数据时，直接在数据库中查
//                    k=k+1;
//                    Intent intent = new Intent();
//                    if (Integer.parseInt(heartRate)<55){
//                        intent.putExtra("condition",Constant.HRART_PIANDI);
//                    }else if (Integer.parseInt(heartRate)>90){
//                        intent.putExtra("condition",Constant.HRART_PIANGAO);
//                    }else {
//                        intent.putExtra("condition",Constant.HRART_ZHENGCHANG);
//                    }
//                    intent.putExtra("HeartRate",Integer.parseInt(heartRate));
//                    intent.putExtra("count",k);
//                    intent.setClass(HeartMeasure.this,MinePersonBodyActivity.class);
//                    startActivity(intent);
                    SaveHeartRecord task2 = new SaveHeartRecord();
                    task2.execute(Constant.URL+"SaveHeartRecord");

                    finish();
                }
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //保存心率值的异步任务，判断是否登录
    public class SaveHeartRecord extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            String info = "";
            try {
                HttpURLConnection connection = util.getURLConnection((String)objects[0]);
                OutputStream os = connection.getOutputStream();
                int feel = -1;
                int rate = Integer.parseInt(heartRate);
                if (rate<55){
                        feel = Constant.HRART_PIANDI;
                    }else if (rate>90){
                    feel = Constant.HRART_PIANGAO;
                    }else {
                        feel = Constant.HRART_ZHENGCHANG;
                    }
                HeartRecord record = new HeartRecord(LoginUser.getPhoneNumber(), rate,feel);
                os.write(new Gson().toJson(record).toString().getBytes());
                InputStream inputStream = connection.getInputStream();
                info = util.readInputStreamToString(inputStream);
                Log.e("info",info);
                util.closeIO(inputStream,os);
            }catch (Exception e){
                e.printStackTrace();
            }
            return info;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            switch ((String)o){
                case "ok":
                    Toast.makeText(HeartMeasure.this,"保存成功！",Toast.LENGTH_SHORT).show();
                    break;
                case "no":
                    Toast.makeText(HeartMeasure.this,"保存失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDialogSave(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("请登录以后保存！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void verifyStoragePermission(Activity activity){
        //1检测权限
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permission!= PermissionChecker.PERMISSION_GRANTED){
            //2没有权限，需要申请权限，弹出对话框
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ActivityCompat.requestPermissions(HeartMeasure.this,new String[]{Manifest.permission.CAMERA},1);
                }
            }).start();

        }
    }

    /**
     * 设置重发按钮的倒计时
     * @param mainHandler
     * @param i
     */
    public static void listenerToGetCode(Handler mainHandler,int i){
        for (; i > 0; i--) {
            Message message = new Message();
            message.what = Constant.HEART_COUNTDOWN_START;
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
        message.what =Constant.HEART_COUNTDOWN_STOP;
        message.arg1 = i;
        mainHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mcamera!=null){
            mcamera.release();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        mcamera.setPreviewCallback(null);
//        mcamera.stopPreview();
//        mcamera.release();
//        mcamera = null;
//    }

    private void updateChart() {
        if(flag==1)
            addY=10;
        else{
            flag=1;
            if(gx<200){
                if(hua[20]>1){
                    showToast(HeartMeasure.this, "请用您的指尖盖住摄像头镜头！",500);
                    hua[20]=0;}
                hua[20]++;
                return;}
            else
                hua[20]=10;
            j=0;

        }
        if(j<20){
            addY=hua[j];
            j++;
        }

        //移除数据集中旧的点集
        mDataset.removeSeries(series);

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
        int length = series.getItemCount();
        int bz=0;
        //		addX = length;
        if (length > 300) {
            length = 300;
            bz=1;
        }
        addX = length;
        //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
        for (int i = 0; i < length; i++) {
            xv[i] = (int) series.getX(i) -bz;
            yv[i] = (int) series.getY(i);
        }

        //点集先清空，为了做成新的点集而准备
        series.clear();
        mDataset.addSeries(series);
        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
        series.add(addX, addY);
        for (int k = 0; k < length; k++) {
            series.add(xv[k], yv[k]);
        }

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        Camera.Parameters mParameters = null;
        try {
            c = Camera.open(); //创建摄像头实例
            c.setDisplayOrientation(90); // 设置摄像头实例的方向
            mParameters = c.getParameters(); // 设置摄像头的参数
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            c.setParameters(mParameters);
        }catch (Exception e) {
            Log.d("摄像头", "Error setting camera preview: " + e.getMessage());
        }

        return c;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

        private Camera mCamera = null;
        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera; // 保存
            previewHolder = preview.getHolder(); //获取控制实例
            previewHolder.addCallback(this); //注册mHolder
            setFocusable(true); //聚焦
            setFocusableInTouchMode(true);
            this.setKeepScreenOn(true); //保持屏幕长亮
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            try {
                mCamera.setPreviewDisplay(previewHolder); // 使用 mHolder 去控制预览
                mcamera.setPreviewCallback(previewCallback);
                mCamera.startPreview(); // 显示预览
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = mcamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
            }
            mcamera.setParameters(parameters);
            mcamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null)
                throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null)
                throw new NullPointerException();
            if (!processing.compareAndSet(false, true))
                return;
            int width = size.width;
            int height = size.height;
            //图像处理
            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(),height,width);
            gx=imgAvg;
//            text1.setText("平均像素值是"+String.valueOf(imgAvg));
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0)?(averageArrayAvg/averageArrayCnt):0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    flag=0;
//                    text2.setText("脉冲数是 :"+String.valueOf(beats));
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize)
                averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;
            if (newType != currentType) {
                currentType = newType;
            }
//获取系统结束时间
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 2) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180||imgAvg<200) {
                    //获取系统开始时间
                    startTime = System.currentTimeMillis();
                    //beats心跳总数
                    beats = 0;
                    processing.set(false);
                    return;
                }
                if (beatsIndex == beatsArraySize)
                    beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;
                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                heartRate = String.valueOf(beatsAvg);
                text.setText(heartRate+"次/分钟");

//                +"  zhi:"+String.valueOf(beatsArray.length)
//                        +"    "+String.valueOf(beatsIndex)+"    "+String.valueOf(beatsArrayAvg)+"    "+String.valueOf(beatsArrayCnt)

                //获取系统时间
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }
    };



    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea)
                        result = size;
                }
            }
        }
        return result;
    }

    /**
     * 显示toast，自己定义显示长短。
     * param1:activity  传入context
     * param2:word   我们需要显示的toast的内容
     * param3:time length  long类型，我们传入的时间长度（如500）
     */
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }
}
