
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
void testConstructor() {
    App app = new App();

    assertEquals(App.class, app.getClass());
}
}
