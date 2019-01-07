package com.paritytrading.nassau.binaryfile;

import static com.paritytrading.nassau.binaryfile.BinaryFILEStatus.*;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

import com.paritytrading.nassau.Messages;
import com.paritytrading.nassau.Strings;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BinaryFILEReaderTest {

    private InputStream stream;

    private Messages<String> messages;

    @Before
    public void setUp() throws Exception {
        stream = getClass().getResourceAsStream("/binaryfile.dat");

        messages = new Messages<>(Strings.MESSAGE_PARSER);
    }

    @After
    public void tearDown() throws Exception {
        stream.close();
    }

    @Test
    public void readStream() throws Exception {
        try (BinaryFILEReader reader = new BinaryFILEReader(stream, messages)) {
            while (reader.read() >= 0);
        }

        assertEquals(asList("foo", "bar", "baz", "quux", ""), messages.collect());
    }

    @Test
    public void readStreamWithStatusListener() throws Exception {
        BinaryFILEStatus status = new BinaryFILEStatus();

        BinaryFILEStatusParser parser = new BinaryFILEStatusParser(messages, status);

        try (BinaryFILEReader reader = new BinaryFILEReader(stream, parser)) {
            while (reader.read() >= 0);
        }

        assertEquals(asList("foo", "bar", "baz", "quux"), messages.collect());
        assertEquals(asList(new EndOfSession()), status.collect());
    }

}
