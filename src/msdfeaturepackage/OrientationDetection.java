/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msdfeaturepackage;
import java.math.*;
/**
 *
 * @author Asus
 */
public class OrientationDetection {
    
    public static int[][] Mat_ori_HSV(double[][][] HSV, int num, int wid, int hei) 
        {
            int[][] ori = new int[wid][hei];

            double gxx = 0.0, gyy = 0.0, gxy = 0.0;

            double rh = 0.0, gh = 0.0, bh = 0.0;
            double rv = 0.0, gv = 0.0, bv = 0.0;

            double theta = 0.0;
            double[][][] hsv = new double[3][wid][hei];


            for (int i = 0; i < wid; i++)  // HSV based on cylinder 
            {
                for (int j = 0; j < hei; j++)
                {
                    hsv[0][i][j] = HSV[1][i][j] * Math.cos(HSV[0][i][j]);
                    hsv[1][i][j] = HSV[1][i][j] * Math.sin(HSV[0][i][j]);
                    hsv[2][i][j] = HSV[2][i][j];
                }
            }


            for (int i = 1; i <= wid - 2; i++)
            {
                for (int j = 1; j <= hei - 2; j++)
                {

                    //--------------------------------------
                    rh = (double)(hsv[0][i - 1][j + 1] + 2 * hsv[0][i][j + 1] + hsv[0][i + 1][j + 1]) - 
                            (hsv[0][i - 1][j - 1] + 2 * hsv[0][i][j - 1] + hsv[0][i + 1][j - 1]);
                    gh = (double)(hsv[1][i - 1][j + 1] + 2 * hsv[1][i][j + 1] + hsv[1][i + 1][j + 1]) - 
                    (hsv[1][i - 1][j - 1] + 2 * hsv[1][i][j - 1] + hsv[1][i + 1][j - 1]);
                    bh = (double)(hsv[2][i - 1][j + 1] + 2 * hsv[2][i][j + 1] + hsv[2][i + 1][j + 1]) - 
                    (hsv[2][i - 1][j - 1] + 2 * hsv[2][i][j - 1] + hsv[2][i + 1][j - 1]);

                    //-----------------------------------------
                    rv = (double)(hsv[0][i + 1][j - 1] + 2 * hsv[0][i + 1][j] + hsv[0][i + 1][j + 1]) - 
                    (hsv[0][i - 1][j - 1] + 2 * hsv[0][i - 1][j] + hsv[0][i - 1][j + 1]);
                    gv = (double)(hsv[1][i + 1][j - 1] + 2 * hsv[1][i + 1][j] + hsv[1][i + 1][j + 1]) - 
                    (hsv[1][i - 1][j - 1] + 2 * hsv[1][i - 1][j] + hsv[1][i - 1][j + 1]);
                    bv = (double)(hsv[2][i + 1][j - 1] + 2 * hsv[2][i + 1][j] + hsv[2][i + 1][j + 1]) - 
                    (hsv[2][i - 1][j - 1] + 2 * hsv[2][i - 1][j] + hsv[2][i - 1][j + 1]);

                    //---------------------------------------
                    gxx = Math.sqrt(rh * rh + gh * gh + bh * bh);
                    gyy = Math.sqrt(rv * rv + gv * gv + bv * bv);
                    gxy = rh * rv + gh * gv + bh * bv;

                    theta = (Math.acos(gxy / (gxx * gyy + 0.0001)) * 180.0 / Math.PI);

                    ori[i][j] = (int)(Math.round(theta * num / 180.0));

                    if (ori[i][j] >= num - 1) ori[i][j] = num - 1;

                }
            }
            
            return ori;
        }
    
}
