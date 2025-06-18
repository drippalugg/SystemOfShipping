package com.example.pprodf2025;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class ShipManagementController {

    @FXML
    private TableView<Ship> shipsTable;

    @FXML
    private TableColumn<Ship, String> nameColumn;

    @FXML
    private TableColumn<Ship, String> typeColumn;

    @FXML
    private TableColumn<Ship, Double> latColumn;

    @FXML
    private TableColumn<Ship, Double> lonColumn;

    @FXML
    private TableColumn<Ship, Double> speedColumn;

    @FXML
    private ScatterChart<Number, Number> mapChart;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TextField latField;

    @FXML
    private TextField lonField;

    @FXML
    private TextField speedField;

    private ObservableList<Ship> shipData = FXCollections.observableArrayList();
    private Random random = new Random();

    @FXML
    private void initialize() {
        // Настройка таблицы
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        latColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        lonColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        shipsTable.setItems(shipData);

        // Настройка выпадающего списка
        typeCombo.getItems().addAll("Грузовое", "Пассажирское", "Танкер", "Рыболовное");

        // Настройка карты
        NumberAxis xAxis = (NumberAxis) mapChart.getXAxis();
        xAxis.setLabel("Долгота");
        NumberAxis yAxis = (NumberAxis) mapChart.getYAxis();
        yAxis.setLabel("Широта");
        mapChart.setTitle("Карта расположения судов");

        // Заполняем тестовыми данными
        generateTestData();
    }

    @FXML
    private void handleAddShip() {
        try {
            Ship ship = new Ship(
                    nameField.getText(),
                    typeCombo.getValue(),
                    Double.parseDouble(latField.getText()),
                    Double.parseDouble(lonField.getText()),
                    Double.parseDouble(speedField.getText())
            );
            shipData.add(ship);
            updateChart();

            // Очищаем поля
            nameField.clear();
            typeCombo.getSelectionModel().clearSelection();
            latField.clear();
            lonField.clear();
            speedField.clear();
        } catch (Exception e) {
            showAlert("Ошибка ввода", "Проверьте правильность введенных данных");
        }
    }

    @FXML
    private void handleDeleteShip() {
        Ship selected = shipsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            shipData.remove(selected);
            updateChart();
        }
    }

    @FXML
    private void handleRandomMove() {
        for (Ship ship : shipData) {
            ship.setLatitude(ship.getLatitude() + (random.nextDouble() - 0.5) * 2);
            ship.setLongitude(ship.getLongitude() + (random.nextDouble() - 0.5) * 4);
            ship.setSpeed(ship.getSpeed() + (random.nextDouble() - 0.5) * 2);
        }
        shipsTable.refresh();
        updateChart();
    }

    private void updateChart() {
        mapChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Судна");

        for (Ship ship : shipData) {
            XYChart.Data<Number, Number> data = new XYChart.Data<>(
                    ship.getLongitude(),
                    ship.getLatitude()
            );

            // Добавляем кружок для точки
            data.setNode(new Circle(5, getColorByShipType(ship.getType())));
            series.getData().add(data);
        }

        mapChart.getData().add(series);
    }

    private Color getColorByShipType(String type) {
        return switch (type) {
            case "Грузовое" -> Color.BLUE;
            case "Пассажирское" -> Color.GREEN;
            case "Танкер" -> Color.RED;
            case "Рыболовное" -> Color.ORANGE;
            default -> Color.BLACK;
        };
    }

    private void generateTestData() {
        shipData.addAll(
                new Ship("Атлантика", "Грузовое", 45.0, -30.0, 15.5),
                new Ship("Океан", "Пассажирское", 35.0, -20.0, 22.0),
                new Ship("Нефтевоз-1", "Танкер", 25.0, -10.0, 12.0),
                new Ship("Рыбак", "Рыболовное", 55.0, -40.0, 8.5)
        );
        updateChart();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Ship {
        private String name;
        private String type;
        private double latitude;
        private double longitude;
        private double speed;

        public Ship(String name, String type, double latitude, double longitude, double speed) {
            this.name = name;
            this.type = type;
            this.latitude = latitude;
            this.longitude = longitude;
            this.speed = speed;
        }

        // Геттеры и сеттеры
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public double getLatitude() { return latitude; }
        public void setLatitude(double latitude) { this.latitude = latitude; }
        public double getLongitude() { return longitude; }
        public void setLongitude(double longitude) { this.longitude = longitude; }
        public double getSpeed() { return speed; }
        public void setSpeed(double speed) { this.speed = speed; }
    }
}