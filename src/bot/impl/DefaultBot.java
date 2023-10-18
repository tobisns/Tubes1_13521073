package bot.impl;

import bot.Bot;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class DefaultBot implements Bot {

    public int[] move() {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
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
}
