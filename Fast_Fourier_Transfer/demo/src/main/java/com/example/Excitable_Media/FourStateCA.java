package com.example.Excitable_Media;

import java.awt.*;
import javax.swing.*;

// STATES:
// 0 - Resting (white)
// 3 - Excited wave front (red)
// 2 - Excited plateau (black)
// 1 - Recovering (gray)

public class FourStateCA {

    final static int N = 50;
    final static int CELL_SIZE = 5;
    final static int DELAY = 100;

    static int[][] state = new int[N][N];
    static int[][] timeToStateChange = new int[N][N]; // Countdown timer for each state
    static boolean[][] excitedNeighbour = new boolean[N][N]; // Track excited (wavefront) neighbors

    static Display display = new Display();

    public static void simple4stateCA(String args[]) throws Exception {

        // ===== Initial State: bottom row excited plateau =====
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j == N - 1) {
                    state[i][j] = 3; // Start as wavefront to spread
                    timeToStateChange[i][j] = 2; // Wavefront lives for 2 steps
                } else {
                    state[i][j] = 0; // Resting
                    timeToStateChange[i][j] = 0;
                }
            }
        }

        display.repaint();
        pause();

        // ===== Main Simulation Loop =====
        int iter = 0;
        while (true) {

            System.out.println("iter = " + iter++);

            // Optional: Chop wave when halfway up
            if (iter == N / 2) {
                for (int i = 0; i < N / 2; i++) {
                    for (int j = 0; j < N; j++) {
                        state[i][j] = 0;
                        timeToStateChange[i][j] = 0;
                    }
                }
            }

            // ==== 1. Detect excited neighbors (wavefronts only) ====
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int ip = Math.min(i + 1, N - 1);
                    int im = Math.max(i - 1, 0);
                    int jp = Math.min(j + 1, N - 1);
                    int jm = Math.max(j - 1, 0);

                    excitedNeighbour[i][j] = (state[i][jp] == 3) || (state[i][jm] == 3)
                            || (state[ip][j] == 3) || (state[im][j] == 3);
                }
            }

            // ==== 2. State Transition Logic ====
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    switch (state[i][j]) {
                        case 0: // Resting
                            if (excitedNeighbour[i][j]) {
                                state[i][j] = 3; // Excited wavefront
                                timeToStateChange[i][j] = 2; // Stay 2 steps as wavefront
                            }
                            break;

                        case 3: // Wavefront
                            timeToStateChange[i][j]--;
                            if (timeToStateChange[i][j] <= 0) {
                                state[i][j] = 2; // Become plateau
                                timeToStateChange[i][j] = 3; // Stay 3 steps in plateau
                            }
                            break;

                        case 2: // Plateau
                            timeToStateChange[i][j]--;
                            if (timeToStateChange[i][j] <= 0) {
                                state[i][j] = 1; // Recovering
                                timeToStateChange[i][j] = 3; // Stay 3 steps recovering
                            }
                            break;

                        case 1: // Recovering
                            timeToStateChange[i][j]--;
                            if (timeToStateChange[i][j] <= 0) {
                                state[i][j] = 0; // Resting
                                timeToStateChange[i][j] = 0;
                            }
                            break;
                    }
                }
            }

            display.repaint();
            pause();
        }
    }

    // ========================== DISPLAY CLASS ==========================
    static class Display extends JPanel {

        final static int WINDOW_SIZE = N * CELL_SIZE;

        Display() {
            setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
            JFrame frame = new JFrame("Four-State Excitable Media Model");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (state[i][j] > 0) {
                        if (state[i][j] == 3) {
                            g.setColor(Color.BLACK);
                        } 
                        else if (state[i][j] == 2){
                            g.setColor(Color.GRAY);
                        }else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillRect(CELL_SIZE * i, CELL_SIZE * j,
                                CELL_SIZE, CELL_SIZE);
                    }
                
                }
            }
        }
    }

  
    static void pause() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
