package igrn.hscan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
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
        }
    }

    // Открывает html-файл для использования парсером
    public static Document openHtmlFile(File htmlFile) {
        Document openedHtmlFile = null;
        try {
            openedHtmlFile = Jsoup.parse(htmlFile, "UTF-8");
        } catch (FileNotFoundException e) {
            //TODO: Добавить логи и стек-трейс
            System.out.println("Указанный файл не найден! Программа будет завершена.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Возникла ошибка при чтении файла! Программа будет завершена.");
            System.exit(1);
        }
        return openedHtmlFile;
    }
}
