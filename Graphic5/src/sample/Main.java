package sample;

import javafx.application.Application;
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
                getTab(new Fractal1(), "Первый фрактал"),
                getTab(new Fractal2(), "Второй фрактал"),
                getTab(new Fractal3(), "Пример"));

        primaryStage.setScene(new Scene(pane, 800, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    Tab getTab(Fractal fractal, String title){
        Canvas canvas = new Canvas(800, 640);

        Tab tab = new Tab(title);
        tab.setContent(canvas);
        tab.setClosable(false);

        Render render = new Render(canvas, fractal);
        render.draw();

        return tab;
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);
    }
}
