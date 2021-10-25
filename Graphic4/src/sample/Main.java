package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TabPane pane = new TabPane();

        pane.getTabs().addAll(
                getTab("Первый пример", new SurfaceExample1()),
                getTab("Второй пример", new SurfaceExample2()));

        primaryStage.setTitle("Поверхности");
        primaryStage.setScene(new Scene(pane, 800, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    Tab getTab(String title, Surface surface){
        Canvas canvas = new Canvas(800, 640);
        canvas.setScaleX(0.8);
        canvas.setScaleY(0.8);
        canvas.rotateProperty();

        surface.draw(canvas);

        Tab tab = new Tab(title);
        tab.setContent(canvas);
        tab.setClosable(false);
        return tab;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
