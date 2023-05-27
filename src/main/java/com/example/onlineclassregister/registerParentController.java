package com.example.onlineclassregister;

import conn.dbConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class registerParentController {

    @FXML
    private Button exitButton;

    @FXML
    private Button generateReportButton;


    @FXML
    private ListView<String> studentsList;

    @FXML
    private ListView<Button> buttonStudents;

    @FXML
    private ListView<String> subjectList;

    @FXML
    private ListView<Button> buttonSubject;

    @FXML
    private Text average;

    @FXML
    private Text chooseStudentAlert;

    @FXML
    private ListView<String> gradesList;

    @FXML
    private ListView<String> attendanceList;

    private Student activeStudent;
    List<regEntry> globalregEntries = new ArrayList<>();

    public void exitButton() {

        Stage stageToClose = (Stage) exitButton.getScene().getWindow();
        stageToClose.close();
    }

    public void initialize(){

        chooseStudentAlert.setOpacity(0);

        average.setText("");
        System.out.println("parent id: "+ loggedUser.userId);

        studentsList.setFixedCellSize(30);
        buttonStudents.setFixedCellSize(30);
        List<Student> students = Student.getStudents();

        for(Student s: students)
            if(s.parent1Id==loggedUser.userId)
            {
                studentsList.getItems().add(s.fName + " " + s.lName);


                Button b = new Button();
                b.setText("Choose");
                b.setUserData(s);

                b.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                b.setPrefWidth(64);
                b.setPrefHeight(23);

                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        editButtonClick(b);
                    }
                });

                buttonStudents.getItems().add(b);
            }

    }

    private int chosenStudentId= -1;

    private void editButtonClick(Button b) {

        Student student = (Student) b.getUserData();
        System.out.println(student.userId);
        subjectList.setFixedCellSize(30);
        buttonSubject.setFixedCellSize(30);

        List<Student> students = new ArrayList<>();
        students=Student.getStudents();

        Student auxStudent = null;

        for(Student s1: students)
            if(s1.userId==student.userId)
            {
                auxStudent =s1;
                activeStudent=s1;
            }

        chosenStudentId=auxStudent.userId;

        Map<Integer, String> subjectNames = Subject.initSubject();


        List<regEntry> registerEntries = new ArrayList<>();
        registerEntries=auxStudent.regEntries;
        globalregEntries=auxStudent.regEntries;

        subjectList.getItems().clear();

        for(regEntry re: registerEntries)
        {
            if(!subjectList.getItems().contains(subjectNames.get(re.subjectId))) {
                subjectList.getItems().add(subjectNames.get(re.subjectId));

                Button bSubject = new Button();
                bSubject.setText("Choose");
                bSubject.setUserData(re.subjectId);
                bSubject.setStyle("-fx-background-color: "+properties.mainColor+"; -fx-border-radius: 25px; -fx-background-radius: 25px");
                bSubject.setPrefWidth(64);
                bSubject.setPrefHeight(23);

                bSubject.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        editButtonClick1(bSubject);
                    }
                });

                buttonSubject.getItems().add(bSubject);
            }
        }
        }





    private void editButtonClick1(Button b) {

        double avg=0;
        double avgCount=0;

        Map<Integer, String> subjectNames = Subject.initSubject();

        int courseId = (int) b.getUserData();

        List<Student> students = new ArrayList<>();
        students= Student.getStudents();
        Student activeStudent = null;

        for(Student s2: students)
            if(s2.userId== chosenStudentId)
                activeStudent =s2;

        List<regEntry> regEntries = new ArrayList<>();
        regEntries=activeStudent.regEntries;

       gradesList.getItems().clear();
       attendanceList.getItems().clear();
       for(regEntry re: regEntries)
           if(re.getValue()!=0 && re.subjectId==courseId)
           {
               String subject= subjectNames.get(re.subjectId);
               double grade = re.getValue();
               avg+=grade;
               avgCount++;
               String date = String.valueOf(re.date);
               gradesList.getItems().add(subject+": "+grade+" / "+date);
           }
       else if(re.value==0 && re.subjectId==courseId){
               String subject= subjectNames.get(re.subjectId);
               boolean motivated = re.getMotivated();
               String date = String.valueOf(re.date);

               if(motivated==false)
                   attendanceList.getItems().add(subject+": Absence / "+date);
               else
                   attendanceList.getItems().add(subject+": Absence / "+date+" - Motivated ");

           }


            average.setText("Current average: "+avg/avgCount);


    }

    public void generateReport() {

        if (chosenStudentId == -1) {
            chooseStudentAlert.setOpacity(100);
        } else {
            // Create a FileChooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            // Show the dialog and get the selected file
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                Platform.runLater(() -> {
                    try {
                        // Create a new document
                        PDDocument document = new PDDocument();

                        // Create a new page
                        PDPage page = new PDPage(PDRectangle.A4);
                        document.addPage(page);

                        // Create a new content stream
                        PDPageContentStream contentStream = new PDPageContentStream(document, page);

                        // Set the font and font size for the title and subtitles
                        PDType1Font titleFont = PDType1Font.HELVETICA_BOLD;
                        float titleFontSize = 18;
                        PDType1Font subtitleFont = PDType1Font.HELVETICA;
                        float subtitleFontSize = 12;

                        // Add logo at the top left
                        // Assuming logo.png is the logo image file
                        contentStream.drawImage(
                                PDImageXObject.createFromFile("classRegLogo.jpeg", document),
                                25, 770, 140, 70
                        );

                        // Add title
                        contentStream.setFont(titleFont, titleFontSize);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(page.getMediaBox().getWidth() / 2, 760);
                        contentStream.showText("Situation Report for " + activeStudent.fName + " " + activeStudent.lName);
                        contentStream.endText();

                        // Add timestamp as a subtitle
                        contentStream.setFont(subtitleFont, subtitleFontSize);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(page.getMediaBox().getWidth() / 2, 740);
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
                        contentStream.showText("Report date: " + timestamp);
                        contentStream.endText();

                        // Add the first table
                        float yTable1 = 680;
                        float margin = 50;
                        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                        float yStart = yTable1;
                        float yPosition = yStart;
                        float bottomMargin = 170;
                        float yPositionNewPage = yStart;
                        float tableHeight = 20;
                        float cellMargin = 5f;

                        Map<Integer, String> subjectNames = Subject.initSubject();
                        ArrayList<String[]> table1Data = new ArrayList<>();
                        ArrayList<String[]> table2Data = new ArrayList<>();

                        for (regEntry re : globalregEntries) {
                            if (re.getValue() != 0) {
                                String subject = subjectNames.get(re.subjectId);
                                double grade = re.getValue();
                                String date = String.valueOf(re.date);
                                table1Data.add(new String[]{subject, String.valueOf(grade), date});
                            } else if (re.getValue() == 0) {
                                String subject = subjectNames.get(re.subjectId);
                                boolean motivated = re.getMotivated();
                                String date = String.valueOf(re.date);

                                if(motivated==true){
                                    table2Data.add(new String[]{subject, date, "Yes"});
                                }
                                else{
                                    table2Data.add(new String[]{subject, date, "No"});
                                }
                            }
                        }

                        String[] table1Headers = {"Subject", "Grade", "Date"};

                        drawTable(contentStream, yPosition, tableWidth, bottomMargin, tableHeight, cellMargin,
                                table1Headers, table1Data);

                        // Add the second table
                        float yTable2 = yTable1 - tableHeight - 150;


                        String[] table2Headers = {"Subject", "Date", "Has Reason"};

                        drawTable(contentStream, yTable2, tableWidth, bottomMargin, tableHeight, cellMargin,
                                table2Headers, table2Data);

                        // Close the content stream
                        contentStream.close();

                        // Save the document to the selected file
                        document.save(selectedFile);
                        document.close();

                        System.out.println("PDF saved successfully.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                System.out.println("Save operation cancelled by the user.");
            }
        }
    }

    private void drawTable(PDPageContentStream contentStream, float y, float tableWidth, float bottomMargin,
                           float tableHeight, float cellMargin, String[] headers, ArrayList<String[]> data) throws IOException {
        float yStart = y;
        float tableTopY = y;
        float tableBottomY = y - tableHeight;
        float xStart = 50;

        float cellWidth = (tableWidth - 2 * cellMargin) / (float) headers.length;
        float cellHeight = tableHeight;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setLineWidth(1f);
        contentStream.moveTo(xStart, y);
        contentStream.lineTo(xStart + tableWidth, y);
        contentStream.stroke();
        float nextX = xStart;
        for (String header : headers) {
            float cellTextPosX = nextX + cellMargin;
            float cellTextPosY = y - 15;
            contentStream.beginText();
            contentStream.newLineAtOffset(cellTextPosX, cellTextPosY);
            contentStream.showText(header);
            contentStream.endText();
            nextX += cellWidth;
            contentStream.moveTo(nextX, y);
            contentStream.lineTo(nextX, y - cellHeight);
            contentStream.stroke();
        }
        contentStream.moveTo(xStart, y);
        contentStream.lineTo(xStart, y - tableHeight);
        contentStream.stroke();
        contentStream.moveTo(xStart + tableWidth, y);
        contentStream.lineTo(xStart + tableWidth, y - tableHeight);
        contentStream.stroke();
        y -= tableHeight;

        // Draw table bottom line
        contentStream.moveTo(xStart, tableBottomY);
        contentStream.lineTo(xStart + tableWidth, tableBottomY);
        contentStream.stroke();

        // Draw table top line
        contentStream.moveTo(xStart, tableTopY);
        contentStream.lineTo(xStart + tableWidth, tableTopY);
        contentStream.stroke();

        // Draw table vertical lines
        for (int i = 0; i <= headers.length; i++) {
            float nextXPos = xStart + i * cellWidth;
            contentStream.moveTo(nextXPos, tableTopY);
            contentStream.lineTo(nextXPos, tableBottomY);
            contentStream.stroke();
        }

        // Draw table bottom margin
        contentStream.moveTo(xStart, tableBottomY - bottomMargin);
        contentStream.lineTo(xStart + tableWidth, tableBottomY - bottomMargin);
        contentStream.stroke();

        // Draw table data
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (String[] row : data) {
            y -= cellHeight;
            nextX = xStart;
            for (String cell : row) {
                float cellTextPosX = nextX + cellMargin;
                float cellTextPosY = y - 15;
                contentStream.beginText();
                contentStream.newLineAtOffset(cellTextPosX, cellTextPosY);
                contentStream.showText(cell);
                contentStream.endText();
                nextX += cellWidth;
            }
        }
    }


}


