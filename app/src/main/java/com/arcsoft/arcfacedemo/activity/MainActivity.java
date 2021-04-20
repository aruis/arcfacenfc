package com.arcsoft.arcfacedemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.arcsoft.arcfacedemo.R;

public class MainActivity extends AppCompatActivity {

    EditText sourceEdit;
    EditText targetEdit;
    DatePicker datePicker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        Button btn = findViewById(R.id.button);

        Intent intent = getIntent();
        byte[] faceFeature = intent.getByteArrayExtra("faceFeature");
        if (faceFeature != null) {
            showLongToast("我获取到数据啦，长度是" + faceFeature.length);
        } else {
            showLongToast("还没有找到你的脸");
        }

        sourceEdit = findViewById(R.id.source);
        targetEdit = findViewById(R.id.target);
        datePicker = findViewById(R.id.date);

    }

    protected void showLongToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void chupiao(View view) {
        String source = sourceEdit.getText().toString();
        String target = targetEdit.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();


        Log.i("HAHA", "日期是：" + year + " " + month + " " + day);

//        startActivity(new Intent(this, FaceAttrPreviewActivity.class));
    }


}
