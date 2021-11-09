package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TabBuilder {
    public Tab getTabWithTextField(String TabTitle, String FieldTitle, final Surface surface){
        final Canvas canvas = getCanvas();
        surface.draw(canvas);

        TextField textField = new TextField("1");
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.equals("")) surface.draw(canvas, Float.parseFloat(newValue));
            }
        });

        Label label = new Label(FieldTitle);

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        hBox.getChildren().addAll(label, textField);
        vBox.getChildren().addAll(hBox, canvas);

        Tab tab = new Tab(TabTitle);
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
