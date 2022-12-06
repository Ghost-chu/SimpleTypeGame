package cn.edu.ytkj;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * 游戏画布
 */
public class GameCanvas extends Canvas {
    /*
     * 活动的游戏对象渲染队列
     * 此队列中的所有对象每次绘制时都会被绘制到画布上
     */
    private final List<GameObject> livingGameObject = Collections.synchronizedList(new ArrayList<>());
    /*
     * 随机数生成器
     */
    private final Random random = new Random();
    private final boolean redown;

    public GameCanvas(boolean redown) {
        this.redown = redown;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        /*
         * 将渲染队列中的所有内容绘制到画布上
         */
        livingGameObject.forEach(obj -> {
            // 设定颜色
            g.setColor(obj.getColor());
            // 绘制文本

            // 如果此对象是渲染队列中最靠下的对象，则应用文字放大效果，突出显示
            Optional<GameObject> op = getYLowestObject();
            if (op.isPresent() && op.get() == obj) {
                g.setFont(new Font("微软雅黑", Font.BOLD, 28));
            } else {
                g.setFont(new Font("微软雅黑", Font.BOLD, 15));
            }
            // 绘制文字
            g.drawString(obj.getString(), obj.getX(), obj.getY());
        });
    }

    /**
     * 注册一个新的 GameObject 到渲染队列
     *
     * @param obj GameObject
     */
    public void insertNewGameObject(GameObject obj) {
        // 随机 X 轴，保留 25px 避免溢出画布
        obj.setX(random.nextInt(this.getWidth() - 25));
        // 初始 Y 值为 0 值
        int targetY = 0;
        // 如果新插入的 GameObject 给定的 Y 值坐标和现有的 GameObject 可能重叠，则将其上挪 20px
        Optional<GameObject> highest = getYHighestObject();
        if (highest.isPresent()) {
            GameObject highestObject = highest.get();
            // 向上挪动，避免画布重叠
            if (highestObject.getY() <= 20) {
                targetY = highestObject.getY() - 20;
            }
        }
        // 更新新设置的 Y 值
        obj.setY(targetY);
        // 插入渲染队列
        this.livingGameObject.add(obj);
    }

    /**
     * 获取当前渲染队列的 GameObjects
     * 该方法会返回引用而非克隆，修改会反映到 livingGameObject 中
     *
     * @return GameObjects
     */
    public List<GameObject> getGameObjects() {
        return livingGameObject;
    }

    /**
     * 将当前渲染队列中的所有 GameObject 向下挪动一定的距离
     *
     * @param distance 距离 (px)
     * @return false = 未被删除 true = 有字母被删除
     */
    public boolean moveDown(int distance) {
        boolean anyDeleted = false;
        // 使用迭代器来循环中删除操作
        Iterator<GameObject> gameObjectIterator = livingGameObject.iterator();
        while (gameObjectIterator.hasNext()) {
            GameObject gameObject = gameObjectIterator.next();
            gameObject.setY(gameObject.getY() + distance);
            // 如果 GameObject 超出画布，则应用重新下落效果或者将其移除
            if (gameObject.getY() > this.getHeight()) {
                if (redown) {
                    gameObject.setY(-5);
                } else {
                    gameObjectIterator.remove();
                    anyDeleted = true;
                }
            }
        }
        this.repaint();
        return anyDeleted;
    }

    /**
     * 获取最低 Y 轴的 GameObject
     *
     * @return 返回一个 GameObject，如果渲染队列没有合适的 GameObject，则返回 null
     */
    public Optional<GameObject> getYLowestObject() {
        GameObject lowestObject = null;
        for (GameObject gameObject : livingGameObject) {
            if (lowestObject == null) {
                lowestObject = gameObject;
                continue;
            }
            // check Y
            if (lowestObject.getY() >= gameObject.getY()) {
                continue;
            }
            lowestObject = gameObject;
        }
        return Optional.ofNullable(lowestObject);
    }

    /**
     * 获取最高 Y 轴的 GameObject
     *
     * @return 返回一个 GameObject，如果渲染队列没有合适的 GameObject，则返回 null
     */
    public Optional<GameObject> getYHighestObject() {
        GameObject highestObject = null;
        for (GameObject gameObject : livingGameObject) {
            if (highestObject == null) {
                highestObject = gameObject;
                continue;
            }
            // check Y
            if (highestObject.getY() <= gameObject.getY()) {
                continue;
            }
            highestObject = gameObject;
        }
        return Optional.ofNullable(highestObject);
    }

    /**
     * 响应按键点击事件
     * 响应后无论是否正确，Y轴最低的 GameObject 总会从画布中移除
     *
     * @param str 按下的按键
     * @return 如果按下的按键和Y轴最低的相同，则返回 true，否则返回 false，如果当前没有GameObject，则返回 null <br />
     * 无论如何，响应后，Y轴最低的 GameObject 总会被从渲染队列中移除，并进行分数操作
     */
    public Optional<Boolean> keyPressed(String str) {
        Optional<GameObject> obj = getYLowestObject();
        if (!obj.isPresent()) {
            return Optional.empty();
        }
        GameObject gameObject = obj.get();
        Optional<Boolean> hit = Optional.of(gameObject.getString().toLowerCase(Locale.ROOT).equals(str.toLowerCase(Locale.ROOT)));
        this.livingGameObject.remove(gameObject);
        return hit;
    }
}