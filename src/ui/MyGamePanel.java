package ui;

import character.*;
import contants.MapConstants;
import contants.TankImageConstants;
import sound.Music;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 游戏面板
 */
public class MyGamePanel extends JPanel implements Runnable {
    public static Vector<EnemyTank> ets = new Vector<>(); // 敌方坦克集合
    public static List<Weapon> bullets = new ArrayList<>(); // oop子弹集合
    public static List<Weapon> blasts = new ArrayList<>(); // 爆破集合
    public static List<Laser> lasers = new ArrayList<>(); // 激光集合

    public static boolean isGameOver = false;
    public static boolean isSwitch = false;
    private int bossY = 0;

    public MyGamePanel() {
        setBackground(Color.BLACK);
        new Thread(new Music("img/start.wav")).start();//开始的音乐
        new Thread(this).start();
        //匿名对象
        new Thread(() -> {
            while (true) {
                if (ets.size() < 3) {
                    Random r = new Random();
                    for (int i = 0; i < 3; i++) {
                        EnemyTank et = new EnemyTank(r.nextInt(31) + 1, 1, MyGamePanel.this, 0, r.nextInt(4));
                        ets.add(et);
                    }
                }
            }
        }).start();

        MapConstants.generateLaser();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < MapConstants.map.length; i++) {
            for (int j = 0; j < MapConstants.map[i].length; j++) {
                int num = MapConstants.map[i][j];//得到地图中标注的数字
                switch (num) {
                    case 1:
                        // 砖头
                        g.drawImage(TankImageConstants.brick, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 60, 60, this);
                        break;
                    case 2:
                        // 铁
                        g.drawImage(TankImageConstants.steels, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 93, 85, this);
                        break;
                    case 3:
                        // 冰
                        g.drawImage(TankImageConstants.ice, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 60, 60, this);
                        break;
                    case 4:
                        // grass
                        g.drawImage(TankImageConstants.grass, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 80, 80, this);
                        break;
                    case 5:
                        // water
                        g.drawImage(TankImageConstants.water, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 122, 122, this);
                        break;
                    case 6:
                        // 边框
                        g.drawImage(TankImageConstants.gray, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 74, 78, this);
                        break;
                    case 7:
                        // boss boss2.gif
                        g.drawImage(TankImageConstants.boss, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, bossY * 34, 41, 34 * (bossY + 1), this);
                        break;
                    case 8:
                        // 激光武器
                        g.drawImage(TankImageConstants.laser, j << 5, i << 5, (j + 1) << 5, (i + 1) << 5, 0, 0, 32, 32, this);
                        break;
                    default:
                        break;
                }
            }
        }

        // 绘制玩家单例对象
        Player player = Player.getPlayer(this);
        player.drawPlayer(g, this);

        if (!isSwitch) {
            player.switchWeapon(); //绘制单个激光, 一次
        }

        // 绘制敌方坦克
        for (EnemyTank et : ets) {
            et.drawEnemyTank(g);
        }

        // 绘制子弹
        for (Weapon bullet : bullets) {
            bullet.drawAll(g, TankImageConstants.bullet, 32);

        }

        //绘制激光
        for (Laser laser : lasers) {
            laser.drawLaser(g);
        }

        //爆破效果
        for (Weapon blast : blasts) {
            blast.drawAll(g, TankImageConstants.blast, 192);
        }

        if (isGameOver) {//显示 GAME OVER 图片
            g.drawImage(TankImageConstants.gameover, 0, 0, getWidth(), getHeight(), 0, 0, 256, 160, this);
        }
    }


    @Override
    public void run() {
        while (true) { // NOSONAR
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++bossY;
            if (bossY == 12) {
                bossY = 0;
            }
            repaint(); // 重绘
        }
    }
}
