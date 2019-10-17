package word.core.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
abstract class Parser {
    /**
     * 词的最大长度
     */
    protected static final int MAX_WORD_LEN = 4;
    /**
     * 待处理的字符串
     */
    protected String text;
    /**
     * 内部词典
     */
    protected Map<String, Word> dict;
    /**
     * 符合条件的候选词
     */
    protected List<Word> candidates;

    public Parser(String text) {
        this.text = clean(text);
        this.dict = new HashMap<>();
        this.candidates = new ArrayList<>();
    }

    private String clean(String str) {
        return str.replaceAll("[^\\u4E00-\\u9FA50-9]+", "");
    }

    public List<Word> parse() {
        Word.N = makeDict();
        filter();
        return this.candidates;
    }

    abstract long makeDict();

    abstract void filter();
}
