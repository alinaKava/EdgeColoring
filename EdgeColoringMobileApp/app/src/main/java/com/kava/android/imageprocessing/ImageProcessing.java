package com.kava.android.imageprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageProcessing {
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
// if width is 0 then it did not read your image.


// for the canny edge detection algorithm, play with these to see different results
        int threshold1 = 70;
        int threshold2 = 100;


        //Mat im_canny = new Mat();  // you have to initialize output image before giving it to the Canny method
        //Imgproc.Canny(image, im_canny, threshold1, threshold2);
        String cannyFilename = outputDir+"ffGaus2.png";// + File.separator + inputFileName + "_canny-" + threshold1 + "-" + threshold2 + "." + outputExtension;
        //Log.d(this.getClass().getSimpleName(), "Writing " + cannyFilename);
        //Mat thresh = new Mat();
        //Imgproc.threshold(im_canny, thresh, 30, 255, Imgproc.THRESH_BINARY_INV);
        Mat imageMat = new Mat();
        Utils.bitmapToMat(myBitmap, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(imageMat, imageMat, new Size(3, 3), 0);
        //Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(3, 3), 0);
        Imgproc.threshold(imageMat, imageMat, 0, 255, Imgproc.THRESH_OTSU);
        Imgcodecs.imwrite(cannyFilename, imageMat);

    }
}

