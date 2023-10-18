package bot.impl.genetic_algorithm;

import bot.Bot;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneticBot implements Bot {

    private static final GeneticOperator go = new GeneticOperator();

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

    @Override
    public int[] move() {
        int turnCount = Math.min(8, GeneticUtil.countEmptyBlock(boardState)); // should be compared (min function) with emptyBlocks
        List<String> population = new ArrayList<>();

        for (int i = 0; i < turnCount; ++i) {
            population.add(go.generateRandomChromosome(turnCount));
        }

        Node root = null;
        int generation = 2;
        for (int i = 0; i < generation; ++i) {
            root = go.formTree(population.get(0));
            for (int j = 1; j < population.size(); ++j) {
                go.mergeTree(population.get(j), root);
            }
            dfs(root, boardState);
            max(root);
            initFitness(root, turnCount);
            population = reproduction(root.getLeafNodes(), population.size());
        }
        List<Node> leafs = root.getLeafNodes();
        List<Node> sortedLeaf = leafs.stream()
                .sorted((n1, n2) -> Integer.compare(n2.getFitness(), n1.getFitness()))
                .limit(1)
                .toList();
        List<Pair<Integer, Integer[]>> topStates = GeneticUtil.getTopStates(boardState);
        Integer[] res = topStates.get(sortedLeaf.get(0).getChromosome().charAt(0)-48).getValue();

        return new int[]{res[0], res[1]};
    }

    // do this after fitness
    public static List<String> reproduction(List<Node> leafs, int populationNumber) {
        int chosen = (int) Math.ceil((double) leafs.size() /4);
        List<Node> sortedLeaf = leafs.stream()
                .sorted((n1, n2) -> Integer.compare(n2.getFitness(), n1.getFitness()))
                .limit(chosen)
                .toList();

        List<String> population = new ArrayList<>();
        while (population.size() < populationNumber) {
            Node leaf1 = sortedLeaf.get(go.getRandomPoint(chosen)),
                    leaf2 = sortedLeaf.get(go.getRandomPoint(chosen));
            String[] children = go.crossover(leaf1.getChromosome(), leaf2.getChromosome(),
                    leaf2.getChromosome().length()/2);
            for (String child: children) {
                if (child != null) {
                    population.add(child);
                }
            }
        }

        return population;
    }

    // do this after min max
    public static void initFitness(Node root, int height) {
        for (Node leaf: root.getLeafNodes()) {
            leaf.setFitness(height - go.getElevationCount(leaf) + 1);
        }
    }

    // initialize all node values
    public static int max(Node root) {
        if (root.getChildren().isEmpty()) {
            return root.getValue();
        }
        int max = -99999999;
        for (int index: root.getChildren().keySet()) {
            max = Math.max(max, min(root.getChild(index)));
        }
        root.setValue(max);
        return max;
    }

    // initialize all node values
    public static int min(Node root){
        if (root.getChildren().isEmpty()) {
            return root.getValue();
        }
        int min = 99999999;
        for (int index: root.getChildren().keySet()) {
            min = Math.min(min, max(root.getChild(index)));
        }
        root.setValue(min);
        return min;
    }

    // initialize all values in leaf
    public static void dfs(Node root, Integer[][] board) {
        if (root.getChildren().isEmpty()) {
            root.setValue(getBoardScore(board));
            return;
        }
        for (Integer i: root.getChildren().keySet()) {
            Integer[][] board_Copy = copyArr(board);
            List<Pair<Integer, Integer[]>> topStates = GeneticUtil.getTopStates(board_Copy);
            Integer[] top = topStates.get(0).getValue();
            System.out.println("top pair: " + top[0] + " " + top[1]);
            fill(board_Copy, top);
            dfs(root.getChild(i), board_Copy);
        }
    }

    public static int getBoardScore(Integer[][] board) {
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

    public static void fill(Integer[][] board, Integer[] move) {
        board[move[0]][move[1]] = -1;
        if(move[1] - 1 >= 0) {
            if (board[move[0]][move[1] - 1] == 1) {
                board[move[0]][move[1] - 1] = -1;
            }
        }
        if(move[1] + 1 < 8) {
            if (board[move[0]][move[1] + 1] == 1) {
                board[move[0]][move[1] + 1] = -1;
            }
        }
        if(move[0] - 1 >= 0) {
            if (board[move[0] - 1][move[1]] == 1) {
                board[move[0] - 1][move[1]] = -1;
            }
        }
        if(move[0] + 1 < 8) {
            if (board[move[0] + 1][move[1]] == 1) {
                board[move[0] + 1][move[1]] = -1;
            }
        }
    }

    public static Integer[][] copyArr(Integer[][] arr) {
        int numRows = arr.length;
        int numCols = arr[0].length;

        // Create a new 2D integer array of the same dimensions.
        Integer[][] copy = new Integer[numRows][numCols];

        // Copy elements from the original array to the new array.
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                copy[i][j] = arr[i][j];
            }
        }

        return copy;
    }

}
