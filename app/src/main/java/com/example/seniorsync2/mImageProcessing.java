package com.example.seniorsync2;
// Abstract class declaration for image processing functions
abstract class mImageProcessing {
    // Static method to decode YUV420SP data and calculate color statistics
    public static double[] decode(byte[] yuv420sp, int width, int height) {
        // Return default values if input data is null
        if (yuv420sp == null) return new double[]{0, 0, 0, 0, 0, 0};
        // Calculate the total number of pixels in the frame
        final int frameSize = width * height;
        // Initialize sums for RGB components and their squared values
        int sum = 0;
        int sumr = 0;
        int sumg = 0;
        int sumb = 0;

        int sum2r = 0;
        int sum2g = 0;
        int sum2b = 0;
        // Iterate over each pixel to compute sums and squared sums
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                // Extract the Y component and adjust its value
                int y = (0xff & yuv420sp[yp]) - 16;
                // Extract U and V components every second pixel
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                // Calculate RGB values using the YUV to RGB conversion formula
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;
                // Calculate final pixel value and extract RGB components
                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                // Accumulate the sum and squared sum for each color component
                sumr += red;
                sumg += green;
                sumb += blue;
            }
        }
        // Compute average RGB values
        double avgr = (double) sumr/frameSize;
        double avgg = (double) sumg/frameSize;
        double avgb = (double) sumb/frameSize;
        // Compute variance for each color component
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                // Repeat extraction and conversion as done above
                int y = (0xff & yuv420sp[yp]) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                // Accumulate squared deviations from the mean for variance calculation
                sum2r += Math.pow(red - avgr, 2);
                sum2g += Math.pow(green - avgg, 2);
                sum2b += Math.pow(blue - avgb, 2);
            }
        }
        // Calculate variance and standard deviation for each color component
        double varr = (double) sum2r/(frameSize - 1);
        double varg = (double) sum2g/(frameSize - 1);
        double varb = (double) sum2b/(frameSize - 1);
        // Return averages and standard deviations as an array
        return new double[]{avgr, Math.sqrt(varr), avgg, Math.sqrt(varg), avgb , Math.sqrt(varb)};
    }
    // Method to calculate the average red value from YUV420SP data
    public static double redAverage(byte[] yuv420sp, int width, int height) {
        // Return 0 if input data is null
        if (yuv420sp == null) return 0;
        // Calculate frame size
        final int frameSize = width * height;
        // Initialize sum for red component
        int sum = 0;
        // Loop through each pixel to calculate the red component sum
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & yuv420sp[yp]) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;

                int pixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                sum += red;
            }
        }
        // Calculate and return the average red value
        return (double) sum/frameSize;
    }
}
