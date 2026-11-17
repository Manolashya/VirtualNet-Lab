package com.virtualnetlab.ui;

import com.virtualnetlab.integration.ModuleRegistry;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ModuleScreen {
    private final Stage stage;
    private final int unit;
    private final BorderPane root = new BorderPane();
    private final TextArea output = new TextArea();

    private static final String[] TITLES = {
            "", "Unit I — Network Fundamentals", "Unit II — Data Link Layer",
            "Unit III — Network Layer", "Unit IV — Transport Layer",
            "Unit V — Application Layer"
    };
    private static final String[] DETAILS = {
            "", "Packet size, bandwidth, distance and packet count",
            "Binary data for parity, CRC, Hamming code and ARQ",
            "IPv4 address, subnet mask and router link costs",
            "Message and packet-loss percentage",
            "DNS, Email, HTTP requests and video streaming simulations"
    };

    public ModuleScreen(Stage stage, int unit) {
        this.stage = stage;
        this.unit = unit;
        build();
    }

    private void build() {
        root.setStyle(UiStyle.ROOT);
        root.setPadding(new Insets(26));

        Button back = new Button("← Dashboard");
        back.setStyle(UiStyle.SECONDARY_BUTTON);
        back.setOnAction(e -> {
            HomeScreen home = new HomeScreen(stage);
            stage.setScene(new Scene(home.getView(), 1100, 720));
        });

        Label title = new Label(TITLES[unit]);
        title.setStyle(UiStyle.HEADING);
        Label details = new Label(DETAILS[unit]);
        details.setStyle(UiStyle.BODY);

        VBox header = new VBox(12, back, title, details);
        header.setPadding(new Insets(0, 0, 22, 0));
        root.setTop(header);

        VBox content = new VBox(14);
        content.setPadding(new Insets(20));
        content.setStyle(UiStyle.CARD);

        Label inputLabel = new Label("Simulation input");
        inputLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 16px; -fx-font-weight: bold;");

        TextField input = new TextField();
        input.setPromptText(promptForUnit());
        input.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 7; -fx-padding: 11;");

        Button run = new Button("Run Simulation");
        run.setStyle(UiStyle.PRIMARY_BUTTON);
        run.setOnAction(e -> {
            String value = input.getText().trim();
            if (value.isEmpty()) {
                output.setText("Please enter the required input before running the simulation.");
                return;
            }
            output.setText(ModuleRegistry.runDemo(unit, value));
        });

        Label outputLabel = new Label("Simulation output");
        outputLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 16px; -fx-font-weight: bold;");

        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefRowCount(12);
        if (unit == 5) {
    output.setText(
            "APPLICATION LAYER SIMULATIONS\n\n" +
            "1. DNS Lookup\n" +
            "www.example.com\n\n" +
            "2. Email Simulation\n" +
            "action=email, from=alice@example.com, to=bob@example.com, subject=Hello, body=Welcome\n\n" +
            "3. HTTP Simulation\n" +
            "action=http, domain=www.example.com, path=/index.html\n\n" +
            "4. Video Streaming\n" +
            "action=stream, domain=video.example.com, chunks=3, chunkSizeMb=5"
    );
} else {
    output.setText("Enter a value and click Run Simulation.");
}
        output.setStyle("-fx-control-inner-background: #08111f; -fx-text-fill: #cbd5e1; " +
                "-fx-highlight-fill: #0ea5e9; -fx-font-family: 'Consolas';");

        content.getChildren().addAll(inputLabel, input, run, outputLabel, output);
        VBox.setVgrow(output, Priority.ALWAYS);
        root.setCenter(content);

        Label note = new Label("Educational simulation only • No physical networking hardware is required");
        note.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        note.setPadding(new Insets(18, 0, 0, 0));
        root.setBottom(note);
    }

    private String promptForUnit() {
        return switch (unit) {
            case 1 -> "Example: packetSize=1000, bandwidthMbps=10, distanceMeters=1000";
            case 2 -> "Example: 1011001";
            case 3 -> "Example: source=A, destination=E";
            case 4 -> "Example: message=HELLO, lossPercent=20";
            case 5 -> "Enter a domain or an Application Layer action";
            default -> "Enter simulation input";
        };
    }

    public void show() {
        stage.setScene(new Scene(root, 1100, 720));
    }
}
