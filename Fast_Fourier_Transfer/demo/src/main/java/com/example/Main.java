package com.example;

import com.example.Fast_Fourier_Transfer.FFTImageFiltering;
import com.example.Fast_Fourier_Transfer.Parallel_FFT;
import com.example.Fourier_Transforms_for_Image_Filtering.SimpleFT;
import com.example.Inverting_the_Radon_Transform.Sinogram;

public class Main{
    public static void main(String [] args) throws Exception {
        //SimpleFT.FT(args);

        //FFTImageFiltering.FFT(args);

        //Parallel_FFT.parallelFFT(args);
        Sinogram.sinogram(args);
}}