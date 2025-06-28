import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.security.SecureRandom;

public class PasswordGeneratorApp extends Application {

    private TextField lengthField;
    private CheckBox lowercaseBox, uppercaseBox, numberBox, symbolBox;
    private TextField outputField;
    private Label messageLabel;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+{}[]<>?/";

    @Override
    public void start(Stage stage) {
        Label title = new Label("🔐 Password Generator");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lengthLabel = new Label("Password Length:");
        lengthField = new TextField("12");

        lowercaseBox = new CheckBox("Include Lowercase");
        lowercaseBox.setSelected(true);
        uppercaseBox = new CheckBox("Include Uppercase");
        uppercaseBox.setSelected(true);
        numberBox = new CheckBox("Include Numbers");
        numberBox.setSelected(true);
        symbolBox = new CheckBox("Include Symbols");
        symbolBox.setSelected(true);

        Button generateButton = new Button("Generate Password");
        generateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        generateButton.setOnAction(e -> generatePassword());

        outputField = new TextField();
        outputField.setPromptText("Generated password will appear here");
        outputField.setEditable(false);

        Button copyBtn = new Button("Copy");
        copyBtn.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(outputField.getText());
            clipboard.setContent(content);
            messageLabel.setText("✅ Copied to clipboard!");
        });

        HBox copyBox = new HBox(10, outputField, copyBtn);
        copyBox.setAlignment(Pos.CENTER_LEFT);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: green;");

        VBox root = new VBox(10,
            title, lengthLabel, lengthField,
            lowercaseBox, uppercaseBox, numberBox, symbolBox,
            generateButton, copyBox, messageLabel
        );
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(root, 420, 400);
        stage.setScene(scene);
        stage.setTitle("Password Generator");
        stage.show();
    }

    private void generatePassword() {
        messageLabel.setText("");
        int length;
        try {
            length = Integer.parseInt(lengthField.getText().trim());
            if (length < 4 || length > 100) {
                outputField.setText("");
                messageLabel.setText("⚠️ Length must be 4–100");
                return;
            }
        } catch (NumberFormatException e) {
            outputField.setText("");
            messageLabel.setText("⚠️ Invalid length");
            return;
        }

        StringBuilder charPool = new StringBuilder();
        if (lowercaseBox.isSelected()) charPool.append(LOWER);
        if (uppercaseBox.isSelected()) charPool.append(UPPER);
        if (numberBox.isSelected()) charPool.append(DIGITS);
        if (symbolBox.isSelected()) charPool.append(SYMBOLS);

        if (charPool.length() == 0) {
            messageLabel.setText("⚠️ Select at least one option");
            return;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        outputField.setText(password.toString());
        messageLabel.setText("✅ Password generated!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
