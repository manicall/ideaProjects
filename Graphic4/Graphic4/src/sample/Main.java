package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Stage - это контейнер, ассоциированный с окном

        // Если вы загляните в файл sample.fxml, то у видете в нем XML объявление элемента GridPane, т.е. табличного контейнера
        // Этот контейнер мы будем считать корневым, т.е. все элементы нашего приложения будут содержаться в нем
        Group root = new Group();

        primaryStage.setTitle("Фракталы");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 640));
        Canvas canvas = new Canvas(800, 640); // создаем новый объект Canvas с шириной 300px, и высотой 275px
        root.getChildren().add(canvas); // добавляем его в корневой контейнер


        MyGraphic myGraphic = new MyGraphic(canvas);

        myGraphic.draw();

        primaryStage.show(); // запускаем окно
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);
    }
}
