package com.example.Fast_Fourier_Transfer;

import com.example.DisplayRead.Display2dFT;
import com.example.DisplayRead.DisplayDensity;
import com.example.DisplayRead.ReadPGM;

public class FFTImageFiltering {

    public static int N = 256 ;
   
    
        public static void FFT(String [] args) throws Exception {
            long startTime = System.currentTimeMillis();
            
    
            double [] [] X = new double [N] [N] ;
            ReadPGM.read(X, "Fast_Fourier_Transfer/demo/src/main/java/com/example/wolf.pgm", N) ;
    
    
            DisplayDensity display =
                    new DisplayDensity(X, N, "Original Image") ;
    
            // create array for in-place FFT, and copy original data to it
            double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;
            for(int k = 0 ; k < N ; k++) {
                for(int l = 0 ; l < N ; l++) {
                    CRe [k] [l] = X [k] [l] ;
                }
            }
    
            fft2d(CRe, CIm, 1) ;  // Fourier transform
    
            Display2dFT display2 =
                    new Display2dFT(CRe, CIm, N, "Discrete FT") ;
            

            //FILTER

            int cutoff = N/16 ;  // for example
            for(int k = 0 ; k < N ; k++) {
                int kSigned = k <= N/2 ? k : k - N ;
                for(int l = 0 ; l < N ; l++) {
                    int lSigned = l <= N/2 ? l : l - N ;
                    if(Math.abs(kSigned) <=  cutoff || Math.abs(lSigned) <= cutoff) {
                        CRe [k] [l] = 0 ;
                        CIm [k] [l] = 0 ;
                    }
                }
            }

          Display2dFT display2a =
                  new Display2dFT(CRe, CIm, N, "Truncated FT") ;
    




            // create array for in-place inverse FFT, and copy FT to it
            double [] [] reconRe = new double [N] [N],
                         reconIm = new double [N] [N] ;
            for(int k = 0 ; k < N ; k++) {
                for(int l = 0 ; l < N ; l++) {
                    reconRe [k] [l] = CRe [k] [l] ;
                    reconIm [k] [l] = CIm [k] [l] ;
                }
            }
    
            fft2d(reconRe, reconIm, -1) ;  // Inverse Fourier transform
    
            DisplayDensity display3 =
                    new DisplayDensity(reconRe, N, "Reconstructed Image") ;
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime; // duration in milliseconds
            System.out.println("Execution time: " + duration + " ms");
        }
    
        //... implementation of fft2d ...
    
        //Swap i and j
        static void transpose(double [] [] a) {
    
            for(int i = 0 ; i < N ; i++) {
                for(int j = 0 ; j < i ; j++) {
                    double temp = a[i][j];
                    a[i][j] = a[j][i];
                    a[j][i] = temp;
                }
            }
        }
    
        static void fft2d(double [] [] re, double [] [] im, int isgn) {
    
            
    
            //... fft1d on all rows of re, im ...
            for (int r=0; r<re.length;r++){
               OneDimFFT.fft1d(re[r], im[r], isgn); 
            }
            
    
            transpose(re) ;
            transpose(im) ;
    
            //... fft1d on all rows of re, im ...
            for (int c=0; c< re.length; c++){
                OneDimFFT.fft1d(re[c], im[c], isgn); 
             }
    
            transpose(re) ;
            transpose(im) ;
    
         
        }   
    }
       
