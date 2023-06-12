package sample;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

public class Main extends Application {
    String bdHost = "localhost";
    String bdPost = "3306";
    String bdUser = "root";
    String bdPass = "12345";
    String bdName = "mybd";
    String url = "jdbc:mysql://localhost:3306/" + bdName;
    String username = bdUser;
    String password = bdPass;

    public static void main(String[] args) {

        Application.launch(args);
    }

    private void setSceneBackground(Parent root, Color color) {
        root.setStyle("-fx-background-color: "
                + toHexCode(color) + ";");
    }

    private String toHexCode(Color color) {
        return "#"
                + color.toString()
                .substring(2, 8);

    }

    @Override
    public void start(Stage stage) throws Exception {


        Label lbl = new Label();
//        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
//
//        lbl.setPrefWidth(screenWidth);

        TextField textField_position = new TextField();
        TextField textField_name_Employee = new TextField();
        TextField textField_surname = new TextField();
        TextField textField_date_of_birth = new TextField();
        TextField textField_salary = new TextField();
        TextField textField_client_id = new TextField();
        TextField textField_car_id = new TextField();
        TextField textField_id_Employees = new TextField();
        TextField textField_Id_Orders = new TextField();

        textField_salary.setPrefWidth(690);

        textField_position.setPrefColumnCount(11);
        textField_name_Employee.setPrefColumnCount(11);
        textField_surname.setPrefColumnCount(11);
        textField_date_of_birth.setPrefColumnCount(11);
        textField_salary.setPrefColumnCount(11);
        textField_id_Employees.setPrefColumnCount(11);
        textField_client_id.setPrefColumnCount(11);
        textField_car_id.setPrefColumnCount(11);
        textField_Id_Orders.setPrefColumnCount(11);


        textField_surname.setPromptText("Введіть прізвище працівника");
        textField_name_Employee.setPromptText("Введіть ім'я працівника");
        textField_position.setPromptText("Введіть посаду працівника");
        textField_date_of_birth.setPromptText("Введіть дату народження працівника");
        textField_salary.setPromptText("Введіть зп працівника");
        textField_client_id.setPromptText("Введіть код клієнта");
        textField_car_id.setPromptText("Введіть код марки авто");
        textField_id_Employees.setPromptText("Введіть код працівника для звільнення");
        textField_Id_Orders.setPromptText("Введіть код працівника для звільнення");


        Button btn = new Button("Виводимо клієнтів, замовлення яких скасовано");
        Button btn2 = new Button("Виводимо середню з/п працівника");
        Button btn3 = new Button("Додати працівника");
        Button btn4 = new Button("Створити всі таблиці");
        Button btn5 = new Button("Вивести замовлення, що комплектуються");
        Button btn6 = new Button("Вивести всі машини");
        Button btn7 = new Button("Замовити авто");
        Button btn8 = new Button("Звільнити");
        Button btn9 = new Button("Вивести всі замовлення");
        Button btn10 = new Button("Обчислити витрати на зп за місяць");
        Button btn11 = new Button("Вивести кількість виконаних замовлень");

        btn.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("" +
                        "SELECT c.name_Client\n" +
                        "FROM clients c\n" +
                        "JOIN orders o ON c.idClients = o.client_id\n" +
                        "WHERE o.status = 'Cancelled';\n");

                StringBuilder resultText = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append(", ");
                    }
                    resultText.append("\n");
                }
                lbl.setText(resultText.toString()
                        .replace("name_Client: ", "Замовлення клієнта ")
                        .replace(",", " - Скасовано"));
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn2.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT AVG(salary) AS average_salary\n" +
//                        "FROM employees;\n");
                String sqlQuery = "" +
                        "SELECT AVG(salary) AS average_salary\n" +
                        "FROM employees;\n";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery();
                StringBuilder resultText = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append(", ");
                    }
                    resultText.append("\n");
                }
                lbl.setText(resultText.toString()
                        .replace("average_salary: ", "Середня заробітна плата працівника - ")
                        .replace(".0000", "")
                        .replace(",", ""));
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn3.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                int index = 5;
                String sqlQuery = "INSERT INTO employees (name_Employees, " +
                        "surname, " +
                        "position, " +
                        "date_of_birth, " +
                        "salary)\n" +
                        "VALUES ( ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, textField_name_Employee.getText());
                preparedStatement.setString(2, textField_surname.getText());
                preparedStatement.setString(3, textField_position.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(textField_date_of_birth.getText()));
                preparedStatement.setInt(5, Integer.parseInt(textField_salary.getText()));
                preparedStatement.executeUpdate();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM employees");
                StringBuilder resultText = new StringBuilder();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append(", ");
                    }
                    resultText.append("\n");
                }
                lbl.setText(resultText.toString());

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn4.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT AVG(salary) AS average_salary\n" +
//                        "FROM employees;\n");
                String sqlQuery =
                        "CREATE TABLE clients ("
                                + "idClients INT NOT NULL,"
                                + "name_Client VARCHAR(45) DEFAULT NULL,"
                                + "surname VARCHAR(45) DEFAULT NULL,"
                                + "address VARCHAR(45) DEFAULT NULL,"
                                + "phone VARCHAR(45) DEFAULT NULL,"
                                + "Email VARCHAR(45) DEFAULT NULL,"
                                + "PRIMARY KEY (idClients)"
                                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci" +
                                "CREATE TABLE `cars` (\n" +
                                "  `id_Cars` int NOT NULL AUTO_INCREMENT,\n" +
                                "  `name` varchar(45) DEFAULT NULL,\n" +
                                "  `producer` varchar(45) DEFAULT NULL,\n" +
                                "  `model` varchar(45) DEFAULT NULL,\n" +
                                "  `graduation year` varchar(45) DEFAULT NULL,\n" +
                                "  `price` int DEFAULT NULL,\n" +
                                "  PRIMARY KEY (`id_Cars`)\n" +
                                ") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                                "SELECT * FROM mybd.employees;" +
                                "CREATE TABLE `employees` (\n" +
                                "  `id_Employees` int NOT NULL AUTO_INCREMENT,\n" +
                                "  `name_Employees` varchar(45) DEFAULT NULL,\n" +
                                "  `surname` varchar(45) DEFAULT NULL,\n" +
                                "  `position` varchar(45) DEFAULT NULL,\n" +
                                "  `date_of_birth` date DEFAULT NULL,\n" +
                                "  `salary` int DEFAULT NULL,\n" +
                                "  PRIMARY KEY (`id_Employees`)\n" +
                                ") ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                                "CREATE TABLE `manufacturers` (\n" +
                                "  `id_Manufacturers` int NOT NULL,\n" +
                                "  `name_Manufacturer` varchar(45) DEFAULT NULL,\n" +
                                "  `country` varchar(45) DEFAULT NULL,\n" +
                                "  PRIMARY KEY (`id_Manufacturers`)\n" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n" +
                                "CREATE TABLE `orders` (\n" +
                                "  `Id_Orders` int NOT NULL,\n" +
                                "  `car_id` varchar(45) DEFAULT NULL,\n" +
                                "  `client_id` varchar(45) DEFAULT NULL,\n" +
                                "  `order_Date` varchar(45) DEFAULT NULL,\n" +
                                "  `status` varchar(45) DEFAULT NULL,\n" +
                                "  PRIMARY KEY (`Id_Orders`)\n" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n";
                statement.executeUpdate(sqlQuery);
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn5.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("" +
                        "SELECT c.name_Client, " +
                        "c.surname, " +
                        "c.address, " +
                        "c.phone\n" +
                        "FROM clients c\n" +
                        "JOIN orders o ON c.idClients = o.client_id\n" +
                        "WHERE o.status = 'In Progress';\n");

                StringBuilder resultText = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append("\n");
                    }
                    resultText.append("\n");
                }
                lbl.setText(resultText.toString()
                        .replace("name_Client: ", "Замовлення комплектується для цих клієнтів\n")
                        .replace("\nsurname: ", " "));
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn6.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("" +
                        "SELECT id_Cars, " +
                        "name, " +
                        "producer " +
                        "FROM cars");

                StringBuilder resultText = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append(", ");
                    }
                    resultText.append("\n");
                }
                lbl.setText(resultText.toString()
                        .replace("id_Cars: ", "Код моделі - ")
                        .replace("name: ", "Назва моделі - ")
                        .replace("producer: ", "Виробник - "));
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn7.setOnAction(event -> {
            try {

                Connection connection = DriverManager.getConnection(url, username, password);
                String sqlQuery = "INSERT INTO orders (" +
                        "car_id, " +
                        "client_id, " +
                        "order_Date, " +
                        "status" +
                        ") " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
                preparedStatement.setInt(1, Integer.parseInt(textField_car_id.getText()));
                preparedStatement.setInt(2, Integer.parseInt(textField_client_id.getText()));
                preparedStatement.setDate(3, currentDate);
                preparedStatement.setString(4, "Pending");
                lbl.setText("Авто замовлено");

                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn8.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                String sqlQuery = "DELETE " +
                        "FROM employees " +
                        "WHERE id_Employees = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setInt(1, Integer.parseInt(textField_id_Employees.getText()));
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    lbl.setText("Працівник успішно видалений.");
                } else {
                    lbl.setText("Не вдалося знайти працівника з таким айді.");
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn9.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("" +
                        "SELECT orders.status, " +
                        "cars.id_Cars, " +
                        "cars.name, " +
                        "cars.producer, " +
                        "clients.name_Client, " +
                        "clients.surname, " +
                        "clients.address, " +
                        "clients.phone\n" +
                        "FROM orders\n" +
                        "INNER JOIN cars ON orders.car_id = cars.id_Cars\n" +
                        "INNER JOIN clients ON orders.client_id = clients.idClients\n");

                StringBuilder resultText = new StringBuilder();

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultText.append(metaData.getColumnName(i))
                                .append(": ")
                                .append(resultSet.getString(i))
                                .append(", ");
                    }
                    resultText.append("\n\n");
                }
                lbl.setText(resultText.toString()
                        .replace("status: ", "Статус замовлення - ")
                        .replace("id_Cars: ", "\nКод моделі - ")
                        .replace("name: ", "Назва моделі - ")
                        .replace("producer: ", "Виробник - ")
                        .replace("address: ", "\nАдреса отримувача - ")
                        .replace("name_Client: ", "\nОтримувач - ")
                        .replace(", surНазва моделі -", "")
                        .replace(", phone: ", "\nТелефон - ")
                        .replace(",", ""));
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn10.setOnAction(event -> {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                CallableStatement statement = connection.prepareCall("{ ? = call calculateTotalSalary() }");
                statement.registerOutParameter(1, Types.DECIMAL);
                statement.execute();
                BigDecimal totalSalary = statement.getBigDecimal(1);
                lbl.setText(("Витрати на зарплату за місяць: " + totalSalary));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        btn11.setOnAction(event -> {
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 CallableStatement stmt = conn.prepareCall("{ ? = call GetCompletedOrdersCount()}")) {
                stmt.registerOutParameter(1, Types.DECIMAL);
                stmt.execute();
                BigDecimal totalCompletedOrders = stmt.getBigDecimal(1);
                lbl.setText("Всього виконаних замовлень - " + totalCompletedOrders);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10,
                textField_name_Employee,
                textField_surname,
                textField_position,
                textField_date_of_birth,
                textField_salary,
                btn,
                btn5,
                btn2,
                btn4,
                btn6,
                btn3,
                textField_car_id,
                textField_client_id,
                btn7,
                textField_id_Employees,
                btn8,
                btn9,
                btn10,
                btn11,
                lbl);
        root.setAlignment(Pos.CENTER);
        lbl.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 1900, 1000);
        stage.setScene(scene);
        setSceneBackground(root, Color.GOLD);
        stage.setTitle("Автосалон");
        stage.setResizable(false);
        stage.show();
    }
}
