package character;

import contants.MapConstants;
import contants.TankImageConstants;
import ui.MyGamePanel;

import javax.swing.*;
import java.awt.*;

public class Laser extends Weapon implements Runnable {
    int x;//激光起始坐标
    int y;

    public Laser(int imageX, int mapX, int mapY, JPanel jp) {
        this.imageX = imageX;
        this.mapX = mapX;
        this.mapY = mapY;
        this.jp = jp;
        new Thread(this).start();

        x = mapX;
        y = mapY;
    }

    public void drawLaser(Graphics g) { // 画笔
        if (imageX == 0) {
            for (int i = x; i <= mapX; i++) {
                g.drawImage(TankImageConstants.laser, (i) << 5, (mapY) << 5, (i + 1) << 5, (mapY + 1) << 5, (imageX << 5) + 2, 0, ((imageX + 1) << 5) - 2, 32, jp);
            }
        }
        if (imageX == 1) {
            for (int i = y; i <= mapY; i++) {
                g.drawImage(TankImageConstants.laser, (mapX) << 5, (i) << 5, (mapX + 1) << 5, (i + 1) << 5, imageX << 5, 0, (imageX + 1) << 5, 32, jp);
            }
        }
        if (imageX == 2) {
            for (int i = x; i >= mapX; i--)
                g.drawImage(TankImageConstants.laser, (i) << 5, (mapY) << 5, (i + 1) << 5, (mapY + 1) << 5, (imageX << 5) + 2, 0, ((imageX + 1) << 5) - 2, 32, jp);
        }
        if (imageX == 3) {
            for (int i = y; i >= mapY; i--) {
                g.drawImage(TankImageConstants.laser, (mapX) << 5, (i) << 5, (mapX + 1) << 5, (i + 1) << 5, imageX << 5, 0, (imageX + 1) << 5, 32, jp);
            }
        }

    }

    @Override
    public void run() {
        // 发射激光
        while (!isOver && !MyGamePanel.isGameOver) {
            try {
                Thread.sleep(15); // 速度
                synchronized (MyGamePanel.lasers) {
                    move();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void move() {
        int num = MapConstants.map[mapY][mapX]; // 地图素材
        if (num == 1 || num == 2 || num == 6) { // 遇到砖块,敌人等需要爆炸效果的地图元素
            if (num == 1) {
                MapConstants.map[mapY][mapX] = 0; // 移除砖块
            }
            characterOver(this);
        } else if (num == 7) { // 激光打到boss
            MapConstants.map[mapY][mapX] = 0;
            characterOver(this);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyGamePanel.isGameOver = true; // 游戏结束
            return;
        }
        keepGoing();
        fightEnemy();
    }

    /**
     * 攻击敌方坦克
     */
    private void fightEnemy() {
        for (int i = 0; i < MyGamePanel.ets.size(); i++) {
            Weapon et = MyGamePanel.ets.get(i); // 遍历敌方坦克
            if (et.toCharacter(this)) {
                characterOver(et); // 结束敌方坦克
                characterOver(this); // 结束此时的子弹
            }
        }
    }
}
