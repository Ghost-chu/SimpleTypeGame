package cn.edu.ytkj;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

public class Bootstrap {
    public static void main(String[] args) {
        // WangYi Fei - Setup JFlatLaf
        AudioUtil.init();
        FlatLightFlatIJTheme.setup();
        new GameMainFrame().setVisible(true);
    }
}
