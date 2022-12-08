package cn.edu.ytkj;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.swing.*;
import java.io.File;

/**
 * 配置文件工具类
 *
 * @author 徐善俊
 */
public class ConfigUtil {
    // YAML 是一个文件格式
    // YAML 配置文件加载器
    private static final YamlConfigurationLoader loader = YamlConfigurationLoader.builder() // 创建一个 YAML 配置文件加载器构建器
            // 构建器相当于建楼用的吊机
            .path(new File("config.yml").toPath()) // 创建一个文件对象.toPath 转换为 Path 路径
            .build();
    // 配置文件根节点
    private static ConfigurationNode root;

    // 只调用一次
    static {
        loadConfig();
    }

    public static ConfigurationNode get() {
        return root;
    }

    /**
     * 加载配置文件
     */
    private static void loadConfig() {
        try {
            // 使用 YAML 配置文件加载器加载配置文件并赋值给根节点变量
            root = loader.load();
        } catch (ConfigurateException e) {
            JOptionPane.showMessageDialog(null, "配置文件读取失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存配置文件
     */
    public static void saveConfig() {
        try {
            // 将 root 节点的所有内容通过 YAML 配置文件加载器保存到文件中
            loader.save(root);
        } catch (ConfigurateException e) {
            JOptionPane.showMessageDialog(null, "配置文件保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
