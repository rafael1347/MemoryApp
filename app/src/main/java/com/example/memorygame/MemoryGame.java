package com.example.memorygame;

import java.util.Random;

public class MemoryGame {
    public static final int GRID_SIZE = 4;

    // Tiles that make up the grid
    private final boolean[][] mTileGrid;

    public MemoryGame() {
        mTileGrid = new boolean[GRID_SIZE][GRID_SIZE];
    }

    public int[] newRound(int round) {
        int[] tileOrder = new int[round];
        Random randNum = new Random();

        for(int i = 0; i < round; i++) {
            int tile = randNum.nextInt(GRID_SIZE*GRID_SIZE);
            tileOrder[i] = tile;
        }

        return tileOrder;
    }
}
