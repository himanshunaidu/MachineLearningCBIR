package colorfeaturepackage;

public class CoordinateTransformation {
	
	protected static final double BLACK = 20d;
    protected static final double YELLOW = 70d;

    //return Lab
    public static double[][][] LAB2Lab(double[][][] RGB, int wid, int hei)
    {
        int i, j;
        double[][][] Lab = new double[3][ wid][ hei];

        for (i = 0; i < wid; i++)
        {
            for (j = 0; j < hei; j++)
            {

                double X, Y, Z, fX, fY, fZ;

                X = 0.412453 * RGB[0][ i][ j] + 0.357580 * RGB[1][ i][ j] + 0.180423 * RGB[2][ i][ j];
                Y = 0.212671 * RGB[0][ i][ j] + 0.715160 * RGB[1][ i][ j] + 0.072169 * RGB[2][ i][ j];
                Z = 0.019334 * RGB[0][ i][ j] + 0.119193 * RGB[1][ i][ j] + 0.950227 * RGB[2][ i][ j];

                X /= (255 * 0.950456);
                Y /= (255 * 1.000000);
                Z /= (255 * 1.088754);

                if (Y > 0.008856)
                {
                    fY = Math.pow(Y, 1.0 / 3.0);
                    Lab[0][ i][ j] = 116.0 * fY - 16.0;
                }
                else
                {
                    fY = 7.787 * Y + 16.0 / 116.0;
                    Lab[0][ i][ j] = 903.3 * Y;
                }

                if (X > 0.008856)
                {
                    fX = Math.pow(X, 1.0 / 3.0);
                }
                else
                {
                    fX = 7.787 * X + (16.0 / 116.0);
                }

                if (Z > 0.008856)
                {
                    fZ = Math.pow(Z, 1.0 / 3.0);
                }
                else
                {
                    fZ = 7.787 * Z + (16.0 / 116.0);
                }

                Lab[1][ i][ j] = 500.0 * (fX - fY);
                Lab[2][ i][ j] = 200.0 * (fY - fZ);

                if (Lab[0][ i][ j] < BLACK)
                {
                    Lab[1][ i][ j] *= Math.exp((Lab[0][i][ j] - BLACK) / (BLACK / 4));
                    Lab[2][ i][ j] *= Math.exp((Lab[0][ i][ j] - BLACK) / (BLACK / 4));
                    Lab[0][ i][ j] = BLACK;
                }
                if (Lab[2][ i][ j] > YELLOW)
                    Lab[2][ i][ j] = YELLOW;

            }
        }
        return Lab;
    }

}
