package com.example.dell.expelliarmus;

import android.app.Application;
import android.util.Config;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this,"5decdef90cafb26033000aaa","",UMConfigure.DEVICE_TYPE_PHONE,"");
        UMShareAPI.get(this);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
//        Config.DEBUG = true;
    }


    //各个平台的配置
    {
        //微信
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //QQ
        PlatformConfig.setQQZone("1110116576", "giapsc0iqc90bUjR");
    }
}
