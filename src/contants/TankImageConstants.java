package contants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 加载游戏面板所需要的图片资源
 */
public class TankImageConstants {

    public static Image grass = null;
    public static Image brick = null;
    public static Image water = null;
    public static Image ice = null;
    public static Image steels = null;
    public static Image boss = null;
    public static Image gray = null;
    public static Image rh = null;
    public static Image star = null;
    public static Image tank = null;
    public static Image enemyTank = null;
    public static Image bullet = null;
    public static Image blast = null;
    public static Image gameover = null;
    public static Image laser = null;
    public static Image character = null;

    static {
        try {
            grass = ImageIO.read(new File("img/grass.png"));
            brick = ImageIO.read(new File("img/wall.gif"));
            water = ImageIO.read(new File("img/water1.jpg"));
            ice = ImageIO.read(new File("img/ice.png"));
            steels = ImageIO.read(new File("img/steel2.png"));
            boss = ImageIO.read(new File("img/boss.gif"));
            gray = ImageIO.read(new File("img/gray.png"));
            rh = ImageIO.read(new File("img/rh.png"));
            bullet = ImageIO.read(new File("img/bullet.png"));

            tank = ImageIO.read(new File("img/player.gif"));
            enemyTank = ImageIO.read(new File("img/tanks.bmp"));
            star = ImageIO.read(new File("img/star.png"));
            blast = ImageIO.read(new File("img/boom.png"));
            gameover = ImageIO.read(new File("img/gameover.bmp"));
            laser = ImageIO.read(new File("img/laser2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TankImageConstants() {
    }
}