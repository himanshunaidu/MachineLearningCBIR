package dwtpackage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DWTProcessor {
	
	//Here widthandHeight = width
	//	   widthandHeight2 = height

	private JFileChooser fc = new JFileChooser();
	
	private int[][] transformPixels(int[][] pixels, int widthHeight, int widthHeight2) {
	    double[][] temp_bank = new double[widthHeight2][widthHeight];
	    double a1 = -1.586134342;
	    double a2 = -0.05298011854;
	    double a3 = 0.8829110762;
	    double a4 = 0.4435068522;

	    // Scale coeff:
	    double k1 = 0.81289306611596146; // 1/1.230174104914
	    double k2 = 0.61508705245700002;// 1.230174104914/2
	    for (int i = 0; i < 2; i++) {
	        for (int col = 0; col < widthHeight2; col++) {
	            // Predict 1
	            for (int row = 1; row < widthHeight - 1; row += 2) {
	                pixels[row][col] += a1 * (pixels[row - 1][col] + pixels[row + 1][col]);
	            }
	            pixels[widthHeight - 1][col] += 2 * a1 * pixels[widthHeight - 2][col];

	            // Update 1
	            for (int row = 2; row < widthHeight-2; row += 2) {
	                pixels[row][col] += a2 * (pixels[row - 1][col] + pixels[row + 1][col]);
	            }
	            pixels[0][col] += 2 * a2 * pixels[1][col];

	            // Predict 2
	            for (int row = 1; row < widthHeight - 1; row += 2) {
	                pixels[row][col] += a3 * (pixels[row - 1][col] + pixels[row + 1][col]);
	            }
	            pixels[widthHeight - 1][col] += 2 * a3 * pixels[widthHeight - 2][col];

	            // Update 2
	            for (int row = 2; row < widthHeight-2; row += 2) {
	                pixels[row][col] += a4 * (pixels[row - 1][col] + pixels[row + 1][col]);
	            }
	            pixels[0][col] += 2 * a4 * pixels[1][col];
	        }

	        for (int row = 0; row < widthHeight; row++) {
	            for (int col = 0; col < widthHeight2; col++) {
	                if (row % 2 == 0)
	                    temp_bank[col][row / 2] = k1 * pixels[row][col];
	                	//temp_bank[col][row/2] = pixels[row][col];
	                else
	                    temp_bank[col][row / 2 + widthHeight / 2] = k2 * pixels[row][col];
	                	//temp_bank[col][row/ 2 + widthHeight / 2] = pixels[row][col];

	            }
	        }

	        for (int row = 0; row < widthHeight; row++) {
	            for (int col = 0; col < widthHeight2; col++) {
	                pixels[row][col] = (int) temp_bank[col][row];
	            }
	        }
	    }
	    
	    int[][] newpixels = new int[widthHeight/4][widthHeight2];
	    for(int j=(3*widthHeight/4); j<(widthHeight-3); j++){
	    	for(int i=0; i<(widthHeight2); i++){
        		System.out.println("New Dimensions "+j+", "+i);
        		newpixels[j-(3*widthHeight/4)][i] = pixels[j][i];
        	}
        }
	    
	    return newpixels;
	}
	
	public BufferedImage openImage(JPanel panel, BufferedImage image) throws InvalidWidthHeightException {
	    try {
	        int returnVal = fc.showOpenDialog(panel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            BufferedImage temp = ImageIO.read(file);
	            if (temp == null)
	                return null;
	            int checkInt = temp.getWidth();
	            //boolean check = (checkInt & (checkInt - 1)) == 0;
	            boolean check = true;
	            if (!check)
	                throw new InvalidWidthHeightException();
	            int widthandHeight = temp.getWidth();
	            int widthandHeight2 = temp.getHeight();
	            image = new BufferedImage(widthandHeight, widthandHeight2, BufferedImage.TYPE_BYTE_GRAY);
	            Graphics g = image.getGraphics();
	            g.drawImage(temp, 0, 0, null);
	            g.dispose();

	            return image;

	        }
	    } catch (IOException e) {
	        System.out.println("Failed to load image!");
	    }
	    return null;

	}

	public BufferedImage transform(int count, BufferedImage transformedImage, BufferedImage image) {
	    int[][] pixels = getGrayValues(image);
	    int transformedPixels[][];
	    int width = pixels.length;
	    int height = pixels[0].length;
	    System.out.println("Dimensions: "+width+", "+height);
	    transformedPixels = transformPixels(pixels, width, height);
	    width/=2;

	    for (int i = 1; i < count; i++) {
	        transformedPixels = transformPixels(transformedPixels, width, height);
	        width/=2;
	    }
	    width = transformedPixels.length;
	    height = transformedPixels[0].length;
	    transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            transformedImage.setRGB(x, y, transformToRGB(transformedPixels[x][y]));
	        }
	    }
	    return transformedImage;
	}

	private int transformToRGB(double d) {
	    int value = (int) d;
	    if (d < 0)
	        d = 0;
	    if (d > 255)
	        d = 255;
	    return 0xffffffff << 24 | value << 16 | value << 8 | value;
	}

	private int[][] getGrayValues(BufferedImage image) {
	    int[][] res = new int[image.getWidth()][image.getHeight()];
	    int r, g, b;
	    for (int i = 0; i < image.getWidth(); i++) {
	        for (int j = 0; j < image.getHeight(); j++) {
	            int value = image.getRGB(i, j);
	            r = (value >> 16) & 0xFF;
	            g = (value >> 8) & 0xFF;
	            b = (value & 0xFF);
	            res[i][j] = (r + g + b) / 3;
	        }
	    }
	    return res;
	}

}
