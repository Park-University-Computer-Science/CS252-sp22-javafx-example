import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/** a simple note taking application */
public class Main extends Application {

    // TODO: add menu items for loading/saving
    // TODO: add file dialog for loading/saving specific file
    // TODO: add an error dialog for bad file selection, io problem

    // area for writing / displaying notes
    private TextArea noteTakingArea = new TextArea("Enter notes here...");
    private Button saveNote = new Button("Save");   // save notes
    private Button loadNote = new Button("Load");   // load saved notes

    /** Set up a maven run profile in intellij or use maven from the command-line.
        Use the javafx:run argument to start the javafx application.
        Update all code and comments in this tempalte to suit your own project.
     */
    @Override
    public void start(Stage stage) {
        saveNote.setDisable(true);                  // disable save button when no changes to be saved

        // enable save button when changes made to textarea
        noteTakingArea.textProperty().addListener(e -> saveNote.setDisable(false));

        // save notes action
        saveNote.setOnAction(e -> {
            File saveFile = null;
            FileChooser fc = new FileChooser();
            fc.setTitle("Select a save file for the note");
            saveFile = fc.showSaveDialog(stage);
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

        // load notes action
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


        BorderPane windowLayout = new BorderPane();  // root window layout
        HBox bottomButtonPanel = new HBox();         // pane to hold buttons
        bottomButtonPanel.setAlignment(Pos.CENTER);  // center the buttons
        windowLayout.setBottom(bottomButtonPanel);   // put buttons at bottom of window
        windowLayout.setCenter(noteTakingArea);      // place note editing area in center

        // add buttons to button pane
        bottomButtonPanel.getChildren().add(loadNote);
        bottomButtonPanel.getChildren().add(saveNote);

        // add root pane to window scene
        Scene scene = new Scene(windowLayout);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

