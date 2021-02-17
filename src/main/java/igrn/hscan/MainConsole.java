package igrn.hscan;

import org.jsoup.nodes.Document;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class MainConsole {
    // Точка входа в программу
    public static void main(String[] args) {
        try (Scanner reader = new Scanner(System.in)) {
            URL url = getValidUrl(reader);
            Document htmlFile = getHtmlFile(url, reader);
            showResults(htmlFile);
        } catch (RuntimeException e) {
            System.out.println("При выполнении программы возникла ошибка! Программа завершена.");
        }
    }

    // Получает от пользователя строку с url-адресом, валидирует ее и возвращает объект класса URL
    private static URL getValidUrl(Scanner reader) {
        String uriPath;
        do {
            System.out.print("Введите url-адрес в формате www.example.com: https://");
            uriPath = "https://" + reader.nextLine();
        } while (!Connector.isValidUrl(uriPath));
        return Connector.getUrl(uriPath);
    }

    // Сохраняет код html-страницы в html-файл ./target/index.html, возвращает открытый файл в виде объекта с DOM-like структурой
    private static Document getHtmlFile(URL url, Scanner reader) {
        Path path = Path.of("target/index.html").toAbsolutePath();
        System.out.println("Страница будет сохранена в файле: " + path.toString());

        if (Files.exists(path)) {
            System.out.println("Внимание: данный файл уже существует и будет перезаписан!");
            exitIfPressedN(reader);
        } else {
            FileManager.createEmptyFile(path);
        }

        FileManager.writeUrlToFile(path, url);
        return FileManager.openFile(path);
    }

    // Находит в html-файле слова и выводит их список с частотой вхождения каждого из них в консоль
    private static void showResults(Document htmlFile) {
        String parsedFile = HtmlScanner.findText(htmlFile);
        Map<String, Integer> foundWords = HtmlScanner.getWordsFrequency(parsedFile);

        System.out.println("Результаты поиска:");
        foundWords.keySet()
                  .stream()
                  .map(word -> word + " - " + foundWords.get(word))
                  .forEach(System.out::println);
    }

    // Завершает программу при нажатии на клавишу 'N'
    private static void exitIfPressedN(Scanner reader) {
        String line;
        do {
            System.out.print("Хотите продолжить? (y/n): ");
            line = reader.nextLine();
        } while (!line.equalsIgnoreCase("y") && !line.equalsIgnoreCase("n"));
        if (line.equalsIgnoreCase("n")) {
            System.out.println("Программа была завершена по требованию пользователя.");
            System.exit(1);
        }
    }
}
