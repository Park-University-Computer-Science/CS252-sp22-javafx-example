import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    /** Set up a maven run profile in intellij or use maven from the command-line.
        Use the javafx:run argument to start the javafx application.
        Update all code and comments in this tempalte to suit your own project.
     */
    @Override
    public void start(Stage stage) {
        Label l = new Label("CS252 JavaFX Starter Template");

        Button saveNote = new Button("Save");
        Button loadNote = new Button("Load");
        saveNote.setDisable(true);

        TextArea noteTakingArea = new TextArea("Enter notes here...");


        BorderPane windowLayout = new BorderPane();
        HBox bottomButtonPanel = new HBox();

        /*
        StackPane p = new StackPane();
        p.getChildren().add(saveNote);
        p.setAlignment(saveNote, Pos.CENTER);
        */
        windowLayout.setBottom(bottomButtonPanel);
        windowLayout.setCenter(noteTakingArea);

        bottomButtonPanel.getChildren().add(loadNote);
        bottomButtonPanel.getChildren().add(saveNote);

        noteTakingArea.setOnKeyTyped(e -> saveNote.setDisable(false));
        saveNote.setOnAction(e -> {
            File saveFile = new File("savedNote.txt");
            try {
                PrintWriter fout = new PrintWriter(new FileWriter(saveFile));
                fout.print(noteTakingArea.getText());
                fout.close();
            } catch(IOException err) {
                System.out.println("ERROR: could not save file");
            }
            System.out.println(noteTakingArea.getText());
            saveNote.setDisable(true);
        });
        loadNote.setOnAction(e -> {
            try {
                File saveFile = new File("savedNote.txt");
                BufferedReader fin = new BufferedReader(new FileReader(saveFile));
                String data = "";
                while(fin.ready())
                    data += fin.readLine() +"\n";
                noteTakingArea.setText(data);
            } catch(IOException err) {
                System.out.println("ERROR LOADING FILE");
            }
        });


        Scene scene = new Scene(windowLayout);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

