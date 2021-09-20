package character;

import contants.MapConstants;
import sound.Music;
import ui.MyGamePanel;

import javax.swing.*;
import java.awt.*;

/**
 * 角色父类
 */
public class Weapon implements Runnable {

    public volatile int mapX;
    public volatile int mapY;
    public volatile boolean isOver = false; // 角色是否存在
    public JPanel jp;
    public int imageX; // 图片切换
    public int imageY;
    public Graphics g;

    public Weapon() {
    }

    /**
     * 绘制图像
     */
    public void drawAll(Graphics g, Image image, int size) {
        g.drawImage(image, (mapX << 5), (mapY << 5), ((mapX + 1) << 5), ((mapY + 1) << 5),
                imageX * size, 0, (imageX + 1) * size, size, jp);
    }

    @Override
    public void run() {
        // override by character
    }

    /**
     * 判断能否移动
     *
     * @param mapX x
     * @param mapY y
     * @return 遇到砖头或钢块或河水返回 false
     */
    public boolean canMove(int mapX, int mapY) {
        int n = MapConstants.map[mapY][mapX];
        return n == 0 || n == 3 || n == 4 || n == 8;
    }

    /**
     * 是否遇到其他该类对象
     */
    public boolean toCharacter(Weapon w) {
        return (this.mapX == w.mapX && this.mapY == w.mapY);
    }

    /**
     * 子弹, 激光前进
     */
    public void keepGoing() {
        if (imageX == 0) { // 右移
            ++mapX;
        } else if (imageX == 1) { // 下移
            ++mapY;
        } else if (imageX == 2) { // 左移
            --mapX;
        } else { // 上移
            --mapY;
        }
    }

    /**
     * 角色结束方法
     */
    public void characterOver(Weapon w) {
        if (w instanceof Bullets) {
            MyGamePanel.bullets.remove(w); // 从集合中移除角色对象
            new Thread(new Music("img/blast.wav")).start(); // 爆炸声效
        }
        if (w instanceof Player) {
            if (Player.getPlayer(jp).life == 0) {
                MyGamePanel.isGameOver = true;  // 游戏结束
                System.out.println("==========游戏结束!===========");
                //todo: 游戏结束后有的线程需要结束
                MyGamePanel.bullets.clear(); // 清空屏幕所有子弹
                MyGamePanel.lasers.clear(); // 清空屏幕残留激光
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (w instanceof EnemyTank) {
            MyGamePanel.ets.remove(w);
        }
        if (w instanceof Laser) {
            MyGamePanel.lasers.remove(w);
            new Thread(new Music("img/blast.wav")).start(); // 爆炸声效
        }
        MyGamePanel.blasts.add(new Blast(mapX, mapY, jp)); // 爆破效果
        w.isOver = true;
    }

}
