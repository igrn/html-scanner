package igrn.hscan;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ConnectorTest {

    @Test
    void testConnectToUrlThrowsException() {
        String uriPath = "https://gugl.gl/123";
        URL notARealUrl = Connector.getUrl(uriPath);
        assertTrue(Connector.isValidUrl(uriPath));
        assertThrows(RuntimeException.class, () -> Connector.connectToUrl(notARealUrl));
    }

    @Test
    void testIfIsValidUrl() {
        assertTrue(Connector.isValidUrl("https://stackoverflow.com"));
        assertTrue(Connector.isValidUrl("https://docs.google.com"));
        assertTrue(Connector.isValidUrl("https://www.youtube.com"));
    }

    @Test
    void testIfIsNotValidUrl() {
        assertFalse(Connector.isValidUrl("htts://abcd"));
        assertFalse(Connector.isValidUrl("NotAUrl/123"));
    }

    @Test
    void testGetUrlThrowsExceptionIfNotValidUrl() {
        assertThrows(RuntimeException.class, () -> Connector.getUrl("NotAUrl/123"));
    }
}