package com.example.avocado.ui.home;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Model;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;
import org.opencv.android.OpenCVLoader;
import org.tensorflow.lite.Interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageProcessing {


    static {
        if (!OpenCVLoader.initDebug()) {
            // OpenCV 초기화 실패
            Log.e("OpenCV", "Failed to initialize OpenCV");
        } else {
            // OpenCV 초기화 성공
            Log.d("OpenCV", "OpenCV initialized successfully");
        }
    }

    public String processImage(Mat image,Interpreter tflite) {
        // Convert the image to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Apply Gaussian blur
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(gray, blurred, new Size(5, 5), 0);

        // Perform edge detection
        Mat edged = new Mat();
        Imgproc.Canny(blurred, edged, 30, 150);

        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edged, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Sort contours from left to right
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint contour1, MatOfPoint contour2) {
                Rect rect1 = Imgproc.boundingRect(contour1);
                Rect rect2 = Imgproc.boundingRect(contour2);
                return Integer.compare(rect1.x, rect2.x);
            }
        });

        // Initialize the list of contour bounding boxes and associated characters
        List<Rect> chars = new ArrayList<>();

        // Loop over the contours
        for (MatOfPoint contour : contours) {
            // Compute the bounding box of the contour
            Rect rect = Imgproc.boundingRect(contour);

            // Filter out bounding boxes that are neither too small nor too large
            if ((rect.width >= 5 && rect.width <= 150) && (rect.height >= 15 && rect.height <= 120)) {
                // Extract the character and threshold it
                Mat roi = gray.submat(rect);
                Mat thresh = new Mat();
                Imgproc.threshold(roi, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

                // Resize the thresholded image to 28x28
                Mat resized = new Mat();
                Size size = new Size(28, 28);
                Imgproc.resize(thresh, resized, size);

                // Prepare the image for classification
                resized.convertTo(resized, CvType.CV_32F);
                Core.divide(resized, new Scalar(255.0), resized);

                // Update the list of characters
                chars.add(rect);
            }
        }

        // OCR the characters using your handwriting recognition model
        // ...

        String ans=null;

        for (Rect rect : chars) {
            ans += performOCR(image, rect,tflite);
            // recognizedText를 처리하거나 저장하는 등의 작업 수행
        }

        // Return the processed image
        return ans;
    }


    private String performOCR(Mat image, Rect rect, Interpreter tflite) {
        // Extract the region of interest (ROI) from the image based on the given bounding box (rect)
        Mat roi = image.submat(rect);

        // Preprocess the ROI for classification
        Mat processedROI = preprocessImage(roi);

        // Prepare the input tensor for the model
        float[][][][] input = new float[1][28][28][1];
        convertMatToInputTensor(processedROI, input);

        // Allocate output tensors
        float[][] output = new float[1][26];

        // Run inference with the TensorFlow Lite interpreter
        tflite.run(input, output);

        // Get the predicted character
        int predictedClassIndex = getPredictedClassIndex(output);
        char predictedChar = (char) ('A' + predictedClassIndex);

        return String.valueOf(predictedChar);
    }

    private Mat preprocessImage(Mat image) {
        // Convert the image to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Apply thresholding
        Mat thresholded = new Mat();
        Imgproc.threshold(gray, thresholded, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        // Resize the image to 28x28
        Mat resized = new Mat();
        Imgproc.resize(thresholded, resized, new Size(28, 28));

        // Normalize the image
        resized.convertTo(resized, CvType.CV_32F);
        Core.divide(resized, new Scalar(255.0), resized);

        return resized;
    }

    private void convertMatToInputTensor(Mat image, float[][][][] input) {
        int rows = image.rows();
        int cols = image.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float pixelValue = (float) image.get(i, j)[0];
                input[0][i][j][0] = pixelValue;
            }
        }
    }

    private int getPredictedClassIndex(float[][] output) {
        int maxIndex = 0;
        float maxProb = output[0][0];
        for (int i = 1; i < output[0].length; i++) {
            if (output[0][i] > maxProb) {
                maxIndex = i;
                maxProb = output[0][i];
            }
        }
        return maxIndex;
    }

}
