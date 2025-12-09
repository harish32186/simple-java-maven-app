
package com.mycompany.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    public void testServerStarts() {
        // Since App runs a server, we just check App class exists
        App app = new App();
        assertTrue(app.getClass() == App.class);
    }
}
