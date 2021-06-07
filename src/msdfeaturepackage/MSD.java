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
public class MSD {
    
    public static double[] microdiscriptor(int[][] ColorX, int CSA, int wid, int hei)
        {
            double[] hist = new double[CSA];

            int[] MS = new int[CSA];
            int[] HA = new int[CSA];

            //----------------------------------------
            for (int i = 0; i < wid - 1; i++)
            {
                for (int j = 0; j < hei - 1; j++)
                {
                    if (ColorX[i][j] >= 0)
                    {
                        HA[ColorX[i][j]] += 1;
                    }
                }
            }
            //----------------------------------------
            for (int i = 3; i < 3 * (wid / 3) - 1; i++)
            {
                for (int j = 3; j < 3 * (hei / 3) - 1; j++)
                {

                    int[] wa = new int[9];


                    wa[0] = ColorX[i - 1][j - 1];
                    wa[1] = ColorX[i - 1][j];
                    wa[2] = ColorX[i - 1][j + 1];

                    wa[3] = ColorX[i + 1][j - 1];
                    wa[4] = ColorX[i + 1][j];
                    wa[5] = ColorX[i + 1][j + 1];

                    wa[6] = ColorX[i][j - 1];
                    wa[7] = ColorX[i][j + 1];
                    wa[8] = ColorX[i][j];
                    //-------------------------
                    int TE1 = 0;

                    for (int m = 0; m < 8; m++)
                    {
                        if ((wa[8] == wa[m]) && (wa[8] >= 0))
                        {
                            TE1 = TE1 + 1;
                        }

                    }
                    if (wa[8] >= 0)
                    {
                        MS[wa[8]] += TE1;
                    }


                }
            }

            // the features vector of MSD 
            for (int i = 0; i < CSA; i++)
            {

                hist[i] = (MS[i] * 1.0) / (8.0 * HA[i] + 0.0001);
            }
            
            return hist;

        }
    
}
