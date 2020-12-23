import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Connector connector;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connector = new Connector((args) -> {
            mainArea.appendText((String)args[0]);
        });
    }

    public void sendMsgAction(ActionEvent actionEvent) { //
        connector.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction() {
        connector.close();
        Platform.exit();
    }
}