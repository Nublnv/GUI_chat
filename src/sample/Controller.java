package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    boolean connected = false;
    @FXML
    TextArea mainTextArea;
    @FXML
    TextField textFieldMessage;
    @FXML
    Button buttonSend;
    @FXML
    MenuItem menuItemConnect;

    @FXML
    private void buttonSend_onClick() {
        if (!textFieldMessage.getText().isEmpty()) {
            String request = textFieldMessage.getText(); // Читам пользовательский ввод
            try {

                out.writeUTF(request); // Отправляем на сервер текст с коносли
                mainTextArea.appendText(textFieldMessage.getText() + "\n");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            textFieldMessage.clear();
        }
    }

    @FXML
    private void textFieldMessage_OnMouseClicked() {
        textFieldMessage.clear();
        textFieldMessage.setOpacity(1.0);
    }

    @FXML
    private void menuItemConnect_OnAction() {
        Thread thread = null;
        if (!connected) {
            try {
                socket = new Socket("localhost", 8188);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                String response = in.readUTF(); // Ждём сообщение от сервера
                mainTextArea.appendText(response + "\n");
                thread = new Thread(() -> {
                    while (true) {
                        try {
                            String response1 = in.readUTF();
                            mainTextArea.appendText(response1 + "\n");
                        } catch (IOException exception) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Сервер недоступен!");
                            alert.setHeaderText("Сервер стал недоступен!");
                            alert.setContentText("Сервер стал недоступен!");
                            alert.showAndWait();
                            Platform.exit();
                        }
                    }
                });
                thread.start();
                connected = true;
                menuItemConnect.setText("Отключиться");
                textFieldMessage.editableProperty().set(true);
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Сервер недоступен!");
                alert.setHeaderText("Сервер недоступен!");
                alert.setContentText("Сервер недоступен!");
                alert.showAndWait();
            }
        }
        else
        {
            if (thread != null)
            {
                if(thread.isAlive())
                    thread.interrupt();
            }
            socket = null;
            in = null;
            out = null;
            connected = false;
            menuItemConnect.setText("Подключиться");
            mainTextArea.clear();
            textFieldMessage.editableProperty().set(false);
        }
    }
}
