package com.example.ocrv20;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class docFullShow extends AppCompatActivity {


    private String[] data;
    private EditText doc_edit_text;
    private EditText doc_edit_comment;
    private TextView doc_edit_time;
    private String tag = "appTest";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docshow);

        Intent intent= getIntent();
        data = intent.getStringArrayExtra("data");

        Uri imageUri = Uri.parse(data[3]);
        Bitmap bitmap = getBitmapFromUri(imageUri);

        ImageView imageView = findViewById(R.id.ocr_fullImage);
        Button btn_doc_delete = findViewById(R.id.btn_doc_delete);
        Button btn_doc_update = findViewById(R.id.btn_doc_update);
        Button btn_doc_extract = findViewById(R.id.btn_doc_extract);
        doc_edit_text = findViewById(R.id.doc_edit_text);
        doc_edit_comment = findViewById(R.id.doc_edit_comment);
        doc_edit_time = findViewById(R.id.doc_edit_time);

        imageView.setImageBitmap(bitmap);
        doc_edit_text.setText(data[2]);
        doc_edit_comment.setText(data[4]);
        doc_edit_time.setText(data[5]);

        btn_doc_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("appTest:","删除条目:"+data[5]);
                deleteData(data[5]);
                Intent docHistoryList_intent = new Intent();
                docHistoryList_intent.putExtra("result","update");
                docFullShow.this.setResult(RESULT_OK,docHistoryList_intent);
                docFullShow.this.finish();
            }
        });

        btn_doc_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] newData = new String[3];
                newData[1] = doc_edit_text.getText().toString();
                newData[2] = doc_edit_comment.getText().toString();
                Log.i("appTest:","更改条目："+data[5]);
                updateData(newData);
                Intent docHistoryList_intent = new Intent();
                docHistoryList_intent.putExtra("result","update");
                docFullShow.this.setResult(RESULT_OK,docHistoryList_intent);
                docFullShow.this.finish();
            }
        });

        //提取文本信息
        btn_doc_extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent extract_intent = new Intent(docFullShow.this,docExtractActivity.class);
                extract_intent.putExtra("text",data[2]);
                startActivity(extract_intent);




            }
        });

    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    //删除数据
    public void deleteData (String index){
        DataBaseHelper dbHelper = new DataBaseHelper(docFullShow.this, "ocr_doc", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i("appTest:","开始删除条目："+index);
        db.execSQL("delete from doc where time=?", new Object[]{index});
        db.close();
    }

    //更改数据
    public void updateData(String data[]){
        DataBaseHelper dbHelper = new DataBaseHelper(docFullShow.this, "ocr_doc", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update doc set text =? where time=?",new Object[]{data[1],this.data[5]});
        db.execSQL("update doc set comment =? where time=?",new Object[]{data[2],this.data[5]});
        db.close();
    }
}
