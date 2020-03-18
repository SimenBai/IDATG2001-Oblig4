package no.simenbai.idatg2001.obligs.four;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Gui extends Application {
    private Stage stage;
    private MemberArchive memberArchive;

    public void startGui(String args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setTitle("Hello world");

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

    public void drawHelloWorldBtn() {
        StackPane root = new StackPane();
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setTitle("Test");
                drawHelloWorldBtn();
            }
        });
        root.getChildren().add(btn);
        showScene(root);
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
        nameColumn.setCellValueFactory(cellvalue -> {
            return new SimpleStringProperty(
                    cellvalue.getValue().getPersonals().getFirstname() +
                            " " +
                            cellvalue.getValue().getPersonals().getSurname());
        });

        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        memberTypeColumn.setCellValueFactory(cellvalue -> {
            return new SimpleStringProperty(BonusMember.getMemberType(cellvalue.getValue()).toString());
        });


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
            showDetails(selectionModel.getSelectedItem());
        });

        Button deleteButton = new Button("Delete selected member");
        deleteButton.setOnAction(actionEvent -> {
            System.out.println("DELETE " + selectionModel.getSelectedItem().getMemberNo());
        });

        Button upgradeButton = new Button("Upgrade eligible members");
        upgradeButton.setOnAction(actionEvent -> {
            memberArchive.checkMembers(LocalDate.now());
        });

        Button addButton = new Button("Add a members");
        addButton.setOnAction(actionEvent -> {
            System.out.println("ADD");
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

    private void showDetails(BonusMember bonusMember) {
        if(bonusMember == null){
            return;
        }
        Stage dialog = new Stage();
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



        gridPane.setPadding(new Insets(20));
        dialog.setScene(new Scene(gridPane));
        dialog.initOwner(this.stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    private ObservableList<BonusMember> getMemberList() {
        return FXCollections.observableArrayList((memberArchive.getMembers()));
    }
}
