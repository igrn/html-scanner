package igrn.hscan;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

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