package PDF_SAR;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class Main extends Application {
    Task<Boolean> worker;
    private static final Logger logger = LogManager.getLogger(Main.class);


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
        primaryStage.setTitle("PDF Search and Rescue");


        final Button button_ChooseDirectory = new Button("Choose Starting Directory");

        final Button button_ChooseCompletedDirectory = new Button("Choose Completed Directory");

        final Button button_StartSearch = new Button("Begin Search and Rescue");
        final TextField searchCriteria = new TextField("");
        final Label label_SearchCriteria = new Label("Search Criteria:");
        final TextField startingDirectory = new TextField();
        final TextField finishingDirectory = new TextField();
        final Pane rootPane = new VBox(12);
        final Label status = new Label("PDF SAR awaiting orders...");
        final GridPane gridPane = new GridPane();
        final ProgressBar progressBar = new ProgressBar(0.0);

        GridPane.setConstraints(button_ChooseDirectory, 0, 0);
        GridPane.setConstraints(startingDirectory, 2, 0, GridPane.REMAINING, 1);


        GridPane.setConstraints(button_ChooseCompletedDirectory, 0, 10);

        GridPane.setConstraints(finishingDirectory, 2, 10, GridPane.REMAINING, 1);


        GridPane.setConstraints(label_SearchCriteria, 0, 20);
        GridPane.setConstraints(searchCriteria, 2, 20, GridPane.REMAINING, 1);


        GridPane.setConstraints(button_StartSearch, 50, 30);
        GridPane.setConstraints(status, 0, 29, 30, 1);
        GridPane.setConstraints(progressBar, 0, 30, 30, 3);

        gridPane.setHgap(6);
        gridPane.setVgap(6);
        gridPane.getChildren().addAll(button_ChooseDirectory, button_ChooseCompletedDirectory, button_StartSearch, progressBar, label_SearchCriteria, startingDirectory, finishingDirectory, searchCriteria, status);

        rootPane.getChildren().addAll(gridPane);
        rootPane.setPadding(new Insets(12, 12, 12, 12));


        button_ChooseDirectory.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                final DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Pick the Folder to Search");
                File file = directoryChooser.showDialog(null);
                if (file != null) {

                    startingDirectory.setText(file.getPath() + File.separator);
                }
            }
        });

        button_ChooseCompletedDirectory.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                final DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Pick the Folder to Search");
                File file = directoryChooser.showDialog(null);
                if (file != null) {
                    finishingDirectory.setText(file.getPath() + File.separator);
                }
            }
        });

        button_StartSearch.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                button_StartSearch.setDisable(true);
                progressBar.setProgress(0.0);
                status.setText("Alpha team is in position!");
                int fileCount = 0;
                File dir = new File(startingDirectory.getText());
                if (dir.isDirectory()) {
                    fileCount = dir.list().length;
                    System.out.println(fileCount);
                }
                worker = createWorker(fileCount, startingDirectory.getText(), searchCriteria.getText());
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(worker.progressProperty());

                worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    public void handle(WorkerStateEvent workerStateEvent) {
                        if (worker.getValue()) {
                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0.0);
                            button_StartSearch.setDisable(false);
                            status.setText("PDF SAR awaiting orders...");
                        }else{
                            System.out.println("Failed");
                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0.0);
                            button_StartSearch.setDisable(false);
                            status.setText("PDF SAR awaiting orders...");
                        }
                    }
                });

                worker.messageProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        System.out.println(t1);
//
                    }
                });

                new Thread(worker).start();

            }
        });

        primaryStage.setScene(new Scene(rootPane));
        primaryStage.show();
    }

    public Task<Boolean> createWorker(final int fileCount, final String path, final String searchPhrase) {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Boolean result = true;

                TextExtract textExtract = new TextExtract();
                Utils utils = new Utils();
                RenameFile renameFile = new RenameFile();

                List<String> filePaths = null;
                String extractedText = null;
                List<File> trueFiles = null;


                System.out.println("Path = " + path);
                System.out.println("Search Phrase = " + searchPhrase);

                utils.setDirectory(new File(path));
                filePaths = utils.pathList();
                int x = 0;
                for (String sPath : filePaths) {
                    textExtract.setPdDocument(sPath);
                    extractedText = textExtract.getTextFromCurrentDoc();
                    if (extractedText.contains(searchPhrase)) {
                        System.out.println(sPath + "  contains Search Phrase: " + searchPhrase);
                        utils.addPathToTrueList(sPath);
                    }
                    textExtract.closeDocument();
                    updateProgress(x + 1, filePaths.size());
                    x++;
                }
                updateProgress(0.0, 1);
                x = 0;
                trueFiles = utils.getFilesThatContainPhrase();

                for (File f : trueFiles) {
                    renameFile.setOldFileName(f);
                    String newFilePath = "C:\\Users\\jcarlett\\Desktop\\Combined\\";
                    //String extension = FilenameUtils.getExtension(f.toString());
                    String builtFilePath = newFilePath + f.getName();
                    System.out.println(builtFilePath);
                    renameFile.setNewFileName(new File(builtFilePath));
                    Boolean isRenamed = renameFile.renameFile();
                    if (isRenamed) {
                        System.out.println("Successfully renamed " + f.toString() + " to " + builtFilePath);
                    } else {
                        System.out.println("There was a problem renaming " + f.toString());
                    }

                    updateProgress(x + 1, trueFiles.size());
                    x++;

                }

                    return true;






            }


        };


    }


    public static void main(String[] args) {

        launch(args);
    }
}
