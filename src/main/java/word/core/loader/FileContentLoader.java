package word.core.loader;

import java.io.*;

/**
 *
 */
public class FileContentLoader implements ContentLoader {
    @Override
    public boolean choose(String url) {
        if (url == null) {
            return false;
        }
        return !url.startsWith("http");
    }

    @Override
    public String load(String url) {
        return readFile(url);
    }

    /**
     * 读取文件
     *
     * @param path
     * @return
     */
    private String readFile(String path) {
        StringBuilder content = new StringBuilder("");
        try {
            String code = resolveCode(path);
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, code);
            BufferedReader br = new BufferedReader(isr);
            String text = "";
            while (null != (text = br.readLine())) {
                content.append(text);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "读取文件失败：url=" + path;
            System.err.println(msg);
            throw new IllegalArgumentException(msg);
        }
        return content.toString();
    }

    /**
     * 确定文件编码
     *
     * @param path
     * @return
     * @throws Exception
     */
    private String resolveCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";
        if (head[0] == -1 && head[1] == -2) {
            code = "UTF-16";
        } else if (head[0] == -2 && head[1] == -1) {
            code = "Unicode";
        } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
            code = "UTF-8";
        }
        inputStream.close();
        return code;
    }
}
