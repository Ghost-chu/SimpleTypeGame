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
        loadExtraJvmProperties();
        AudioUtil.init();
        FlatDarculaLaf.setup();
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
        while (parent.getCause() != null) {
            parent = parent.getCause();
        }
        OutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        parent.printStackTrace();
        parent.printStackTrace(printStream);
        printStream.flush();
        String errorMessage = out.toString();
        errorMessage = errorMessage.trim();
        String[] arrays = errorMessage.split("\n");
        StringBuilder builder = new StringBuilder();
        int loop = Math.min(15, arrays.length);
        for (int i = 0; i < loop; i++) {
            builder.append(arrays[i]).append("\n");
        }
        if (arrays.length > loop) {
            builder.append("????????? ").append(arrays.length - loop).append(" ??????????????????????????????????????????...");
        }
        int response = JOptionPane.showConfirmDialog(null, "Java ???????????????????????? " + thread.getName() + " ????????????????????????:\n\n" + builder + "\n\n???????????????????????????????????????????????????????????????????????????????????????????????????????????????", "Java ???????????????", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
        if (response == 1) {
            JOptionPane.showMessageDialog(null, "??????????????????????????????????????????Java ????????????????????????????????????", "Java ???????????????", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
        } else {
            JOptionPane.showMessageDialog(null, "????????????????????????????????????????????????????????????????????????????????????????????????", "Java ???????????????", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
