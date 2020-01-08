/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author Shraddha Belbase
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField addText;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button quitButton;

    @FXML
    private PieChart PieChart;

    @FXML
    private AnchorPane Cloud;

    //Adds word to the pane and adds slice to the Pie-Chart
    private void add() {
        //Creates a pie-chart
        PieChart.Data newSlice = new PieChart.Data(addText.getText(), 1);
        //Adds slice to the pie
        PieChart.getData().add(newSlice);
        PieChart.setLabelsVisible(false);

        //Make a new word cloud
        Label newWord = new Label();
        newWord.setText(addText.getText());
        newWord.setFont(new Font(newWord.getFont().getName(), 20));
        
        //Choose Random Color for the word
        List<Color> temp = new ArrayList<>();
        temp.add(Color.web("#EE4035"));
        temp.add(Color.web("#403C0A")); 
        temp.add(Color.web("#9781D5")); 
        temp.add(Color.web("#7BC043")); 
        temp.add(Color.web("#0392CF"));
        Random random = new Random();
        newWord.setTextFill(temp.get(random.nextInt(temp.size())));

        placeWord(newWord);

        //If clicked on a slice
        newSlice.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                //Incereases the value 
                int newPieValue = ((int) newSlice.getPieValue()) + 1;
                newSlice.setPieValue(newPieValue);
                //Increases the font size of the word
                double currFontSize = newWord.getFont().getSize();
                newWord.setFont(new Font(newWord.getFont().getName(), currFontSize + 2));
                placeWord(newWord);

            }
        }
        );

        //if the word is clicked on in cloud
        newWord.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //Adds a point in the pie-chart
                int newPieValue = ((int) newSlice.getPieValue()) + 1;
                newSlice.setPieValue(newPieValue);
                //Increases the font size by 2
                double currFontSize = newWord.getFont().getSize();
                newWord.setFont(new Font(newWord.getFont().getName(), currFontSize + 2));
                placeWord(newWord);

            }
        }
        );

    }
    //Clears everything if the Clear all button is clicked
    public void clearAll(ActionEvent e) {
        Button clicked = (Button) e.getSource();

        //if the clear button was clicked
        if (clicked == clearButton) {
            PieChart.getData().clear();
            Cloud.getChildren().clear();
            addText.setText("");
        }
    }
    //Quits the program after making sure from the user
    public void quit(ActionEvent e) {
        Button clicked = (Button) e.getSource();

        
        //Popup to make sure user wants to quit
        if (clicked == quitButton) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Quit?");
            alert.setContentText("You are about to exit the program");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            } else if (result.get() == ButtonType.CANCEL) {
            }
        }
    }
    //Updates the Pie-Chart
    @FXML
    public void updatePie(ActionEvent e) {

        Button clicked = (Button) e.getSource();

        boolean matchFound = false;

        //if the add button is clicked
        if (clicked == addButton) {
            if (addText.getText().length() > 0) {
                for (PieChart.Data w : PieChart.getData()) {
                    if (w.getName().equals(addText.getText())) {
                        //If it was duplicate
                        matchFound = true;

                    }
                } //If no match was found
                if (!matchFound) {
                    add();
                }
                //If no text was entered
            } else 
            {
                popupWindow("Please enter a word");
            }

        }
        //reset text
        addText.setText(""); 

    }

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    //Popup Window for the quit button
    private void popupWindow(String string) {
        Scene scene = new Scene(new Group(new Text(25, 25, string)));

        Stage myStage = new Stage();

        myStage.setTitle("Error");
        myStage.setScene(scene);
        myStage.sizeToScene();
        myStage.setMinWidth(225.00);
        myStage.setMinHeight(150.00);
        myStage.showAndWait();
        VBox box = new VBox();
        myStage.setScene(new Scene(box));
    }
    //Calculates where to put the word in the cloud
   public void placeWord(Label newWord) {

      
        boolean intersects = false;

        double radius = 1;
        double angle = 0;
        double middleX = Cloud.getWidth() / 2;
        double middleY = Cloud.getHeight() / 2;
        do {
            double xPos = middleX + radius * Math.cos(angle);
            double yPos = middleY + radius * Math.sin(angle);
            newWord.setLayoutX(xPos);
            newWord.setLayoutY(yPos);
            newWord.setRotate(0);
            intersects = causesIntersection(newWord);
            if (intersects) {
                newWord.setRotate(90);
                intersects = causesIntersection(newWord);
            }
            
            radius += 0.4;
            angle += 0.1;

           
            if (intersects == false) {

                Cloud.getChildren().add(newWord);
                           }
        } while (intersects);

    }


   //Causes and checks intersection
   public boolean causesIntersection(Label newWord) {
        boolean flag = false;
        List<String> temp = new ArrayList<>();
        for (Node n : Cloud.getChildren()) {
            Label oldWord = (Label) n;

            Bounds bound1 = newWord.getBoundsInParent();
            Bounds bound2 = oldWord.getBoundsInParent();
            flag = (bound2.intersects(bound1) && (oldWord.equals(newWord) == false));
            temp.add(Boolean.toString(flag));
        }
       
        if (temp.contains("true")) {
            return true;
        } else {
            return false;
        }

    }
}


