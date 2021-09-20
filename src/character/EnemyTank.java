package character;

import contants.MapConstants;
import contants.TankImageConstants;
import ui.MyGamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * 敌方坦克
 */
public class EnemyTank extends Weapon implements Runnable {

    private volatile boolean isDrawStar = false; // 是否绘制过星星★
    private int starImageX = 0; // 星星的坐标

    public EnemyTank(int mapX, int mapY, JPanel jp, int imageX, int imageY) {
        super();
        this.mapX = mapX;
        this.mapY = mapY;
        this.jp = jp;
        this.imageX = imageX;
        this.imageY = imageY;

        new Thread(this).start(); // 启动移动线程
        new Thread(
                () -> {
                    while (!isOver) {
                        if (isDrawStar) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            fire();
                        }
                    }
                }
        ).start();
    }

    /**
     * 敌方发射子弹
     */
    private void fire() {
        int x = (imageY == 0 || imageY == 2) ? mapX : (imageY == 1 ? mapX + 1 : mapX - 1);
        int y = (imageY == 1 || imageY == 3) ? mapY : (imageY == 0 ? mapY - 1 : mapY + 1);
        // 获取子弹在图中的方向(由坦克的方向获取)
        int direction = imageY == 0 ? 3 : imageY == 1 ? 0 : imageY == 2 ? 1 : 2; // 0:↑ 1:→
        Bullets bullets = new Bullets(x, y, direction, jp, MapConstants.CHARACTER_ENEMY);
        MyGamePanel.bullets.add(bullets);
    }

    /**
     * 绘制坦克
     */
    public void drawEnemyTank(Graphics g) {
        if (!isDrawStar) {
            g.drawImage(TankImageConstants.star, mapX << 5, mapY << 5, (mapX + 1) << 5,
                    (mapY + 1) << 5, starImageX * 192, 0, (starImageX + 1) * 192, 192, jp);

        } else {
            g.drawImage(TankImageConstants.enemyTank, mapX << 5, mapY << 5, (mapX + 1) << 5,
                    (mapY + 1) << 5, imageX * 28, imageY * 28, (imageX + 1) * 28, (imageY + 1) * 28, jp);
            if (MapConstants.map[mapY][mapX] == 4) {// 当前位置是草
                g.drawImage(TankImageConstants.grass, (mapX << 5) - 5, (mapY << 5) - 5,
                        ((mapX + 1) << 5) + 5, ((mapY + 1) << 5) + 5, 0, 0, 87, 83, jp);
            }
        }

    }

    @Override
    public void run() {
        try {
            while (true) { // NOSONAR
                if (!isDrawStar) {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(300);
                        starImageX = i;
                        jp.repaint();
                    }
                    isDrawStar = true;
                }

                Thread.sleep(20);
                imageX ^= 1; // 履带刷新, 若当前为 0,则下一次为 1

                Thread.sleep(250); // 敌方移动频率
                switch (imageY) { // 根据方法写坐标的移动
                    case 0: // 向上
                        if (isCanMove(mapX, mapY - 1)) {
                            --mapY;
                        }
                        break;
                    case 1: // 向右
                        if (isCanMove(mapX + 1, mapY)) {
                            ++mapX;
                        }
                        break;
                    case 2: // 向下
                        if (isCanMove(mapX, mapY + 1)) {
                            ++mapY;
                        }
                        break;
                    case 3: // 向左
                        if (isCanMove(mapX - 1, mapY)) {
                            --mapX;
                        }
                        break;
                    default:
                        break;
                }
                Random random = new Random();
                if (mapY == random.nextInt(20) + 1 || mapY == random.nextInt(20) + 1 || mapY == random.nextInt(20) + 1)
                    imageY = random.nextInt(4);//随机转向
                jp.repaint();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断指定的地方是否可以到达
     *
     * @param toX 将要去的x轴
     * @param toY 将要去的y轴
     */
    public boolean isCanMove(int toX, int toY) {
        int num = MapConstants.map[toY][toX];
        // 判断可以到达的地方
        if (num == 0 || num == 3 || num == 4) {
            if (toEnemyTank(toX, toY) || this.toCharacter(Player.getPlayer(jp))) {
                while (true) {
                    int dir = new Random().nextInt(4);
                    if (dir != imageY) {
                        imageY = dir;
                        break;
                    }
                }
            } else {
                return true;
            }
        } else { // 不能继续走了，需要转向
            while (true) {
                int dir = new Random().nextInt(4);
                if (dir != imageY) {
                    imageY = dir;
                    break;
                }

            }
        }
        return false;
    }

    /**
     * 遇到敌方坦克
     *
     * @param toX to x
     * @param toY to y
     * @return 遇到返回 true
     */
    public boolean toEnemyTank(int toX, int toY) {
        for (int i = 0; i < MyGamePanel.ets.size(); i++) {
            EnemyTank et = MyGamePanel.ets.get(i);
            if (toX == et.mapX && toY == et.mapY) {
                return true;
            }
        }
        return false;
    }

}
