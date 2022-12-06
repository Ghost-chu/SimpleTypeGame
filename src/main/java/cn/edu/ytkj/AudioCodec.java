package cn.edu.ytkj;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class AudioCodec {

    public static void playMPEG3(InputStream s) throws UnsupportedAudioFileException, IOException {
        MpegAudioFileReader mp = new MpegAudioFileReader();
        AudioInputStream stream = mp.getAudioInputStream(s);
        AudioFormat baseFormat = stream.getFormat();
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        stream = AudioSystem.getAudioInputStream(format, stream);
        AudioFormat target = stream.getFormat();
        DataLine.Info dinfo = new DataLine.Info(SourceDataLine.class, target, AudioSystem.NOT_SPECIFIED);
        SourceDataLine line;
        int len;
        try {
            line = (SourceDataLine) AudioSystem.getLine(dinfo);
            line.open(target);
            line.start();
            byte[] buffer = new byte[1024];
            while ((len = stream.read(buffer)) > 0) {
                line.write(buffer, 0, len);
            }
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            stream.close();
        }
    }
}
