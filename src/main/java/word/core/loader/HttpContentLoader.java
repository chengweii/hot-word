package word.core.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class HttpContentLoader implements ContentLoader {
    @Override
    public boolean choose(String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("http");
    }

    @Override
    public String load(String url) {
        try {
            URL urls = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) urls.openConnection();
            InputStreamReader input = new InputStreamReader(httpConn
                    .getInputStream(), getUrlCharset(url));
            BufferedReader bufReader = new BufferedReader(input);
            String line = "";
            StringBuilder contentBuf = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                contentBuf.append(line);
            }

            String result = extractText(contentBuf.toString());

            //关闭相应的流
            bufReader.close();
            input.close();
            httpConn.getInputStream().close();

            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalArgumentException("读取网页内容失败：url=" + url);
        }
    }

    /**
     * 从html中提取纯文本
     *
     * @param strHtml
     * @return
     */
    private static String extractText(String strHtml) {
        //剔出<html>的标签
        String txtcontent = strHtml.replaceAll("</?[^>]+>", "");
        //去除字符串中的空格,回车,换行符,制表符
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        return txtcontent;
    }

    public static String getUrlCharset(String url) {
        return BytesEncodingDetect.detectUrlEncoding(url);
    }
}
