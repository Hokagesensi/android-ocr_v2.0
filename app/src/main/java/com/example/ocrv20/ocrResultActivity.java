package com.example.ocrv20;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ocrResultActivity extends AppCompatActivity {

    public int[] number = new int[6];
    private EditText edit_high;
    private EditText edit_low;
    private EditText edit_hr;
    private EditText edit_comment;

    private Button btn_save;
    private Button btn_ocr_cancel;

    private String hp;
    private String lp;
    private String hr;
    private String bitmapUri;
    private String time;
    public String[] text=new String[4];
    private String typeinfo = "bp_ocr";
    private int ocr_result_ok = 233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrshow);

        edit_high = findViewById(R.id.edit_high);
        edit_low = findViewById(R.id.edit_lp);
        edit_hr = findViewById(R.id.edit_hr);
        edit_comment = findViewById(R.id.edit_comment);
        btn_save = findViewById(R.id.btn_save);

        Intent intent = getIntent();
        hp = intent.getStringExtra("hp");
        lp = intent.getStringExtra("lp");
        hr = intent.getStringExtra("hr");
        bitmapUri = intent.getStringExtra("bitmapUri");
        Log.i("appTest:","1:"+hp+",2:"+lp+",3:"+hr);

        edit_high.setText(hp);
        edit_low.setText(lp);
        edit_hr.setText(hr);
        edit_comment.setText(null);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text[0]=edit_high.getText().toString();
                text[1]=edit_low.getText().toString();
                text[2]=edit_hr.getText().toString();
                text[3]=edit_comment.getText().toString();
                for(int i=0;i<4;i++){
                    Log.i("appTest:ocrResultActivity","第"+i+"项:"+text[i]);
                }
                //依靠DatabaseHelper带全部参数的构造函数创建数据库
                DataBaseHelper dbHelper = new DataBaseHelper(ocrResultActivity.this, "ocr_bp",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                time = simpleDateFormat.format(date);
                values.put("typeInfo",typeinfo);
                values.put("highBP",text[0]);
                values.put("lowBP",text[1]);
                values.put("hr",text[2]);
                values.put("bitmap",bitmapUri);
                values.put("comment",text[3]);
                values.put("time",time);
                values.put("typeInfo","bp_ocr");
                //数据库执行插入命令
                db.insert("bp", null, values);
                Log.i("appTest:","ocrResultActivity 插入数据到数据库："+time);
                Intent main_intent = new Intent();
                main_intent.putExtra("result","update");
                ocrResultActivity.this.setResult(ocr_result_ok,main_intent);
                ocrResultActivity.this.finish();
            }
        });


    }
}
