package com.mfathy.wav.decoder;

import com.badlogic.audio.io.EndianDataInputStream;
import com.mfathy.wav.data.Constants;
import com.mfathy.wav.data.WaveFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Mohammed Fathy @ 25/05/2019
 * dev.mfathy@gmail.com
 */
public class AFSKDecoderTest {

    private AFSKDecoder decoder;

    @Before
    public void setUp() throws Exception {
        File file = new File(Constants.FILENAMES[0]);
        WaveFormat waveFormat = WaveFormat.readWaveFormat(file);
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file), waveFormat.getNumBytes());
        EndianDataInputStream edis = new EndianDataInputStream(bin);

        decoder = new AFSKDecoder(waveFormat, edis);
    }

    @Test
    public void testClassifyPulseWidthsSucceed() {
        List<Integer> pulseList = new ArrayList<>();
        pulseList.add(13);
        pulseList.add(14);
        pulseList.add(28);
        Map<Integer, Set<Integer>> integerSetMap = decoder.classifyPulseWidths(pulseList);

        assertTrue(integerSetMap.get(1).contains(13));
        assertTrue(integerSetMap.get(1).contains(14));
        assertTrue(integerSetMap.get(0).contains(28));
    }

    @Test
    public void testClassifyPulseWidthsSkipBaseConditions() {
        List<Integer> pulseList = new ArrayList<>();
        pulseList.add(1);
        pulseList.add(1);
        Map<Integer, Set<Integer>> integerSetMap = decoder.classifyPulseWidths(pulseList);

        assertEquals(integerSetMap.get(0).size(), 0);
        assertEquals(integerSetMap.get(1).size(), 0);
    }

    @Test
    public void testCalculatePulseWidthsSucceed() {
        //  this should has 1 pulse with width of 6.
        int[] samples = {
                        0,0,0,0,0,0,0,0,0,0,0,
                        -1000,-1000,-1000,-1000,-1000,-1000,
                        1000,1000,1000,1000,1000,1000,1000};

        List<Integer> pulses = decoder.calculatePulseWidths(samples);
        assertTrue(pulses.get(0).equals(6));
    }


    @Test
    public void testGetByteValueFromStringSuccess() {
        byte value = decoder.getByteValueFromString("01100000011");
        assertEquals(value, 3);
    }
}