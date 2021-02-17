package igrn.hscan;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @Test
    void testCreateEmptyFileIfFileAlreadyExists() {
        URL url = this.getClass().getResource("/test.html");
        Path path = new File(url.getFile()).toPath();
        assertThrows(RuntimeException.class, () -> FileManager.createEmptyFile(path));
    }

    @Test
    void testWriteUrlToFileIfNotARealPath() {
        Path notARealPath = Path.of("not/a/path.123");
        URL url = Connector.getUrl("https://google.com");
        assertThrows(RuntimeException.class, () -> FileManager.writeUrlToFile(notARealPath, url));
    }

    @Test
    void testWriteUrlToFileIfNotRealUrl() {
        URL url = this.getClass().getResource("/test.html");
        Path path = new File(url.getFile()).toPath();
        URL notARealUrl = Connector.getUrl("https://gugl.gl");
        assertThrows(RuntimeException.class, () -> FileManager.writeUrlToFile(path, notARealUrl));
    }

    @Test
    void testOpenHtmlFileNotFound() {
        Path notARealPath = Path.of("not/a/path.123");
        assertThrows(RuntimeException.class, () -> FileManager.openFile(notARealPath));
    }

    @Test
    void testOpenHtmlIfNotAFile() {
        URL url = this.getClass().getResource("/notAFile");
        Path path = new File(url.getFile()).toPath();
        assertThrows(RuntimeException.class, () -> FileManager.openFile(path));
    }
}
