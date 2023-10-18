package bot.impl;

import bot.Bot;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.*;

public class AlphaBetaBot implements Bot {
    public int[] move() {
        long startTime = System.nanoTime();
        Integer[] nextMove = minmax(true, 3, new Pair<Integer[], Integer[][]>(new Integer[]{0,0}, boardState), -10000, 10000).getKey();
        System.out.println("Next move is:" + nextMove[0] + nextMove[1]);
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Execution time: " + elapsedTime + " ms");
        return new int[]{nextMove[0], nextMove[1]};
    }

    public Pair<Integer[], Integer[][]> minmax(boolean isMax, Integer depth, Pair<Integer[], Integer[][]> state, Integer a, Integer b) {
        List<Pair<Integer[], Integer[][]>> neighbors = createSuccessor(isMax, getEmptyBlock(state.getValue()), state.getValue());
//        Comparator<Pair<Integer[], Integer[][]>> pairComparator = new Comparator<Pair<Integer[], Integer[][]>>() {
//            @Override
//            public int compare(Pair<Integer[], Integer[][]> pair1, Pair<Integer[], Integer[][]> pair2) {
//                if(isMax){
//                    return Integer.compare(getBoardScore(pair2.getValue()), getBoardScore(pair1.getValue()));
//                } else {
//                    return Integer.compare(getBoardScore(pair1.getValue()), getBoardScore(pair2.getValue()));
//                }
//            }
//        };
//        Collections.sort(neighbors, pairComparator);
        if(depth == 0 || neighbors.size() == 0){ return state; }
        Pair<Integer[], Integer[][]> chosenState = neighbors.get(0);
        if (Thread.currentThread().isInterrupted()) {
            return null;

        }
        if(isMax){
            for (Pair<Integer[], Integer[][]> neighbor : neighbors) {
                Integer neighborValue = getBoardScore(minmax(false, depth - 1, neighbor, a, b).getValue());
                if(a < neighborValue) { a = neighborValue; chosenState = neighbor;}
                System.out.println(depth);
                if(a >= b) {System.out.println("prune alpha : "+ a + " beta : " + b + " depth :" + depth); return chosenState; }
            }

            return chosenState;
        } else {
            for (Pair<Integer[], Integer[][]> neighbor : neighbors) {
                Integer neighborValue = getBoardScore(minmax(true, depth - 1, neighbor, a, b).getValue());
                if(b > neighborValue) { b = neighborValue; chosenState = neighbor;}
                System.out.println(depth);
                if(a >= b) {System.out.println("prune alpha : "+ a + " beta : " + b + " depth :" + depth); return chosenState; }
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
