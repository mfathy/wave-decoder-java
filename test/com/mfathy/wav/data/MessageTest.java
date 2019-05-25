package com.mfathy.wav.data;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Mohammed Fathy @ 25/05/2019
 * dev.mfathy@gmail.com
 * <p>
 * Message Unit testing.
 */
public class MessageTest {

    @Test
    public void testDecodePrintsTheMessage() {
        Message message = createMessage();
        message.isFirstBytesFound(message.getStart(), Constants.START_BYTE_VALUE_1);
        message.isFirstBytesFound(message.getStart(), Constants.START_BYTE_VALUE_2);

        message.decode();

        assertEquals(message.getBytesCount(), 0);
        assertEquals(message.getMessagesCount(), 2);
    }

    @Test
    public void testResetMessageResetsTheMessage() {
        Message message = createMessage();
        message.resetMessage();

        assertEquals(message.getBytesCount(), 0);
        assertEquals(message.getMessageBytes()[0], 0);
    }

    @Test
    public void testIsFirstBytesFoundDetectsFirstStartByte() {
        Message message = createMessage();
        message.isFirstBytesFound(message.getStart(), Constants.START_BYTE_VALUE_1);

        assertTrue(message.getStart()[0]);
        assertFalse(message.getStart()[1]);
    }

    @Test
    public void testIsFirstBytesFoundDetectsSecondStartByte() {
        Message message = createMessage();
        message.isFirstBytesFound(message.getStart(), Constants.START_BYTE_VALUE_2);

        assertTrue(message.getStart()[1]);
        assertFalse(message.getStart()[0]);
    }

    private Message createMessage() {
        byte[] byteMessage = {73, 116, 32, 119, 97, 115, 32, 116, 104, 101, 32, 87, 104, 105, 116, 101, 32, 82, 97, 98, 98, 105, 116, 44, 32, 116, 114, 111, 116, 116, 0};
        int byteCount = 30;
        int messageCount = 1;
        boolean[] start = {false, false};
        byte byteValue = -89;
        return new Message(
                byteMessage,
                byteCount,
                messageCount,
                start,
                byteValue
        );
    }
}