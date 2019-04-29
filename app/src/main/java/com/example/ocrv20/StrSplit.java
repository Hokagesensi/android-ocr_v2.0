package com.example.ocrv20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StrSplit {
    //相应的16关键词
    public static String[] keyWords = {"姓名","性别","年龄","民族",
            "职业","住院号","婚姻","出生地",
            "住址","入院日期","记录日期","病史叙述者","病史陈述者",
            "可靠程度","主诉","现病史","既往史",
            "系统回顾","头颈五官","呼吸系統","循环系统",
            "造血系统","内分泌与代谢系统","肌肉骨骼系统","神经系统",
            "精神状态","个人史","月经史","婚育史",
            "家族史","家庭地址","联系人","关系",
            "联系人地址","电话号码","床号","病案号",
            "籍贯","宗教","工作单位","身份证号码",
            "起病诱因","起病形式"};
    public static int keyWordsLength = keyWords.length;

    //分离文本
    public static String[] splitText(String text) {
        String str=text.replace(" ", "").replace("\n","");

        int[] index = getIndex(str);
        int[] sortedIndex=sortIndex(index);
        String[] sortedKeyWords = sortedKeyWords(keyWords,index,sortedIndex);
        setKeyWords(sortedKeyWords);
        int[] length = getLength(sortedIndex,sortedKeyWords);

        int sortedLength = sortedIndex.length;
        String[] str1=new String[sortedLength];
        for(int i=0;i<sortedLength-1;i++)
            str1[i] = str.substring(sortedIndex[i]+sortedKeyWords[i].length(), sortedIndex[i]+sortedKeyWords[i].length()+length[i]);
        str1[sortedLength-1]=str.substring(sortedIndex[sortedLength-1]+sortedKeyWords[sortedLength-1].length(),str.length());
        String[] str2=clear(str1);
        System.out.println("sortedLength:"+str1.length);
        return str2;
    }
    //去除分割后每行开头的':'
    public static String[] clear(String[] str) {
        String[] strClear = new String[str.length];
        for(int i=0;i<str.length;i++) {
            if(str[i].length()>0) {
                if(str[i].charAt(0)==':')
                    strClear[i] = str[i].substring(1, str[i].length());
                else
                    strClear[i] = str[i];
            }
        }

        return strClear;
    }



    //获取关键词的位置
    public static int[] getIndex(String text) {
        //获取关键词的索引位置
        int[] index = new int[keyWords.length];

        for(int i=0;i<keyWords.length;i++)
            index[i] = text.indexOf(keyWords[i]);
        return index;
    }
    //筛选文本中存在的索引
    public static int[] sortIndex(int[] index) {
        int count=0;
        for(int i=0;i<index.length;i++) {
            if(index[i]!=-1)
                count++;
        }
        System.out.println("获得病历关键词个数："+count);
        int[] sortedIndex = new int[count];
        int k=0;
        System.out.println("排序前的index:");
        for(int j=0;j<index.length;j++) {
            if(index[j]!=-1) {
                sortedIndex[k++]=index[j];
                System.out.println(index[j]);
            }
        }

        sort(sortedIndex,0,sortedIndex.length-1);
        System.out.println("排序后的index:");
        for(int i=0;i<sortedIndex.length;i++)
            System.out.println(sortedIndex[i]);
        return sortedIndex;
    }
    //筛选文本关键词
    public static String[] sortedKeyWords(String[] keyWords,int[] index,int[] sortedIndex) {
        int sortedLength = sortedIndex.length;
        String[] sortedKeyWords = new String[sortedLength];
        int j;
        for(int i=0;i<sortedLength;i++) {
            j=0;
            while(j<index.length&&sortedKeyWords[i]==null) {
                if(sortedIndex[i]==index[j])
                    sortedKeyWords[i]=keyWords[j];
                j++;
            }
        }
        return sortedKeyWords;
    }

    //获取关键字的长度
    public static int[] getLength(int[] index,String[] keyWords) {
        int[] length = new int[index.length];
        //获取每条关键词间的子串长度
        for(int i=0;i<index.length-1;i++)
            length[i]=index[i+1]-index[i]-keyWords[i].length();
        return length;
    }

    //快速排序算法
    public static int partition(int []array,int lo,int hi){
        //三数取中
        int mid=lo+(hi-lo)/2;
        if(array[mid]>array[hi]){
            swap(array[mid],array[hi]);
        }
        if(array[lo]>array[hi]){
            swap(array[lo],array[hi]);
        }
        if(array[mid]>array[lo]){
            swap(array[mid],array[lo]);
        }
        int key=array[lo];

        while(lo<hi){
            while(array[hi]>=key&&hi>lo){
                hi--;
            }
            array[lo]=array[hi];
            while(array[lo]<=key&&hi>lo){
                lo++;
            }
            array[hi]=array[lo];
        }
        array[hi]=key;
        return hi;
    }

    public static void swap(int a,int b){
        int temp=a;
        a=b;
        b=temp;
    }
    public static void sort(int[] array,int lo ,int hi){
        if(lo>=hi){
            return ;
        }
        int index=partition(array,lo,hi);
        sort(array,lo,index-1);
        sort(array,index+1,hi);
    }

    public static void setKeyWords(String[] sortedkeyWords) {
        keyWords = sortedkeyWords;
    }

    public static String[] getKeyWords() {
        return keyWords;
    }

    public static int getKeyWordsLength() {
        return keyWordsLength;
    }
}
