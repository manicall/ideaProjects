package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();

        primaryStage.setTitle("3D графика");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 640));
        Canvas canvas = new Canvas(800, 640);
        root.getChildren().add(canvas);

        MyGraphic myGraphic = new MyGraphic(canvas);

        myGraphic.draw();

        primaryStage.show(); // запускаем окно
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);
    }
}
