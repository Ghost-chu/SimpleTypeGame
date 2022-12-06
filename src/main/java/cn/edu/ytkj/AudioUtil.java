package cn.edu.ytkj;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AudioUtil {
    private static final Random RANDOM = new Random();
    private static final Map<String, Clip> audioMap = new HashMap<>();

    static {
        register("click.wav");
        register("click2.wav");
        register("combo-break.wav");
        register("combo-created.wav");
        register("game-end.wav");
        register("welcome.wav");
    }

    public static void init() {

    }

    public static void register(String fileName) {
        try (InputStream stream = AudioUtil.class.getResourceAsStream("/" + fileName)) {
            if (stream == null) throw new IllegalArgumentException("File not exists!");
            Clip clip = AudioSystem.getClip();
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
