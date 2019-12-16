package com.example.dell.expelliarmus;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MinePersonNameActivity extends AppCompatActivity {

    private TextView save;
    private LinearLayout lvnick;
    private EditText nickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_person_name);

        TextView imageView = findViewById(R.id.img_return);
        save = findViewById(R.id.tv_save);
        lvnick = findViewById(R.id.lv_nickName);
        nickName = findViewById(R.id.et_nickName);
//        LayoutInflater factory = LayoutInflater.from(PersonNameActivity.this);
//        View layout = factory.inflate(R.layout.person_data, null);
//        final TextView textView = layout.findViewById(R.id.et_name);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = nickName.getText().toString();
                Intent intent = new Intent(MinePersonNameActivity.this,MinePersonDataActivity.class);
                intent.putExtra("name",str.trim());
                startActivity(intent);
                finish();

            }
        });

    }
}
