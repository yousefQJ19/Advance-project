package edu.najah.cap.data;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {
    private JTextArea textArea;

    public ConsoleOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Append the character to the JTextArea
        textArea.append(String.valueOf((char) b));
        // Ensure the last line is always visible
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // Handle thread safety (Swing components should be updated in the Event Dispatch Thread)
        if (SwingUtilities.isEventDispatchThread()) {
            textArea.update(textArea.getGraphics());
        }
    }
}
