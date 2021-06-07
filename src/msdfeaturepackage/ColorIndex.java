/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package msdfeaturepackage;

/**
 *
 * @author Asus
 */
public class ColorIndex {
    
    public static int[][] Mat_color_HSV(double[][][] HSV, int colnum1, int colnum2, int colnum3, int wid, int hei)
    {
            int[][] img = new int[wid][hei];

           
            int VI = 0, SI = 0, HI = 0;

            for (int i = 0; i < wid; i++)
            {
                for (int j = 0; j < hei; j++)
                {

                 VI = (int)(HSV[0][i][j] * (colnum1 / 360.0));


                    if (VI >= colnum1 - 1)
                    {
                        VI = colnum1 - 1;
                    }
                    //-------------------------------------

                    SI = (int)(HSV[1][i][j] * (colnum2 / 1.0));

                    if (SI >= colnum2 - 1)
                    {
                        SI = colnum2 - 1;
                    }
                    // -------------------------------------------

                    HI = (int)(HSV[2][i][j] * (colnum3 / 1.0));

                    if (HI >= colnum3 - 1)
                    {
                        HI = colnum3 - 1;
                    }

                   
                    //-------------------------------------------
                    img[i][j] = (colnum3 * colnum2) * VI + colnum3 * SI + HI;

                }
            }
            return img;
    }
    
}
