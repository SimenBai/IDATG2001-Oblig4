package no.simenbai.idatg2001.obligs.four;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

public class Gui extends Application {
    private Stage stage;
    private MemberArchive memberArchive;

    public void startGui(String args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        setTitle("Bonus member application");

        this.memberArchive = new MemberArchive();
        this.memberArchive.populate();

        drawMemberArchiveTable();
    }

    public void showScene(Pane root) {
        this.stage.setScene(new Scene(root, 800, 600));
        this.stage.show();
    }

    private void setTitle(String title) {
        stage.setTitle(title);
    }

    public void drawMemberArchiveTable() {
        TableView<BonusMember> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No rows to display"));

        TableColumn<BonusMember, Integer> memberNumberColumn = new TableColumn<>("Member number");
        TableColumn<BonusMember, String> nameColumn = new TableColumn<>("Name");
        TableColumn<BonusMember, String> pointsColumn = new TableColumn<>("Points");
        TableColumn<BonusMember, String> memberTypeColumn = new TableColumn<>("Member Type");

        memberNumberColumn.setMinWidth(200);
        nameColumn.setMinWidth(200);
        pointsColumn.setMinWidth(200);
        memberTypeColumn.setMinWidth(200);


        memberNumberColumn.setCellValueFactory(new PropertyValueFactory<>("memberNo"));
        nameColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(
                cellvalue.getValue().getPersonals().getFirstname() +
                        " " +
                        cellvalue.getValue().getPersonals().getSurname()));

        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        memberTypeColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(BonusMember.getMemberType(cellvalue.getValue()).toString()));


        ObservableList<BonusMember> list = getMemberList();
        tableView.setItems(list);

        tableView.getColumns().addAll(memberNumberColumn, nameColumn, pointsColumn, memberTypeColumn);

        tableView.setRowFactory(tv -> {
            TableRow<BonusMember> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    BonusMember clickedRow = row.getItem();
                    System.out.println(clickedRow.getMemberNo());
                }
            });
            return row;
        });

        BorderPane root = new BorderPane();
        root.setTop(getSettingMenu(tableView));
        root.setCenter(tableView);
        showScene(root);
    }

    public GridPane getSettingMenu(TableView<BonusMember> tableView) {
        TableView.TableViewSelectionModel<BonusMember> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        Button detailsButton = new Button("See details of selected member");
        detailsButton.setOnAction(actionEvent -> {
            if (showDetails(selectionModel.getSelectedItem())) {
                drawMemberArchiveTable();
            }
        });

        Button deleteButton = new Button("Delete selected member");
        deleteButton.setOnAction(actionEvent -> {
            memberArchive.removeMember(selectionModel.getSelectedItem().getMemberNo());
            drawMemberArchiveTable();
        });

        Button upgradeButton = new Button("Upgrade eligible members");
        upgradeButton.setOnAction(actionEvent -> {
            memberArchive.checkMembers(LocalDate.now());
            drawMemberArchiveTable();
        });

        Button addButton = new Button("Add a members");
        addButton.setOnAction(actionEvent -> {
            addUserView();
            drawMemberArchiveTable();
        });

        GridPane gSettings = new GridPane();
        gSettings.setAlignment(Pos.CENTER);
        gSettings.setHgap(10);
        gSettings.setVgap(10);
        gSettings.setPadding(new Insets(20));
        gSettings.add(addButton, 1, 0);
        gSettings.add(detailsButton, 2, 0);
        gSettings.add(upgradeButton, 3, 0);
        gSettings.add(deleteButton, 4, 0);
        return gSettings;
    }

    private boolean showDetails(BonusMember bonusMember) {
        if (bonusMember == null) {
            return false;
        }

        final boolean[] returnValue = {false};

        Stage dialog = new Stage();
        dialog.setTitle("Detailed member view");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        Text displaying = new Text("Displaying member: ");
        Text memberNo = new Text(Integer.toString(bonusMember.getMemberNo()));

        Text fullNameLabel = new Text("Fullname: ");
        Text fullName = new Text(
                bonusMember.getPersonals().getFirstname() + " " +
                        bonusMember.getPersonals().getSurname());

        Text emailLabel = new Text("E-mail address: ");
        Text email = new Text(bonusMember.getPersonals().getEMailAddress());

        Text memberTypeLabel = new Text("Member type: ");
        Text memberType = new Text(BonusMember.getMemberType(bonusMember).toString());

        Text enrolledLabel = new Text("Enrolled date: ");
        Text enrolled = new Text(bonusMember.getEnrolledDate().toString());

        Text qPointslabel = new Text("Qualifying points: ");
        Text qPoints = new Text(Integer.toString(bonusMember.findQualificationPoints(LocalDate.now())));

        Text pointsLabel = new Text("Points: ");
        Text points = new Text(Integer.toString(bonusMember.getPoints()));

        Text spacer = new Text(" ");
        Text spacer1 = new Text(" ");

        Button editButton = new Button("Edit details");
        editButton.setOnAction(actionEvent -> returnValue[0] = editView(dialog, bonusMember));

        gridPane.add(displaying, 0, 0);
        gridPane.add(memberNo, 1, 0);

        gridPane.add(fullNameLabel, 0, 1);
        gridPane.add(fullName, 1, 1);

        gridPane.add(emailLabel, 0, 2);
        gridPane.add(email, 1, 2);

        gridPane.add(spacer, 0, 3);

        gridPane.add(memberTypeLabel, 0, 4);
        gridPane.add(memberType, 1, 4);

        gridPane.add(enrolledLabel, 0, 5);
        gridPane.add(enrolled, 1, 5);

        gridPane.add(qPointslabel, 0, 6);
        gridPane.add(qPoints, 1, 6);

        gridPane.add(pointsLabel, 0, 7);
        gridPane.add(points, 1, 7);

        gridPane.add(spacer1, 0, 8);
        gridPane.add(editButton, 0, 9, 2, 1);
        editButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);


        gridPane.setPadding(new Insets(20));
        dialog.setScene(new Scene(gridPane));
        dialog.initOwner(this.stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
        return returnValue[0];
    }

    private boolean editView(Stage details, BonusMember bonusMember) {
        Stage editStage = new Stage();
        // Seems to not be needed
        // editStage.setOnHiding(windowEvent -> System.out.println("Stage is closing"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        editStage.setTitle("Edit member");

        Text title = new Text("Editing member: " + bonusMember.getMemberNo());

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        Label registerPointsLabel = new Label("Register points:");
        TextField points = new TextField();
        points.setTextFormatter(textFormatter);

        Text spacer = new Text(" ");

        Label changePassword = new Label("Change password:");
        Label password1Label = new Label("Old password:");
        PasswordField password1 = new PasswordField();
        Label password2Label = new Label("New password:");
        PasswordField password2 = new PasswordField();

        Button saveButton = new Button("Save details");
        saveButton.setOnAction(actionEvent -> {
            if (!points.getText().isEmpty()) {
                //We know this must be an integer because of the text formatter
                bonusMember.registerPoints(Integer.parseInt(points.getText()));
            }
            if (!password1.getText().isEmpty()) {
                if (!password2.getText().isEmpty()) {
                    bonusMember.getPersonals().changePassword(password1.getText(), password2.getText());
                }
            }
            editStage.close();
        });

        int i = 0;
        gridPane.add(title, 0, i++);
        gridPane.add(registerPointsLabel, 0, i++);
        gridPane.add(points, 0, i++);
        gridPane.add(spacer, 0, i++);
        gridPane.add(changePassword, 0, i++);
        gridPane.add(password1Label, 0, i++);
        gridPane.add(password1, 0, i++);
        gridPane.add(password2Label, 0, i++);
        gridPane.add(password2, 0, i++);
        gridPane.add(saveButton, 0, i);

        gridPane.setPadding(new Insets(20));
        editStage.setScene(new Scene(gridPane));
        editStage.initOwner(details);
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.showAndWait();
        return true;
    }

    private void addUserView() {
        Stage editStage = new Stage();
        // Seems to not be needed
        // editStage.setOnHiding(windowEvent -> System.out.println("Stage is closing"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        editStage.setTitle("Add new member");

        Text title = new Text("Adding new member");


        Label firstNameLabel = new Label("First name: ");
        TextField firstName = new TextField();

        Label surnameLabel = new Label("Surname:");
        TextField surname = new TextField();

        Label emailLabel = new Label("Email address:");
        TextField email = new TextField();

        Text spacer = new Text(" ");
        Text spacer1 = new Text(" ");

        Label passwordLabel = new Label("Password:");
        PasswordField password = new PasswordField();

        Button saveButton = new Button("Save details");
        saveButton.setOnAction(actionEvent -> {
            if (firstName.getText().isEmpty() ||
                    surname.getText().isEmpty() ||
                    email.getText().isEmpty() ||
                    password.getText().isEmpty()) {
                return;
            }

            if (email.getText().matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                System.out.println(email.getText());
                Personals personals = new Personals(firstName.getText(), surname.getText(), email.getText(), password.getText());
                memberArchive.addMember(personals, LocalDate.now());
                editStage.close();
            }
        });

        int i = 0;
        gridPane.add(title, 0, i++);
        gridPane.add(firstNameLabel, 0, i++);
        gridPane.add(firstName, 0, i++);
        gridPane.add(surnameLabel, 0, i++);
        gridPane.add(surname, 0, i++);
        gridPane.add(spacer, 0, i++);
        gridPane.add(emailLabel, 0, i++);
        gridPane.add(email, 0, i++);
        gridPane.add(spacer1, 0, i++);
        gridPane.add(passwordLabel, 0, i++);
        gridPane.add(password, 0, i++);
        gridPane.add(saveButton, 0, i);

        gridPane.setPadding(new Insets(20));
        editStage.setScene(new Scene(gridPane));
        editStage.initOwner(this.stage);
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.showAndWait();
    }

    private ObservableList<BonusMember> getMemberList() {
        return FXCollections.observableArrayList((memberArchive.getMembers()));
    }
}
