package word.core;

import word.core.loader.ContentLoader;
import word.core.loader.FileContentLoader;
import word.core.loader.HttpContentLoader;
import word.core.parser.OnceParser;
import word.core.parser.Word;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class WordFrequency {

    private static List<ContentLoader> contentLoaderList;

    static {
        contentLoaderList = new ArrayList<>();
        contentLoaderList.add(new FileContentLoader());
        contentLoaderList.add(new HttpContentLoader());
    }

    /**
     * 通过文本地址统计词频
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static List<Word> wordFrequency(String url, int topN) throws IOException {
        ContentLoader contentLoader = null;
        for (ContentLoader item : contentLoaderList) {
            if (item.choose(url)) {
                contentLoader = item;
                break;
            }
        }

        if (contentLoader == null) {
            return null;
        }

        String article = contentLoader.load(url);
        OnceParser parser = new OnceParser(article);
        List<Word> words = parser.parse();
        if (words != null && words.size() > 0) {
            words.sort(new Comparator<Word>() {
                @Override
                public int compare(Word o1, Word o2) {
                    return o2.getFrequency() > o1.getFrequency() ? 1 : -1;
                }
            });
            if (words.size() > topN) {
                return words.subList(0, topN);
            }
        }
        return words;
    }

    public static void main(String[] args) {
        try {
            List<Word> list = WordFrequency.wordFrequency("D:\\test.txt", 30);
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
