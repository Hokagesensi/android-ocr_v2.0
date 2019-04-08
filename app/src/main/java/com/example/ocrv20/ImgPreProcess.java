package com.example.ocrv20;

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
        fy = 1000/nHeight;
        fx = 1000/nHeight;
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
        //灰度化阈值
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
        //自适应阈值
        Imgproc.adaptiveThreshold(img, img,255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, 15, 10);
        //中值滤波
        Imgproc.medianBlur(img, img, 5);
        //二值化像素取反
        Imgreverse(img);

        List<Mat> lines = new ArrayList<Mat>();
        List<Mat> words = new ArrayList<Mat>();
        List<Mat> rawImg = new ArrayList<Mat>();
        List<Mat> trueImg = new ArrayList<Mat>();
        List<String> data = new ArrayList<String>();

        lines = ocr.cutImgX(img);
        int count =1;
        int[] number = new int[6];
        for(Mat line : lines)
        {
            words = ocr.cutImgY(line);
            number[count]=0;
            for(Mat word : words)
            {
                if(ocr.imgFilt(word))
                {
                    //精细切割文本
                    word = ocr.cutTiny(word);
                    number[count]*=10;
                    if(ocr.charRecognize(word)!=-1)
                        number[count]+=ocr.charRecognize(word);
                }
            }
            count++;
        }
        int[] result = new int[6];
        int k=0;
        for(int i =0;i<number.length;i++)
            if(number[i]!=0)
                result[k++]=number[i];
        return result;
    }


    /*
     * 二值图像取反
     * 输入：Mat图像
     * 返回：无
     * @author: madao
     */
    public static void Imgreverse(Mat img)
    {
        for(int i=0;i<img.height();i++)
            for(int j=0;j<img.width();j++)
            {
                if(img.get(i, j)[0]==0.0)
                    img.put(i, j, 255.0);
                else
                    img.put(i, j, 0);

            }
    }


}
