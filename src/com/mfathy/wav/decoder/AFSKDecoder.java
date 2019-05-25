package com.mfathy.wav.decoder;


import com.badlogic.audio.io.EndianDataInputStream;
import com.mfathy.wav.data.Constants;
import com.mfathy.wav.data.Message;
import com.mfathy.wav.data.WaveFormat;

import java.util.*;

/**
 * Created by Mohammed Fathy @ 10/05/2019
 * dev.mfathy@gmail.com
 *
 * AFSK decoder class.
 */
public class AFSKDecoder {

    //  Variables
    private final WaveFormat waveFormat;
    private final EndianDataInputStream endianInputStream;

    public AFSKDecoder(WaveFormat wf, EndianDataInputStream in) {
        waveFormat = wf;
        endianInputStream = in;
    }

    /**
     * Read wav file / audio file sample data at once.
     * @param samples of the file.
     * @param channels number of channels per audio file.
     * @param in input stream used to read sample data.
     * @return an updated version of sample data array.
     */
    public int[] readAudioSamples(int[] samples, int channels, EndianDataInputStream in) {
        try {
            for (int streamIndex = 0; streamIndex < samples.length; streamIndex++) {
                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {
                    samples[streamIndex] = in.readShortLittleEndian();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return samples;
    }

    /**
     * Classifies signal pulse widths by their length, this is based on the mentioned formula:
     * A one signal is a rectangle signal of t = 320 microseconds
     * A zero signal is a rectangle signal of t = 640 microseconds
     * A one signal is half length of zero signal.
     * @param pulseList to classify its widths.
     * @return a map of a classified lengths.
     */
    public Map<Integer, Set<Integer>> classifyPulseWidths(List<Integer> pulseList) {

        Set<Integer> oneSignalPulses = new HashSet<>();
        Set<Integer> zeroSignalPulses = new HashSet<>();

        Set<Integer> pulseSet = new HashSet<>(pulseList);

        for (Integer firstInteger : pulseSet) {

            //  Base condition #1: ignore all smaller pulses.
            if (firstInteger.equals(1))
                continue;
            for (Integer secondInteger : pulseSet) {

                //  Base condition #2: exit if the pulse is already exists.
                if (oneSignalPulses.contains(firstInteger) || zeroSignalPulses.contains(firstInteger))
                    break;

                //  Base condition #1: skip all smaller pulses.
                if (secondInteger.equals(1))
                    continue;

                //  Base condition #3: skip equal pulses.
                if (firstInteger.equals(secondInteger))
                    continue;

                //  One signal is half length of Zero signal.
                if (firstInteger / (float) secondInteger < Constants.PULSE_0_55_DIVIDEND
                        && firstInteger / (float) secondInteger > Constants.PULSE_0_45_DIVIDEND) {
                    oneSignalPulses.add(firstInteger);
                    zeroSignalPulses.add(secondInteger);
                }

                //  Zero signal is 2 times length of One signal.
                if (firstInteger / (float) secondInteger >= Constants.PULSE_2_0_DIVIDEND) {
                    oneSignalPulses.add(secondInteger);
                    zeroSignalPulses.add(firstInteger);
                }
            }
        }

        Map<Integer, Set<Integer>> classifiedPulses = new HashMap<>();
        classifiedPulses.put(1, oneSignalPulses);
        classifiedPulses.put(0, zeroSignalPulses);

        return classifiedPulses;
    }

    /**
     * Calculate pulse widths which creates the signal waveform.
     * This is done by looking at the distance between two zero crossing
     * Which creates a single bit.
     * @param samples to calculate their pulse widths.
     * @return a list of signal pulses.
     */
    public List<Integer> calculatePulseWidths(int[] samples) {
        int pulseWidth = 0;
        List<Integer> pulseWidths = new ArrayList<>();
        for (int sampleIndex = Constants.WAV_START_DATA_INDEX; sampleIndex < samples.length; sampleIndex++) {
            pulseWidth++;

            int nextSampleIndex = sampleIndex + 1;
            if (nextSampleIndex == samples.length)
                break;

            int a = samples[sampleIndex];
            int b = samples[nextSampleIndex];

            //  Detects above zero signal falling down the center line.
            if (a - b > Constants.PULSE_THRESHOLD && a >= 0 && b < 0) {
                pulseWidths.add(pulseWidth);
                pulseWidth = 0;
            }

            //  Detects below zero signal heading up the center line.
            if (b - a > Constants.PULSE_THRESHOLD && b >= 0 && a < 0) {
                pulseWidths.add(pulseWidth);
                pulseWidth = 0;
            }

        }

        return pulseWidths;
    }

    /**
     * Build a bit steam string representation and byte message, then decode the message.
     * @param pulseWidths that constructs the bit stream.
     * @param classifiedPulses a map to handle the classification.
     */
    public void buildBitStreamRepresentation(List<Integer> pulseWidths, Map<Integer, Set<Integer>> classifiedPulses) {

        StringBuilder bitBuilder = new StringBuilder();

        byte[] byteMessage = new byte[Constants.MESSAGE_BYTE_COUNT];
        int byteCount = 0;
        int messageCount = 0;
        boolean[] start = {false, false};

        for (Integer pulse : pulseWidths) {

            //  Classify pulse widths.
            if (classifiedPulses.get(1).contains(pulse)) {
                bitBuilder.append("1");
            } else if (classifiedPulses.get(0).contains(pulse)) {
                bitBuilder.append("0");
            }

            //  Reset bit builder & create a new one.
            if (bitBuilder.length() == 11) {
                byte byteValue = getByteValueFromString(bitBuilder.toString());

                //  If decoded all messages and found the last byte, then end decoding process.
                if (messageCount == Constants.MESSAGES_COUNT && byteValue == Constants.LAST_BYTE_VALUE) {
                    break;
                }

                //  Decode message
                Message message = new Message(byteMessage, byteCount, messageCount, start, byteValue).decode();

                //  Continue previous decoding session.
                messageCount = message.getMessagesCount();
                byteMessage = message.getMessageBytes();
                byteCount = message.getBytesCount();
                start = message.getStart();

                //  Reset bit builder
                bitBuilder = new StringBuilder();
            }
        }
    }

    /**
     * Convert a 11 bits string to 8 bits string in a byte after reversing it as the MSB is coming last.
     * @param byte11Bits to be converted.
     * @return a byte after conversion.
     */
    public byte getByteValueFromString(String byte11Bits) {
        StringBuilder byte8Bits = new StringBuilder(byte11Bits.substring(1, 9)).reverse();
        int byteIntValue = Integer.parseInt(byte8Bits.toString(), 2);
        return (byte) byteIntValue;
    }

    /**
     * @return a waveFormat object.
     */
    public WaveFormat getWaveFormat() {
        return waveFormat;
    }

    /**
     * @return a endian input stream.
     */
    public EndianDataInputStream getInputStream() {
        return endianInputStream;
    }
}
