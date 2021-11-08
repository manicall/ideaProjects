package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TabPane pane = new TabPane();

        TabBuilder tabBuilder = new TabBuilder();

        pane.getTabs().addAll(
                tabBuilder.getTabWithTextField("Третья поверхность", new SurfaceTask3()),
                tabBuilder.getSimpleTab("Первый пример", new SurfaceExample1()),
                tabBuilder.getSimpleTab("Второй пример", new SurfaceExample2()),
                tabBuilder.getSimpleTab("пример из интернета", new SurfaceExample3()));

        primaryStage.setTitle("Поверхности");
        primaryStage.setScene(new Scene(pane, 800, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
