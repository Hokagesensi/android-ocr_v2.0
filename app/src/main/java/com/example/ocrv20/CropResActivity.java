package com.example.ocrv20;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;

public class CropResActivity  extends AppCompatActivity {
    private ImageView cropImageView;
    private Button bt_ocr;
    private Button bt_save;
    private Bitmap bitmap;
    private File photoFile;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropImageView = findViewById(R.id.cropImageView);
        bt_ocr = findViewById(R.id.btn_ocr);
        Intent intent = getIntent();
        String filepath = intent.getStringExtra("filepath");
        photoFile = new File(filepath);
        if(photoFile.exists()) {
            bitmap = BitmapFactory.decodeFile(photoFile.getPath());
            Log.i("appTest:cropResActivity","bitmap大小:"+bitmap.getWidth()+","+bitmap.getHeight());
            cropImageView.setImageBitmap(bitmap);
        }

        bt_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("appTest:","cropped size:height"+bitmap.getHeight()
                        +",width:"+bitmap.getWidth());
                Intent intent = new Intent(CropResActivity.this,CropTinyActivity.class);
                intent.putExtra("filepath",photoFile.toString());
                startActivityForResult(intent,1);
            }
        });






    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Intent main_intent = new Intent();
            main_intent.putExtra("result","update");
            CropResActivity.this.setResult(RESULT_OK,main_intent);
            CropResActivity.this.finish();
        }
    }


}
