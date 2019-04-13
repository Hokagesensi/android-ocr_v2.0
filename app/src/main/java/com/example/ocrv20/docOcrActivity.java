package com.example.ocrv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class docOcrActivity extends AppCompatActivity {

    private EditText edit_text;
    private EditText edit_text_comment;
    private Button btn_doc_save;

    private String text;
    private String bitmapUri;
    private int doc_result_ok = 777;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dor_res);

        edit_text = findViewById(R.id.edit_text);
        edit_text_comment = findViewById(R.id.edit_text_comment1);
        btn_doc_save = findViewById(R.id.btn_doc_save);

        Intent intent = getIntent();
        text = intent.getStringExtra("text");
        text = text.replaceAll(" ","");
        text = text.replaceAll("\r|\n","");
        bitmapUri = intent.getStringExtra("bitmap");

        Log.i("appTest:","获取的数据:\n"+text+"\n得到的图片uri:"+bitmapUri);
        edit_text.setText(text);

        btn_doc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = edit_text.getText().toString();
                String comment = edit_text_comment.getText().toString();
                String time = getTime();
                ContentValues values = new ContentValues();

                values.put("text",text);
                values.put("bitmap",bitmapUri);
                values.put("comment",comment);
                values.put("time",time);
                values.put("typeInfo","doc_ocr");

                DocDataBaseHelper dbHelper = new DocDataBaseHelper(docOcrActivity.this, "ocr_doc",null,1);
                SQLiteDatabase db_doc = dbHelper.getWritableDatabase();
                db_doc.insert("doc", null, values);
                Intent main_intent = new Intent();
                docOcrActivity.this.setResult(doc_result_ok,main_intent);
                docOcrActivity.this.finish();
            }
        });

    }

    public String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }
}
