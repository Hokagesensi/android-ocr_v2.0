package com.example.ocrv20;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImgPreProcess {
    /*
     * 图片向右旋转90度
     * @param src:输入图片Mat
     * @param flipcode:1是顺时针90度旋转，0是逆时针90度旋转
     * @返回值 result:输出图片Mat
     */
    public static Mat RotateRightImg(Mat src,int flipcode) {
        Mat temp = new Mat();
        Core.transpose(src,temp);
        Mat result =new Mat();
        Core.flip(temp, result, flipcode);
        return result;
    }

    /*
    调整图片的大小
    宽度范围设定在1000左右
     */
    public static Mat AdjustImgSize(Mat src){
        double nHeight = src.rows(),nWidth=src.cols();
        Mat dst=new Mat();
        double fx,fy;
        fy = 500/nHeight;
        fx = 500/nHeight;
        Size dsize = new Size(Math.round(fx*nWidth),Math.round(fy*nHeight));
        Imgproc.resize(src,dst,dsize,fx,fy,Imgproc.INTER_NEAREST);
        return dst;
    }

    public static Mat AdjustImgSize2(Mat src,int newHeight){
        double nHeight = src.rows(),nWidth=src.cols();
        Mat dst=new Mat();
        double fx,fy;
        fy = newHeight/nHeight;
        fx = newHeight/nHeight;
        Size dsize = new Size(Math.round(fx*nWidth),Math.round(fy*nHeight));
        Imgproc.resize(src,dst,dsize,fx,fy,Imgproc.INTER_NEAREST);
        return dst;
    }



    /*
     *图像灰度化，阈值化处理
     *@param
     *
     */
    public static int[] preprocess(Mat img){

        Mat dst1 = AdjustImgSize(img);
        //灰度化
        Imgproc.cvtColor(dst1, dst1, Imgproc.COLOR_RGB2GRAY);
        //中值滤波
        Imgproc.medianBlur(dst1, dst1, 11);
        //自适应阈值
        Imgproc.adaptiveThreshold(dst1, dst1,255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY, 39, 8);
        ocr.setFrameWhite(dst1, 5);
        //形态学膨胀
        Mat kernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT, new Size(5,10));
        Imgproc.erode(dst1, dst1, kernel);
        Log.i("appTest:","图像预处理完成！");

        return ocrRes(dst1);

    }


    public static int[] ocrRes(Mat img){
        List<Mat> lines = new ArrayList<Mat>();
        List<Mat> words = new ArrayList<Mat>();
        List<Integer> res = new ArrayList<Integer>();
        Log.i("appTest","开始文本分行！");
;        lines = ocr.cutImgX(img);
        int count =0;
        int[] number = new int[6];
        int wordcount;
        for(Mat line : lines)
        {
            number[count]=0;
            Log.i("appTest","开始分割第"+count+"字符！");
            words = ocr.cutImgY(line);
            //滤除一行中不属于字符的图片
            for(Mat word : words)
            {
                if(ocr.imgFilt(word))
                {
                    number[count]=10*number[count];
                    //精细切割文本
                    word = ocr.cutTiny(word);
                    if(ocr.charRecognize(word)!=-1&&ocr.imgFilt(word)) {
                        number[count]+=ocr.charRecognize(word);
                    }
                }
            }
            count++;
        }
        return number;
    }


}
