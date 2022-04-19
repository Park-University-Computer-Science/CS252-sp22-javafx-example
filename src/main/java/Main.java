import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/** a simple note taking application */
public class Main extends Application {

    // TODO: add menu items for loading/saving

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
                if(saveFile == null) throw new IOException();
                PrintWriter fout = new PrintWriter(new FileWriter(saveFile));
                fout.print(noteTakingArea.getText());
                fout.close();
                saveNote.setDisable(true);
            } catch(IOException err) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("Error");
                dialog.setContentText("Could not save note to file: " + saveFile);
                dialog.showAndWait();
            }
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


        // create menu
        Menu fileMenu = new Menu("File");
        MenuItem loadMenuOption = new MenuItem("Load");
        MenuItem saveMenuOption = new MenuItem("Save");
        MenuItem aboutMenuOption = new MenuItem("About");
        aboutMenuOption.setOnAction(e -> {
            var aboutScreen = new Alert(Alert.AlertType.INFORMATION);
            aboutScreen.setTitle("About NoteTakingApp");
            aboutScreen.setContentText("Example JavaFX application for CS252");
            aboutScreen.showAndWait();
        });
        fileMenu.getItems().add(loadMenuOption);
        fileMenu.getItems().add(saveMenuOption);
        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(aboutMenuOption);
        MenuBar menu = new MenuBar(fileMenu, helpMenu);
        windowLayout.setTop(menu);

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

