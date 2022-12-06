package cn.edu.ytkj;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.swing.*;
import java.io.File;

/**
 * 配置文件工具类
 *
 * @author Wang YiFei
 */
public class ConfigUtil {
    private static final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
            .path(new File("config.yml").toPath()) // Set where we will load and save to
            .build();
    private static ConfigurationNode root;

    static {
        loadConfig();
    }

    public static ConfigurationNode get() {
        return root;
    }

    private static void loadConfig() {
        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            JOptionPane.showMessageDialog(null, "配置文件读取失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void saveConfig() {
        try {
            loader.save(root);
        } catch (ConfigurateException e) {
            JOptionPane.showMessageDialog(null, "配置文件保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
