package word.core.parser;

import java.util.Map;

/**
 * 词频统计器
 */
public class OnceParser extends Parser {

    /**
     * 通过文本统计词频
     *
     * @param text
     */
    public OnceParser(String text) {
        super(text);
    }

    @Override
    long makeDict() {
        int N = this.text.length();
        long count = 0;
        for (int j = 0; j < N; j++) {
            for (int len = 1; j + len <= N && len <= MAX_WORD_LEN; len++) {
                String w = this.text.substring(j, j + len).intern();
                Word word = this.dict.get(w);
                if (word == null) {
                    word = new Word(w);
                    word.setFrequency(1);
                    this.dict.put(w, word);
                } else {
                    word.setFrequency(word.getFrequency() + 1);
                }

                if (len > 1 && j > 0) {
                    char l = this.text.charAt(j - 1);
                    word.addLeftWords(l);
                }
                if (len > 1 && j + len < N) {
                    char r = this.text.charAt(j + len);
                    word.addRightWords(r);
                }
                count++;
            }
        }
        return count;
    }

    @Override
    void filter() {
        filterInner(300, 0.5);
    }

    private void filterInner(int mi, double entropy) {
        for (Map.Entry<String, Word> entry : this.dict.entrySet()) {
            Word word = entry.getValue();
            word.setMi(mi(word));
            word.setEntropy(entropy(word));
            // 筛选条件
            if (word.getMi() > mi && word.getEntropy() >= entropy) {
                this.candidates.add(word);
            }
        }

        if (this.candidates.size() <= 0) {
            filterInner(mi - 10 > 0 ? mi - 10 : 0, entropy - 0.1 > 0 ? entropy - 0.1 : 0);
        }
    }

    private float mi(Word word) {
        if (word.getValue().length() == 1) {
            return 1.0f;
        }

        float tmp = 1.0f;
        int len = word.getValue().length();
        String val = word.getValue();
        for (int i = 1; i < len; i++) {
            String left = val.substring(0, i);
            String right = val.substring(i);
            if (i == 1 || tmp < this.dict.get(left).getFrequency() * this.dict.get(right).getFrequency()) {
                tmp = this.dict.get(left).getFrequency() * this.dict.get(right).getFrequency();
            }
        }

        return Word.N * word.getFrequency() / tmp;
    }

    private float entropy(Word word) {
        if (word.getValue().length() == 1
                || word.getLeftWordsNum() == 0
                || word.getRightWordsNum() == 0) {
            return 0.0f;
        }

        float left = 0.0f;
        float right = 0.0f;
        for (Map.Entry<Character, Integer> entry : word.getLeftWords().entrySet()) {
            float p = (float) entry.getValue() / word.getLeftWordsNum();
            left += (-p) * Math.log(p);
        }
        for (Map.Entry<Character, Integer> entry : word.getRightWords().entrySet()) {
            float p = (float) entry.getValue() / word.getRightWordsNum();
            right += (-p) * Math.log(p);
        }

        return left > right ? right : left;
    }
}
