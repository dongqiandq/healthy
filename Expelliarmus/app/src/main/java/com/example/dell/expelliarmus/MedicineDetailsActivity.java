package com.example.dell.expelliarmus;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MedicineDetailsActivity extends AppCompatActivity {
    private int ITEM=0;
    private TextView back;
    private Button btnBName;
    private TextView tvName;
    private TextView tvEnglishName;
    private TextView tvChineseName;
    private TextView tvTraits;
    private TextView tvIndication;
    private TextView tvDosage;
    private TextView tvPrecautions;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineDetailsActivity.this.finish();
            }
        });

        final Button addBox = findViewById(R.id.btn_addBox);
        addBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ITEM==0){
                    addBox.setText("已加入");
                    addBox.setTextColor(Color.BLACK);
                    ITEM = 1;
                }
            }
        });
    }

    private void init(){
        back= findViewById(R.id.btn_backm);
        btnBName=findViewById(R.id.btn_bname);
        tvName=findViewById(R.id.tv_name);
        tvEnglishName=findViewById(R.id.tv_EnglishName);
        tvChineseName=findViewById(R.id.tv_ChineseName);
        tvTraits=findViewById(R.id.tv_traits);
        tvIndication=findViewById(R.id.tv_indication);
        tvDosage=findViewById(R.id.tv_dosage);
        tvPrecautions=findViewById(R.id.tv_precautions);

        id=getIntent().getIntExtra("id",0);
        btnBName.setText(getIntent().getStringExtra("name"));
        tvName.setText(getIntent().getStringExtra("name"));
        tvEnglishName.setText(getIntent().getStringExtra("englishName"));
        tvChineseName.setText(getIntent().getStringExtra("chineseName"));
        tvTraits.setText(getIntent().getStringExtra("traits"));
        tvIndication.setText(getIntent().getStringExtra("indication"));
        tvDosage.setText(getIntent().getStringExtra("dosage"));
        tvPrecautions.setText(getIntent().getStringExtra("precautions"));
    }
}
