package character;

import javax.swing.*;

/**
 * 爆破效果
 */
public class Blast extends Weapon implements Runnable {

    public Blast(int mapX, int mapY, JPanel jp) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.jp = jp;
        new Thread(this).start();
    }

    /**
     * 刷新爆炸图片
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                imageX++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
