package com.mfathy.wav.data;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mohammed Fathy @ 20/05/2019
 * dev.mfathy@gmail.com
 */
public class WaveFormat {
    private int byteLength;
    private int frameLength;
    private AudioFormat format;

    /**
     * @return number of bytes per frame.
     */
    public int getNumBytes() {
        return 1024 * format.getFrameSize();
    }

    /**
     * Reads wav file header and construct a readable wav format object.
     * @param file to read its header.
     * @return an updated {@link WaveFormat} object.
     */
    public static WaveFormat readWaveFormat(File file) {
        WaveFormat wf = new WaveFormat();
        try {
            AudioFileFormat format = AudioSystem.getAudioFileFormat(file);
            wf.setByteLength(format.getByteLength());
            wf.setFrameLength(format.getFrameLength());
            wf.setFormat(format.getFormat());
        } catch (UnsupportedAudioFileException | IOException e) {
            System.err.println("Unable to read file info");
        }
        return wf;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getByteLength() {
        return byteLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    public AudioFormat getFormat() {
        return format;
    }
}
