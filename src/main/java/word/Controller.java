package word;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import word.core.WordFrequency;
import word.core.parser.Word;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Controller implements Initializable {
    @FXML
    private JFXTextField url;

    @FXML
    private JFXTextArea result;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXButton cancel;

    /**
     *
     */
    public static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RequiredFieldValidator validator2 = new RequiredFieldValidator();
        validator2.setMessage("请输入文件地址或网页地址...");
        url.getValidators().add(validator2);
        url.focusedProperty().addListener((o, oldVal, newVal) -> {
            url.setFocusTraversable(true);
            if (!newVal) {
                url.validate();
            }
        });

        result.setEditable(false);

        submit.setOnAction((event) -> {
            try {
                result.setText("内容分析中....");

                if (!url.validate()) {
                    return;
                }

                executor.submit(() -> {
                    try {
                        List<Word> wordList = WordFrequency.wordFrequency(url.getText(), 100);
                        StringBuilder sb = new StringBuilder();
                        wordList.forEach(item -> {
                            sb.append(item.toString()).append("\n");
                        });
                        result.setText(sb.toString());
                    } catch (Throwable e) {
                        e.printStackTrace();
                        result.setText(e.getMessage());
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
                result.setText(e.getMessage());
            }
        });

        cancel.setOnAction((event) -> {
            clear();
        });
    }

    /**
     * 清空内容
     */
    private void clear() {
        result.setText("");
        url.setText("");
    }
}
