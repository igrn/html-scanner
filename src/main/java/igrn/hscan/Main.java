package igrn.hscan;

import org.jsoup.nodes.Document;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner reader = new Scanner(System.in)) {
            URL url = getValidUrl(reader);
            Document htmlFile = getHtmlFile(url, reader);
            showResults(htmlFile);
        }
    }

    private static URL getValidUrl(Scanner reader) {
        String uriPath;
        do {
            System.out.print("Введите url-адрес в формате www.example.com: https://");
            uriPath = "https://" + reader.nextLine();
        } while (!Connector.isValidUrl(uriPath));
        return Connector.getUrl(uriPath);
    }

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
        return FileManager.openHtmlFile(path.toFile());
    }

    private static void showResults(Document htmlFile) {
        String parsedFile = Parser.findText(htmlFile);
        Map<String, Integer> foundWords = Parser.getWordsFrequency(parsedFile);

        System.out.println("Результаты поиска:");
        foundWords.keySet()
                  .stream()
                  .map(word -> word + " - " + foundWords.get(word))
                  .forEach(System.out::println);
    }

    private static void exitIfPressedN(Scanner reader) {
        String line;
        do {
            System.out.print("Хотите продолжить? (y/n): ");
            line = reader.nextLine();
        } while (!line.equalsIgnoreCase("y") && !line.equalsIgnoreCase("n"));
        if (line.equalsIgnoreCase("n")) {
            System.out.println("Программа была завершена досрочно.");
            System.exit(1);
        }
    }
}
