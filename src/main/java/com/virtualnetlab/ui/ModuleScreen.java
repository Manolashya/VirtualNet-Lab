package com.virtualnetlab.ui;

import com.virtualnetlab.integration.ModuleRegistry;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final List<Label> topologyLinks = new ArrayList<>();
private final List<VBox> topologyDevices = new ArrayList<>();

private Label animationStatus;
private Label packetIcon;
private StackPane topologyArea;
private Pane animationLayer;

private Timeline packetTimeline;

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
    if (packetTimeline != null) {
        packetTimeline.stop();
    }

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
            String result = ModuleRegistry.runDemo(unit, value);
output.setText(result);

if (unit == 1) {
    animatePackets(result);
}
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

if (unit == 1) {
    HBox mainContent = new HBox(18, content, createTopologyPanel());
    HBox.setHgrow(content, Priority.ALWAYS);
    root.setCenter(mainContent);
} else {
    root.setCenter(content);
}

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

private void animatePackets(String simulationOutput) {

    if (packetTimeline != null) {
        packetTimeline.stop();
    }

    if (topologyLinks.size() != 3
            || topologyDevices.size() != 4
            || animationStatus == null
            || packetIcon == null) {
        return;
    }

    root.applyCss();
    root.layout();
    topologyArea.applyCss();
    topologyArea.layout();
    animationLayer.applyCss();
    animationLayer.layout();
    packetIcon.applyCss();

    List<String[]> packetResults = new ArrayList<>();

    String currentCase = null;

    Pattern lostPattern = Pattern.compile(
            "^Lost at: (.+?) -> (.+?) \\("
    );

    for (String line : simulationOutput.split("\\R")) {

        line = line.trim();

        if (line.startsWith("CASE 1:")) {
            currentCase = "P001 (0% loss)";
        } else if (line.startsWith("CASE 2:")) {
            currentCase = "P001 (100% loss)";
        } else if (currentCase != null
                && line.equals("Status: PACKET DELIVERED")) {

            packetResults.add(new String[]{
                    currentCase, "DELIVERED", "-1"
            });

        } else if (currentCase != null
                && line.equals("Status: PACKET LOST")) {

            packetResults.add(new String[]{
                    currentCase, "LOST", "-1"
            });

        } else if (line.startsWith("Lost at: ")
                && !packetResults.isEmpty()) {

            Matcher matcher = lostPattern.matcher(line);

            if (matcher.find()) {
                String[] result =
                        packetResults.get(packetResults.size() - 1);

                int linkIndex = getLinkIndex(
                        matcher.group(1),
                        matcher.group(2)
                );

                result[2] = String.valueOf(linkIndex);
            }
        }
    }

    if (packetResults.isEmpty()) {
        resetTopologyLinks();
        packetIcon.setVisible(false);
        animationStatus.setText(
                "No packet results available to animate."
        );
        return;
    }

    List<KeyFrame> frames = new ArrayList<>();

    double time = 0;
    int deliveredCount = 0;
    int lostCount = 0;

    double packetWidth = packetIcon.getLayoutBounds().getWidth();
    double packetHeight = packetIcon.getLayoutBounds().getHeight();

    for (String[] result : packetResults) {

        String packetId = result[0];
        boolean delivered = result[1].equals("DELIVERED");

        int lostLinkIndex = Integer.parseInt(result[2]);

        if (!delivered && (lostLinkIndex < 0 || lostLinkIndex > 2)) {
            continue;
        }

        if (delivered) {
            deliveredCount++;
        } else {
            lostCount++;
        }

        int finalLink = delivered ? 2 : lostLinkIndex;

        for (int link = 0; link <= finalLink; link++) {

            final int currentLink = link;
            final String currentPacket = packetId;

            final boolean packetLost =
                    !delivered && link == lostLinkIndex;

            Point2D start = getDeviceCenter(currentLink);
            Point2D destination = getDeviceCenter(currentLink + 1);

            double progress = packetLost ? 0.75 : 1.0;

            double targetX = start.getX()
                    + (destination.getX() - start.getX()) * progress;

            double targetY = start.getY()
                    + (destination.getY() - start.getY()) * progress;

            frames.add(new KeyFrame(
                    Duration.millis(time),
                    event -> {
                        packetIcon.setVisible(true);
                        packetIcon.relocate(
                                start.getX() - packetWidth / 2,
                                start.getY() - packetHeight / 2
                        );
                    }
            ));

            time += 450;

            frames.add(new KeyFrame(
                    Duration.millis(time),
                    event -> {

                        resetTopologyLinks();

                        Label activeLink =
                                topologyLinks.get(currentLink);

                        if (packetLost) {

                            activeLink.setText("✕");
                            activeLink.setStyle(
                                    "-fx-text-fill: #ef4444;" +
                                    "-fx-font-size: 22px;" +
                                    "-fx-font-weight: bold;"
                            );

                            packetIcon.setVisible(false);

                            animationStatus.setText(
                                    currentPacket
                                    + " LOST at link "
                                    + (currentLink + 1)
                            );

                            animationStatus.setStyle(
                                    "-fx-text-fill: #ef4444;" +
                                    "-fx-font-size: 12px;" +
                                    "-fx-padding: 10;" +
                                    "-fx-background-color: #08111f;" +
                                    "-fx-background-radius: 7;"
                            );

                        } else {

                            activeLink.setText("● →");
                            activeLink.setStyle(
                                    "-fx-text-fill: #22c55e;" +
                                    "-fx-font-size: 20px;" +
                                    "-fx-font-weight: bold;"
                            );

                            animationStatus.setText(
                                    currentPacket
                                    + " travelling through link "
                                    + (currentLink + 1)
                            );

                            animationStatus.setStyle(
                                    "-fx-text-fill: #22c55e;" +
                                    "-fx-font-size: 12px;" +
                                    "-fx-padding: 10;" +
                                    "-fx-background-color: #08111f;" +
                                    "-fx-background-radius: 7;"
                            );
                        }
                    },
                    new KeyValue(
                            packetIcon.layoutXProperty(),
                            targetX - packetWidth / 2
                    ),
                    new KeyValue(
                            packetIcon.layoutYProperty(),
                            targetY - packetHeight / 2
                    )
            ));
        }

        time += 500;

        final String completedPacket = packetId;
        final boolean completedSuccessfully = delivered;

        frames.add(new KeyFrame(
                Duration.millis(time),
                event -> {

                    resetTopologyLinks();
                    packetIcon.setVisible(false);

                    if (completedSuccessfully) {
                        animationStatus.setText(
                                completedPacket + " DELIVERED to Server 1"
                        );
                        animationStatus.setStyle(
                                "-fx-text-fill: #22c55e;" +
                                "-fx-font-size: 12px;" +
                                "-fx-padding: 10;" +
                                "-fx-background-color: #08111f;" +
                                "-fx-background-radius: 7;"
                        );
                    } else {
                        animationStatus.setText(
                                completedPacket + " transmission failed"
                        );
                        animationStatus.setStyle(
                                "-fx-text-fill: #ef4444;" +
                                "-fx-font-size: 12px;" +
                                "-fx-padding: 10;" +
                                "-fx-background-color: #08111f;" +
                                "-fx-background-radius: 7;"
                        );
                    }
                }
        ));
    }

    if (frames.isEmpty()) {
        resetTopologyLinks();
        packetIcon.setVisible(false);
        animationStatus.setText(
                "No valid packet routes available to animate."
        );
        return;
    }

    time += 500;

    final int totalDelivered = deliveredCount;
    final int totalLost = lostCount;

    frames.add(new KeyFrame(
            Duration.millis(time),
            event -> {

                resetTopologyLinks();
                packetIcon.setVisible(false);

                animationStatus.setText(
                        "Animation complete\nDelivered: "
                        + totalDelivered
                        + " | Lost: "
                        + totalLost
                );

                animationStatus.setStyle(
                        "-fx-text-fill: #38bdf8;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 10;" +
                        "-fx-background-color: #08111f;" +
                        "-fx-background-radius: 7;"
                );
            }
    ));

    packetTimeline = new Timeline(
            frames.toArray(new KeyFrame[0])
    );

    packetTimeline.setCycleCount(1);
    packetTimeline.playFromStart();
}
private Point2D getDeviceCenter(int index) {

    VBox device = topologyDevices.get(index);

    Bounds bounds = device.localToScene(
            device.getBoundsInLocal()
    );

    return animationLayer.sceneToLocal(
            bounds.getCenterX(),
            bounds.getCenterY()
    );
}

    private int getLinkIndex(String source, String destination) {

    if (source == null || destination == null) {
        return -1;
    }

    if (source.equals("Computer 1") && destination.equals("Switch 1")) {
        return 0;
    }

    if (source.equals("Switch 1") && destination.equals("Router 1")) {
        return 1;
    }

    if (source.equals("Router 1") && destination.equals("Server 1")) {
        return 2;
    }

    return -1;
}

private void resetTopologyLinks() {

    for (Label link : topologyLinks) {
        link.setText("↓");
        link.setStyle(
                "-fx-text-fill: #38bdf8;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );
    }
}

    private VBox createTopologyPanel() {
    VBox panel = new VBox(14);

    panel.setPadding(new Insets(20));
    panel.setPrefWidth(240);
    panel.setMinWidth(220);
    panel.setStyle(UiStyle.CARD);
    panel.setAlignment(Pos.TOP_CENTER);

    Label heading = new Label("Network Topology");
    heading.setStyle(UiStyle.HEADING);

    Label description = new Label("Packet transmission path");
    description.setStyle(UiStyle.BODY);

    VBox devices = new VBox(7);
    devices.setAlignment(Pos.TOP_CENTER);

    String[] deviceNames = {
            "Computer 1", "Switch 1", "Router 1", "Server 1"
    };

    String[] deviceIcons = {"PC", "SW", "RT", "SRV"};

    for (int i = 0; i < deviceNames.length; i++) {
        VBox device = new VBox(3);

        Label icon = new Label(deviceIcons[i]);
        icon.setStyle(
                "-fx-text-fill: #38bdf8;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        Label name = new Label(deviceNames[i]);
        name.setStyle(
                "-fx-text-fill: #f8fafc;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );

        device.getChildren().addAll(icon, name);
        device.setAlignment(Pos.CENTER);
        device.setPadding(new Insets(9));
        device.setMaxWidth(Double.MAX_VALUE);
        device.setStyle(
                "-fx-background-color: #1e293b;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #0ea5e9;" +
                "-fx-border-radius: 8;"
        );

        topologyDevices.add(device);
        devices.getChildren().add(device);

        if (i < deviceNames.length - 1) {
            Label arrow = new Label("↓");
            arrow.setStyle(
                    "-fx-text-fill: #38bdf8;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;"
            );

            topologyLinks.add(arrow);
            devices.getChildren().add(arrow);
        }
    }

    topologyArea = new StackPane();
    topologyArea.setPrefSize(200, 330);
    topologyArea.setMinHeight(330);

    animationLayer = new Pane();
    animationLayer.setMouseTransparent(true);

    packetIcon = new Label("●");
    packetIcon.setStyle(
            "-fx-text-fill: #38bdf8;" +
            "-fx-font-size: 23px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #082f49;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 1 4 1 4;" +
            "-fx-border-color: #38bdf8;" +
            "-fx-border-radius: 20;"
    );
    packetIcon.setVisible(false);

    animationLayer.getChildren().add(packetIcon);

    topologyArea.getChildren().addAll(devices, animationLayer);
    StackPane.setAlignment(devices, Pos.TOP_CENTER);
    StackPane.setAlignment(animationLayer, Pos.TOP_LEFT);

    animationStatus = new Label("Waiting for simulation...");
    animationStatus.setWrapText(true);
    animationStatus.setAlignment(Pos.CENTER);
    animationStatus.setMaxWidth(Double.MAX_VALUE);
    animationStatus.setStyle(
            "-fx-text-fill: #cbd5e1;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 10;" +
            "-fx-background-color: #08111f;" +
            "-fx-background-radius: 7;"
    );

    Label legend = new Label("● Moving packet   ↓ Normal   ✕ Lost");
    legend.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 11px;"
    );

    panel.getChildren().addAll(
            heading, description, topologyArea, animationStatus, legend
    );

    return panel;
}

    public void show() {
        stage.setScene(new Scene(root, 1100, 720));
    }
}
