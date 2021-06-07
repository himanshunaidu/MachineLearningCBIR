package colorfeaturepackage;

public class CDH {
	//return hist
	public static double[] compute(int[][] ColorX, int[][] ori, double[][][] LAB, int wid, int hei, int CSA, int CSB, int D)
        {	
			double[] hist;
            double[][][] Arr = new double[3][ wid][ hei];

            Arr = CoordinateTransformation.LAB2Lab(LAB, wid, hei);

            double[] Matrix = new double[CSA + CSB];

            hist = new double[CSA + CSB];

            //-------------------calculate the color difference of different directions------------

            int i, j;

            //----------direction=0--------------------

            for (i = 0; i <= wid - 1; i++)
            {
                for (j = 0; j <= hei - D - 1; j++)
                {
                    double value = 0.0;

                    if (ori[i][ j + D] == ori[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i][ j + D] - Arr[2][ i][ j], 2));

                        Matrix[ColorX[i][ j]] += value;

                    }
                    if (ColorX[i][ j + D] == ColorX[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i][ j + D] - Arr[2][ i][ j], 2));

                        Matrix[ori[i][ j] + CSA] += value;
                    }
                }
            }

            //-----------direction=90---------------------

            for (i = 0; i <= wid - D - 1; i++)
            {
                for (j = 0; j <= hei - 1; j++)
                {
                    double value = 0.0;

                    if (ori[i + D][ j] == ori[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i + D][ j] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i + D][ j] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i + D][ j] - Arr[2][ i][ j], 2));

                        Matrix[ColorX[i][ j]] += value;

                    }
                    if (ColorX[i + D][ j] == ColorX[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i + D][ j] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i + D][ j] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i + D][ j] - Arr[2][ i][ j], 2));

                        Matrix[ori[i][ j] + CSA] += value;

                    }
                }
            }

            //-----------direction=135---------------------

            for (i = 0; i <= wid - D - 1; i++)
            {
                for (j = 0; j <= hei - D - 1; j++)
                {
                    double value = 0.0;

                    if (ori[i + D][ j + D] == ori[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i + D][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i + D][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i + D][ j + D] - Arr[2][ i][ j], 2));

                        Matrix[ColorX[i][ j]] += value;

                    }
                    if (ColorX[i + D][ j + D] == ColorX[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i + D][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i + D][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i + D][ j + D] - Arr[2][ i][ j], 2));

                        Matrix[ori[i][ j] + CSA] += value;

                    }
                }
            }

            //-----------direction=45---------------------

            for (i = D; i <= wid - 1; i++)
            {
                for (j = 0; j <= hei - D - 1; j++)
                {
                    double value = 0.0;

                    if (ori[i - D][ j + D] == ori[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i - D][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i - D][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i - D][ j + D] - Arr[2][ i][ j], 2));
                        Matrix[ColorX[i][ j]] += value;

                    }
                    if (ColorX[i - D][ j + D] == ColorX[i][ j])
                    {
                        value = Math.sqrt(Math.pow(Arr[0][ i - D][ j + D] - Arr[0][ i][ j], 2) + Math.pow(Arr[1][ i - D][ j + D] - Arr[1][ i][ j], 2) + Math.pow(Arr[2][ i - D][ j + D] - Arr[2][ i][ j], 2));
                        Matrix[ori[i][ j] + CSA] += value;

                    }
                }
            }

            for (i = 0; i < CSA + CSB; i++)
            {

                hist[i] = (Matrix[i]) / 4.0;

            }
            
            
            return hist;
        }
        
}
