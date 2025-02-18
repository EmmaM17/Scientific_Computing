import com.example.DisplayRead.ReadPGM;

public class Parallel_FFT{

    public static int N = 256; // image dimensions
    public static int P = 2; // number of threads
    final static int B = N / P; // block size

    static double[][] reconRe = new double[N][N]; // Stores reconstructed real part
    static double[][] reconIm = new double[N][N]; //imaginary

    public static void parallelFFT(String [] args) throws Exception {

        double [] [] X = new double [N] [N] ; //original image pixels
            ReadPGM.read(X, "Fast_Fourier_Transfer/demo/src/main/java/com/example/wolf.pgm", N) ;


        
    }


}