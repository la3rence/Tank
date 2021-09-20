package ui;

import character.Player;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏框架
 */
public class GameFrame extends JFrame {

    public GameFrame() {
        // 获取屏幕对象
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // 获取屏幕尺寸
        int height = (int) dim.getHeight();
        int weight = (int) dim.getWidth();
        // System.out.println(height + ", " + weight);
        int w = 1072;
        int h = 770;
        int x = (weight - w) >> 1;
        int y = (height - h) >> 1;
        setBounds(x, y, w, h);

        JPanel jp = new MyGamePanel();
        setContentPane(jp); // JFrame设置面板

        addKeyListener(Player.getPlayer(jp)); // 注册键盘

        setVisible(true); // 可见
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭按钮的同时结束虚拟机
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
