package com.example.ocrv20;

public class caseData {
    private String filename;
    private String filepath;

    public caseData(){

    }

    public caseData(String filepath){
        this.filepath = filepath;
    }

    public String getFilepath(){
        return filepath;
    }

    public String getFilename(){
        int index = filepath.lastIndexOf("/");
        filename = filepath.substring(index+1,filepath.length());
        return filename;
    }

    public void setFilepath(String filepath){
        this.filepath = filepath;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

}
