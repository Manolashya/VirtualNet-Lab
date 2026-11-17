package com.virtualnetlab.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HomeScreen {
    private final Stage stage;
    private final BorderPane root = new BorderPane();

    public HomeScreen(Stage stage) {
        this.stage = stage;
        build();
    }

    public Parent getView() {
        return root;
    }

    private void build() {
        root.setStyle(UiStyle.ROOT);
        root.setPadding(new Insets(28));

        Label brand = new Label("VIRTUALNET LAB");
        brand.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label title = new Label("Interactive Virtual Computer Networking Laboratory");
        title.setStyle(UiStyle.HEADING);
        Label subtitle = new Label("Explore the five CNIP units through guided software simulations.");
        subtitle.setStyle(UiStyle.BODY);

        VBox header = new VBox(10, brand, title, subtitle);
        header.setPadding(new Insets(10, 4, 28, 4));
        root.setTop(header);

        String[] names = {
                "Unit I  |  Network Fundamentals",
                "Unit II  |  Data Link Layer",
                "Unit III  |  Network Layer",
                "Unit IV  |  Transport Layer",
                "Unit V  |  Application Layer"
        };
        String[] descriptions = {
                "Explore network devices, links, packet movement, delay and throughput.",
                "Try parity, CRC, Hamming code and Stop-and-Wait ARQ.",
                "Explore IPv4 addressing, subnetting and shortest-path routing.",
                "Compare simplified TCP and UDP behaviour under packet loss.",
                "Simulate DNS lookups and HTTP request/response exchanges."
        };

        Tile[] tiles = new Tile[5];
        for (int i = 0; i < 5; i++) {
            final int unit = i + 1;
            tiles[i] = new Tile(names[i], descriptions[i],
                    () -> new ModuleScreen(stage, unit).show());
        }

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(18);
        grid.add(tiles[0], 0, 0);
        grid.add(tiles[1], 1, 0);
        grid.add(tiles[2], 0, 1);
        grid.add(tiles[3], 1, 1);
        grid.add(tiles[4], 0, 2, 2, 1);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(c1, c2);
        root.setCenter(grid);

        Label footer = new Label("CNIP Semester Project  •  JavaFX Desktop Application");
        footer.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        footer.setPadding(new Insets(24, 4, 0, 4));
        root.setBottom(footer);
    }

    private static class Tile extends VBox {
        Tile(String titleText, String description, Runnable action) {
            setSpacing(12);
            setPadding(new Insets(20));
            setStyle(UiStyle.CARD);
            setMinHeight(145);
            setAlignment(Pos.TOP_LEFT);

            Label title = new Label(titleText);
            title.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 17px; -fx-font-weight: bold;");
            title.setWrapText(true);

            Label desc = new Label(description);
            desc.setStyle(UiStyle.BODY);
            desc.setWrapText(true);

            Button open = new Button("Open module  →");
            open.setStyle(UiStyle.PRIMARY_BUTTON);
            open.setOnAction(e -> action.run());

            getChildren().addAll(title, desc, open);
            setOnMouseEntered(e -> setStyle(
                    "-fx-background-color: #17263c; -fx-background-radius: 14; " +
                    "-fx-border-color: #38bdf8; -fx-border-radius: 14; -fx-padding: 20;"));
            setOnMouseExited(e -> setStyle(UiStyle.CARD));
        }
    }
}
