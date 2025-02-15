package com.example.Fourier_Transforms_for_Image_Filtering;
import com.example.DisplayRead.Display2dFT;
import com.example.DisplayRead.DisplayDensity;
import com.example.DisplayRead.ReadPGM;

public class SimpleFT {
  
    public static int N = 256 ;

    public static void FT(String [] args) throws Exception {
        long startTime = System.currentTimeMillis();

        double [] [] X = new double [N] [N] ;
        ReadPGM.read(X, "Fast_Fourier_Transfer/demo/src/main/java/com/example/wolf.pgm", N) ;

        DisplayDensity display =
                new DisplayDensity(X, N, "Original Image") ;

        double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;

        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                double sumRe = 0, sumIm = 0 ;
                // Nested for loops performing sum over X elements
                for(int m = 0; m < N-1; m++) {  //loop over rows
                    for(int n = 0; n < N-1 ; n++) { //loop over columns
                         double arg = -2 * Math.PI *((k*m)+(n*l)) /N; //exponential 
                         double cos = Math.cos(arg) ;  //real part
                         double sin = Math.sin(arg) ; // imaginary part
                         sumRe += cos * X [m] [n] ;  //sum real
                         sumIm += sin * X [m] [n] ; //sum imaginary
                    }
                }
                CRe [k] [l] = sumRe ;
                CIm [k] [l] = sumIm ;
            }
            System.out.println("Completed FT line " + (k+1) + " out of " + N) ;
        }

        Display2dFT display2 =
                new Display2dFT(CRe, CIm, N, "Discrete FT") ;
/* 
                // APPLY HIGH-PASS FILTER
                int cutoff = N / 4;  // For example, cutoff at N/8
                for (int k = 0; k < N; k++) {
                    int kSigned = k <= N / 2 ? k : k - N;
                    for (int l = 0; l < N; l++) {
                        int lSigned = l <= N / 2 ? l : l - N;
                        if (Math.abs(kSigned) <= cutoff && Math.abs(lSigned) <= cutoff) {
                            CRe[k][l] = 0;
                            CIm[k][l] = 0;
                        }
                    }
                } */


                Display2dFT display2a =
                        new Display2dFT(CRe, CIm, N, "Truncated FT") ;


                // INVERSE FOURIER TRANSFORMATION
                double [] [] reconstructed = new double [N] [N] ;

                for(int m = 0 ; m < N ; m++) {
                    for(int n = 0 ; n < N ; n++) {
                        double sum = 0;

                        for (int k = 0; k < N-1; k++){
                            for (int l = 0; l < N-1; l++){
                                double arg = 2 * Math.PI *((k*m)+(n*l)) /N; //exponential 

                                // Add the real and imaginary parts to the sums
                                sum += Math.cos(arg) * CRe[k][l] - Math.sin(arg) * CIm[k][l];
                              
                            }
                        }
                        
                        
                        reconstructed [m] [n] = sum / (N * N) ;
                    }
                    System.out.println("Completed inverse FT line " + (m+1) + " out of " + N) ;
                }

                DisplayDensity display3 =
                        new DisplayDensity(reconstructed, N, "Reconstructed Image") ;

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime; // duration in milliseconds
                System.out.println("Execution time: " + duration + " ms");
            }
}