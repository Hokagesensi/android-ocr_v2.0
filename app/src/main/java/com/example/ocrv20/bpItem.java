package com.example.ocrv20;

import android.graphics.Bitmap;

public class bpItem {
    private String time;
    private Bitmap bitmap;

    public bpItem(String time, Bitmap bitmap){
        this.time = time;
        this.bitmap = bitmap;
    }

    public String getTime(){
        return time;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
