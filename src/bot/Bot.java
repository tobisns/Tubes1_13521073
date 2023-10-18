package bot;

import javafx.scene.control.Button;

import java.util.List;

public interface Bot {
    public Integer[][] boardState = new Integer[8][8];

    public void setBoardState(Button[][] buttons);

    public int[] move();

    public List<Integer[]> getEmptyBlock(Integer[][] state);

}
