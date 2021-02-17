package igrn.hscan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileManager {

    public static void createEmptyFile(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            //TODO: добавить логи + сообщение
        }
    }

    // Сохраняет удержанную в буфере страницу в html-файл
    public static void writeUrlToFile(Path path, URL url) {
        try (InputStream input = Connector.connectToUrl(url)) {
            Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Файл был успешно сохранен.");
        } catch (IOException e) {
            //TODO: логи и сообщение
            System.out.println("Внимание: возникла ошибка при записи в файл!");
            throw new RuntimeException(e);
        }
    }

    // Открывает html-файл для использования классом HtmlScanner
    public static Document openFile(Path path) {
        try {
            return Jsoup.parse(path.toFile(), "UTF-8");
        } catch (FileNotFoundException e) {
            //TODO: Добавить логи и стек-трейс
            System.out.println("Внимание: указанный файл не найден или является папкой!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Внимание: возникла ошибка при чтении файла!");
            throw new RuntimeException(e);
        }
    }
}
