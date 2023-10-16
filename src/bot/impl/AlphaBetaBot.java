package bot.impl;

import bot.Bot;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class AlphaBetaBot implements Bot {
    public int[] move() {

        return new int[0];
    }

    public Integer[] minmax() {
        return new Integer[0];
    }

    public List<Integer[][]> getNeighbors() {
        List<Integer[][]> neighbors = new ArrayList<>();
        List<Integer[]> moves = this.getAvailableMoves();

        for(Integer[] move : moves) {
            neighbors.add(this.getAfterState(move));
        }

        return neighbors;
    }

    public List<Integer[]> getAvailableMoves() {
        List<Integer[]> moves = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(this.boardState[i][j] == 0){
                    moves.add(new Integer[]{i, j});
                }
            }
        }

        return moves;
    }

    public Integer[][] getAfterState(Integer[] move){
        Integer[][] afterState = this.boardState.clone();
        if(move[0] < 8 && move[1] < 8 && move[0] >= 0 && move[1] >= 0) {
            afterState[move[0]][move[1]] = 1;
            if(move[1] - 1 >= 0) {
                afterState[move[0]][move[1] - 1] = afterState[move[0]][move[1] - 1] == 1 ? 1 : 0;
            }
            if(move[1] + 1 < 8) {
                afterState[move[0]][move[1] + 1] = afterState[move[0]][move[1] + 1] == 1 ? 1 : 0;
            }
            if(move[0] - 1 >= 0) {
                afterState[move[0] - 1][move[1]] = afterState[move[0] - 1][move[1]] == 1 ? 1 : 0;
            }
            if(move[0] + 1 < 8) {
                afterState[move[0] + 1][move[1]] = afterState[move[0] + 1][move[1]] == 1 ? 1 : 0;
            }
        }

        return afterState;
    }

    @Override
    public void setBoardState(Button[][] buttons) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (buttons[i][j].getText().equals("")) {
                    boardState[i][j] = 0;
                } else if (buttons[i][j].getText().equals("X")) {
                    boardState[i][j] = 1;
                }
                else {
                    boardState[i][j] = -1;
                }
            }
        }
    }
}
