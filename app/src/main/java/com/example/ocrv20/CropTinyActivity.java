package com.example.ocrv20;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.ChineseCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.googlecode.tesseract.android.TessBaseAPI;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CropTinyActivity extends AppCompatActivity{


    private File photoFile;
    private Bitmap bitmap;
    private Button btn_cut;
    private Button btn_back;
    private Button btn_ocr;
    private Button btn_DocOcr;
    private ImageView cropImageView;

    public Uri finalBitmapUri;
    private TessBaseAPI baseApi;
    private boolean success=false;
    public String text;
    private int ocr_result_ok = 233;
    private int doc_result_ok = 777;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croptiny);

        btn_ocr = findViewById(R.id.btn_ocr);
        btn_cut = findViewById(R.id.btn_cut);
        btn_back = findViewById(R.id.btn_back);
        btn_DocOcr = findViewById(R.id.btn_DocOcr);
        cropImageView = findViewById(R.id.imageCrop);

        photoFile = new File(getExternalFilesDir("img"), "scan.jpg");
        finalBitmapUri = Uri.parse(photoFile.getPath());
        if(photoFile.exists()) {
            bitmap = BitmapFactory.decodeFile(photoFile.getPath());
            Log.i("appTest:cropTinyActivity","bitmap大小:"+bitmap.getWidth()+","+bitmap.getHeight());
            cropImageView.setImageBitmap(bitmap);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropTinyActivity.this.finish();
            }
        });

        btn_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUCrop(CropTinyActivity.this,photoFile.toString(),UCrop.REQUEST_CROP,0,0);
            }
        });

        btn_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] res = ocr(bitmap);
                Log.i("appTest:","开始启动结果显示");
                Intent intent = new Intent(CropTinyActivity.this,ocrResultActivity.class);
                intent.putExtra("hp",String.valueOf(res[0]));
                intent.putExtra("lp",String.valueOf(res[1]));
                intent.putExtra("hr",String.valueOf(res[2]));
                intent.putExtra("bitmapUri",finalBitmapUri.toString());
                Log.i("appTest","res.length="+res.length);
                startActivityForResult(intent,1);
//                    CropTinyActivity.this.finish();
            }
        });
        //文本识别
        btn_DocOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!success) {
                    try {
                        initTessBaseAPI();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                text = recognizeTextImage(bitmap);
                Log.i("appTest:CropTinyActivity","识别结果：\n"+text);
//                    Toast.makeText(CropTinyActivity.this,text,Toast.LENGTH_LONG).show();

                Intent doc_ocr_intent = new Intent(CropTinyActivity.this,docOcrActivity.class);
                doc_ocr_intent.putExtra("text",text);
                doc_ocr_intent.putExtra("bitmap",finalBitmapUri.toString());
                startActivityForResult(doc_ocr_intent,2);
            }
        });

    }

    public int[] ocr(Bitmap bitmap){
        Log.i("appTest:","ocr bitmap size:"+bitmap.getHeight());
        Mat img = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,img);
        Log.i("appTest:finalImgSize:","width:"+img.width()+",height:"+img.height());
        int[] number = ImgPreProcess.preprocess(img);
        Log.i("appTest","ocr结果行数"+number.length);
        for(int i=0;i<number.length;i++){
            if(number[i]!=0)
            Log.i("appTest:","第"+i+"行："+number[i]);
        }
        return number;
    }


    /**
     * 启动裁剪
     * @param activity 上下文
     * @param sourceFilePath 需要裁剪图片的绝对路径
     * @param requestCode 比如：UCrop.REQUEST_CROP
     * @param aspectRatioX 裁剪图片宽高比
     * @param aspectRatioY 裁剪图片宽高比
     * @return
     */
    public static String startUCrop(Activity activity, String sourceFilePath,
                                    int requestCode, float aspectRatioX, float aspectRatioY) {
        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start(activity, requestCode);
        return cameraScalePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            finalBitmapUri = resultUri;
            bitmap = getBitmapFromUri(resultUri);
            Log.i("appTest:CropTinyActivity","bitmap size height:"
                    +bitmap.getHeight()+", width"+bitmap.getWidth());
            cropImageView.setImageBitmap(bitmap);


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }else if(resultCode==ocr_result_ok||resultCode ==doc_result_ok) {
                Intent cropResult_intent = new Intent();
                CropTinyActivity.this.setResult(RESULT_OK, cropResult_intent);
                CropTinyActivity.this.finish();
        }
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

    private void initTessBaseAPI() throws IOException {
        baseApi = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory()+"/tesseract/";
        File dir = new File(datapath + "tessdata/");
        if(!dir.exists()){
            dir.mkdirs();
            InputStream input = getResources().openRawResource(R.raw.chi_sim);
            File file = new File(dir,"chi_sim.traineddata");
            FileOutputStream output = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len=0;
            while((len=input.read(buff))!=-1){
                output.write(buff,0,len);
            }
            input.close();
            output.close();
        }
        success = baseApi.init(datapath, "chi_sim");
        if(success){
            Log.i("appTest:","load Tesseract ocr Engine successfully...");
        }else{
            Log.i("appTest:","Warnning: could not initialize Tesseract data...");
        }
    }

    private  String recognizeTextImage(Bitmap img){
//        Resources r = CropTinyActivity.this.getResources();
//        Bitmap img2 = BitmapFactory.decodeResource(r,R.drawable.apptest01);
        baseApi.setImage(img);
        String recognizeText = baseApi.getUTF8Text();
        return recognizeText;
    }

}
