package igrn.hscan;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        try (Scanner reader = new Scanner(System.in)) {
            URL url;
            do {
                System.out.print("Введите url-адрес в формате www.example.com: ");
                String webpage = "https://" + reader.nextLine();
                url = validateUrl(webpage);
            } while (url == null);

            Path path = Path.of("target/index.html").toAbsolutePath();
            createEmptyFile(path, reader);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                 BufferedWriter bw = Files.newBufferedWriter(path)
            ) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                }
            } catch (IOException e) {
            }
        }
    }

    private static void createEmptyFile(Path path, Scanner reader) {
        System.out.println("Страница будет сохранена в файле: " + path.toString());
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Внимание: данный файл уже существует и будет перезаписан!");
            exitIfPressedN(reader);
        } catch (IOException e) {
            //Не должен исполняться никогда
            //добавить логи + сообщение
        }
    }

    private static void exitIfPressedN(Scanner reader) {
        String line;
        do {
            System.out.print("Хотите продолжить? (y/n): ");
            line = reader.nextLine();
        } while (!line.equalsIgnoreCase("y") && !line.equalsIgnoreCase("n"));
        if (line.equals("n")) {
            System.exit(1);
        }
    }

    private static URL validateUrl(String line) {
        final String urlRegex = "((http|https)://)?((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
        URL url = null;
        try {
            if (!Pattern.matches(urlRegex, line)) {
                throw new URISyntaxException(line, "Введенная строка не является валидным url-адресом");
            }
            url = new URL(line);
            url.toURI();
        } catch (MalformedURLException e) {
            //логирование + сообщение в консоль
        } catch (URISyntaxException e) {
            //логирование + сообщение в консоль
        }
        return url;
    }
}
