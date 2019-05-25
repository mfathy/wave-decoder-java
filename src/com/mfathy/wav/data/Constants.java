package com.mfathy.wav.data;

/**
 * Created by Mohammed Fathy @ 20/05/2019
 * dev.mfathy@gmail.com
 *
 * Solution constants.
 */
public interface Constants {

    /**
     * Wav file names.
     */
    String[] FILENAMES = {"res/file_1.wav", "res/file_2.wav", "res/file_3.wav"};

    /**
     * Wav file start index of data, after bypassing header information.
     * This index is identified after reading sample data for the wav file.
     */
    int WAV_START_DATA_INDEX = 11;

    /**
     * Number of bytes of 1 message used for encoding.
     * 30 message for message itself, the last one for its checksum.
     */
    int MESSAGE_BYTE_COUNT = 31;

    /**
     * Pulse threshold is used to only detect steep edges belonging to pulses with a certain amplitude.
     */
    int PULSE_THRESHOLD = 1300;

    /**
     * Last byte value after decoding 64 messages.
     */
    int LAST_BYTE_VALUE = 0x00;

    /**
     * Total messages count.
     */
    int MESSAGES_COUNT = 64;

    /**
     * 1st byte value before decoding messages.
     */
    byte START_BYTE_VALUE_1 = 0x42;

    /**
     * 2nd byte value before decoding messages.
     */
    byte START_BYTE_VALUE_2 = 0x03;


    /**
     * One signals is half dividend of zero signals time, so when classifying pulses
     * One signals should return a value between 0.45 and 0.55 to detect it.
     */
    double PULSE_0_45_DIVIDEND = 0.45;
    double PULSE_0_55_DIVIDEND = 0.55;

    /**
     * Zero signals is 2 times of one signals.
     */
    double PULSE_2_0_DIVIDEND = 2.0;
}
