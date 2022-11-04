
package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.testfx.framework.junit.ApplicationTest;

/**
 *
 * @author essih
 */
public class AppTest extends ApplicationTest {
    
    private String testName = "PreSave Pena";
    private String testId = "00000_forTesting";

    
    @Override
    public void start(Stage stage) throws IOException {
        new App().start(stage);
    }
    
    /**
     * Verify the error message when adding student with empty name.
     */
    @org.junit.Test
    public void testAddNewStudentWithEmptyName() {
        
        addNewStudent("", "12345");
        Label errorLabel = fromAll().lookup("#invalidDataLabel").query();

        String errorMsg = "Invalid data!";
        assertEquals(errorLabel.getText(), errorMsg);
    }
    
    /**
     * Verify the error message when adding student with empty id.
     */
    @org.junit.Test
    public void testAddNewStudentWithEmptyId() {
        
        addNewStudent("Essi Esimerkki", "");
        Label errorLabel = fromAll().lookup("#invalidDataLabel").query();

        String errorMsg = "Invalid data!";
        assertEquals(errorMsg, errorLabel.getText());
    }
    
    /**
     * Add new student and verify the labels to have the newly added data. 
     */
    @org.junit.Test
    public void testAddNewStudent() {

        Label studentInfoLabel = fromAll().lookup("#studentInfoLabel").query();
        Label nameLabel = fromAll().lookup("#nameLabel").query();
        
        String newName = "Essi Esimerkki";
        String newId = "A123456";
        
        addNewStudent(newName, newId);
        
        String expected = "Essi Esimerkki (A123456)";
        assertEquals(newName, nameLabel.getText());
        assertEquals(expected, studentInfoLabel.getText());
    }
    
    @org.junit.Test
    public void testEmptyStudentId() {
        
        TextField id = fromAll().lookup("#studentIdField").query();
        Label errorLabel = fromAll().lookup("#notFoundLabel").query();
        Button findBtn = fromAll().lookup("#findStudentBtn").query();
        
        String emptyID = "";
        String errorMsg = "Student not found!";
        
        id.setText(emptyID);
        clickOn(findBtn);
        
        assertEquals(errorMsg, errorLabel.getText());
    }
    
    @org.junit.Test
    public void testNonexistingStudent() {
        
        TextField id = fromAll().lookup("#studentIdField").query();
        Label errorLabel = fromAll().lookup("#notFoundLabel").query();
        Button findBtn = fromAll().lookup("#findStudentBtn").query();
        
        String bogusID = "6666";
        String errorMsg = "Student not found!";
        
        id.setText(bogusID);
        clickOn(findBtn);
        
        assertEquals(errorMsg, errorLabel.getText());
    }
    
    /**
     * Test opening an existing student (JSON-file given in root).
     */
    @org.junit.Test
    public void testOpenExisting() {
        
        TextField id = fromAll().lookup("#studentIdField").query();
        Button findBtn = fromAll().lookup("#findStudentBtn").query();
        Label studentInfoLabel = fromAll().lookup("#studentInfoLabel").query();
        Label nameLabel = fromAll().lookup("#nameLabel").query();
        
        id.setText(testId);
        clickOn(findBtn);
        
        String expected = testName + " (" + testId + ")";
        
        assertEquals(testName, nameLabel.getText());
        assertEquals(expected, studentInfoLabel.getText());
    }
    
    /**
     * Creates a new test student and waits for degrees to be fetched.
     * Asserts that degrees exist. 
     */
    @org.junit.Test
    public void testFetchingDegrees() {
        
        VBox degreeBox = fromAll().lookup("#degreeBox").query();
        addNewStudent("FetchDegreeTest", "FDT");
        
        waitForDegrees();
        
        var degrees = degreeBox.getChildren();
        assertTrue(degrees.size() > 0);
    }
    
    /**
     * Creates a new test student and waits for degrees and then modules to be 
     * fetched.
     * Asserts that modules and courses exist. 
     */
    @org.junit.Test
    public void testFetchingModules() {
        
        VBox moduleBox = fromAll().lookup("#moduleBox").query();
        addNewStudent("FetchModuleTest", "FMT");
        Button saveDegreeBtn = fromAll().lookup("#saveDegreeBtn").query();
        var degreeTab = fromAll().lookup("#degreeTab").query();
        var moduleTab = fromAll().lookup("#moduleTab").query();
        
        waitForDegrees();
        clickOn(degreeTab);
        clickOn(saveDegreeBtn);
        waitForModules();
        clickOn(moduleTab);
        
        var modules = moduleBox.getChildren();
        assertTrue(modules.size() > 0);
    }
    
    /**
     * Sets new degree for student and checks whether the labels then show
     * the correct text.
     */
    @org.junit.Test
    public void testSetNewDegree() {
        TextField name = fromAll().lookup("#nameField").query();
        TextField id = fromAll().lookup("#idField").query();
        Button saveBtn = fromAll().lookup("#saveBtn").query();
        var degreeTab = fromAll().lookup("#degreeTab").query();
        
        // Give correct values
        String newName = "Testi Kassinen";
        String newId = "Testikassinen";
        name.setText(newName);
        id.setText(newId);
        clickOn(saveBtn);
        
        waitForDegrees();
        clickOn(degreeTab);
        
        String newDegree = "Arkkitehtuurin kandidaattiohjelma";
        var degree = fromAll().lookup(newDegree).query();
        var saveDegreeBtn = fromAll().lookup("#saveDegreeBtn").query();
        
        clickOn(degree);
        clickOn(saveDegreeBtn);
        
        waitForModules();
        
        Label degreeLabelTop = fromAll().lookup("#degreeLabel").query();
        Label degreeLabelBottom = fromAll().lookup("#degreeInfoLabel").query();
        
        assertEquals(newDegree, degreeLabelTop.getText());
        assertEquals(newDegree, degreeLabelBottom.getText());
    }
    
    /**
     * Adds new student, picks degree program and adds two courses in order to
     * test saving the progress and re-opening the student. 
     */
    @org.junit.Test
    public void testSaving() {
        
        var degreeTab = fromAll().lookup("#degreeTab").query();
        var moduleTab = fromAll().lookup("#moduleTab").query();
        var studentTab = fromAll().lookup("#studentTab").query();
        String name = "SaveTest";
        String id = "ST";
        
        // Delete the file if the test has been run before
        File file = new File("ST.json"); 
        file.delete();
        
        addNewStudent(name, id);
        
        waitForDegrees();
        clickOn(degreeTab);
        
        String newDegree = "Arkkitehtuurin kandidaattiohjelma";
        var degree = fromAll().lookup(newDegree).query();
        var saveDegreeBtn = fromAll().lookup("#saveDegreeBtn").query();
        clickOn(degree);
        clickOn(saveDegreeBtn);
        waitForModules();
        clickOn(moduleTab);
        
        var module = fromAll().lookup("Joint Studies in Architecture, "
                + "BSc (0/40 cr)").query();
        clickOn(module);
        VBox courseBox = fromAll().lookup("#courseBox").query();
        
        var course1 = courseBox.getChildren().get(1);
        var course2 = courseBox.getChildren().get(2);
        clickOn(course1);
        clickOn(course2);
        
        Button saveChanges = fromAll().lookup("#saveChanges").query();
        Button saveProgress = fromAll().lookup("#saveProgressBtn").query();
        clickOn(saveChanges);
        clickOn(saveProgress);
        clickOn(studentTab);
        changeIntoDummy();
        
        TextField idField = fromAll().lookup("#studentIdField").query();
        Button findBtn = fromAll().lookup("#findStudentBtn").query();
        Label studentInfoLabel = fromAll().lookup("#studentInfoLabel").query();
        Label degreeInfoLabel = fromAll().lookup("#degreeInfoLabel").query();
        
        idField.setText(id);
        clickOn(findBtn);
        String expectedName = name + " (" + id + ")";
        String expectedDegree = newDegree;
        
        assertEquals(expectedName, studentInfoLabel.getText());
        assertEquals(expectedDegree, degreeInfoLabel.getText());
    }
    
    @org.junit.Test
    public void testAddingCourse() {
        addNewStudent("Add Course", "ACT");
        var degreeTab = fromAll().lookup("#degreeTab").query();
        var moduleTab = fromAll().lookup("#moduleTab").query();
        VBox moduleBox = fromAll().lookup("#moduleBox").query();
        
        waitForDegrees();
        clickOn(degreeTab);
        String newDegree = "Arkkitehtuurin kandidaattiohjelma";
        var degree = fromAll().lookup(newDegree).query();
        var saveDegreeBtn = fromAll().lookup("#saveDegreeBtn").query();
        clickOn(degree);
        clickOn(saveDegreeBtn);
        waitForModules();
        clickOn(moduleTab);
        
        var module = fromAll().lookup("Joint Studies in Architecture, "
                + "BSc (0/40 cr)").query();
        clickOn(module);
        VBox courseBox = fromAll().lookup("#courseBox").query();
        
        var course = courseBox.getChildren().get(1);
        clickOn(course);
        Button saveChanges = fromAll().lookup("#saveChanges").query();
        clickOn(saveChanges);
        
        String courseName = "Introduction to Architecture Studies I (2 cr) "
                + "(Completed)";
        
        boolean isCourseMarkedCompleted = false;
        boolean rightColor = false;
        Label courseLabel;
        
        try {
            courseLabel = fromAll().lookup(courseName).query();
            isCourseMarkedCompleted = true;
            rightColor = courseLabel.getBackground().getFills().
                    contains(new BackgroundFill(Color.AQUAMARINE, null, null));
        } catch (Exception e) {
            System.out.println("Catch");
        }
        
        assertTrue(isCourseMarkedCompleted);
        assertTrue(rightColor);
    }
    
    public void testUpdateProgress() {
        
        addNewStudent("Check progress", "CPR");
        var degreeTab = fromAll().lookup("#degreeTab").query();
        var moduleTab = fromAll().lookup("#moduleTab").query();
        ProgressBar progressBar = fromAll().lookup("#progressBar").query();
        Label creditLabel = fromAll().lookup("#creditLabel").query();
        Label progressLabel = fromAll().lookup("#progressLabel").query();
        
        assertEquals(0.0, progressBar.getProgress());
        
        waitForDegrees();
        clickOn(degreeTab);
        
        String newDegree = "Arkkitehtuurin kandidaattiohjelma";
        var degree = fromAll().lookup(newDegree).query();
        var saveDegreeBtn = fromAll().lookup("#saveDegreeBtn").query();
        clickOn(degree);
        clickOn(saveDegreeBtn);
        waitForModules();
        clickOn(moduleTab);
        
        var module = fromAll().lookup("Opintosuunta 1 (180 cr)").query();
        clickOn(module);
        VBox courseBox = fromAll().lookup("#courseBox").query();
        
        var course1 = courseBox.getChildren().get(1);
        var course2 = courseBox.getChildren().get(2);
        clickOn(course1);
        clickOn(course2);
        Button saveChanges = fromAll().lookup("#saveChanges").query();
        clickOn(saveChanges);
        
        double expectedPercentage =( (double) 10 / (double) 180 );
        String expectedCredits = "10/180 cr";
        String expectedPercentString = String.format("%.2f %% completed", 
                expectedPercentage*100);
        
        assertEquals(expectedPercentage, progressBar.getProgress());
        assertEquals(expectedCredits, creditLabel.getText());
        assertEquals(expectedPercentString, progressLabel.getText());
        
    }
    
    
    private void addNewStudent(String nameToAdd, String idToAdd) {
        TextField name = fromAll().lookup("#nameField").query();
        TextField id = fromAll().lookup("#idField").query();
        Button saveBtn = fromAll().lookup("#saveBtn").query();
        
        String newName = nameToAdd;
        String newId = idToAdd;
        name.setText(newName);
        id.setText(newId);
        clickOn(saveBtn);
    }
    
    
    /**
     * Waits for the degree programs to appear in their tab.
     */
    private void waitForDegrees() {
        
        var degreeTab = fromAll().lookup("#degreeTab").query();
        
        while (true) {
            if (degreeTab.isDisable()) {
                sleep(5000);
            } else break;
        }
    }
    
    
    /**
     * Waits for the modules and courses to appear in their tab.
     */
    private void waitForModules() {
        
        var moduleTab = fromAll().lookup("#moduleTab").query();
        
        while (true) {
            if (moduleTab.isDisable()) {
                sleep(5000);
            } else break;
        }
    }
    
    
    /**
     * Changes to a new user.
     * Is used to log off from previous student to enable testing logging back in.
     */
    private void changeIntoDummy() {
        TextField name = fromAll().lookup("#nameField").query();
        TextField id = fromAll().lookup("#idField").query();
        Button saveBtn = fromAll().lookup("#saveBtn").query();
        
        name.setText("Name Surname");
        id.setText("ID111");
        clickOn(saveBtn);
    }
    
    

}
