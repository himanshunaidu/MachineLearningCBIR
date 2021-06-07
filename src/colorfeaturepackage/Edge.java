package colorfeaturepackage;

public class Edge {
	//return ori
	public static int[][] maxgrad_and_mingrad_Lab(double[][][] LAB, int num, int wid, int hei)
    {
        double[][][] Lab = new double[3][wid][hei];

        Lab = CoordinateTransformation.LAB2Lab(LAB, wid, hei);

        int[][] ori = new int[wid][ hei];

        double gxx = 0.0, gyy = 0.0, gxy = 0.0;

        double rh = 0.0, gh = 0.0, bh = 0.0;
        double rv = 0.0, gv = 0.0, bv = 0.0;

        double theta = 0.0;
        
        

        for (int i = 1; i <= wid - 2; i++)
        {
            for (int j = 1; j <= hei - 2; j++)
            {


                rh = (Lab[0][ i - 1][ j + 1] + 2 * Lab[0][ i][ j + 1] + Lab[0][ i + 1][ j + 1]) - (Lab[0][ i - 1][ j - 1] + 2 * Lab[0][ i][ j - 1] + Lab[0][ i + 1][ j - 1]);
                gh = (Lab[1][ i - 1][ j + 1] + 2 * Lab[1][ i][ j + 1] + Lab[1][ i + 1][ j + 1]) - (Lab[1][ i - 1][ j - 1] + 2 * Lab[1][ i][ j - 1] + Lab[1][ i + 1][ j - 1]);
                bh = (Lab[2][ i - 1][ j + 1] + 2 * Lab[2][ i][ j + 1] + Lab[2][ i + 1][ j + 1]) - (Lab[2][ i - 1][ j - 1] + 2 * Lab[2][ i][ j - 1] + Lab[2][ i + 1][ j - 1]);

                //-----------------------------------------
                rv = (Lab[0][ i + 1][ j - 1] + 2 * Lab[0][ i + 1][ j] + Lab[0][ i + 1][ j + 1]) - (Lab[0][ i - 1][ j - 1] + 2 * Lab[0][ i - 1][ j] + Lab[0][ i - 1][ j + 1]);
                gv = (Lab[1][ i + 1][ j - 1] + 2 * Lab[1][ i + 1][ j] + Lab[1][ i + 1][ j + 1]) - (Lab[1][ i - 1][ j - 1] + 2 * Lab[1][ i - 1][ j] + Lab[1][ i - 1][ j + 1]);
                bv = (Lab[2][ i + 1][ j - 1] + 2 * Lab[2][ i + 1][ j] + Lab[2][ i + 1][ j + 1]) - (Lab[2][ i - 1][ j - 1] + 2 * Lab[2][ i - 1][ j] + Lab[2][ i - 1][ j + 1]);

                //---------------------------------------
                gxx = rh * rh + gh * gh + bh * bh;
                gyy = rv * rv + gv * gv + bv * bv;
                gxy = rh * rv + gh * gv + bh * bv;

                theta = Math.round(Math.atan(2.0 * gxy / (gxx - gyy + 0.00001)) / 2.0*Math.pow(10, 4))/Math.pow(10, 4);

                double G1 = 0.0;
                double G2 = 0.0;

                G1 = Math.sqrt(0.5 * ((gxx + gyy) + (gxx - gyy) * Math.cos(2.0 * theta) + 2.0 * gxy * Math.sin(2.0 * theta)));
                G2 = Math.sqrt(0.5 * ((gxx + gyy) + (gxx - gyy) * Math.cos(2.0 * (theta + (Math.PI / 2.0))) + 2.0 * gxy * Math.sin(2.0 * (theta + (Math.PI / 2.0)))));

                double dir = 0;


                if (Math.max(G1, G2) == G1)
                {
                    dir = 90.0 + theta * 180.0 / Math.PI;
                    ori[i][ j] = (int)(dir * num / 360.0);
                }
                else
                {
                    dir = 180.0 + (theta + Math.PI / 2.0) * 180.0 / Math.PI;

                    ori[i][ j] = (int)(dir * num / 360.0);
                }

                if (ori[i][ j] >= num - 1) ori[i][ j] = num - 1;

            }
        }
        return ori;
    }

}
