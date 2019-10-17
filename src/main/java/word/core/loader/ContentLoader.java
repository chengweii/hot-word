package word.core.loader;

/**
 * 内容加载器
 */
public interface ContentLoader {
    /**
     * 选定
     * @param url
     * @return
     */
    boolean choose(String url);

    /**
     * 读取内容
     * @param url
     * @return
     */
    String load(String url);
}
