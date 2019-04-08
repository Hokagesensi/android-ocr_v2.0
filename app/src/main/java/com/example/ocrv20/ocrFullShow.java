package com.example.ocrv20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ocrFullShow extends AppCompatActivity {

    private String[] data;
    private ImageView imageView;
    private TextView text_hp;
    private TextView text_lp;
    private TextView text_hr;
    private TextView text_comment;
    private TextView text_time;
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

        Intent intent= getIntent();
        data = intent.getStringArrayExtra("data");
        Log.i("appTest: ", "ocr full show"
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

    }

    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}
