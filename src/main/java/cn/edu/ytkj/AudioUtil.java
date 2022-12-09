package cn.edu.ytkj;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 音频工具
 *
 * @author 魏传烁
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

    }

    /**
     * 注册音频文件为 Clip 并放置到 Map 中
     *
     * @param fileName 文件名，放在项目 resources 文件夹下，会自动添加 / 前缀
     */
    public static void register(String fileName) {
        try (InputStream s = AudioUtil.class.getResourceAsStream("/" + fileName)) {
            if (s == null)
                throw new IllegalArgumentException("需要注册的文件没有放置在 resources 文件夹下或者没有打包到 JAR 中!");
            BufferedInputStream bf = new BufferedInputStream(s);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(getAudioFormat(), AudioSystem.getAudioInputStream(bf)));
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

    /**
     * 提供指定音频格式
     * 从网上复制的
     *
     * @return 音频格式类
     */
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
