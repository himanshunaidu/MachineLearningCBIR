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
public class StructureMap {
    
    public static int[][] Map(int[][] ori, int[][] img, int wid, int hei, int Dx, int Dy)
        {
            int[][] Color = new int[wid][hei];

            for (int i = 1; i < wid / 3 ; i++)
            {
                for (int j = 1; j < hei / 3 ; j++)
                {
                    int[] WA = new int[9];
                    //===========================
                    int m = 3 * i + Dx;
                    int n = 3 * j + Dy;

                    WA[0] = ori[m - 1][n - 1];
                    WA[1] = ori[m - 1][n];
                    WA[2] = ori[m - 1][n + 1];

                    WA[3] = ori[m + 1][n - 1];
                    WA[4] = ori[m + 1][n];
                    WA[5] = ori[m + 1][n + 1];

                    WA[6] = ori[m][n - 1];
                    WA[7] = ori[m][n + 1];
                    WA[8] = ori[m][n];

                    //-------------------------
                    if (WA[8] == WA[0])
                    {
                        Color[m - 1][n - 1] = img[m - 1][n - 1];
                    }
                    else
                    {
                        Color[m - 1][n - 1] = -1;
                    }
                    //--------------------
                    if (WA[8] == WA[1])
                    {
                        Color[m - 1][n] = img[m - 1][n];
                    }
                    else
                    {
                        Color[m - 1][n] = -1;
                    }
                    //----------------------
                    if (WA[8] == WA[2])
                    {
                        Color[m - 1][n + 1] = img[m - 1][n + 1];
                    }
                    else
                    {
                        Color[m - 1][n + 1] = -1;
                    }
                    //----------------------
                    if (WA[8] == WA[3])
                    {
                        Color[m + 1][n - 1] = img[m + 1][n - 1];
                    }
                    else
                    {
                        Color[m + 1][n - 1] = -1;

                    }
                    //-------------------------
                    if (WA[8] == WA[4])
                    {
                        Color[m + 1][n] = img[m + 1][n];
                    }
                    else
                    {
                        Color[m + 1][n] = -1;
                    }
                    //--------------------------
                    if (WA[8] == WA[5])
                    {
                        Color[m + 1][n + 1] = img[m + 1][n + 1];
                    }
                    else
                    {
                        Color[m + 1][n + 1] = -1;
                    }
                    //-----------------------------------------
                    if (WA[8] == WA[6])
                    {

                        Color[m][n - 1] = img[m][n - 1];
                    }
                    else
                    {
                        Color[m][n - 1] = -1;
                    }
                    //----------------------------------------
                    if (WA[8] == WA[7])
                    {
                        Color[m][n + 1] = img[m][n + 1];
                    }
                    else
                    {
                        Color[m][n + 1] = -1;
                    }
                    //------------------------------------------
                    if (WA[8] == WA[8]) Color[m][n] = img[m][n];
                }
            }
            return Color;
        }
    
}
