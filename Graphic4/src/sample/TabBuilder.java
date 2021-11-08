package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabBuilder {
    public Tab getTabWithTextField(String title, final Surface surface){
        final Canvas canvas = getCanvas();
        surface.draw(canvas);

        TextField textField = new TextField();
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) surface.draw(canvas, Float.parseFloat(newValue));
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(textField, canvas);

        Tab tab = new Tab(title);
        tab.setContent(vBox);
        tab.setClosable(false);
        return tab;
    }

    public Tab getSimpleTab(String title, Surface surface){
        Tab tab = new Tab(title);
        Canvas canvas = getCanvas();
        surface.draw(canvas);
        tab.setContent(canvas);
        tab.setClosable(false);
        return tab;
    }

    private Canvas getCanvas(){
        Canvas canvas = new Canvas(800, 640);
        canvas.setScaleX(0.8);
        canvas.setScaleY(0.8);
        return canvas;
    }
}
