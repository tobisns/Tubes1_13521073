package bot.impl;

import bot.Bot;

public class DefaultBot implements Bot {

    public int[] move() {
        // create random move
        return new int[]{(int) (Math.random()*8), (int) (Math.random()*8)};
    }

}
