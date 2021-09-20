package character;

import contants.MapConstants;
import ui.MyGamePanel;

import javax.swing.*;

public class Bullets extends Weapon implements Runnable {

    public int roleType;// 区分敌我子弹 1:player 2:enemy

    public Bullets(int mapX, int mapY, int imageX, JPanel jp, int roleType) {
        this.mapX = mapX;
        this.mapY = mapY;
        this.imageX = imageX;
        this.jp = jp;
        this.roleType = roleType;
        new Thread(this).start(); // 启动子弹线程: run() --> move()
    }

    @Override
    public void run() {
        // 移动子弹
        while (!isOver && !MyGamePanel.isGameOver) {
            try {
                Thread.sleep(80);
                synchronized (MyGamePanel.bullets) {
                    move();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 子弹移动
     */
    public void move() {
        int num = MapConstants.map[mapY][mapX]; // 地图元素
        Player player = Player.getPlayer(jp); // 玩家对象
        if (num == 1 || num == 2 || num == 6 || num == 7 || (toCharacter(player) && roleType == MapConstants.CHARACTER_ENEMY)) {//遇到砖块等需要爆炸效果的地图元素
            characterOver(this);  //首先展示子弹爆破效果,结束子弹
            if (num == 1) {
                MapConstants.map[mapY][mapX] = 0;//移除砖块
            }
            if (num == 7 || (toCharacter(player) && roleType == MapConstants.CHARACTER_ENEMY)) {
                characterOver(player);
                player.reborn();
            }
        } else {
            for (int i = 0; i < MyGamePanel.bullets.size(); i++) {
                Weapon b = MyGamePanel.bullets.get(i);
                if (toCharacter(b) && b.imageX != this.imageX) { //任意子弹相遇,不考虑 roleType
                    characterOver(this);
                    characterOver(b);
                }
            }
        }
        keepGoing();
        fightEnemy();
    }

    /**
     * 玩家攻击敌方坦克
     */
    private void fightEnemy() {
        for (int i = 0; i < MyGamePanel.ets.size(); i++) {
            Weapon et = MyGamePanel.ets.get(i); //遍历敌方坦克
            if (this.toCharacter(et) && roleType == MapConstants.CHARACTER_PLAYER) {//
                characterOver(et);  //结束敌方坦克
                characterOver(this); //结束此时的子弹
            }
        }
    }
}
