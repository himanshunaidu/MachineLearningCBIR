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
public class MicrostructureDetect {
    
    public static int[][] microstructure(int[][] ori, int[][] ImageX, int wid, int hei)
        {
            int[][] ColorA = new int[wid][hei];
            int[][] ColorB = new int[wid][hei];
            int[][] ColorC = new int[wid][hei];
            int[][] ColorD = new int[wid][hei];

            ColorA = StructureMap.Map(ori,ImageX, wid, hei, 0, 0);
            ColorB = StructureMap.Map(ori,ImageX, wid, hei, 0, 1);
            ColorC = StructureMap.Map(ori,ImageX, wid, hei, 1, 0);
            ColorC = StructureMap.Map(ori,ImageX, wid, hei, 1, 1);

            //=========the final micro-structure map===============
            int[][] micro = new int[wid][hei];

            for (int i = 0; i < wid; i++)
            {

                for (int j = 0; j < hei; j++)
                {

                    micro[i][j] = Math.max(ColorA[i][j], Math.max(ColorB[i][j], 
                            Math.max(ColorC[i][j], ColorD[i][j])));
                }
            }
            
            return micro;
            //============================================
        }
    
}
