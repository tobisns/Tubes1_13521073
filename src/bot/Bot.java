package bot;

import javafx.scene.control.Button;

public interface Bot {
    public Integer[][] boardState = new Integer[8][8];

    public void setBoardState(Button[][] buttons);

    public int[] move();

}
