
package com.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void testAdd() {
        assertEquals(10, App.add(5, 5));
    }

    @Test
    void testAddNegativeNumbers() {
        assertEquals(-10, App.add(-5, -5));
    }

    @Test
    void testAddZero() {
        assertEquals(5, App.add(5, 0));
    }

    @Test
    void testMain() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));

            App.main(new String[]{});

            assertEquals(
                "SonarQube Demo Application",
                output.toString().trim()
            );

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainAgain() {

        App.main(new String[]{});
    }
}
