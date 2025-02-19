package com.example.Fast_Fourier_Transfer;

import com.example.DisplayRead.DisplayDensity;
import com.example.DisplayRead.ReadPGM;
import java.util.concurrent.CyclicBarrier;

public class Parallel_FFT extends Thread {

    public static int N = 256; // Image dimensions
    public static int P = 2;   // Number of threads
    final static int B = N / P; // Block size per thread

    static double[][] reconRe = new double[N][N]; // Reconstructed real part
    static double[][] reconIm = new double[N][N]; // Imaginary part
    static double[][] X = new double[N][N];       // Original image pixels

    static CyclicBarrier barrier = new CyclicBarrier(P); // Barrier for thread synchronization

    public static void parallelFFT(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        // Read image
        ReadPGM.read(X, "Fast_Fourier_Transfer/demo/src/main/java/com/example/wolf.pgm", N);
        DisplayDensity display = new DisplayDensity(X, N, "Original Image");

        // Create and start threads
        Parallel_FFT[] threads = new Parallel_FFT[P];
        for (int me = 0; me < P; me++) {
            threads[me] = new Parallel_FFT(me);
            threads[me].start();
        }

        // Wait for all threads to complete
        for (int me = 0; me < P; me++) {
            threads[me].join();
        }

       
        DisplayDensity display3 = new DisplayDensity(reconRe, N, "Reconstructed Image");

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time (ms): " + (endTime - startTime));
    }

    int me;

    Parallel_FFT(int me) {
        this.me = me;
    }

    public void run() {
        try {
            // Compute block range
            int begin = me * B;
            int end = begin + B;

            // Local processing arrays
            double[][] CRe = new double[B][N], CIm = new double[B][N];

            // Copy block from original image
            for (int k = 0; k < B; k++) {
                System.arraycopy(X[begin + k], 0, CRe[k], 0, N);
            }

            // Perform Forward FFT
            paraFft2d(CRe, CIm, 1);

            // Synchronize before copying to global arrays
            barrier.await();

            // Copy FFT result to global arrays
            for (int k = 0; k < B; k++) {
                System.arraycopy(CRe[k], 0, reconRe[begin + k], 0, N);
                System.arraycopy(CIm[k], 0, reconIm[begin + k], 0, N);
            }

            // Synchronize before performing Inverse FFT
            barrier.await();

            // Perform Inverse FFT
            paraFft2d(reconRe, reconIm, -1);

            // Normalize after inverse FFT
            for (int k = 0; k < B; k++) {
                for (int l = 0; l < N; l++) {
                    reconRe[begin + k][l] /= (N * N);
                }
            }

            // Final barrier to ensure all threads finish before display
            barrier.await();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Transpose matrix (needed for row-column FFT)
    static void paraTranspose(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < i; j++) {
                double temp = a[i][j];
                a[i][j] = a[j][i];
                a[j][i] = temp;
            }
        }
    }

    // 2D FFT processing
    static void paraFft2d(double[][] re, double[][] im, int isgn) {
        // Perform 1D FFT on all rows
        for (int r = 0; r < re.length; r++) {
            OneDimFFT.fft1d(re[r], im[r], isgn);
        }

        // Transpose before column-wise FFT
        paraTranspose(re);
        paraTranspose(im);

        // Perform 1D FFT on all columns
        for (int c = 0; c < re.length; c++) {
            OneDimFFT.fft1d(re[c], im[c], isgn);
        }

        // Transpose back
        paraTranspose(re);
        paraTranspose(im);
    }

    // Normalize image values to range [0, 255]
    static void normalize(double[][] img) {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

        // Find min and max values
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (img[i][j] < min) min = img[i][j];
                if (img[i][j] > max) max = img[i][j];
            }
        }

        double range = max - min;
        if (range == 0) return;

        // Scale values to fit [0, 255]
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                img[i][j] = 255 * (img[i][j] - min) / range;
            }
        }
    }
}
