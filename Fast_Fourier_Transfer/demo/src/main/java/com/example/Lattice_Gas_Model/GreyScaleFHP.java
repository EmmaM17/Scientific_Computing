package com.example.Lattice_Gas_Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GreyScaleFHP {

    final static int NX = 80, NY = 60 ;  // Lattice dimensions
    final static int q = 6 ;  // population
     
    final static int NITER = 1000 ;
    final static int DELAY = 500 ;

    final static double DENSITY = 1.0 ;  // initial state, between 0 and 1.0.

    static Display display = new Display() ;

    static boolean [] [] [] fin = new boolean [NX] [NY] [q] ;
    static boolean [] [] [] fout = new boolean [NX] [NY] [q] ;

    public static void greyFhp(String args []) throws Exception {

        // initialize - populate a subblock of grid
         for(int i = 0; i < NX ; i++) { 
            for(int j = 0; j < NY ; j++) { 
                boolean [] fin_ij = fin [i] [j] ;
                for(int d = 0 ; d < q ; d++) {
                    if(Math.random() < DENSITY /100) {
                        fin_ij [d] = true ;
                    }
                }
            }
        } 

       /*  fin [NX/2 - 1] [NY/2] [1] = true;
        fin [NX/2] [NY/2 + 1] [2] = true;
        fin [NX/2 + 1] [NY/2 - 1] [5] = true;
 */

        display.repaint() ;
        Thread.sleep(DELAY) ;

        for(int iter = 0 ; iter < NITER ; iter++) {

            // Collision

            for(int i = 0; i < NX ; i++) { 
                for(int j = 0; j < NY ; j++) { 
                    boolean [] fin_ij = fin [i] [j] ;
                    boolean [] fout_ij = fout [i] [j] ;
                    int pop = 0 ;
                    for(int d = 0 ; d < q ; d++) {
                        if(fin_ij [d]) pop++ ;
                        fout_ij [d] = fin_ij [d] ;
                    }
                    if(pop == 2) {
                        // head on collisions
                        if(fin_ij [0] && fin_ij [1]) {
                            fout_ij [0] = false ;
                            fout_ij [1] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [2] = true ;
                                fout_ij [3] = true ;
                            }
                            else {
                                fout_ij [4] = true ;
                                fout_ij [5] = true ;
                            }
                        }    
                        if(fin_ij [2] && fin_ij [3]) {
                            fout_ij [2] = false ;
                            fout_ij [3] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [4] = true ;
                                fout_ij [5] = true ;
                            }
                            else {
                                fout_ij [0] = true ;
                                fout_ij [1] = true ;
                            }
                        }    
                        if(fin_ij [4] && fin_ij [5]) {
                            fout_ij [4] = false ;
                            fout_ij [5] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [0] = true ;
                                fout_ij [1] = true ;
                            }
                            else {
                                fout_ij [2] = true ;
                                fout_ij [3] = true ;
                            }
                        }    
                    }
                    else if(pop == 4) {
                        // double head on collisions
                        if(!fin_ij [0] && !fin_ij [1]) {
                            fout_ij [0] = true ;
                            fout_ij [1] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [2] = false ;
                                fout_ij [3] = false ;
                            }
                            else {
                                fout_ij [4] = false ;
                                fout_ij [5] = false ;
                            }
                        }    
                        if(!fin_ij [2] && !fin_ij [3]) {
                            fout_ij [2] = true ;
                            fout_ij [3] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [4] = false ;
                                fout_ij [5] = false ;
                            }
                            else {
                                fout_ij [0] = false ;
                                fout_ij [1] = false ;
                            }
                        }    
                        if(!fin_ij [4] && !fin_ij [5]) {
                            fout_ij [4] = true ;
                            fout_ij [5] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [0] = false ;
                                fout_ij [1] = false ;
                            }
                            else {
                                fout_ij [2] = false ;
                                fout_ij [3] = false ;
                            }
                        }    
                    }
                    else if(pop == 3) {
                        // three way collisions
                        if(fin_ij [0] && fin_ij [3] && fin_ij [4]) {
                            fout_ij [0] = false ;
                            fout_ij [1] = true ;
                            fout_ij [2] = true ;
                            fout_ij [3] = false ;
                            fout_ij [4] = false ;
                            fout_ij [5] = true ;
                        }
                        if(fin_ij [1] && fin_ij [2] && fin_ij [5]) {
                            fout_ij [0] = true ;
                            fout_ij [1] = false ;
                            fout_ij [2] = false ;
                            fout_ij [3] = true ;
                            fout_ij [4] = true ;
                            fout_ij [5] = false ;
                        }
                        // head on with spectator
                        if(fin_ij [0] && fin_ij [1]) {
                            fin_ij [0] = false ;
                            fin_ij [1] = false ;
                            if(fin_ij [2] || fin_ij [3]) {
                                fin_ij [4] = true ;
                                fin_ij [5] = true ;
                            }
                            else {
                                fin_ij [2] = true ;
                                fin_ij [3] = true ;
                            }
                        }
                        if(fin_ij [2] && fin_ij [3]) {
                            fin_ij [2] = false ;
                            fin_ij [3] = false ;
                            if(fin_ij [4] || fin_ij [5]) {
                                fin_ij [0] = true ;
                                fin_ij [1] = true ;
                            }
                            else {
                                fin_ij [4] = true ;
                                fin_ij [5] = true ;
                            }
                        }
                        if(fin_ij [4] && fin_ij [5]) {
                            fin_ij [4] = false ;
                            fin_ij [5] = false ;
                            if(fin_ij [0] || fin_ij [1]) {
                                fin_ij [2] = true ;
                                fin_ij [3] = true ;
                            }
                            else {
                                fin_ij [0] = true ;
                                fin_ij [1] = true ;
                            }
                        }
                    }
                }
            }

            // Streaming

            for(int i = 0; i < NX ; i++) { 
                int iP1 = (i + 1) % NX ;
                int iM1 = (i - 1 + NX) % NX ;
                for(int j = 0; j < NY ; j++) { 
                    int jP1 = (j + 1) % NY ;
                    int jM1 = (j - 1 + NY) % NY ;

                    fin [iM1] [j] [0] = fout [i] [j] [0] ;
                    fin [iP1] [j] [1] = fout [i] [j] [1] ;
                    fin [i] [jM1] [2] = fout [i] [j] [2] ;
                    fin [i] [jP1] [3] = fout [i] [j] [3] ;
                    fin [iP1] [jM1] [4] = fout [i] [j] [4] ;
                    fin [iM1] [jP1] [5] = fout [i] [j] [5] ;
                }
            }

            System.out.println("iter = " + iter) ;

            display.repaint() ;
            Thread.sleep(DELAY) ;
        }
    }

    
    static class Display extends JPanel {

        static final double ROW_HEIGHT = Math.sqrt(3) / 2 ;

        int displaySizeX = CELL_SIZE * NX ;
        int displaySizeY = (int) (ROW_HEIGHT * CELL_SIZE * NY + 0.5) ;

        final static int CELL_SIZE = 14 ; 

        

        Display() {

            setPreferredSize(new Dimension(displaySizeX, displaySizeY)) ;

            JFrame frame = new JFrame("FHP Greyscaled");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        public void paintComponent(Graphics g) {

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, CELL_SIZE * NX, CELL_SIZE * NY); 
            
            for (int i = 0; i < NX; i++) {
                for (int j = 0; j < NY; j++) {
                    boolean[] f_ij = fin[i][j];
                    int density = 0;

                    for (int k = 0; k < q; k++) {
                        if (f_ij[k]) {
                            density = 1;
                        }
                    }

                    if (density == 1) {
                        // Main square
                        g.setColor(Color.BLACK);
                    } else {
                        // Compute density of neighbors
                        int neighborDensity = 0;
                        for (int di = -1; di <= 1; di++) {
                            for (int dj = -1; dj <= 1; dj++) {
                                int ni = i + di;
                                int nj = j + dj;
                                if (ni >= 0 && ni < NX && nj >= 0 && nj < NY) {
                                    for (int k = 0; k < q; k++) {
                                        if (fin[ni][nj][k]) {
                                            neighborDensity++;
                                        }
                                    }
                                }
                            }
                        }

                        // Map neighbor density to grayscale
                        int grayscale = 255 - (neighborDensity * 50);
                        grayscale = Math.max(0, Math.min(255, grayscale));
                        g.setColor(new Color(grayscale, grayscale, grayscale));
                    }

                    // Draw square
                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        
            
        }            


    }
}