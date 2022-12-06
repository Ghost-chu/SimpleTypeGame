package cn.edu.ytkj;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.spongepowered.configurate.ConfigurationNode;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Bootstrap {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Bootstrap::errorHooker);
        // 加载额外 JVM 属性
        loadExtraJvmProperties();
        // 加载音频播放器
        AudioUtil.init();
        // 加载 FlatDarculaLaf 配色
        FlatDarculaLaf.setup();
        // 检查硬件加速未启用导致的闪烁
        if (!"true".equalsIgnoreCase(System.getProperty("sun.java2d.opengl"))) {
            JOptionPane.showMessageDialog(null, "不启用硬件加速将会导致 Canvas 画布在绘制字母时出现闪烁问题，请转到 DEBUG 选项卡打勾 `java2d.opengl` 并重启应用程序以使硬件加速生效。", "硬件加速未启用", JOptionPane.WARNING_MESSAGE);
        }
        // 初始化主界面
        new GameMainFrame().setVisible(true);

    }


    public static void loadExtraJvmProperties() {
        ConfigurationNode node = ConfigUtil.get().node("jvm-extra");
        if (!node.node("opengl").isNull())
            System.setProperty("sun.java2d.opengl", String.valueOf(node.node("opengl").getBoolean()));
        if (!node.node("aatext").isNull())
            System.setProperty("swing.aatext", String.valueOf(node.node("aatext").getBoolean()));
        if (!node.node("java2dd3d").isNull())
            System.setProperty("sun.java2d.d3d", String.valueOf(node.node("java2dd3d").getBoolean()));
        if (!node.node("nativeDoubleBuffering").isNull())
            System.setProperty("awt.nativeDoubleBuffering", String.valueOf(node.node("nativeDoubleBuffering").getBoolean()));
        if (!node.node("java2dnoddraw").isNull())
            System.setProperty("sun.java2d.noddraw", String.valueOf(node.node("java2dnoddraw").getBoolean()));
        if (!node.node("java2dddscale").isNull())
            System.setProperty("sun.java2d.ddscale", String.valueOf(node.node("java2dddscale").getBoolean()));
    }

    public static void errorHooker(Thread thread, Throwable e) {
        Throwable parent = e;
        // 取真实原因
        while (parent.getCause() != null) {
            parent = parent.getCause();
        }
        // 新建一个 ByteArray 的输出流
        OutputStream out = new ByteArrayOutputStream();
        // 重定向错误到输出流
        PrintStream printStream = new PrintStream(out);
        parent.printStackTrace();
        parent.printStackTrace(printStream);
        printStream.flush();
        // 转换为文本
        String errorMessage = out.toString();
        errorMessage = errorMessage.trim();
        String[] arrays = errorMessage.split("\n");
        StringBuilder builder = new StringBuilder();
        int loop = Math.min(15, arrays.length);
        for (int i = 0; i < loop; i++) {
            builder.append(arrays[i]).append("\n");
        }
        if (arrays.length > loop) {
            builder.append("省略了 ").append(arrays.length - loop).append(" 行未被显示，请检查控制台输出...");
        }
        int response = JOptionPane.showConfirmDialog(null, "Java 打字青春版在线程 " + thread.getName() + " 上出现了一个异常:\n\n" + builder + "\n\n是否忽略错误继续运行？如果课堂演示请点击“否”并重启程序，不然可能会翻车。", "Java 打字青春版", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        // 0=是 1=否
        if (response == 1) {
            JOptionPane.showMessageDialog(null, "由于遇到非预期的运行时错误，Java 青春打字版程序即将退出。", "Java 打字青春版", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        } else {
            JOptionPane.showMessageDialog(null, "已忽略运行时错误，这可能会导致一些异常，但程序将会继续尝试运行。", "Java 打字青春版", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
