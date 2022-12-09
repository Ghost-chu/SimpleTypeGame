/*
 * Created by JFormDesigner on Tue Dec 06 08:39:39 CST 2022
 */

package cn.edu.ytkj;

import com.sun.management.HotSpotDiagnosticMXBean;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import javax.management.MBeanServer;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.*;
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
    private JButton startGame;
    private JLabel label3;
    private JLabel label4;
    private JTextField myName;
    private JScrollPane scrollPane1;
    private JList historyList;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JCheckBox negativeFailure;
    private JCheckBox combo;
    private JCheckBox badHitPunish;
    private JCheckBox textRandomColor;
    private JCheckBox textReDown;
    private JComboBox speedSelector;
    private JLabel diffselect;
    private JCheckBox focusMode;
    private JCheckBox blindMode;
    private JPanel panel2;
    private JLabel label5;
    private JTextField maxChars;
    private JLabel label6;
    private JTextField punishScore;
    private JPanel panel3;
    private JPanel panel4;
    private JButton dbg_testexception;
    private JButton outputConfPath;
    private JButton outputConfMem;
    private JButton dbg_VM;
    private JButton dumpHeap;
    private JCheckBox dbg_java2d_opengl;
    private JCheckBox dbg_swing_aatext;
    private JCheckBox dbg_java2d_d3d;
    private JCheckBox dbg_awt_nativedoublebuffering;
    private JCheckBox dbg_java2d_noddraw;
    private JCheckBox dbg_java2d_ddscale;

    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public GameMainFrame() {
        initComponents();
        initDefaultValues();
    }

    /**
     * 初始化默认值
     */
    private void initDefaultValues() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.speedSelector.addItem("有手就行");
        this.speedSelector.addItem("非常慢");
        this.speedSelector.addItem("慢");
        this.speedSelector.addItem("正常");
        this.speedSelector.addItem("快");
        this.speedSelector.addItem("非常快");
        this.speedSelector.addItem("★中国★");
        loadConf();
        this.startGame.addActionListener((v) -> {
            try {
                saveConf();
                Object selectedItem = speedSelector.getSelectedItem();
                if (!(selectedItem instanceof String))
                    selectedItem = "正常";
                new GameCanvasFrame((String) selectedItem, textReDown.isSelected(),
                        textRandomColor.isSelected(), badHitPunish.isSelected(), combo.isSelected(),
                        myName.getText(), Integer.parseInt(this.maxChars.getText()),
                        Integer.parseInt(this.punishScore.getText()), this.negativeFailure.isSelected(),
                        this, focusMode.isSelected(), blindMode.isSelected()).setVisible(true);
                AudioUtil.playGameStart();
            } catch (SerializationException e) {
                throw new RuntimeException(e);
            }
        });
        // DEBUG 代码段 开始
        this.dbg_testexception.addActionListener((v) -> {
            Thread thread = new Thread(() -> {
                throw new RuntimeException("DEBUG - 测试异常");
            });
            thread.setName("异常测试线程");
            thread.setDaemon(true);
            thread.start();
        });
        this.outputConfPath.addActionListener((v) -> {
            JOptionPane.showMessageDialog(this, "配置文件当前保存在: \n" + new File("config.yml").getAbsolutePath() + "\n点击确定在资源管理器中打开。", "配置文件路径", JOptionPane.INFORMATION_MESSAGE);
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + new File("config.yml").getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.outputConfMem.addActionListener((v) -> JOptionPane.showMessageDialog(this, ConfigUtil.get().raw(), "内存配置文件", JOptionPane.INFORMATION_MESSAGE));
        this.dbg_VM.addActionListener((v) -> {
            StringBuilder builder = new StringBuilder();
            MemoryUsage usage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            builder.append("Memory: \n\tIn-heap(init/used/max/committed): ").append(usage.getInit()).append("/").append(usage.getUsed()).append("/").append(usage.getMax()).append("/").append(usage.getCommitted()).append("\n");
            usage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
            builder.append("\tOff-heap(init/used/max/committed): ").append(usage.getInit()).append("/").append(usage.getUsed()).append("/").append(usage.getMax()).append("/").append(usage.getCommitted()).append("\n");
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            builder.append("Runtime: \n\tName: ").append(runtimeBean.getName()).append(", VM: ").append(runtimeBean.getVmName()).append(", Vendor: ").append(runtimeBean.getVmVendor()).append(", Version: ").append(runtimeBean.getVmVersion()).append("\n");
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            builder.append("OS: \n\tName: ").append(osBean.getName()).append(", Arch: ").append(osBean.getArch()).append(", Version: ").append(osBean.getVersion()).append(", Processors: ").append(osBean.getAvailableProcessors()).append(", Load Avg: ").append(osBean.getSystemLoadAverage()).append("\n");
            ThreadInfo[] threadInfos = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
            builder.append("Threads: \n");
            for (ThreadInfo threadInfo : threadInfos) {
                builder.append("\tId: ").append(threadInfo.getThreadId()).append(", Name: ").append(threadInfo.getThreadName()).append(", State: ").append(threadInfo.getThreadState().name()).append(", Suspended: ").append(threadInfo.isSuspended()).append("\n");
            }
            JOptionPane.showMessageDialog(this, builder.toString(), "虚拟机信息", JOptionPane.INFORMATION_MESSAGE);
        });
        this.dumpHeap.addActionListener((v) -> {
            File heapDump = new File("dump.hprof");
            if (heapDump.exists()) heapDump.delete();
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            HotSpotDiagnosticMXBean mxBean;
            try {
                mxBean = ManagementFactory.newPlatformMXBeanProxy(
                        server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
                mxBean.dumpHeap(heapDump.getPath(), true);
                JOptionPane.showMessageDialog(this, "转储存储在：" + heapDump.getAbsolutePath(), "堆转储", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "转储失败：" + e.getMessage(), "堆转储", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        List<JCheckBox> dbgBoxes = new ArrayList<>();
        dbgBoxes.add(dbg_swing_aatext);
        dbgBoxes.add(dbg_awt_nativedoublebuffering);
        dbgBoxes.add(dbg_java2d_ddscale);
        dbgBoxes.add(dbg_java2d_opengl);
        dbgBoxes.add(dbg_java2d_d3d);
        dbgBoxes.add(dbg_java2d_noddraw);
        dbgBoxes.forEach(box -> box.addActionListener((v) -> {
            JOptionPane.showMessageDialog(this, "对 DEBUG 菜单下的参数进行修改需要重启应用程序才能生效！", "需要重启", JOptionPane.WARNING_MESSAGE);
            try {
                saveConf();
                loadConf();
            } catch (SerializationException e) {
                JOptionPane.showMessageDialog(this, "保存配置文件失败: " + e.getMessage(), "无法保存配置文件", JOptionPane.ERROR_MESSAGE);
            }
        }));
        // DEBUG 代码段 结束
    }

    /**
     * 从配置文件加载值（或者提供默认值）
     */
    private void loadConf() {
        this.badHitPunish.setSelected(ConfigUtil.get().node("badHitPunish").getBoolean(true));
        this.speedSelector.setSelectedItem(ConfigUtil.get().node("speedSelector").getString("正常"));
        this.combo.setSelected(ConfigUtil.get().node("combo").getBoolean(true));
        this.myName.setText(ConfigUtil.get().node("name").getString("游客"));
        this.textReDown.setSelected(ConfigUtil.get().node("text-redown").getBoolean(false));
        this.textRandomColor.setSelected(ConfigUtil.get().node("text-randomcolor").getBoolean(true));
        this.negativeFailure.setSelected(ConfigUtil.get().node("negative-failure").getBoolean(true));
        this.punishScore.setText(String.valueOf(ConfigUtil.get().node("bad-hit-punish").getInt(5)));
        this.maxChars.setText(String.valueOf(ConfigUtil.get().node("max-chars").getInt(6)));
        this.blindMode.setSelected(ConfigUtil.get().node("blind-mode").getBoolean(false));
        this.focusMode.setSelected(ConfigUtil.get().node("focus-mode").getBoolean(true));
        this.dbg_swing_aatext.setSelected(Boolean.parseBoolean(System.getProperty("swing.aatext")));
        this.dbg_awt_nativedoublebuffering.setSelected(Boolean.parseBoolean(System.getProperty("awt.nativeDoubleBuffering")));
        this.dbg_java2d_ddscale.setSelected(Boolean.parseBoolean(System.getProperty("sun.java2d.ddscale")));
        this.dbg_java2d_opengl.setSelected(Boolean.parseBoolean(System.getProperty("sun.java2d.opengl")));
        this.dbg_java2d_d3d.setSelected(Boolean.parseBoolean(System.getProperty("sun.java2d.d3d")));
        this.dbg_java2d_noddraw.setSelected(Boolean.parseBoolean(System.getProperty("sun.java2d.noddraw")));
        if (dbg_java2d_opengl.isSelected()) {
            dbg_java2d_opengl.setForeground(Color.GREEN);
        } else {
            dbg_java2d_opengl.setForeground(Color.RED);
        }
        flushRanks();
    }

    /**
     * 保存配置文件
     *
     * @throws SerializationException 如果出现序列化失败错误时，抛出此异常
     */
    private void saveConf() throws SerializationException {
        ConfigUtil.get().node("badHitPunish").set(this.badHitPunish.isSelected());
        ConfigUtil.get().node("speedSelector").set(this.speedSelector.getSelectedItem());
        ConfigUtil.get().node("combo").set(this.combo.isSelected());
        ConfigUtil.get().node("name").set(this.myName.getText());
        ConfigUtil.get().node("text-redown").set(this.textReDown.isSelected());
        ConfigUtil.get().node("text-randomcolor").set(this.textRandomColor.isSelected());
        ConfigUtil.get().node("negative-failure").set(this.negativeFailure.isSelected());
        ConfigUtil.get().node("bad-hit-punish").set(Integer.parseInt(this.punishScore.getText()));
        ConfigUtil.get().node("max-chars").set(Integer.parseInt(this.maxChars.getText()));
        ConfigUtil.get().node("focus-mode").set(this.focusMode.isSelected());
        ConfigUtil.get().node("blind-mode").set(this.blindMode.isSelected());
        ConfigurationNode node = ConfigUtil.get().node("jvm-extra");
        node.node("opengl").set(dbg_java2d_opengl.isSelected());
        node.node("aatext").set(dbg_swing_aatext.isSelected());
        node.node("java2dd3d").set(dbg_java2d_d3d.isSelected());
        node.node("nativeDoubleBuffering").set(dbg_awt_nativedoublebuffering.isSelected());
        node.node("java2dnoddraw").set(dbg_java2d_noddraw.isSelected());
        node.node("java2dddscale").set(dbg_java2d_ddscale.isSelected());
        ConfigUtil.saveConfig();
        Bootstrap.loadExtraJvmProperties();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        title = new JLabel();
        startGame = new JButton();
        label3 = new JLabel();
        label4 = new JLabel();
        myName = new JTextField();
        scrollPane1 = new JScrollPane();
        historyList = new JList();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        negativeFailure = new JCheckBox();
        combo = new JCheckBox();
        badHitPunish = new JCheckBox();
        textRandomColor = new JCheckBox();
        textReDown = new JCheckBox();
        speedSelector = new JComboBox();
        diffselect = new JLabel();
        focusMode = new JCheckBox();
        blindMode = new JCheckBox();
        panel2 = new JPanel();
        label5 = new JLabel();
        maxChars = new JTextField();
        label6 = new JLabel();
        punishScore = new JTextField();
        panel3 = new JPanel();
        panel4 = new JPanel();
        dbg_testexception = new JButton();
        outputConfPath = new JButton();
        outputConfMem = new JButton();
        dbg_VM = new JButton();
        dumpHeap = new JButton();
        dbg_java2d_opengl = new JCheckBox();
        dbg_swing_aatext = new JCheckBox();
        dbg_java2d_d3d = new JCheckBox();
        dbg_awt_nativedoublebuffering = new JCheckBox();
        dbg_java2d_noddraw = new JCheckBox();
        dbg_java2d_ddscale = new JCheckBox();

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
        title.setBounds(245, 10, 274, 75);

        //---- startGame ----
        startGame.setText("\u5f00\u59cb\u6e38\u620f");
        startGame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
        startGame.setFocusCycleRoot(true);
        contentPane.add(startGame);
        startGame.setBounds(520, 360, 175, 45);

        //---- label3 ----
        label3.setText("\u5206\u6570\u6392\u884c\u699c\uff1a");
        label3.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(442, 155), label3.getPreferredSize()));

        //---- label4 ----
        label4.setText("\u60a8\u7684\u540d\u5b57\u662f\uff1a");
        label4.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(442, 120), label4.getPreferredSize()));
        contentPane.add(myName);
        myName.setBounds(517, 115, 180, myName.getPreferredSize().height);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(historyList);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(517, 155, 180, 180);

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {
                panel1.setLayout(null);

                //---- negativeFailure ----
                negativeFailure.setText("\u8d1f\u5206\u5931\u8d25");
                negativeFailure.setSelected(true);
                negativeFailure.setToolTipText("\u5f53\u5206\u6570 <0 \u540e\u7acb\u523b\u7ed3\u675f\u6e38\u620f\u5e76\u7ed3\u7b97");
                panel1.add(negativeFailure);
                negativeFailure.setBounds(10, 150, 116, 21);

                //---- combo ----
                combo.setText("Combo");
                combo.setSelected(true);
                combo.setToolTipText("\u542f\u7528 COMBO \u8fde\u51fb\u529f\u80fd\uff0c\u8fdb\u5165 COMBO \u72b6\u6001\u6bcf\u6b21\u8fde\u51fb\u989d\u5916 +5 \u5206\uff01");
                panel1.add(combo);
                combo.setBounds(10, 125, 116, 21);

                //---- badHitPunish ----
                badHitPunish.setText("\u9519\u6309\u60e9\u7f5a");
                badHitPunish.setSelected(true);
                badHitPunish.setToolTipText("\u6bcf\u6b21\u6309\u9519\u4e00\u4e2a\u5b57\u6bcd\u90fd\u4f1a\u62635\u5206\uff01");
                panel1.add(badHitPunish);
                badHitPunish.setBounds(10, 100, 130, 21);

                //---- textRandomColor ----
                textRandomColor.setText("\u6587\u5b57\u968f\u673a\u989c\u8272");
                textRandomColor.setSelected(true);
                textRandomColor.setToolTipText("R!G!B!");
                panel1.add(textRandomColor);
                textRandomColor.setBounds(10, 75, 125, 21);

                //---- textReDown ----
                textReDown.setText("\u6587\u5b57\u91cd\u65b0\u4e0b\u843d");
                textReDown.setToolTipText("\u5f53\u6587\u5b57\u6389\u51fa\u5c4f\u5e55\u540e\u91cd\u65b0\u56de\u5230\u9876\u90e8\uff0c\u4e0d\u4f1a\u6263\u5206");
                panel1.add(textReDown);
                textReDown.setBounds(10, 50, 130, 21);
                panel1.add(speedSelector);
                speedSelector.setBounds(55, 10, 116, 23);

                //---- diffselect ----
                diffselect.setText("\u96be\u5ea6\uff1a");
                diffselect.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
                diffselect.setHorizontalAlignment(SwingConstants.RIGHT);
                panel1.add(diffselect);
                diffselect.setBounds(-4, 7, 60, 28);

                //---- focusMode ----
                focusMode.setText("\u805a\u7126\u6a21\u5f0f");
                focusMode.setToolTipText("\u6700\u540e\u4e00\u4e2a\u5b57\u6bcd\u603b\u662f\u6bd4\u8f83\u5927");
                panel1.add(focusMode);
                focusMode.setBounds(new Rectangle(new Point(10, 174), focusMode.getPreferredSize()));

                //---- blindMode ----
                blindMode.setText("\u6700\u5f3a\u5927\u8111\u6a21\u5f0f");
                blindMode.setToolTipText("\u8003\u9a8c\u4f60\u7684\u8bb0\u5fc6\u529b\uff01\u9700\u8981\u6309\u4e0b\u7684\u5b57\u6bcd\u9760\u8fd1\u5e95\u90e8\u540e\u5c06\u88ab\u66ff\u6362\u4e3a\"?\"");
                panel1.add(blindMode);
                blindMode.setBounds(10, 200, 110, 22);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel1.getComponentCount(); i++) {
                        Rectangle bounds = panel1.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel1.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel1.setMinimumSize(preferredSize);
                    panel1.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("\u57fa\u672c\u8bbe\u7f6e", panel1);

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //---- label5 ----
                label5.setText("\u6700\u591a\u5b57\u7b26\u6570\u91cf\uff1a");
                label5.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
                label5.setHorizontalAlignment(SwingConstants.RIGHT);
                panel2.add(label5);
                label5.setBounds(10, 10, 130, 28);
                panel2.add(maxChars);
                maxChars.setBounds(144, 12, 101, 25);

                //---- label6 ----
                label6.setText("\u6309\u9519\u65f6\u60e9\u7f5a\u5206\u6570\uff1a");
                label6.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
                label6.setHorizontalAlignment(SwingConstants.RIGHT);
                panel2.add(label6);
                label6.setBounds(10, 43, 131, 28);
                panel2.add(punishScore);
                punishScore.setBounds(144, 45, 101, 25);

                //======== panel3 ========
                {
                    panel3.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for (int i = 0; i < panel3.getComponentCount(); i++) {
                            Rectangle bounds = panel3.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel3.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel3.setMinimumSize(preferredSize);
                        panel3.setPreferredSize(preferredSize);
                    }
                }
                panel2.add(panel3);
                panel3.setBounds(new Rectangle(new Point(135, 105), panel3.getPreferredSize()));

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel2.getComponentCount(); i++) {
                        Rectangle bounds = panel2.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel2.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel2.setMinimumSize(preferredSize);
                    panel2.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("\u9ad8\u7ea7\u8bbe\u7f6e", panel2);

            //======== panel4 ========
            {
                panel4.setLayout(null);

                //---- dbg_testexception ----
                dbg_testexception.setText("\u6d4b\u8bd5\u9519\u8bef\u62e6\u622a");
                panel4.add(dbg_testexception);
                dbg_testexception.setBounds(new Rectangle(new Point(10, 5), dbg_testexception.getPreferredSize()));

                //---- outputConfPath ----
                outputConfPath.setText("\u8f93\u51fa\u914d\u7f6e\u6587\u4ef6\u8def\u5f84");
                panel4.add(outputConfPath);
                outputConfPath.setBounds(new Rectangle(new Point(10, 40), outputConfPath.getPreferredSize()));

                //---- outputConfMem ----
                outputConfMem.setText("\u8f93\u51fa\u914d\u7f6e\uff08\u5185\u5b58\uff09");
                panel4.add(outputConfMem);
                outputConfMem.setBounds(new Rectangle(new Point(10, 75), outputConfMem.getPreferredSize()));

                //---- dbg_VM ----
                dbg_VM.setText("\u8c03\u8bd5 JVM \u865a\u62df\u673a");
                panel4.add(dbg_VM);
                dbg_VM.setBounds(new Rectangle(new Point(10, 110), dbg_VM.getPreferredSize()));

                //---- dumpHeap ----
                dumpHeap.setText("Heap \u5185\u5b58\u8f6c\u50a8");
                panel4.add(dumpHeap);
                dumpHeap.setBounds(new Rectangle(new Point(10, 145), dumpHeap.getPreferredSize()));

                //---- dbg_java2d_opengl ----
                dbg_java2d_opengl.setText("java2d.opengl");
                panel4.add(dbg_java2d_opengl);
                dbg_java2d_opengl.setBounds(165, 7, 115, 25);

                //---- dbg_swing_aatext ----
                dbg_swing_aatext.setText("swing.aatext");
                panel4.add(dbg_swing_aatext);
                dbg_swing_aatext.setBounds(165, 30, 125, 25);

                //---- dbg_java2d_d3d ----
                dbg_java2d_d3d.setText("java2d.d3d");
                panel4.add(dbg_java2d_d3d);
                dbg_java2d_d3d.setBounds(165, 54, 169, 25);

                //---- dbg_awt_nativedoublebuffering ----
                dbg_awt_nativedoublebuffering.setText("awt.nativeDoubleBuffering");
                panel4.add(dbg_awt_nativedoublebuffering);
                dbg_awt_nativedoublebuffering.setBounds(165, 77, 205, 25);

                //---- dbg_java2d_noddraw ----
                dbg_java2d_noddraw.setText("java2d.noddraw");
                dbg_java2d_noddraw.setAutoscrolls(true);
                panel4.add(dbg_java2d_noddraw);
                dbg_java2d_noddraw.setBounds(165, 99, 210, 30);

                //---- dbg_java2d_ddscale ----
                dbg_java2d_ddscale.setText("java2d.ddscale");
                dbg_java2d_ddscale.setAutoscrolls(true);
                panel4.add(dbg_java2d_ddscale);
                dbg_java2d_ddscale.setBounds(165, 125, 210, 30);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel4.getComponentCount(); i++) {
                        Rectangle bounds = panel4.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel4.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel4.setMinimumSize(preferredSize);
                    panel4.setPreferredSize(preferredSize);
                }
            }
            tabbedPane1.addTab("DEBUG", panel4);
        }
        contentPane.add(tabbedPane1);
        tabbedPane1.setBounds(20, 105, 380, 289);

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
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:off
    }

    /**
     * 更新排名数据
     */
    public void flushRanks() {
        DefaultListModel<String> dlm = new DefaultListModel<>();
        Map<String, Integer> middleware = new HashMap<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> objectEntry : ConfigUtil.get().node("score-history").childrenMap().entrySet()) {
            middleware.put((String) objectEntry.getKey(), objectEntry.getValue().getInt());
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(middleware.entrySet());
        // Value 倒序排序
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        list.forEach((v) -> dlm.addElement(v.getKey() + " : " + v.getValue()));
        this.historyList.setModel(dlm);
    }

}
