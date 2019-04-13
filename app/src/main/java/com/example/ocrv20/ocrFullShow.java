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

public class ocrFullShow extends AppCompatActivity {

    private String[] data;
    private int index;

    private ImageView imageView;
    private EditText text_hp;
    private EditText text_lp;
    private EditText text_hr;
    private EditText text_comment;
    private TextView text_time;

    private Button btn_delete;
    private Button btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_full);

        imageView = findViewById(R.id.ocr_fullImage);
        text_hp = findViewById(R.id.ocr_highbp);
        text_lp = findViewById(R.id.ocr_lowbp);
        text_hr = findViewById(R.id.ocr_hr);
        text_comment = findViewById(R.id.ocr_comment);
        text_time = findViewById(R.id.ocr_time);

        btn_edit = findViewById(R.id.btn_ocr_sava);
        btn_delete = findViewById(R.id.btn_delete);

        Intent intent= getIntent();
        data = intent.getStringArrayExtra("data");
        Log.i("appTest:","默认index:"+index);
        Log.i("appTest: ", "ocr full show："
                + data[0] + " " + data[1] + " " + data[2] + " " + data[3] + " " + data[4] + " " +
                data[5] + " " + data[6]+ " "+ data[7]);
        Uri imageUri = Uri.parse(data[5]);
        Bitmap bitmap = getBitmapFromUri(imageUri);
        Log.i("appTest:","ocr Fullshow bitmap size:h"+bitmap.getWidth()+" w:"+bitmap.getWidth());

        imageView.setImageBitmap(bitmap);
        text_hp.setText(data[2]);
        text_lp.setText(data[3]);
        text_hr.setText(data[4]);
        text_comment.setText(data[6]);
        text_time.setText(data[7]);

        //保存数据
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("appTest","点击保存按钮");
                String[] newData = new String[4];
                newData[0] = text_hp.getText().toString();
                newData[1] = text_lp.getText().toString();
                newData[2] = text_hr.getText().toString();
                newData[3] = text_comment.getText().toString();
                Log.i("appTest:","更改条目："+data[5]);
                updateData(newData);
                Intent docHistoryList_intent = new Intent();
                docHistoryList_intent.putExtra("result","update");
                ocrFullShow.this.setResult(RESULT_OK,docHistoryList_intent);
                ocrFullShow.this.finish();

            }
        });

        //删除当前数据
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("appTest:","删除条目:"+data[7]);
                deleteData(data[7]);
                Intent docHistoryList_intent = new Intent();
                docHistoryList_intent.putExtra("result","update");
                ocrFullShow.this.setResult(RESULT_OK,docHistoryList_intent);
                ocrFullShow.this.finish();

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
        DataBaseHelper dbHelper = new DataBaseHelper(ocrFullShow.this, "ocr_bp", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i("appTest:","开始删除条目："+index);
        db.execSQL("delete from bp where time=?", new Object[]{index});
        db.close();
    }

    //更改数据
    public void updateData(String data[]){
        DataBaseHelper dbHelper = new DataBaseHelper(ocrFullShow.this, "ocr_bp", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update bp set highBP =? where time=?",new Object[]{data[0],this.data[7]});
        db.execSQL("update bp set lowBP =? where time=?",new Object[]{data[1],this.data[7]});
        db.execSQL("update bp set hr =? where time=?",new Object[]{data[2],this.data[7]});
        db.execSQL("update bp set comment =? where time=?",new Object[]{data[3],this.data[7]});
        db.close();
    }

}
