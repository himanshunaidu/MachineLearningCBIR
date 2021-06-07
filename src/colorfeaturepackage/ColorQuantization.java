package colorfeaturepackage;

public class ColorQuantization {
	//return img
	public static int[][] cLab(double[][][] Lab, int colnum1, int colnum2, int colnum3, int wid, int hei)
    {
        int[][] img = new int[wid][ hei];


        int L = 0, a = 0, b = 0;

        for (int i = 0; i < wid; i++)
        {
            for (int j = 0; j < hei; j++)
            {
                L = (int)(Lab[0][ i][ j] * colnum1 / 100.0);

                if (L >= (colnum1 - 1))
                {
                    L = colnum1 - 1;
                }
                if (L < 0)
                {
                    L = 0;
                }

                a = (int)((Lab[1][ i][ j] + 127) * colnum2 / 254.0);
                if (a >= (colnum2 - 1))
                {
                    a = colnum2 - 1;
                }
                if (a < 0)
                {
                    a = 0;
                }

                b = (int)((Lab[2][ i][ j] + 127) * colnum3 / 254.0);
                if (b >= (colnum3 - 1))
                {
                    b = colnum3 - 1;
                }
                if (b < 0)
                {
                    b = 0;
                }

                //-------------------------------------------
                img[i][ j] = (colnum3 * colnum2) * L + colnum3 * a + b;

            }
        }
        return img;
    }

}
