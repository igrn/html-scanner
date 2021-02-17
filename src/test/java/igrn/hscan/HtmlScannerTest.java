package igrn.hscan;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlScannerTest {
    static Map<String, Integer> testMap;
    static String testString;

    @BeforeAll
    static void setUp() {
        testMap = new HashMap<>() {{
            put("TEST", 3);
            put("TAG", 3);
            put("DIV", 1);
            put("A", 1);
            put("P", 1);
        }};
        testString = "Test div tag Test a tag Test p tag ";
    }

    @AfterAll
    static void tearDown() {
        testMap = null;
        testString = null;
    }

    @Test
    void testFindText() {
        String expected = testString;
        URL url = this.getClass().getResource("/test.html");
        Path path = new File(url.getFile()).toPath();
        Document document = FileManager.openFile(path);
        String actual = HtmlScanner.findText(document);
        assertEquals(expected, actual);
    }

    @Test
    void testGetWordsFrequency() {
        Map<String, Integer> expected = testMap;
        Map<String, Integer> actual = HtmlScanner.getWordsFrequency(testString);
        assertEquals(expected, actual);
    }
}