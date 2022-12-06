/*
 * Created by JFormDesigner on Tue Dec 06 09:28:07 CST 2022
 */

package cn.edu.ytkj;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Test
 */
public class GameCanvasFrame extends JFrame {
    private static final AtomicInteger SCORE_COUNTER = new AtomicInteger(0);
    private static final Random RANDOM = new Random();
    private final int speed;
    private final boolean redown;
    private final boolean randColor;
    private final boolean hitPunish;
    private final boolean combo;
    private final String username;
    private final int maxLength;
    private final int perPunishScore;
    private final boolean failureSummary;
    // 使用 Timer 代替循环 Thread, 在 Thread 中使用 Thread.sleep 会导致 busy-wait 对性能不利
    private final Timer TIME_TIMER = new Timer("时间计时器");
    private final Timer DRAW_TIMER = new Timer("游戏绘制计时器");
    private final AtomicInteger TIMER_ATOMIC = new AtomicInteger(0);
    private final AtomicInteger COMBO_COUNTER = new AtomicInteger(0);
    private final GameMainFrame gameMainFrame;
    private boolean stopped = false;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel scoreDisplayer;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    private GameCanvas panel1;

    public GameCanvasFrame(String speed, boolean redown, boolean randColor, boolean hitPunish,
                           boolean combo, String username, int maxLength, int perPunishScore, boolean failureSummary, GameMainFrame gameMainFrame) {
        this.redown = redown;
        this.randColor = randColor;
        this.hitPunish = hitPunish;
        this.combo = combo;
        this.username = username;
        this.maxLength = maxLength;
        this.perPunishScore = perPunishScore;
        this.failureSummary = failureSummary;
        this.gameMainFrame = gameMainFrame;
        this.gameMainFrame.setVisible(false);
        switch (speed) {
            case "有手就行":
                this.speed = 4000;
                break;
            case "非常慢":
                this.speed = 3000;
                break;
            case "慢":
                this.speed = 2000;
                break;
            case "正常":
                this.speed = 1000;
                break;
            case "快":
                this.speed = 500;
                break;
            case "非常快":
                this.speed = 200;
                break;
            case "★中国★":
                this.speed = 100;
                break;
            default:
                this.speed = 100;
                break;
        }
        initComponents();
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        this.panel1 = new GameCanvas(redown);
        this.add(panel1);
        initTimers();
        initCanvas();
        registerListeners();
        scoreDisplayer.setText("分数：" + SCORE_COUNTER.get());
        startGame();
    }

    /**
     * 生成随机颜色
     *
     * @return 随机 Swing 组件颜色
     */
    private static Color generateColor() {
        Color[] colors = new Color[]{Color.BLACK, Color.BLUE, Color.RED, Color.MAGENTA, Color.ORANGE};
        return colors[RANDOM.nextInt(colors.length)];
    }

    /**
     * 生成随机字符
     * A smart fox jumps over a lazy dog - 该句子包含所有 26 个字母
     * 该方法返回所有字母的几率不是 1:1 的
     *
     * @return 随机字符
     */
    private static String generateChar() {
        char[] chars = "ASMARTFOXJUMPSOVERALAZYDOG".toCharArray();
        return String.valueOf(chars[RANDOM.nextInt(chars.length)]);
    }

    private void initCanvas() {
        panel1.setBounds(0, 40, this.getWidth(), this.getHeight() - 40);
    }

    private void registerListeners() {
        panel1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (!radioButton1.isSelected()) {
                    return;
                }
                if (isInCombo()) {
                    AudioUtil.playComboClick();
                } else {
                    AudioUtil.playClick();
                }
                Optional<Boolean> result = panel1.keyPressed(String.valueOf(e.getKeyChar()));
                if (result.isPresent()) {
                    if (result.get()) {
                        SCORE_COUNTER.incrementAndGet();
                        updateScores(true);
                    } else {
                        SCORE_COUNTER.addAndGet(-perPunishScore);
                        updateScores(false);
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        radioButton3.addActionListener(v -> {
            if (radioButton3.isSelected()) {
                endGame();
            }
        });
    }

    public boolean isInCombo() {
        return combo && COMBO_COUNTER.get() > 5;
    }

    public void breakCombo() {
        if (isInCombo()) {
            AudioUtil.playComboBreaked();
        }
        COMBO_COUNTER.set(0);
    }

    public void createCombo() {
        AudioUtil.playComboCreated();
    }

    public void addCombo() {
        if (combo && COMBO_COUNTER.incrementAndGet() > 5) {
            createCombo();
        }
    }

    private void updateScores(boolean increase) {
        if (increase) {
            addCombo();
        } else {
            breakCombo();
        }
        if (isInCombo()) {
            SCORE_COUNTER.addAndGet(5);
            scoreDisplayer.setText("分数：" + SCORE_COUNTER.get() + " (COMBO x" + COMBO_COUNTER.get() + ")");
        } else {
            scoreDisplayer.setText("分数：" + SCORE_COUNTER.get());
        }
        if (failureSummary) {
            if (SCORE_COUNTER.get() < 0) {
                endGame();
            }
        }
    }

    private void startGame() {
        radioButton1.setSelected(true);
        //radioButton2.setSelected(false);
        //radioButton3.setSelected(false);
    }

    private void initTimers() {
        DRAW_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timedRepeatTask();
            }
        }, 0, speed);
        TIME_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                bumpTime();
            }
        }, 0, 1000L);
    }

    private void bumpTime() {
        // radioButton1 如果被选中，那么游戏即在运行
        if (!radioButton1.isSelected()) {
            return;
        }
        int timePassedAway = TIMER_ATOMIC.incrementAndGet();
        label1.setText(Util.formatTime(timePassedAway));
    }

    private void timedRepeatTask() {
        if (!radioButton1.isSelected()) {
            return;
        }
        panel1.requestFocusInWindow();
        if (panel1.getGameObjects().size() < maxLength) {
            // x 和 y 在注册到渲染队列后会被重新赋值，且 Y 会持续增加
            panel1.insertNewGameObject(new GameObject(randColor ? generateColor() : Color.BLACK, generateChar(), -1, -1));
        }
        if (panel1.moveDown(10)) {
            SCORE_COUNTER.addAndGet(-perPunishScore);
            updateScores(false);
        }
    }

    private void endGame() {
        if (stopped) return;
        stopped = true;
        AudioUtil.playGameEnd();
        DRAW_TIMER.cancel();
        TIME_TIMER.cancel();
        radioButton1.setSelected(false);
        radioButton2.setSelected(false);
        radioButton3.setSelected(true);
        JOptionPane.showMessageDialog(this, "游戏结束，你的分数是：" + SCORE_COUNTER.get());
        try {
            ConfigurationNode node = ConfigUtil.get().node("score-history").node(username);
            if (node.isNull() || node.getInt() < SCORE_COUNTER.get()) {
                node.set(SCORE_COUNTER.get());
            }
            ConfigUtil.saveConfig();
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
        this.dispose();
        gameMainFrame.setVisible(true);
        gameMainFrame.flushRanks();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scoreDisplayer = new JLabel();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        radioButton3 = new JRadioButton();
        label1 = new JLabel();

        //======== this ========
        setTitle("\u6e38\u620f UI ");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- scoreDisplayer ----
        scoreDisplayer.setText("\u8ba1\u5206\u5668");
        scoreDisplayer.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreDisplayer.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        contentPane.add(scoreDisplayer);
        scoreDisplayer.setBounds(533, 5, 222, 34);

        //---- radioButton1 ----
        radioButton1.setText("\u7ee7\u7eed");
        contentPane.add(radioButton1);
        radioButton1.setBounds(new Rectangle(new Point(315, 10), radioButton1.getPreferredSize()));

        //---- radioButton2 ----
        radioButton2.setText("\u6682\u505c");
        contentPane.add(radioButton2);
        radioButton2.setBounds(365, 10, 50, 21);

        //---- radioButton3 ----
        radioButton3.setText("\u505c\u6b62\u5e76\u7ed3\u7b97");
        contentPane.add(radioButton3);
        radioButton3.setBounds(415, 10, 85, 21);

        //---- label1 ----
        label1.setText("\u8ba1\u65f6\u5668");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        contentPane.add(label1);
        label1.setBounds(12, 8, 275, 29);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        setSize(780, 515);
        setLocationRelativeTo(getOwner());

        //---- controllerButtons ----
        ButtonGroup controllerButtons = new ButtonGroup();
        controllerButtons.add(radioButton1);
        controllerButtons.add(radioButton2);
        controllerButtons.add(radioButton3);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
