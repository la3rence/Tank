package character;

import contants.MapConstants;
import contants.TankImageConstants;
import ui.MyGamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 玩家类(单例模式)
 */
public class Player extends Weapon implements KeyListener, Runnable {

    public static int whichFire = 1;
    public int life = 3; // 生命值

    private static Player player = null;// 在类的内部进行静态的实例化对象,私有.

    private Player() {
    }

    // 对构造方法私有, 隐藏外界的访问
    private Player(int mapX, int mapY, JPanel jp, int whichFire, int life) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.jp = jp;
        this.whichFire = whichFire;
        this.life = life;
    }

    public static Player getPlayer(JPanel jp) {// 对外开放一个可以获取到以上创建出来的实例的方法
        if (player == null) {
            player = new Player(13, 21, jp, whichFire, 3);
            new Thread(player).start();// 启动线程
        }
        return player;
    }

    /**
     * 复活
     */
    public void reborn() {
        mapX = 13;
        mapY = 21;
        --life;
        System.out.println("复活, 剩余生命值: " + life);
    }

    public void drawPlayer(Graphics g, JPanel jp) {
        // 绘制我方坦克
        g.drawImage(TankImageConstants.tank, mapX << 5, mapY << 5, (mapX + 1) << 5, (mapY + 1) << 5, imageX * 28, imageY * 28, (imageX + 1) * 28, (imageY + 1) * 28, jp);
        if (MapConstants.map[mapY][mapX] == 4) {// 当前位置是草,将草变大
            g.drawImage(TankImageConstants.grass, (mapX << 5), (mapY << 5), ((mapX + 1) << 5), ((mapY + 1) << 5), 10, 10, 70, 70, jp);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!MyGamePanel.isGameOver) { // 可移动前提: 未被敌方坦克击中
            whichFire = MyGamePanel.isSwitch ? MapConstants.FIRE_LASER : MapConstants.FIRE_BULLET;
            int step = 1;
            switch (e.getKeyCode()) { // 获取键盘输入
                case KeyEvent.VK_J:
                case KeyEvent.VK_SPACE:
                    if (whichFire == MapConstants.FIRE_BULLET) {
                        fire();
                    } else {
                        int f = 0;
                        while (f++ <= 20) {
                            fire();
                        }
                    }
                    break;
                case KeyEvent.VK_1: // 切换子弹
                    System.out.println("子弹模式");
                    whichFire = MapConstants.FIRE_BULLET;
                    MyGamePanel.isSwitch = false;
                    break;
                case KeyEvent.VK_2: // 切换激光
                    System.out.println("激光模式");
                    whichFire = MapConstants.FIRE_LASER;
                    MyGamePanel.isSwitch = true;
                    break;
                case KeyEvent.VK_W:
                    // System.out.println("↑");
                    if (imageY == 0 && canMove(mapX, mapY - 1)) {
                        mapY -= step;
                    }
                    imageY = 0;
                    break;
                case KeyEvent.VK_S:
                    // System.out.println("↓");
                    if (imageY == 2 && canMove(mapX, mapY + 1)) {
                        mapY += step;
                    }
                    imageY = 2;
                    break;
                case KeyEvent.VK_A:
                    // System.out.println("←");
                    if (imageY == 3 && canMove(mapX - 1, mapY)) {
                        mapX -= step;
                    }
                    imageY = 3;
                    break;
                case KeyEvent.VK_D:
                    // System.out.println("→");
                    if (imageY == 1 && canMove(mapX + 1, mapY)) {
                        mapX += step;
                    }
                    imageY = 1;
                    break;
                default:
                    break;
            }
            jp.repaint();
        } else {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                MyGamePanel.bullets.clear(); // 清空屏幕所有子弹
                MyGamePanel.isGameOver = false;
                MapConstants.map[21][15] = 7; // boss 复活
                player.life = 3;
            }
        }
    }

    /**
     * 玩家发射子弹
     * imageY: 我方坦克的方向
     * (x, y): 子弹的坐标
     */
    public void fire() {
        int x = (imageY == 0 || imageY == 2) ? mapX : (imageY == 1 ? mapX + 1 : mapX - 1);
        int y = (imageY == 1 || imageY == 3) ? mapY : (imageY == 0 ? mapY - 1 : mapY + 1);
        // 获取子弹在图中的方向(由坦克的方向获取), 再映射到子弹图片的具体位置
        int direction = imageY == 0 ? 3 : imageY == 1 ? 0 : imageY == 2 ? 1 : 2; // 0:↑ 1:→

        if (whichFire == MapConstants.FIRE_BULLET) {

            Bullets bullets = new Bullets(x, y, direction, jp, MapConstants.CHARACTER_PLAYER);
            MyGamePanel.bullets.add(bullets);
        } else {
            Laser laser = new Laser(direction, x, y, jp);
            MyGamePanel.lasers.add(laser);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * 履带刷新
     */
    @Override
    public void run() {
        try {
            while (true) { // NOSONAR
                Thread.sleep(200);
                imageX ^= 1;  // 通过异或修改原图所在位置  imageX = 1- imageX
                jp.repaint(); // 重绘
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成激光武器,并判断玩家是否获取
     */
    public void switchWeapon() {
        Player player = Player.getPlayer(jp);
        if (player.mapY == MapConstants.randomY && player.mapX == MapConstants.randomX) {
            MapConstants.map[mapY][mapX] = 0; // 获取后武器消失
            System.out.println("获取激光武器!");
            MyGamePanel.isSwitch = true;
        }
    }
}
