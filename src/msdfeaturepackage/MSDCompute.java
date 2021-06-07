/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msdfeaturepackage;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Asus
 */
public class MSDCompute {

    public static double[] MSD_feature_extract(int[][][] RGB, int wid, int hei)
        {

            // RGB is the input original image datas in RGB color space

            int CSA = 72;  // the total color quantization  number of HSV color space
            int CSB = 6;   //the quantization number of edge orientation

            //--------------------------------------------
            int Cn1 = 8;   //the quantization number of H 
            int Cn2 = 3;   //the quantization number of S 
            int Cn3 = 3;   //the quantization number of V 

            double[] hist = new double[CSA]; // the features vector of MSD 

            int[][] ori = new int[wid][hei];  // the orientation image
            int[][] ImageX = new int[wid][hei]; // the color index image

            double[][][] HSV = new double[3][wid][hei];

            HSV = RGB2HSV.mat_color(RGB, wid, hei); //transform RGB color space to  HSV color space 

            ori = OrientationDetection.Mat_ori_HSV(HSV, CSB, wid, hei);  // edge orientation detection in HSV color space

            ImageX = ColorIndex.Mat_color_HSV(HSV, Cn1, Cn2, Cn3, wid, hei);  //color quantization in HSV color space 

            int D = 1;

            int[][] micro = new int[wid][hei];  //micro-structure image

            micro = MicrostructureDetect.microstructure(ori, ImageX, wid, hei);   //micro-structure map extraction

            hist = MSD.microdiscriptor(micro, CSA, wid, hei);    //micro-structure representation

            /*for(int k=0; k<hist.length; k++){
                System.out.print(hist[k]+" ");
            }*/
            
            return hist;
        }
    
}
