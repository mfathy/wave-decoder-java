package com.mfathy.wav.data;

/**
 * Created by Mohammed Fathy @ 24/05/2019
 * dev.mfathy@gmail.com
 *
 * Message class that should be decoded from the audio file.
 */
public class Message {

    /**
     * Bytes of 1 message.
     */
    private byte[] messageBytes;

    /**
     * Counter for messages, that will be decoded.
     * It's also used as a flag to stop the decoding process.
     */
    private int messagesCount;

    /**
     * Bytes counter, used to construct a message.
     */
    private int bytesCount;

    /**
     * Start indicator flags to indicate when to start decoding messages.
     */
    private boolean[] start;

    /**
     * Byte value itself.
     */
    private byte byteValue;

    public Message(
            byte[] byteMessages,
            int bytesCount,
            int messagesCount,
            boolean[] start,
            byte byteValue
    ) {
        this.messageBytes = byteMessages;
        this.bytesCount = bytesCount;
        this.messagesCount = messagesCount;
        this.start = start;
        this.byteValue = byteValue;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    public int getBytesCount() {
        return bytesCount;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public boolean[] getStart() {
        return start;
    }

    /**
     * Decode the message and print its binary content.
     * @return a decoded message.
     */
    public Message decode() {
        //  Check if you found all start bytes.
        if (start[0] && start[1]) {

            //  Construct the message by adding its bytes.
            messageBytes[bytesCount] = byteValue;
            bytesCount++;

            //  If the messages bytes all filled successfully, then print. reset and increase messagesCount.
            if (bytesCount == Constants.MESSAGE_BYTE_COUNT) {
                printMessage(messageBytes);
                resetMessage();
                messagesCount++;
            }
        }

        //  Search for start bytes.
        start = isFirstBytesFound(start, byteValue);
        return this;
    }

    /**
     * Prints the message binary representation as a test.
     * @param messageBytes to be printed.
     */
    public void printMessage(byte[] messageBytes) {
        System.out.print(new String(messageBytes, 0, 30));
    }

    /**
     * Resets the message itself & byte counter.
     */
    public void resetMessage() {
        bytesCount = 0;
        messageBytes = new byte[Constants.MESSAGE_BYTE_COUNT];
    }

    /**
     * Checks if byteValue is equal to any start byte value.
     * @param start flags to indicate both start bytes.
     * @param byteValue to be checked.
     * @return an updated value of start flags.
     */
    boolean[] isFirstBytesFound(boolean[] start, byte byteValue) {
        if (byteValue == Constants.START_BYTE_VALUE_1) {
            start[0] = true;
        }

        if (byteValue == Constants.START_BYTE_VALUE_2) {
            start[1] = true;
        }
        return start;
    }
}
