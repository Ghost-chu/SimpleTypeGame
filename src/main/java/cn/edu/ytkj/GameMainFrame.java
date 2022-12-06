/*
 * Created by JFormDesigner on Tue Dec 06 08:39:39 CST 2022
 */

package cn.edu.ytkj;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author unknown
 */
public class GameMainFrame extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel title;
    private JLabel diffselect;
    private JComboBox speedSelector;
    private JCheckBox textReDown;
    private JCheckBox textRandomColor;
    private JCheckBox badHitPunish;
    private JCheckBox combo;
    private JLabel label2;
    private JButton startGame;
    private JLabel label3;
    private JLabel label4;
    private JTextField myName;
    private JScrollPane scrollPane1;
    private JList historyList;
    public GameMainFrame() {
        initComponents();
        initDefaultValues();
    }

    private void initDefaultValues() {
        // 设置窗体关闭默认选项
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 添加选择项
        this.speedSelector.addItem("有手就行");
        this.speedSelector.addItem("非常慢");
        this.speedSelector.addItem("慢");
        this.speedSelector.addItem("正常");
        this.speedSelector.addItem("快");
        this.speedSelector.addItem("非常快");
        this.speedSelector.addItem("★中国★");
        // 从配置文件加载默认值
        loadConf();
        this.startGame.addActionListener((v) -> {
            // 保存配置
            try {
                saveConf();
                Object selectedItem = speedSelector.getSelectedItem();
                if (!(selectedItem instanceof String))
                    selectedItem = "正常";
                new GameCanvasFrame((String) selectedItem, textReDown.isSelected(), textRandomColor.isSelected(), badHitPunish.isSelected(), combo.isSelected(),
                        myName.getText(), 6, 3, true, this).setVisible(true);
                AudioUtil.playGameStart();
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadConf() {
        this.badHitPunish.setSelected(ConfigUtil.get().node("badHitPunish").getBoolean(true));
        this.speedSelector.setSelectedItem(ConfigUtil.get().node("speedSelector").getString("正常"));
        this.combo.setSelected(ConfigUtil.get().node("combo").getBoolean(true));
        this.myName.setText(ConfigUtil.get().node("name").getString("游客"));
        this.textReDown.setSelected(ConfigUtil.get().node("text-redown").getBoolean(false));
        this.textRandomColor.setSelected(ConfigUtil.get().node("text-randomcolor").getBoolean(true));
        flushRanks();
    }

    private void saveConf() throws SerializationException {
        ConfigUtil.get().node("badHitPunish").set(this.badHitPunish.isSelected());
        ConfigUtil.get().node("speedSelector").set(this.speedSelector.getSelectedItem());
        ConfigUtil.get().node("combo").set(this.combo.isSelected());
        ConfigUtil.get().node("name").set(this.myName.getText());
        ConfigUtil.get().node("text-redown").set(this.textReDown.isSelected());
        ConfigUtil.get().node("text-randomcolor").set(this.textRandomColor.isSelected());
        ConfigUtil.saveConfig();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        title = new JLabel();
        diffselect = new JLabel();
        speedSelector = new JComboBox();
        textReDown = new JCheckBox();
        textRandomColor = new JCheckBox();
        badHitPunish = new JCheckBox();
        combo = new JCheckBox();
        label2 = new JLabel();
        startGame = new JButton();
        label3 = new JLabel();
        label4 = new JLabel();
        myName = new JTextField();
        scrollPane1 = new JScrollPane();
        historyList = new JList();

        //======== this ========
        setTitle("Java \u6253\u5b57\u9752\u6625\u7248 \u4e3b\u83dc\u5355");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- title ----
        title.setText("Java \u6253\u5b57\u9752\u6625\u7248");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
        contentPane.add(title);
        title.setBounds(245, 10, 275, 75);

        //---- diffselect ----
        diffselect.setText("\u901f\u5ea6\uff1a");
        diffselect.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        contentPane.add(diffselect);
        diffselect.setBounds(80, 140, 60, 28);
        contentPane.add(speedSelector);
        speedSelector.setBounds(125, 140, 115, speedSelector.getPreferredSize().height);

        //---- textReDown ----
        textReDown.setText("\u6587\u5b57\u91cd\u65b0\u4e0b\u843d");
        contentPane.add(textReDown);
        textReDown.setBounds(new Rectangle(new Point(80, 180), textReDown.getPreferredSize()));

        //---- textRandomColor ----
        textRandomColor.setText("\u6587\u5b57\u968f\u673a\u989c\u8272");
        textRandomColor.setSelected(true);
        contentPane.add(textRandomColor);
        textRandomColor.setBounds(new Rectangle(new Point(80, 210), textRandomColor.getPreferredSize()));

        //---- badHitPunish ----
        badHitPunish.setText("\u6309\u9519\u6263\u5206");
        badHitPunish.setSelected(true);
        contentPane.add(badHitPunish);
        badHitPunish.setBounds(new Rectangle(new Point(80, 240), badHitPunish.getPreferredSize()));

        //---- combo ----
        combo.setText("Combo \u989d\u5916\u52a0\u5206");
        combo.setSelected(true);
        contentPane.add(combo);
        combo.setBounds(new Rectangle(new Point(80, 270), combo.getPreferredSize()));

        //---- label2 ----
        label2.setText("\u6ce8\uff1a\u5173\u95ed Combo \u989d\u5916\u52a0\u5206\uff0c\u5219 Combo \u529f\u80fd\u4f1a\u5168\u90e8\u7981\u7528");
        contentPane.add(label2);
        label2.setBounds(84, 300, 299, 19);

        //---- startGame ----
        startGame.setText("\u5f00\u59cb\u6e38\u620f");
        startGame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
        contentPane.add(startGame);
        startGame.setBounds(520, 365, 122, 34);

        //---- label3 ----
        label3.setText("\u5386\u53f2\u6700\u9ad8\u5206\uff1a");
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(407, 155), label3.getPreferredSize()));

        //---- label4 ----
        label4.setText("\u60a8\u7684\u540d\u5b57\u662f\uff1a");
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(407, 120), label4.getPreferredSize()));
        contentPane.add(myName);
        myName.setBounds(482, 115, 180, myName.getPreferredSize().height);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(historyList);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(482, 155, 180, 180);

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
        setSize(780, 480);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    public void flushRanks() {
        DefaultListModel<String> dlm = new DefaultListModel<>();
        Map<String, Integer> middleware = new HashMap<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> objectEntry : ConfigUtil.get().node("score-history").childrenMap().entrySet()) {
            middleware.put((String) objectEntry.getKey(), objectEntry.getValue().getInt());
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(middleware.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        list.forEach((v) -> dlm.addElement(v.getKey() + " : " + v.getValue()));
        this.historyList.setModel(dlm);
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
