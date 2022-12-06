package cn.edu.ytkj;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 音频工具
 *
 * @author Wang YiFei
 */
public class AudioUtil {
    /*
     * 音频注册表
     */
    private static final Map<String, Clip> audioMap = new HashMap<>();

    static {
        // 注册自带音效文件
        register("click.wav");
        register("click2.wav");
        register("combo-break.wav");
        register("combo-created.wav");
        register("game-end.wav");
        register("welcome.wav");
    }

    public static void init() {
        // 这是一个空方法，调用此空方法时，类加载器会初始化 static 代码段
    }

    /**
     * 注册音频文件为 Clip 并放置到 Map 中
     * @param fileName 文件名，放在项目 resources 文件夹下，会自动添加 / 前缀
     */
    public static void register(String fileName) {
        try (InputStream stream = AudioUtil.class.getResourceAsStream("/" + fileName)) {
            if (stream == null)
                throw new IllegalArgumentException("需要注册的文件没有放置在 resources 文件夹下或者没有打包到 JAR 中!");
            Clip clip = AudioSystem.getClip();
            // 使用 getAudioFormat 强制使用指定的位深度和波特率，避免出现流格式不兼容错误
            clip.open(AudioSystem.getAudioInputStream(getAudioFormat(), AudioSystem.getAudioInputStream(stream)));
            audioMap.put(fileName, clip);
            System.out.println("Audio " + fileName + " registered!");
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void play(String fileName) {
        Clip clip = audioMap.get(fileName);
        if (clip == null) throw new IllegalArgumentException("Audio " + fileName + " not registered!");
        clip.setFramePosition(0);
        clip.start();
    }

    private static AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = true;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public static void playClick() {
        play("click2.wav");

    }

    public static void playComboClick() {
        // play("click.wav");
        play("click2.wav");
    }

    public static void playComboCreated() {
        play("combo-created.wav");
    }

    public static void playComboBreaked() {
        play("combo-break.wav");
    }

    public static void playGameStart() {
        play("welcome.wav");
    }

    public static void playGameEnd() {
        play("game-end.wav");
    }
}
