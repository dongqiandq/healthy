package com.example.dell.expelliarmus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ImageButton back = findViewById(R.id.btn_title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineActivity.this.finish();
            }
        });
    }

    public void llOnClick(View view) {
        Intent intent = new Intent(MedicineActivity.this,MedicineListviewActivity.class);
        switch (view.getId()){
            case R.id.ganmao:
                intent.putExtra("flag",MedicineContent.ITEM1);
                startActivity(intent);
                break;
            case R.id.changwei:
                intent.putExtra("flag",MedicineContent.ITEM2);
                startActivity(intent);
                break;
            case R.id.jijiu:
                intent.putExtra("flag",MedicineContent.ITEM3);
                startActivity(intent);
                break;
            case R.id.fuke:
                intent.putExtra("flag",MedicineContent.ITEM4);
                startActivity(intent);
                break;
            case R.id.nanke:
                intent.putExtra("flag",MedicineContent.ITEM5);
                startActivity(intent);
                break;
            case R.id.ertong:
                intent.putExtra("flag",MedicineContent.ITEM6);
                startActivity(intent);
                break;
            case R.id.laoren:
                intent.putExtra("flag",MedicineContent.ITEM7);
                startActivity(intent);
                break;
            case R.id.qita:
                intent.putExtra("flag",MedicineContent.ITEM8);
                startActivity(intent);
                break;
            case R.id.tangniaobing:
                intent.putExtra("flag",MedicineContent.ITEM9);
                startActivity(intent);
                break;
            case R.id.gaoxueya:
                intent.putExtra("flag",MedicineContent.ITEM10);
                startActivity(intent);
                break;
            case R.id.yigan:
                intent.putExtra("flag",MedicineContent.ITEM11);
                startActivity(intent);
                break;
            case R.id.weikuiyang:
                intent.putExtra("flag",MedicineContent.ITEM12);
                startActivity(intent);
                break;
            case R.id.naoxueshuan:
                intent.putExtra("flag",MedicineContent.ITEM13);
                startActivity(intent);
                break;
            case R.id.xinznagbing:
                intent.putExtra("flag",MedicineContent.ITEM14);
                startActivity(intent);
                break;
            case R.id.zhongfeng:
                intent.putExtra("flag",MedicineContent.ITEM15);
                startActivity(intent);
                break;
            case R.id.feijiehe:
                intent.putExtra("flag",MedicineContent.ITEM16);
                startActivity(intent);
                break;
        }
    }
}
