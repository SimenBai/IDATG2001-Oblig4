package no.simenbai.idatg2001.obligs.four;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        TableView tableView = new TableView();

        TableColumn<String, Personals> column1 = new TableColumn<>("First Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstname"));


        TableColumn<String, Personals> column2 = new TableColumn<>("Last Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("surname"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        this.memberArchive.getMembers().forEach(
                (member) -> tableView.getItems().add(member.getPersonals()
                ));

        StackPane root = new StackPane();
        root.getChildren().add(tableView);
        showScene(root);
    }
}
