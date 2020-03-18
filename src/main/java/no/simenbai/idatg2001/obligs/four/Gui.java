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
import javafx.stage.Stage;

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
            if (cellvalue.getValue() instanceof BasicMember) {
                return new SimpleStringProperty("Basic Member");
            } else if (cellvalue.getValue() instanceof SilverMember) {
                return new SimpleStringProperty("Silver Member");
            } else {
                return new SimpleStringProperty("Gold Member");
            }
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
            System.out.println("Details");
            showDetails(selectionModel.getSelectedItem());
        });

        Button deleteButton = new Button("Delete selected member");
        deleteButton.setOnAction(actionEvent -> {
            System.out.println("DELETE " + selectionModel.getSelectedItem().getMemberNo());
        });

        Button upgradeButton = new Button("Upgrade eligible members");
        upgradeButton.setOnAction(actionEvent -> {
            System.out.println("UPGRADE ");
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
        gSettings.add(addButton, 1,0);
        gSettings.add(detailsButton, 2, 0);
        gSettings.add(upgradeButton, 3, 0);
        gSettings.add(deleteButton, 4, 0);
        return gSettings;
    }

    private void showDetails(BonusMember selectedItem) {

    }

    private ObservableList<BonusMember> getMemberList() {
        return FXCollections.observableArrayList((memberArchive.getMembers()));
    }
}
