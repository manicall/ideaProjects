package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TabPane pane = new TabPane();

        pane.getTabs().addAll(
                getTab("Первая поверхность"));

        primaryStage.setTitle("Поверхности");
        primaryStage.setScene(new Scene(pane, 800, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    Tab getTab(String title){
        Canvas canvas = new Canvas(800, 640);

        Tab tab = new Tab(title);
        tab.setContent(canvas);
        tab.setClosable(false);

        Render render = new Render(canvas);
        render.draw();

        return tab;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
