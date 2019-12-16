package com.example.dell.expelliarmus;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

//import static com.google.gson.internal.bind.util.ISO8601Utils.format;

//个人信息
public class MinePersonDataActivity extends AppCompatActivity {
    private ImageView ivImg;
    private static final int CHOOSE_PICTURE = 2;
    private static final int CROP_1_REQUEST = 3;
    private static final int CROP_2_REQUEST = 4;

    private static final int CAMERA_REQUEST = 1;
    private File file;
    private String path, saveName;
    // 照片保存路径
    private File cameraFile;
    private Uri photoUri;
    private Uri returnUri;
    private byte[] returnBitmap;
//
//    protected static final int CHOOSE_PICTURE = 0;
//    protected static final int TAKE_PICTURE = 1;
//    protected static final int CROP_SMALL_PICTURE = 2;
//    protected static Uri tempUri;

    private RelativeLayout head;
    private RelativeLayout name;
    private RelativeLayout sex;
    private ImageView imghead;
    private TextView nickName;
    private TextView tvSex;

//(1)
//    private Drawable drawable;
//    drawable = getResources().getDrawable(R.drawable.ic);
//    BitmapDrawable bd = (BitmapDrawable) drawable;
//    Bitmap bm= bd.getBitmap();

//(2)
//Resources res = getResources();
//Bitmap bmp = BitmapFactory.decodeResource(res,R.drawable.yangzi);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_data);

        TextView imageView = findViewById(R.id.img_return);
        head = findViewById(R.id.rl_touxiang);
        name = findViewById(R.id.rl_name);
        sex = findViewById(R.id.rl_sex);
        tvSex = findViewById(R.id.et_sex);
        imghead = findViewById(R.id.img_head);
        nickName = findViewById(R.id.et_name);
        final String nick = getIntent().getStringExtra("name");
        if(nick != null){
            nickName.setText(nick);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("returnUri的值1","22222222");
//                Intent intent = new Intent(MinePersonDataActivity.this,MainActivity.class);
//                Log.e("returnUri的值2",returnUri+"");
//                if (returnBitmap!=null){
//                    intent.putExtra("img1",returnBitmap);
//                }else {
//                    intent.putExtra("img2",returnUri);
//                }
//                intent.putExtra("nick",nick.trim());
//                startActivity(intent);
                finish();
            }
        });
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MinePersonDataActivity.this,MinePersonNameActivity.class);
                startActivity(intent);
            }
        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseSexDialog();
            }
        });

    }

    public byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    //显示修改性别的对话框
    protected void showChooseSexDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        builder.setTitle("修改性别");
        final String[] items = {"男","女"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvSex.setText(items[which]);
                Toast.makeText(MinePersonDataActivity.this,items[which],Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void openDialog() {
        final MineHeadDialog headDialog = new MineHeadDialog(this);
        headDialog.setCancelable(false);
        Window win=headDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        win.setAttributes(lp);

        headDialog.show();
        headDialog.setClicklistener(new MineHeadDialog.ClickListenerInterface() {
            @Override
            public void doGetCamera() {
                // 相机
                headDialog.dismiss();
//                camera();
            }

            @Override
            public void doGetPic() {
                // 图库
                headDialog.dismiss();
                // Toast.makeText(getApplicationContext(), "tuku", Toast.LENGTH_SHORT).show();
                crop(); // 打开图库并剪切
//                 gallery(); // 只是打开图库选一张照片
            }

            @Override
            public void doCancel() {
                // 取消
                headDialog.dismiss();
            }
        });
    }

    // 相机
//    private void camera() {
//        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        String filename = format(new Date());
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, filename);
//        photoUri =getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
////        cameraFile =generatePhotoFile(this);//DiskUtils
//        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri );//Uri.fromFile(cameraFile)
//
//        String camera=photoUri.getPath();
//        cameraFile=new File(camera);
//        this.startActivityForResult(cameraintent, CAMERA_REQUEST);
//    }

    // 图库——剪裁
    private void crop() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,CROP_1_REQUEST);
    }

    // 图库
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        this.returnUri=data.getData();
        switch (requestCode) {

            // 图库返回值
            case CHOOSE_PICTURE:
                if (data==null){
                    //实现时改为从服务器端获取图片
                    imghead.setImageResource(R.drawable.yangzi);
                }else{
                    Uri uri = data.getData();
                    this.returnUri=uri;
                    //显示图片
                    path = getRealPathFromURI(uri);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    // 设置进头像
                    imghead.setImageBitmap(bitmap);
                    file = new File(path);
                }
                break;

            // 图库剪切
            case CROP_1_REQUEST:
                Uri uri_1 = data.getData();
                this.returnUri=uri_1;
                startPhotoZoom(uri_1);
                break;
            case CROP_2_REQUEST:
//                this.returnUri=data.getData();
                Bitmap bitmap1 = data.getExtras().getParcelable("data");
                this.returnBitmap=Bitmap2Bytes(bitmap1);
                imghead.setImageBitmap(bitmap1);
                break;

            // 相机
            case CAMERA_REQUEST:
                this.returnUri=photoUri;
                try {
                    Bitmap bitmap3 = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(photoUri));
                    imghead.setImageBitmap(bitmap3);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 图片剪切
    private void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);

        this.startActivityForResult(intent, CROP_2_REQUEST);
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
//    //显示修改头像的对话框
//    protected void showChoosePicDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("设置头像");
//        String[] items = {"选择本地照片","拍照"};
//        builder.setNegativeButton("取消",null);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case CHOOSE_PICTURE://选择本地照片
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setType("image/*");
//                        startActivityForResult(intent,CHOOSE_PICTURE);
//                        break;
//                    case TAKE_PICTURE:
//                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
//                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        intent1.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
//                        startActivityForResult(intent1,TAKE_PICTURE);
//                        break;
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(resultCode == RESULT_OK){//如果返回码可用
//            switch (requestCode){
//                case TAKE_PICTURE:
//                    startPhotoZoom(tempUri);//对图片进行裁剪处理
//                    break;
//                case CHOOSE_PICTURE:
//                    startPhotoZoom(data.getData());
//                    break;
//                case CROP_SMALL_PICTURE:
//                    if(data != null){
////                        setImageToView(data);//让刚才选择裁剪得到的图片显示在界面上
//                    }
//                    break;
//            }
//        }
//    }
//
//    //裁剪图片
//    protected void startPhotoZoom(Uri uri){
//        if(uri == null){
//            Log.i("tag","The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri,"image/*");
//        //设置裁剪
//        intent.putExtra("crop","true");
//        //aspectX aspectY 是裁剪图片宽高的比例
//        intent.putExtra("aspectX",150);
//        intent.putExtra("aspectY",150);
//        intent.putExtra("return-data",true);
//        startActivityForResult(intent,CROP_SMALL_PICTURE);
//    }
//
//    // 5. Drawable----> Bitmap
//    public static Bitmap DrawableToBitmap(Drawable drawable) {
//
//        // 获取 drawable 长宽
//        int width = drawable.getIntrinsicWidth();
//        int heigh = drawable.getIntrinsicHeight();
//
//        drawable.setBounds(0, 0, width, heigh);
//
//        // 获取drawable的颜色格式
//        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                : Bitmap.Config.RGB_565;
//        // 创建bitmap
//        Bitmap bitmap = Bitmap.createBitmap(width, heigh, config);
//        // 创建bitmap画布
//        Canvas canvas = new Canvas(bitmap);
//        // 将drawable 内容画到画布中
//        drawable.draw(canvas);
//        return bitmap;
//    }
//
////    //保存裁剪之后的图片数据
////    protected void setImageToView(Intent data){
////        Bundle extras = data.getExtras();
////        if(extras != null){
////            Bitmap photo = extras.getParcelable("data");
////            photo = Utils.toRoundBitmap(photo,tempUri);
////            imghead.setImageBitmap(photo);
////            uploadPic(photo);
////        }
////    }
////
////    private void uploadPic(Bitmap bitmap){
////        //上传至服务器
////        //在这里把Bitmap转换成File，然后得到file的url，做文件上传工作
////        //此时图片已为圆形图片
////        //bitmap没有做过圆形处理，但已经被裁剪了
////
////        String imagePath = Ut
////    }
}
