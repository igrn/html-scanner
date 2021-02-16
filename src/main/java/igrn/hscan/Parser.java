package igrn.hscan;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Parser {
    // Возвращает строку со всем текстом, содержащимся внутри тегов тела указанной web-страницы
    public static String findText(Document html) {
        StringBuilder parsedHtml = new StringBuilder();
        Elements tags = html.body().select("*");
        for (Element tag : tags) {
            //TextNode выделяет вложенные теги в отдельные словосочетания
            for (TextNode tn : tag.textNodes()) {
                String tagText = tn.text().trim();
                if (tagText.length() > 0) {
                    parsedHtml.append(tagText).append(" ");
                }
            }
        }
        return parsedHtml.toString();
    }

    // Возвращает словарь типа "Найденное слово - Количество вхождений"
    public static Map<String, Integer> getWordsFrequency(String parsedHtml) {
        Map<String, Integer> foundWords = new HashMap<>();
        for (String word : parsedHtml.split("[ ,.!?\";:\n\r\t]")) { // Делим строку на отдельные слова
            word = word.toUpperCase();
            if (Pattern.matches("[A-ZА-Я]([A-ZА-Я0-9-]*?)", word)) { // Пропускаем числа, отдельные символы (напр. ©)
                if (foundWords.containsKey(word)) {
                    foundWords.put(word, foundWords.get(word) + 1);
                } else {
                    foundWords.put(word, 1);
                }
            }
        }
        return foundWords;
    }
}
