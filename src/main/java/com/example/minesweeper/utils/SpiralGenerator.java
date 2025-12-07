package com.example.minesweeper.utils;

public class SpiralGenerator {

    private int x = 0;
    private int z = 0;
    private int step = 1;

    private int dirIndex = 0;
    private int stepsDone = 0;
    private int changes = 0;

    private final int[][] dirs = {
            {1, 0},   // Est
            {0, 1},   // Sud
            {-1, 0},  // Ouest
            {0, -1}   // Nord
    };

    public int[] next() {
        int dx = dirs[dirIndex][0];
        int dz = dirs[dirIndex][1];

        x += dx;
        z += dz;

        stepsDone++;

        if (stepsDone >= step) {
            stepsDone = 0;
            dirIndex = (dirIndex + 1) % 4;
            changes++;

            if (changes % 2 == 0) {
                step++;
            }
        }

        return new int[]{x, z};
    }
}
