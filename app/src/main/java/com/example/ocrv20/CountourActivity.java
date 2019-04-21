package com.example.ocrv20;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.pqpo.smartcropperlib.view.CropImageView;

public class CountourActivity extends AppCompatActivity {

    //控件定义

    CropImageView ivCrop;
    Button btnCancel;
    Button btnOk;
    Button btnRightSpain;
    Button btnLeftSpain;

    private String ImagePath;

    File photoFile;
//    private Line current = new Line();


    //画图
    public Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countours_find);

        ivCrop = findViewById(R.id.iv_crop);
        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        btnLeftSpain = findViewById(R.id.btn_LeftSpain);
        btnRightSpain = findViewById(R.id.btn_RightSpain);

        photoFile = new File(getExternalFilesDir("img"), System.currentTimeMillis() + ".jpg");

        //获取Intent事件
        Intent last_intent = getIntent();
        Bundle bundle = last_intent.getExtras();
        int key = bundle.getInt("key");
        ImagePath = bundle.getString("url");

        //两种不同方式不同获取图片的方法
        if(key==1)
        {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(ImagePath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(key==2){
            bitmap = BitmapFactory.decodeFile(ImagePath);
        }
        //显示原图片
        Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,src);
        showBitmap(src,1);


        //设置按钮监听事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                CountourActivity.this.finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (ivCrop.canRightCrop()) {
                    Bitmap crop = ivCrop.crop();
                    if (crop != null) {
                        saveImage(crop, photoFile);
                        setResult(RESULT_OK);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    finish();
                } else {
                    Toast.makeText(CountourActivity.this, "cannot crop correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRightSpain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(bitmap,src);
                showBitmap(src,1);
            }
        });

        btnLeftSpain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
                Utils.bitmapToMat(bitmap,src);
                showBitmap(src,0);
            }
        });






    }

    /*
     *  显示原图片的ImageView
     */
    private void showBitmap(Mat src,int code){
        Mat dst = ImgPreProcess.RotateRightImg(src,code);


//        Mat dst2 = ImgPreProcess.AdjustImgSize(dst);
        bitmap = Bitmap.createBitmap(
                dst.cols(), dst.rows(),
                Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmap);
        ivCrop.setImageToCrop(bitmap);
    }


    private void saveImage(Bitmap bitmap, File saveFile) {
        if(saveFile.exists()){
            saveFile.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Intent intent = new Intent(CountourActivity.this,CropResActivity.class);
            intent.putExtra("filepath",photoFile.toString());
            startActivityForResult(intent,1);
//            CountourActivity.this.finish();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            Intent main_intent = new Intent();
            main_intent.putExtra("result","update");
            CountourActivity.this.setResult(RESULT_OK,main_intent);
            CountourActivity.this.finish();
        }
    }








}
