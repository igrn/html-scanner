package igrn.hscan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class Connector {
    private static final Logger logger = LogManager.getLogger();

    // Устанавливает соединение с удаленным url-ресурсом, возвращает байтовый поток InputStream
    public static InputStream connectToUrl(URL url) throws IOException {
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            return conn.getInputStream();
        } catch (IOException e) {
            logger.warn("Невозможно установить соединение с данным url-адресом: " + url.toString());
            throw new RuntimeException(e);
        }
    }

    // Проверяет переданную строку с url на валидность
    public static boolean isValidUrl(String uriPath) {
        final String urlRegex = "((http|https)://)?((\\w)*|([0-9]*)|([-|_])*)+([\\.|/]((\\w)*|([0-9]*)|([-|_])*))+";
        try {
            if (!Pattern.matches(urlRegex, uriPath)) {
                throw new URISyntaxException(uriPath, "Данная строка не является валидным url-адресом");
            }
            URL url = new URL(uriPath);
            url.toURI();
            return true;
        } catch (URISyntaxException e) {
            logger.warn(e.getMessage());
        } catch (MalformedURLException e) {
            logger.warn("В данной строке отсутствует протокол передачи данных: " + uriPath);
        }
        return false;
    }

    // Метод-обертка над стандартным конструктором объекта URL, чтобы не нужно было ловить MalformedURLException в основном классе
    // Исключение по большей части игнорируется, т.к. предполагается, что валидация будет осуществляться методом isValidUrl
    public static URL getUrl(String uriPath) {
        try {
            if (isValidUrl(uriPath)) {
                return new URL(uriPath);
            } else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
