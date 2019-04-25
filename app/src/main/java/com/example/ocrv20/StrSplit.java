package com.example.ocrv20;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StrSplit {
    //相应的16关键词
    public static String[] keyWords = {"姓名","性别","年龄","民族",
            "婚姻","出生地","住址","入院日期",
            "记录日期","病史叙述者","可靠程度","主诉",
            "现病史","既往史","系统回顾","头颈五官",
            "呼吸系統","循环系统","造血系统","内分泌与代谢系统",
            "肌肉骨骼系统","神经系统","精神状态","个人史",
            "月经史","婚育史","家族史"};
    public static int keyWordsLength = keyWords.length;
    //分离文本
    public static String[] splitText(String text) {
        String[] str1=new String[keyWordsLength];
        String str=text.replace(" ", "").replace("\n","");
        int[] index = getIndex(str);
        int[] length = getLength(index);
        for(int i=0;i<keyWordsLength-1;i++)
            str1[i] = str.substring(index[i]+keyWords[i].length(), index[i]+keyWords[i].length()+length[i]);
        str1[keyWordsLength-1]=str.substring(index[keyWordsLength-1]+keyWords[keyWordsLength-1].length(),str.length());
        String[] str2=clear(str1);
        return str2;
    }
    //去除分割后每行开头的':'
    public static String[] clear(String[] str) {
        String[] strClear = new String[keyWordsLength];
        for(int i=0;i<keyWordsLength;i++) {
            if(str[i].length()>0) {
                if(str[i].charAt(0)==':')
                    strClear[i] = str[i].substring(1, str[i].length());
                else
                    strClear[i] = str[i];
            }
        }

        return strClear;
    }

    public static String readFile(String filepath) {
        StringBuffer sb = new StringBuffer();
        try (FileReader reader = new FileReader(filepath);
             // 建立一个对象，它把文件内容转成计算机能读懂的语言
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //获取关键词的位置
    public static int[] getIndex(String text) {
        //获取关键词的索引位置
        int[] index = new int[keyWordsLength];

        for(int i=0;i<keyWordsLength;i++)
            index[i] = text.indexOf(keyWords[i]);
        return index;
    }

    //获取关键字的长度
    public static int[] getLength(int[] index) {
        int[] length = new int[keyWordsLength];
        //获取每条关键词间的子串长度
        for(int i=0;i<keyWordsLength-1;i++)
            length[i]=index[i+1]-index[i]-keyWords[i].length();
        return length;
    }

    public static String[] getKeyWords() {
        return keyWords;
    }

    public static int getKeyWordsLength() {
        return keyWordsLength;
    }
}
