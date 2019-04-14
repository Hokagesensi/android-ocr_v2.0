package com.example.ocrv20;

public class bpData {
    private int highBp;
    private int lowBp;
    private int hr;
    private String time;

    public bpData(int highBp,int lowBp,int hr,String time){

        this.highBp = highBp;
        this.lowBp = lowBp;
        this.hr = hr;
        this.time = time;
    }
    public int getHighBp(){
        return highBp;
    }

    public int getLowBp(){
        return lowBp;
    }

    public int getHr(){
        return hr;
    }

    public String getTime(){
        return time;
    }

}
