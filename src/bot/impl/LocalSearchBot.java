package bot.impl;

import bot.Bot;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.*;

public class LocalSearchBot implements Bot {
    @Override
    public int[] move() {
        Integer[] nextMove = hillclimbing();
        System.out.println("Next move is:" + nextMove[0] + nextMove[1]);
        return new int[]{nextMove[0], nextMove[1]};
    }

    @Override
    public void setBoardState(Button[][] buttons) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (buttons[i][j].getText().equals("")) {
                    this.boardState[i][j] = 0;
                } else if (buttons[i][j].getText().equals("X")) {
                    this.boardState[i][j] = 1;
                }
                else {
                    this.boardState[i][j] = -1;
                }
            }
        }
    }

    public List<Integer[]> getEmptyBlock() {
        List<Integer[]> emptyBlock = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] == 0) {
                    emptyBlock.add(new Integer[]{i, j});
                }
            }
        }

        return emptyBlock;
    }

    public Integer[] hillclimbing() {
        List<Integer[]> emptyBlock = new ArrayList<>(getEmptyBlock());
        List<Pair<Integer[], Integer[][]>> successorList = new ArrayList<>(createSuccessor(emptyBlock));
        Integer[][] nextBoard = boardState;
        Integer[] nextMove = new Integer[]{(int) (Math.random()*8), (int) (Math.random()*8)};
        int limit = 10;

        while(true) {
            Pair<Integer[], Integer[][]> neighbor = getNeighbor(successorList);
            if (getBoardScore(neighbor.getValue()) < getBoardScore(nextBoard)) {
                return nextMove;
            }
            else if (getBoardScore(neighbor.getValue()) == getBoardScore(nextBoard)) {
                limit -= 1;
            }
            else {
                limit = 10;
                nextBoard = neighbor.getValue();
                nextMove = neighbor.getKey();
            }

            if (limit == 0) {
                return nextMove;
            }
        }
    }

    public List<Pair<Integer[], Integer[][]>> createSuccessor(List<Integer[]> emptyBlock) {
        List<Pair<Integer[], Integer[][]>> successor = new ArrayList<>();

        for (Integer[] move: emptyBlock) {
            Integer[][] nextBoardState = Arrays.stream(boardState).map(Integer[]::clone).toArray(Integer[][]::new);

            nextBoardState[move[0]][move[1]] = -1;
            if(move[1] - 1 >= 0) {
                if (nextBoardState[move[0]][move[1] - 1] == 1) {
                    nextBoardState[move[0]][move[1] - 1] = -1;
                }
            }
            if(move[1] + 1 < 8) {
                if (nextBoardState[move[0]][move[1] + 1] == 1) {
                    nextBoardState[move[0]][move[1] + 1] = -1;
                }
            }
            if(move[0] - 1 >= 0) {
                if (nextBoardState[move[0] - 1][move[1]] == 1) {
                    nextBoardState[move[0] - 1][move[1]] = -1;
                }
            }
            if(move[0] + 1 < 8) {
                if (nextBoardState[move[0] + 1][move[1]] == 1) {
                    nextBoardState[move[0] + 1][move[1]] = -1;
                }
            }

            successor.add(new Pair<>(move, nextBoardState));
        }

        return successor;
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

    public Pair<Integer[], Integer[][]> getNeighbor(List<Pair<Integer[], Integer[][]>> successorList) {
        Collections.shuffle(successorList);

        Pair<Integer[], Integer[][]> neighbor = successorList.get(0);
        int currentScore = getBoardScore(neighbor.getValue());
        int successorScore;

        for (Pair<Integer[], Integer[][]> successor: successorList) {
            successorScore = getBoardScore(successor.getValue());
            if (currentScore < successorScore) {
                neighbor = successor;
                currentScore = successorScore;
            }
        }

        return neighbor;
    }
}
