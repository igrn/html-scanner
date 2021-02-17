package igrn.hscan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileManager {
    private static final Logger logger = LogManager.getLogger();

    // Создает пустой файл (с директориями) по заданному пути
    public static void createEmptyFile(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            logger.debug("Новый файл был успешно создан.");
        } catch (FileAlreadyExistsException e) {
            logger.warn("Внимание: файл с таким именем уже существует!");
            logger.debug("Путь к файлу: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Внимание: возникла ошибка при создании файла!");
            logger.debug("Путь к файлу: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Сохраняет удержанную в буфере страницу в html-файл
    public static void writeUrlToFile(Path path, URL url) {
        try (InputStream input = Connector.connectToUrl(url)) {
            Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Файл был успешно сохранен.");
        } catch (IOException e) {
            logger.error("Внимание: возникла ошибка при записи в файл!");
            logger.debug("Путь к файлу: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Открывает html-файл для использования классом HtmlScanner, возвращает объект с DOM-like структурой
    public static Document openFile(Path path) {
        try {
            return Jsoup.parse(path.toFile(), "UTF-8");
        } catch (FileNotFoundException e) {
            logger.error("Внимание: указанный ресурс не найден или не является файлом!");
            logger.debug("Путь к ресурсу: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("Внимание: возникла ошибка при чтении файла!");
            logger.debug("Путь к файлу: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
