package com.example;

import com.example.Fast_Fourier_Transfer.FFTImageFiltering;
import com.example.Fast_Fourier_Transfer.Parallel_FFT;
import com.example.Fourier_Transforms_for_Image_Filtering.SimpleFT;
import com.example.Inverting_the_Radon_Transform.Sinogram;
import com.example.Inverting_the_Radon_Transform.SinogramUnfiltered;
import com.example.Lattice_Gas_Model.FHP;
import com.example.Lattice_Gas_Model.GreyScaleFHP;
import com.example.Lattice_Gas_Model.HPP;
import com.example.Sky_Imaging.Imaging;

public class Main{
    public static void main(String [] args) throws Exception {
        //SimpleFT.FT(args);

        //FFTImageFiltering.FFT(args);

        //Parallel_FFT.parallelFFT(args);

        //Sinogram.fftSinogram(args);
        //SinogramUnfiltered.sinogramUnfiltered(args);

        //Imaging.imaging(args);

         //HPP.hpp(args);
         //FHP.fhp(args);
         GreyScaleFHP.greyFhp(args);
}}