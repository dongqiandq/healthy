package com.example.dell.expelliarmus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class HospitalFragment extends Fragment {
    private int layout_item;
    private String address;
    //private LocationClient locationClient;
    private final int REQUEST_GPS = 1;

    //定义控件
    MapView mMapView = null;
    // 定位相关
    LocationClient mLocClient;
    //配置定位图层显示方式
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    //定义 BaiduMap 地图对象的操作方法与接口
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    public MyLocationListenner myListener = new MyLocationListenner();

    private String city;
    private Button search;
    private TextView tv01;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.hospital,container,false);

        tv01 =view.findViewById(R.id.tv_city);
        search = view.findViewById(R.id.btn_search);

        mMapView=(MapView)view.findViewById(R.id.bmapView);
        mLocClient = new LocationClient(getActivity().getApplicationContext());

        //动态申请GPS权限
        verifyStoragePermission(getActivity());
//        ActivityCompat.requestPermissions(getActivity(),
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_GPS);

        mBaiduMap=mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        mCurrentMode = LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.location);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //设置定位完成后需要返回地址
        option.setIsNeedAddress(true);
        //设置定位完成后需要的定位描述
        option.setIsNeedLocationDescribe(true);
        mLocClient.setLocOption(option);

        mLocClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                address=bdLocation.getCity();
                // map view 销毁后不在处理新接收的位置
                if (bdLocation == null || mMapView == null) {
                    return;
                }
//              tv_map.setText("[我的位置]\n" + location.getStreet());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();

//                //定位当前城市
                city=bdLocation.getCity();
                if (null!=getActivity().getIntent().getStringExtra("city") && !"".equals(getActivity().getIntent().getStringExtra("city"))){
                    tv01.setText(getActivity().getIntent().getStringExtra("city"));
                }else {
                    tv01.setText(city);//将当前定位的城市设置给textview
                }
                mBaiduMap.setMyLocationData(locData);

                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
        mLocClient.start();

        //创建POI检索实例
        PoiSearch mPoiSearch = PoiSearch.newInstance();

        MyListener listener = new MyListener();
        tv01.setOnClickListener(listener);
        search.setOnClickListener(listener);

        return  view;
    }

    private void verifyStoragePermission(final Activity activity){
        //1检测权限
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission!= PermissionChecker.PERMISSION_GRANTED){
            //2没有权限，需要申请权限，弹出对话框
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_GPS);
                }
            }).start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_GPS:
                if (grantResults[0]!= PackageManager.PERMISSION_GRANTED){

                }
                return;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //创建POI检索监听器
     OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult poiResult) {

        }
        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }
        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
        //废弃
        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }
    };




    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            address=location.getCity();
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
//              tv_map.setText("[我的位置]\n" + location.getStreet());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            //定位当前城市
            city=location.getCity();
            tv01.setText(city);//将当前定位的城市设置给textview
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocClient.stop();
        //定位层关闭
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_city:
                    Intent intent=new Intent(getActivity(),HospitalCity.class);   //this什么时候是getActivity,什么时候是getContext
                    //intent.putExtra("wordAll",(Serializable)list);
                    intent.putExtra("city",city);
                    startActivity(intent);
                    break;
            }
        }
    }
}
