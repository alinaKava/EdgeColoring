package com.kava.android.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageProcessing {

    public Mat difOfGaussianDetect(Mat originalMat)
    {
        Mat grayMat = new Mat();
        Mat blur1 = new Mat();
        Mat blur2 = new Mat();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(grayMat, blur1, new Size(15, 15), 5);
        Imgproc.GaussianBlur(grayMat, blur2, new Size(21, 21), 5);

        Mat doG = new Mat();

        Core.absdiff(blur1, blur2, doG);

        Core.multiply(doG, new Scalar(100), doG);
        Imgproc.threshold(doG, doG, 50, 255, Imgproc.THRESH_BINARY_INV);

        return doG;
    }

    public Mat cannyDetect(Mat originalMat)
    {
        Mat grayMat = new Mat();
        Mat cannyEdges = new Mat();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Canny(grayMat, cannyEdges, 70, 100);

        Imgproc.threshold(cannyEdges, cannyEdges, 30, 255, Imgproc.THRESH_BINARY_INV);

        return cannyEdges;
    }

    public Mat laplacianDetect(Mat originalMat)
    {
        Mat grayMat = new Mat();
        Mat laplacianImage = new Mat();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Laplacian(grayMat, laplacianImage, CvType.CV_8U);
        Imgproc.threshold(laplacianImage, laplacianImage, 30, 255, Imgproc.THRESH_BINARY_INV);

        return laplacianImage;
    }

    public Mat sobelDetect(Mat originalMat)
    {
        Mat grayMat = new Mat();
        Mat sobel = new Mat();

        Mat gradX = new Mat();
        Mat absGradX = new Mat();
        Mat gradY = new Mat();
        Mat absGradY = new Mat();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Sobel(grayMat, gradX, CvType.CV_16S, 1, 0, 3, 1, 0);
        Imgproc.Sobel(grayMat, gradY, CvType.CV_16S, 0, 1, 3, 1, 0);

        Core.convertScaleAbs(gradX, absGradX);
        Core.convertScaleAbs(gradY, absGradY);

        Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 1, sobel);

        Imgproc.threshold(sobel, sobel, 30, 255, Imgproc.THRESH_BINARY_INV);

        return sobel;
    }

    public Mat harrisCornerDetect(Mat originalMat)
    {
        Mat grayMat = new Mat();
        Mat corners = new Mat();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Mat tempDst = new Mat();
        Imgproc.cornerHarris(grayMat, tempDst, 2, 3, 0.04);

        Mat tempDstNorm = new Mat();
        Core.normalize(tempDst, tempDstNorm, 0, 255, Core.NORM_MINMAX);
        Core.convertScaleAbs(tempDstNorm, corners);

        Random r = new Random();
        for (int i = 0; i < tempDstNorm.cols(); i++)
        {
            for (int j = 0; j < tempDstNorm.rows(); j++)
            {
                double[] value = tempDstNorm.get(j, i);
                if (value[0] > 150)
                    Imgproc.circle(corners, new Point(i, j), 5, new Scalar(r.nextInt(255)), 255);
            }
        }

        return corners;
    }

    public Mat getContours(Mat originalMat) {
        Mat grayMat = new Mat();
        Mat cannyEdges = new Mat();
        Mat hierarchy = new Mat();

        List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();

        Imgproc.cvtColor(originalMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Canny(grayMat, cannyEdges, 10, 100);
        Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat contours = new Mat();
        contours.create(cannyEdges.rows(), cannyEdges.cols(), CvType.CV_8UC3);
        Random r = new Random();

        for (int i = 0; i < contourList.size(); i++)
        {
            Imgproc.drawContours(contours, contourList, i, new Scalar(r.nextInt(255)), -1);
        }
        Imgproc.threshold(contours, contours, 30, 255, Imgproc.THRESH_BINARY_INV);
        return contours;
    }

    public void loadImage(String imagePath)
    {
//        File imgFile = new File("/sdcard/Images/test_image.jpg");
//
//        if(imgFile.exists()){
//
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//        }
        String inputFileName="simm_01";
        String inputExtension = "jpg";
        String inputDir = imagePath;//getCacheDir().getAbsolutePath();  // use the cache directory for i/o
        String outputDir = imagePath;//getCacheDir().getAbsolutePath();
        String outputExtension = "png";
        String inputFilePath = inputDir + File.separator + inputFileName + "." + inputExtension;

        File imgFile = new File(inputFilePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        }
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Log.d(this.getClass().getSimpleName(), "loading " + inputFilePath + "...");
        Mat image = Imgcodecs.imread(inputFilePath);
        Log.d (this.getClass().getSimpleName(), "width of " + inputFileName + ": " + image.width());

        String cannyFilename = outputDir+"ffLapl2.png";// + File.separator + inputFileName + "_canny-" + threshold1 + "-" + threshold2 + "." + outputExtension;

        Mat imageMat = new Mat();
        Utils.bitmapToMat(myBitmap, imageMat);
        imageMat = laplacianDetect(imageMat);

        Imgcodecs.imwrite(cannyFilename, imageMat);

    }
}

