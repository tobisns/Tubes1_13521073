package bot.impl;

import bot.Bot;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaBot implements Bot {
    public int[] move() {
        Integer[] nextMove = minmax(true, 3, new Pair<Integer[], Integer[][]>(new Integer[]{0,0}, boardState), -10000, 10000).getKey();
        System.out.println("Next move is:" + nextMove[0] + nextMove[1]);
        return new int[]{nextMove[0], nextMove[1]};
    }

    public Pair<Integer[], Integer[][]> minmax(boolean isMax, Integer depth, Pair<Integer[], Integer[][]> state, Integer a, Integer b) {
        List<Pair<Integer[], Integer[][]>> neighbors = createSuccessor(isMax, getEmptyBlock(state.getValue()), state.getValue());
        Pair<Integer[], Integer[][]> chosenState = neighbors.get(0);
        if(depth == 0){ return state; }
        if(isMax){
            for (Pair<Integer[], Integer[][]> neighbor : neighbors) {
                Integer neighborValue = getBoardScore(minmax(false, depth - 1, neighbor, a, b).getValue());
                if(a < neighborValue) { a = neighborValue; chosenState = neighbor;}
                if(a > b) {System.out.println("prune alpha : "+ a + " beta : " + b); return chosenState; }
            }

            return chosenState;
        } else {
            for (Pair<Integer[], Integer[][]> neighbor : neighbors) {
                Integer neighborValue = getBoardScore(minmax(true, depth - 1, neighbor, a, b).getValue());
                if(b > neighborValue) { b = neighborValue; chosenState = neighbor;}
                if(a > b) {System.out.println("prune alpha : "+ a + " beta : " + b); return chosenState; }
            }

            return chosenState;
        }
    }

    public List<Pair<Integer[], Integer[][]>> createSuccessor(boolean isBot, List<Integer[]> emptyBlock, Integer[][] state) {
        List<Pair<Integer[], Integer[][]>> successor = new ArrayList<>();
        int value = -1;
        if(!isBot) { value = -value; }
        for (Integer[] move: emptyBlock) {
            Integer[][] nextBoardState = Arrays.stream(state).map(Integer[]::clone).toArray(Integer[][]::new);

            nextBoardState[move[0]][move[1]] = -1;
            if(move[1] - 1 >= 0) {
                if (nextBoardState[move[0]][move[1] - 1] == -value) {
                    nextBoardState[move[0]][move[1] - 1] = value;
                }
            }
            if(move[1] + 1 < 8) {
                if (nextBoardState[move[0]][move[1] + 1] == -value) {
                    nextBoardState[move[0]][move[1] + 1] = value;
                }
            }
            if(move[0] - 1 >= 0) {
                if (nextBoardState[move[0] - 1][move[1]] == -value) {
                    nextBoardState[move[0] - 1][move[1]] = value;
                }
            }
            if(move[0] + 1 < 8) {
                if (nextBoardState[move[0] + 1][move[1]] == -value) {
                    nextBoardState[move[0] + 1][move[1]] = value;
                }
            }

            successor.add(new Pair<>(move, nextBoardState));
        }

        return successor;
    }

    public List<Integer[]> getEmptyBlock(Integer[][] state) {
        List<Integer[]> emptyBlock = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (state[i][j] == 0) {
                    emptyBlock.add(new Integer[]{i, j});
                }
            }
        }

        return emptyBlock;
    }

    public int getBoardScore(Integer[][] board) {
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == -1) {
                    score += 1;
                }
                if (board[i][j] == 1) {
                    score -= 1;
                }
            }
        }

        return score;
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
