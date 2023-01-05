package com.example.onlineclassregister;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class createStudentController {
    @FXML
    private TextField email;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    public void addButtonClick() {
    }

    public void cancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void chooseFile() {


        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {

            try {
                FileReader reader = new FileReader(file);
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

                for (CSVRecord record : records) {
                    // Access the data in the record
                    // String col1 = record.get(0);
                    //String col2 = record.get(1);

                }
                reader.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
