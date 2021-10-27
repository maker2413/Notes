/*
2D Arrays: Image Manipulation Project
In this project, you will be creating an application which is able to modify images as well as create new images using 2D arrays! The first section covers stretching the image horizontally, shrinking the image vertically, negating the color, applying a color filter, and inverting the image. The second section covers creating an image consisting of random pixels, placing a rectangle in the image, and using the method to randomly place many rectangles in the image.

Here is an overview about how images work in Java:

Images consist of pixels which are the individual points in the image containing some color. Each pixel has some red, green, blue, and alpha value which represents the amount of each of those colors in the pixel. The red, green, and blue values can be mixed to create all of the visible colors on your screen. The alpha value represents the transparency of the pixel (or how close the color of the pixel is to the background color of the image). A higher resolution image means that there are more pixels contained within it.

In Java, a loaded image is stored into a BufferedImage object. From this object, we can extract each pixel value and store it into a 2D array which we can manipulate. The pixel values are stored as ints because each pixel value in the BufferedImage object is represented by a hexadecimal value which contains the red, green, blue, and alpha components. The maximum value of any of the RGBA values is 255 and the minimum is 0. There are some methods provided for you in this project which handle the conversion between images and 2D arrays as well as extracting the R, G, B, and A values from a pixel. You will only need to implement methods which work with the 2D arrays.

Each of these methods is executable independently so you can select which methods you want to complete or you can complete all of them. You also may want to adjust the window sizes in the workspace to better see the code and the image. You can drag the window borders to adjust the size.

There are a lot of different parts to this project, so you may feel overwhelmed when first looking at it. Remember, the goal here is to practice using 2D arrays. We’ve included the solution code as a file named Solution.java in case you get stuck.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class ImageProcessing {
    public static void main(String[] args) {
        // The provided images are apple.jpg, flower.jpg, and kitten.jpg
        int[][] imageData = imgToTwoD("./kitten.jpg");
        // Or load your own image using a URL!
        //int[][] imageData = imgToTwoD("https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");
        // viewImageData(imageData);
        int[][] trimmed = trimBorders(imageData, 30);
        twoDToImage(trimmed, "./trimmed_kitten.jpg");
        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "./negative_kitten.jpg");
        int[][] horizontal = stretchHorizontally(imageData);
        twoDToImage(horizontal, "./horizontal_kitten.jpg");
        int[][] vertical = shrinkVertically(imageData);
        twoDToImage(vertical, "./vertical_kitten.jpg");
        int[][] flip = invertImage(imageData);
        twoDToImage(flip, "./flip_kitten.jpg");
        int[][] filter = colorFilter(imageData, -50, 20 ,75);
        twoDToImage(filter, "./filter_kitten.jpg");
        int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));
        twoDToImage(allFilters, "./allFilters_kitten.jpg");
        // Painting with pixels
        int[][] painting = new int[500][500];
        painting = paintRandomImage(painting);
        twoDToImage(painting, "./painting.jpg");
        int[] rgba = {255, 255, 0, 255};
        int[][] rectangleImg = paintRectangle(imageData, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImg, "./rectangle.jpg");
        int[][] randomRectangle = generateRectangles(imageData, 1000);
        twoDToImage(randomRectangle, "./random_rectangle.jpg");
    }

    // Image Processing Methods
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        // Example Method
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }

    public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];
        for(int i = 0; i < imageTwoD.length; i++) {
            for(int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);

                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];

                newImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return newImage;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int it = 0;
        for(int i = 0; i < imageTwoD.length; i++) {
            for(int j = 0; j < imageTwoD[i].length; j++) {
                it = j * 2;
                newImage[i][it] = imageTwoD[i][j];
                newImage[i][it+1] = imageTwoD[i][j];
            }
        }
        return newImage;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] newImage = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for(int i = 0; i < imageTwoD[0].length; i++) {
            for(int j = 0; j < imageTwoD.length - 1; j+=2) {
                newImage[j/2][i] = imageTwoD[j][i];
            }
        }
        return newImage;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];
        for(int i = 0; i < imageTwoD.length; i++) {
            for(int j = 0; j < imageTwoD[i].length; j++) {
                newImage[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
        return newImage;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] newImage = new int[imageTwoD.length][imageTwoD[0].length];
            for(int i = 0; i < imageTwoD.length; i++) {
                for(int j = 0; j < imageTwoD[i].length; j++) {
                    int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                    int r = rgba[0] + redChangeValue;
                    int g = rgba[1] + greenChangeValue;
                    int b = rgba[2] + blueChangeValue;
                    if(r > 255) {
                        r = 255;
                    } else if(r < 0) {
                        r = 0;
                    }
                    if(g > 255) {
                        g = 255;
                    } else if(g < 0) {
                        g = 0;
                    }
                    if(b > 255) {
                        b = 255;
                    } else if(b < 0) {
                        b = 0;
                    }

                    rgba[0] = r;
                    rgba[1] = g;
                    rgba[2] = b;

                    newImage[i][j] = getColorIntValFromRGBA(rgba);
                }
            }
            return newImage;
    }

    // Painting Methods
    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        int[][] newImage = new int[canvas.length][canvas[0].length];
        for(int i = 0; i < canvas.length; i++) {
            for(int j = 0; j < canvas[i].length; j++) {
                int[] rgba = {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255};
                newImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return newImage;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
        for(int i = 0; i < canvas.length; i++) {
            for(int j = 0; j < canvas[i].length; j++) {
                if(i >= rowPosition && i <= rowPosition + width) {
                    if(j >= colPosition && j <= colPosition + height) {
                        canvas[i][j] = color;
                    }
                }
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for(int i = 0; i<numRectangles; i++) {
            int randomWidth = rand.nextInt(canvas[0].length);
            int randomHeight = rand.nextInt(canvas.length);
            int randomRowPos = rand.nextInt(canvas.length-randomHeight);
            int randomColPos = rand.nextInt(canvas[0].length-randomWidth);
            int[] rgba = {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255};
            int randomColor = getColorIntValFromRGBA(rgba);

            canvas = paintRectangle(canvas, randomWidth, randomHeight, randomRowPos, randomColPos, randomColor);
        }
        return canvas;
    }

    // Utility Methods
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }

    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");
            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
