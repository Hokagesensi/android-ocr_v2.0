package com.example.ocrv20;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class bpItem {
    private String time;
    private Bitmap bitmap;
    private String typeinfo;

    public bpItem(String time, Bitmap bitmap,String typeinfo){
        this.time = time;
        this.bitmap = bitmap;
        this.typeinfo = typeinfo;
    }

    public String getTime(){
        return time;
    }
    public Bitmap getBitmap(){
        //统一图片大小
        Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,src);
        Mat dst = ImgPreProcess.AdjustImgSize2(src,300);
        bitmap = Bitmap.createBitmap(
                dst.cols(), dst.rows(),
                Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bitmap);
        return bitmap;
    }
    public String getTypeinfo(){
        return typeinfo;
    }
}
