package cn.edu.ytkj;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 游戏画布
 */
public class GameCanvas extends Canvas {
    /*
     * 活动的游戏对象渲染队列
     * 此队列中的所有对象每次绘制时都会被绘制到画布上
     */
    private final List<GameObject> livingGameObject = Collections.synchronizedList(new ArrayList<>());
    private final ReentrantLock LOCK = new ReentrantLock();
    /*
     * 随机数生成器
     */
    private final Random random = new Random();
    /*
    字符重新下落
     */
    private final boolean redown;
    private final boolean focusMode;
    private final boolean blindMode;

    /**
     * 构造 GameCanvas 游戏主画布
     *
     * @param redown    字符是否会重新下落
     * @param focusMode 聚焦模式
     * @param blindMode 抓瞎模式
     */
    public GameCanvas(boolean redown, boolean focusMode, boolean blindMode) {
        this.redown = redown;
        this.focusMode = focusMode;
        this.blindMode = blindMode;
    }

    /**
     * 绘制游戏画布
     *
     * @param g the specified Graphics context
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        LOCK.lock();
        try {
            livingGameObject.forEach(obj -> {
                g.setColor(obj.getColor());
                Optional<GameObject> op = getYLowestObject();
                if (op.isPresent() && op.get() == obj) {
                    if (focusMode) {
                        g.setFont(new Font("微软雅黑", Font.BOLD, 28));
                    } else {
                        g.setFont(new Font("微软雅黑", Font.BOLD, 15));
                    }
                } else {
                    g.setFont(new Font("微软雅黑", Font.BOLD, 15));
                }
                if (blindMode && obj.getY() > this.getHeight() / 1.8) {
                    g.drawString("?", obj.getX(), obj.getY());
                } else {
                    g.drawString(obj.getString(), obj.getX(), obj.getY());
                }

            });
        } finally {
            LOCK.unlock();
        }

    }

    /**
     * 注册一个新的 GameObject 到渲染队列
     *
     * @param obj GameObject
     */
    public void insertNewGameObject(GameObject obj) {
        obj.setX(random.nextInt(this.getWidth() - 25));
        int targetY = 0;
        Optional<GameObject> highest = getYHighestObject();
        if (highest.isPresent()) {
            GameObject highestObject = highest.get();
            if (highestObject.getY() <= 20) {
                targetY = highestObject.getY() - 20;
            }
        }
        obj.setY(targetY);
        LOCK.lock();
        try {
            this.livingGameObject.add(obj);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取当前渲染队列的 GameObjects
     * 该方法会返回引用而非克隆，修改会反映到 livingGameObject 中
     *
     * @return GameObjects 渲染队列
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
        LOCK.lock();
        try {
            Iterator<GameObject> gameObjectIterator = livingGameObject.listIterator();
            while (gameObjectIterator.hasNext()) {
                GameObject gameObject = gameObjectIterator.next();
                gameObject.setY(gameObject.getY() + distance);
                if (gameObject.getY() > this.getHeight()) {
                    if (redown) {
                        gameObject.setY(-5);
                    } else {
                        gameObjectIterator.remove();
                        anyDeleted = true;
                    }
                }
            }
        } finally {
            LOCK.unlock();
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
        LOCK.lock();
        try {
            for (GameObject gameObject : livingGameObject) {
                if (lowestObject == null) {
                    lowestObject = gameObject;
                    continue;
                }
                if (lowestObject.getY() >= gameObject.getY()) {
                    continue;
                }
                // 上一个匹配对象设置为当前对象
                lowestObject = gameObject;
            }
            return Optional.ofNullable(lowestObject);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取最高 Y 轴的 GameObject
     *
     * @return 返回一个 GameObject，如果渲染队列没有合适的 GameObject，则返回 null
     */
    public Optional<GameObject> getYHighestObject() {
        GameObject highestObject = null;
        LOCK.lock();
        try {
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
        } finally {
            LOCK.unlock();
        }
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
        LOCK.lock();
        try {
            Optional<GameObject> obj = getYLowestObject();
            if (!obj.isPresent()) {
                return Optional.empty();
            }
            GameObject gameObject = obj.get();
            boolean hitBool = gameObject.getString().toLowerCase(Locale.ROOT).equals(str.toLowerCase(Locale.ROOT));
            Optional<Boolean> hit = Optional.of(hitBool);
            this.livingGameObject.remove(gameObject);
            return hit;
        } finally {
            LOCK.unlock();
        }

    }
}
