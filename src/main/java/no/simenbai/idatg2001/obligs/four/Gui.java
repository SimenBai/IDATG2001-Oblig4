package no.simenbai.idatg2001.obligs.four;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void showScene(StackPane root) {
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
        
        StackPane root = new StackPane();
        root.getChildren().add(tableView);
        showScene(root);
    }

    private ObservableList<BonusMember> getMemberList() {
        return FXCollections.observableArrayList((memberArchive.getMembers()));
    }
}
