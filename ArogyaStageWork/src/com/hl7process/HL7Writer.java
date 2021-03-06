package com.hl7process;


import java.io.IOException;

/**
 * Implemented by classes whose instances are capable of sending out HL7 messages.
 */
public interface HL7Writer {

    /**
     * Write a message.
     *
     * @throws IOException if there is some error
     * @throws IOException if {@link #close} has already been invoked
     */
    void writeMessage(HL7Message message) throws IOException;

    /**
     * Close this instance.
     */
    void close() throws IOException;
}

