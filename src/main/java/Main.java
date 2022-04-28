import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

/** a simple note taking application */
public class Main extends Application {

    // area for writing / displaying notes
    private TextArea noteTakingArea = new TextArea("Enter notes here...");
    private Button saveNote = new Button("Save");   // save notes
    private Button loadNote = new Button("Load");   // load saved notes
    private Stage mainStage = null;
    private File saveFile = null;
    private Menu fileMenu = new Menu("File");
    private Menu helpMenu = new Menu("Help");
    private MenuBar menu = new MenuBar(fileMenu, helpMenu);
    private MenuItem loadMenuOption = new MenuItem("Load");
    private MenuItem saveMenuOption = new MenuItem("Save");
    private MenuItem saveAsMenuOption = new MenuItem("Save As");
    private MenuItem aboutMenuOption = new MenuItem("About");


    public void chooseFileSave() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select a save file for the note");
        saveFile = fc.showSaveDialog(mainStage);
    }

    public void chooseFileLoad() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select a file to load for the note");
        saveFile = fc.showOpenDialog(mainStage);
    }

    public void saveToFile() {
        if(saveFile == null) { // select a file to save to first:
            chooseFileSave();
        }
        if(saveFile == null) { // if still no file selected, user hit cancel
            return;
        }
        try {
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
    }

    public void loadFile() {
        chooseFileLoad();
        if(saveFile == null) { // if still no file selected, user hit cancel
            return;
        }
        try {
            BufferedReader fin = new BufferedReader(new FileReader(saveFile));
            String data = "";
            while(fin.ready())
                data += fin.readLine() +"\n";
            noteTakingArea.setText(data);
            fin.close();
        } catch(IOException err) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setContentText("Could not load note file: " + saveFile);
            dialog.showAndWait();
        }
    }

    /** Set up a maven run profile in intellij or use maven from the command-line.
        Use the javafx:run argument to start the javafx application.
        Update all code and comments in this tempalte to suit your own project.
     */
    @Override
    public void start(Stage stage) {
        this.mainStage = stage;

        saveNote.setDisable(true);
        saveMenuOption.setDisable(true);

        // load park logo
        ImageView logoView = null;
        try {
            URL url = getClass().getResource("/park.jpg");
            Image logo = new Image(url.openStream());
            logoView = new ImageView(logo);
        } catch (IOException e) {
            System.out.println("Error -- could not load logo");
        }

        // set action listeners
        noteTakingArea.textProperty().addListener(e -> {
            saveNote.setDisable(false);
            saveMenuOption.setDisable(false);
        });
        saveNote.setOnAction(e -> saveToFile());
        saveMenuOption.setOnAction(e -> saveToFile());
        saveAsMenuOption.setOnAction(e -> {
            chooseFileSave();
            saveToFile();
        });
        loadNote.setOnAction(e -> loadFile());
        loadMenuOption.setOnAction(e -> loadFile());
        aboutMenuOption.setOnAction(e -> {
            var aboutScreen = new Alert(Alert.AlertType.INFORMATION);
            aboutScreen.setTitle("About NoteTakingApp");
            aboutScreen.setContentText("Example JavaFX application for CS252");
            aboutScreen.showAndWait();
        });

        // create menu
        fileMenu.getItems().add(loadMenuOption);
        fileMenu.getItems().add(saveMenuOption);
        fileMenu.getItems().add(saveAsMenuOption);
        helpMenu.getItems().add(aboutMenuOption);

        // layout elements
        BorderPane windowLayout = new BorderPane();  // root window layout
        HBox bottomButtonPanel = new HBox();         // pane to hold buttons
        bottomButtonPanel.getChildren().add(loadNote);
        bottomButtonPanel.getChildren().add(saveNote);
        bottomButtonPanel.setAlignment(Pos.CENTER);  // center the buttons
        windowLayout.setBottom(bottomButtonPanel);   // put buttons at bottom of window
        windowLayout.setCenter(noteTakingArea);      // place note editing area in center
        windowLayout.setTop(menu);
        windowLayout.setBottom(logoView);

        // add root pane to window scene
        Scene scene = new Scene(windowLayout);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

