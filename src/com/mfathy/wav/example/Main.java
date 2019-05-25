package com.mfathy.wav.example;

import com.badlogic.audio.io.EndianDataInputStream;
import com.mfathy.wav.data.Constants;
import com.mfathy.wav.data.WaveFormat;
import com.mfathy.wav.decoder.AFSKDecoder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mohammed Fathy @ 23/05/2019
 * dev.mfathy@gmail.com
 */
public class Main {

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < Constants.FILENAMES.length; i++) {
            String filename = Constants.FILENAMES[i];

            System.out.println(String.format("Decoding %s ...\n", filename));

            File file = new File(filename);
            WaveFormat waveFormat = WaveFormat.readWaveFormat(file);
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file), waveFormat.getNumBytes());
            EndianDataInputStream edis = new EndianDataInputStream(bin);

            AFSKDecoder decoder = new AFSKDecoder(waveFormat, edis);

            start(decoder);

            System.out.println("\n\n-------------------------------------------\n");
        }



    }

    private static void start(AFSKDecoder decoder) {
        //  Read all sample data from wav file.
        int[] samples = decoder.readAudioSamples(
                new int[decoder.getWaveFormat().getFrameLength()],
                decoder.getWaveFormat().getFormat().getChannels(),
                decoder.getInputStream()
        );

        //  Calculate pulse widths.
        List<Integer> pulseWidths = decoder.calculatePulseWidths(samples);

        //  Classify pulse widths
        Map<Integer, Set<Integer>> classifiedPulses = decoder.classifyPulseWidths(pulseWidths);

        //  Build Bit stream String representations.
        decoder.buildBitStreamRepresentation(pulseWidths, classifiedPulses);
    }
}
