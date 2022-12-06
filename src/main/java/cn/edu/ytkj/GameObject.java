package cn.edu.ytkj;

import java.awt.*;

/**
 * 游戏对象 （下落字）
 */
public class GameObject {
    // 对象颜色
    private Color color;
    // 对象文本
    private String string;
    // 当前 X 坐标
    private int x;
    // 当前 Y 坐标
    private int y;

    /**
     * 构造 GameObject
     *
     * @param color  颜色
     * @param string 文本
     * @param x      初始 X 坐标
     * @param y      初始 Y 坐标
     */
    public GameObject(Color color, String string, int x, int y) {
        this.color = color;
        this.string = string;
        this.x = x;
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "color=" + color +
                ", string='" + string + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
