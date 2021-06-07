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
public class RGB2HSV {
    
    public static double[][][] mat_color(int[][][] RGB, int wid, int hei)
        {
            int i, j;
            double[][][] HSV = new double[3][wid][hei];

            for (i = 0; i < wid; i++)
            {
                for (j = 0; j < hei; j++)
                {
                    double cMax = 255.0;


                    int max, min, temp;

                    max = Math.max(RGB[0][i][j], Math.max(RGB[1][i][j], RGB[2][i][j]));
                    min = Math.min(RGB[0][i][j], Math.min(RGB[1][i][j], RGB[2][i][j]));

                    temp = max - min;

                    // V component

                    HSV[2][i][j] = max * 1.0 / cMax;

                    //S component
                    if (max > 0)
                    {
                        HSV[1][i][j] = temp * 1.0 / max;
                    }
                    else
                    {
                        HSV[1][i][j] = 0.0;
                    }
                    //H component
                    if (temp > 0)
                    {
                        double rr = (max - RGB[0][i][j]) * 1.0 / temp * 1.0;
                        double gg = (max - RGB[1][i][j]) * 1.0 / temp * 1.0;
                        double bb = (max - RGB[2][i][j]) * 1.0 / temp * 1.0;
                        double hh = 0.0;

                        if (RGB[0][i][j] == max)
                        {
                            hh = bb - gg;
                        }
                        else if (RGB[1][i][j] == max)
                        {
                            hh = rr - bb + 2.0;
                        }
                        else
                        {
                            hh = gg - rr + 4.0;
                        }
                        if (hh < 0)
                        {
                            hh = hh + 6;
                        }
                        HSV[0][i][j] = hh / 6;

                    }

                    HSV[0][i][j] *= 360.0;

                }
            }
            return HSV;
        }
    
}
