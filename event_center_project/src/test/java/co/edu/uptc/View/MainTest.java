package co.edu.uptc.View;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void testMain() {
        String simulatedInput = "2\n3\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            assertDoesNotThrow(() -> Main.main(new String[0]));
        } finally {
            System.setIn(originalIn);
        }
    }
}
