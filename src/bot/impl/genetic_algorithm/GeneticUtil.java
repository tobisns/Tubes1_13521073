package bot.impl.genetic_algorithm;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneticUtil {

    public static int countEmptyBlock(Integer[][] state) {
        int res = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (state[i][j] == 0) {
                    res += 1;
                }
            }
        }

        return res;
    }

    public static int getPointAddition(Integer[][] board, int i, int j) {
        int result = 0;
        if(j - 1 >= 0) {
            if (board[i][j - 1] == 1) {
                result += 1;
            }
        }
        if(j + 1 < 8) {
            if (board[i][j + 1] == 1) {
                result += 1;
            }
        }
        if(i - 1 >= 0) {
            if (board[i - 1][j] == 1) {
                result += 1;
            }
        }
        if(i + 1 < 8) {
            if (board[i + 1][j] == 1) {
                result += 1;
            }
        }
        return result;
    }

    // return max 8 next states with highest state value
    public static List<Pair<Integer, Integer[]>> getTopStates(Integer[][] boardState) {
        List<Pair<Integer, Integer[]>> res = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] != 0) {
                    continue;
                }
                res.add(new Pair<>(getPointAddition(boardState, i, j), new Integer[]{i, j}));
            }
        }

        return res.stream()
                .sorted((el1, el2) -> Integer.compare(el2.getKey(), el1.getKey()))
                .limit(8)
                .toList();
    }
}
