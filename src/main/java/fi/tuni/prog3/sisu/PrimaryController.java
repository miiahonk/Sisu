package fi.tuni.prog3.sisu;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * Class for controlling the user interface.
 */
public class PrimaryController {
    
    private Student student;
    private TreeSet<String> completedCourses;
    private HashMap<String, DegreeProgramme> degreePrograms;
    private HashMap<String, Course> allCourses = new HashMap<>();
    private HashMap<String, SisuModule> allModules = new HashMap<>();
    
    private DegreeProgramme currentDegree;
    private boolean populated = false;
    DegreeBuilder degreeBuilder;
    
    private TreeSet<String> stagedChanges = new TreeSet<>();
    private TreeSet<String> stagedDeletions = new TreeSet<>();
    
    ToggleGroup degreeGroup;
    boolean firstDegree = true;
    int currCredits;
    int subCredits;
    
    @FXML
    private TextField nameField;
    @FXML
    private TextField idField;
    @FXML 
    private Label invalidDataLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label notFoundLabel;
    @FXML
    private TextField studentIdField;
    @FXML
    private Label studentWarningLabel;
    @FXML
    private Label degreeWarningLabel;
    @FXML
    private Label degreeLabel;
    @FXML
    private Tab degreeTab;
    @FXML
    private Tab moduleTab;
    @FXML
    private VBox degreeBox;
    @FXML 
    private VBox moduleBox;
    @FXML
    private VBox courseBox;
    @FXML
    private Button saveChanges;
    @FXML
    private Label degreeInfoLabel;
    @FXML
    private Label studentInfoLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private Label creditLabel;
    @FXML
    private Button saveProgressBtn;
    
    
    /**
     * Creates a new Student instance.
     * Checks the user input and gives error messages in case of faulty inputs.
     */
    @FXML
    private void saveNewStudent() {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        
        String studentFile = id + ".json";
        
        if ("".equals(name) || "".equals(id)) {
            invalidDataLabel.setText("Invalid data!");
        } else 
        // Check if the student has already been saved
            if (new File(studentFile).exists()) {
                invalidDataLabel.setText("Existing student!");
            } else {
                // Set labels
                student = new Student(name, id);
                invalidDataLabel.setText("");
                
                
                nameLabel.setText(name);
                studentInfoLabel.setText(student.getName() + " (" + 
                    student.getId() + ")");
                degreeInfoLabel.setText("");
                degreeLabel.setText("");
                completedCourses = new TreeSet<>();

                addWarningToStudentTab();
                
                // Enable saving the progress of newly created student
                saveProgressBtn.setDisable(false);
                populateDegrees();
                
                // Reset the progress
                resetProgress();
            }
        nameField.setText("");
        idField.setText("");
        studentIdField.setText("");
    }
    
    /**
     * Gets existing student and course data if the given filename exists.
     * Gives error messages in dedicated label if one of the fields is empty
     * or the student with given ID does not exist. 
     */
    @FXML
    private void openExisting() {
        
        String filename = studentIdField.getText().trim() + ".json";
        
        if ("".equals(filename)) {
            notFoundLabel.setText("ID must be given!");
            return;
        }
        
        try {
            // Try opening the file to make sure it exists.
            student = DegreeSaver.getStudent(filename);
            // Add course IDs to the internal set for easier bookkeeping
            completedCourses = new TreeSet<>();
            student.getCompletedCourses().forEach( course -> {
                completedCourses.add(course);
            });
            nameLabel.setText(student.getName());
            studentInfoLabel.setText(student.getName() + " (" +
                    student.getId() + ")");

            populateDegrees();

            // If degree is pre-set, populate modules tab accordingly.
            if (student.isDegreeSet()) {
                currentDegree = degreePrograms.get(student.getDegreeProgramme());
                degreeLabel.setText(currentDegree.getName());
                degreeInfoLabel.setText(currentDegree.getName());
                populateModules(currentDegree);
                setInitialProgress();
            }
        } catch (IOException e) {
            notFoundLabel.setText("Student not found!");
        }
        
        nameField.setText("");
        idField.setText("");
        studentIdField.setText("");
        
    }
    
    /**
     * Add warning text of unsaved changes to student tab.
     */
    private void addWarningToStudentTab() {
        studentWarningLabel.setText("NOTE: You have unsaved changes. Changing "
                + "the student before saving will dismiss all unsaved changes.");
    }
    
    /**
     * Add warning text of unsaved changes to degree program tab.
     */
    private void addWarningToDegreeTab() {
        degreeWarningLabel.setText("NOTE: You have unsaved changes. Changing "
                + "the degree program before saving will dismiss all unsaved "
                + "changes.");
    }

    /**
     * Empties the warning labels in Student and Degree program tabs. 
     */
    private void emptyWarningLabels() {
        studentWarningLabel.setText("");
        degreeWarningLabel.setText("");
    }

    
    /**
     * Fetches the degree programs and populates the tab.
     * Shows the degree programs as radiobuttons, so the user can choose exactly
     * one degree program. 
     * Degree programs are populated only once. 
     */
    @FXML
    private void populateDegrees() {
        
        if (populated) {
            return;
        }
        
        degreeBuilder = new DegreeBuilder();
        degreePrograms = degreeBuilder.getDegrees();
        // Activate the tab
        degreeTab.setDisable(false);
        
        degreeGroup = new ToggleGroup();
        
        degreePrograms.forEach( (id, degree) -> {
            
            // Set degree name as the label and the degree instance as the 
            // retrievable value
            RadioButton newButton = new RadioButton(degree.getName());
            newButton.setUserData(degree);
            newButton.setToggleGroup(degreeGroup);
            
            // Set as selected if this is the current degree. If no degree
            // is set, the first degree is selected.
            if (student.isDegreeSet() && id.equals(student.getDegreeProgramme()) ) {
                newButton.setSelected(true);
            } else if (firstDegree) {
                newButton.setSelected(true);
                firstDegree = false;
            }
            
            degreeBox.getChildren().add(newButton);
            
        });
        
        this.populated = true;
    }
    
    /**
     * Updates the degree program chosen in the degree program tab to be the
     * student's chosen program. 
     * 
     */
    @FXML
    private void setNewDegree() {
        
        currentDegree = 
                (DegreeProgramme) degreeGroup.getSelectedToggle().getUserData();
        
        student.setDegreeProgramme(currentDegree.getId());
        degreeLabel.setText(currentDegree.getName());
        degreeInfoLabel.setText(currentDegree.getName());
        
        if (completedCourses.size() > 0) {
            completedCourses.clear();
        }
        
        addWarningToDegreeTab();
        saveProgressBtn.setDisable(false);
        populateModules(currentDegree);
        
        student.clearCompletedCourses();
        setInitialProgress();
        courseBox.getChildren().clear();
    }
    
    /**
     * Populates the modules and courses -tab with the modules of given
     * degree program.
     * Adds the courses of the current degree to the courses-hashmap.
     * 
     * @param degreeProgram DegreeProgramme. The contents of this degree program
     * will be shown in the modules and courses tab. 
     */
    @FXML
    private void populateModules(DegreeProgramme degreeProgram) {
        
        moduleTab.setDisable(false);
        
        DegreeProgramme fullDegreeProgram = degreeBuilder.getDegreeContent(degreeProgram);
        
        moduleBox.getChildren().clear();
        moduleBox.getChildren().add(addSubModules(degreeProgram));
    }
    
    /**
     * Recursively initiates nodes to be added. 
     * If the given instance is a course, stops recursion by returning a label. 
     * Otherwise returns a TitledPane that's content is the instance's child 
     * nodes.
     * @param instance SisuModule instance
     * @return Node. If the given instance is a course, returns a Label. If the
     * instance is one of a higher hierarchy level, returns a TitledPane. 
     */
    private Node addSubModules(SisuModule instance) {

        if (instance instanceof Course) {
            allCourses.put(instance.getId(), ( (Course) instance ) );
            Label courseLabel = new Label();
            
            // Set color as green if marked completed!
            if ( completedCourses.contains(((Course) instance).getId()) ) {
                courseLabel.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
                courseLabel.setText(instance.toString() + " (Completed)");
            } else {
                courseLabel.setText(instance.toString());
            }
            courseLabel.setId(instance.getId());
            
            return courseLabel; 
        }
        
        allModules.put(instance.getId(), instance);
        
        // Add the current module and its submodules recursively as titlepanes
        var children = instance.getChildren();
        
        VBox newBox = new VBox();
        
        children.forEach( arrayOfChildren -> {
            arrayOfChildren.forEach( subModule -> {
                
                newBox.getChildren().add(addSubModules(subModule));
                
            });
            
        });
        
        //TODO
        
        int credits = moduleCredits(instance);
        String instanceInfo = String.format("%s (%d/%d cr)", 
                instance.getName(), credits, instance.getCredits());
        
        TitledPane newPane = new TitledPane(instanceInfo, newBox);
        
        //TitledPane newPane = new TitledPane(instance.toString() + " Suor: " + crX, 
        //        newBox);
        
        if (!(instance instanceof DegreeProgramme)) {
            newPane.setExpanded(false);
        }
        
        newPane.setId(instance.getId());
        
        // If the instance has courses as direct children enable
        // showing them on click. 
        if (instance.getCourses().size() > 0) {
            
            newPane.setOnMouseClicked( (MouseEvent e) -> {
                showCourses(instance);
            });
        } 
        return newPane;
    }
    
    private int moduleCredits(SisuModule module) {
        
        if (module instanceof Course) {
            if ( completedCourses.contains(((Course) module).getId()) ) {
                return module.getCredits();
            } else {
                return 0;
            }
        }
        
        subCredits = 0;
        var children = module.getChildren();
        children.forEach( arrOfChildren -> {
            arrOfChildren.forEach( submodule -> { 
                subCredits += moduleCredits(submodule);
            });
        });
        
        return subCredits;
    }
    
    /**
     * Shows the courses of the given module in the side pane. 
     * Presents the courses as checkboxes that can be selected to represent
     * completed course. The changes made are saved to stagedChanges or
     * stagedDeletions depending on the action.
     * Clears all previous staged changes when starting, as only the courses
     * of one module can be shown and updated at a time. 
     * 
     * @param module The module whose courses are shown in the side pane.
     */
    @FXML
    private void showCourses(SisuModule module) {
        // Delete unsaved changes
        stagedChanges.clear();
        stagedDeletions.clear();
        
        courseBox.getChildren().clear();
        Label headerLabel = new Label("Courses (" + module.getName() + "):");
        courseBox.getChildren().add(headerLabel);
        
        var courseArray = module.getCourses();
        
        courseArray.forEach( course -> {
            String courseInfo = course.toString();
            CheckBox newCheckbox = new CheckBox(courseInfo);
            
            // Set selected if the course is completed already
            if (completedCourses.contains(course.getId())) {
                newCheckbox.setSelected(true);
            }

            // Set toolTip to be presented on hover. Tooltip has additional 
            // information about the course.
            var info = new Tooltip(String.format("%s (%s) %n"
                            + "Credits: %d cr %n"
                            + "Course contents: %s",
                    course.getName(), course.getCode(), course.getCredits(), 
                    course.getContent()
            ));
            
            info.setMaxWidth(200);
            info.setWrapText(true);
            
            newCheckbox.setTooltip(info);
            
            // If toggled, add/remove the id from the temporary changes.
            newCheckbox.setOnAction( (ActionEvent e) -> {
                
                if (saveChanges.isDisable()) {
                    saveChanges.setDisable(false);
                }
                
                String id = course.getId();
                // New course marked as completed:
                if (newCheckbox.isSelected() && !completedCourses.contains(id)) {
                    stagedChanges.add(id);
                } 
                // If unchecked but is previously completed, add to the
                // list of courses to un-complete.
                else if (!newCheckbox.isSelected() && completedCourses.contains(id)) {
                    stagedDeletions.add(id);
                } else {
                    stagedChanges.remove(id);
                }
            });
            
            courseBox.getChildren().add(newCheckbox);
        });
        
    }
    
    /**
     * Saves the staged changes made to the courses of the currently active
     * module. 
     * Checks both added and removed completions and updates the UI accordingly.
     */
    @FXML
    private void saveCourses() {

        // Get the root module (degree program) and loop through to 
        // mark the courses as complete/not complete.
        
        var rootModule = moduleBox.getChildren().get(0);
        
        if (stagedChanges.size() > 0) {
            completedCourses.addAll(stagedChanges);
            setAsComplete(rootModule, stagedChanges);
            stagedChanges.clear();
        }

        if (stagedDeletions.size() > 0) {
            completedCourses.removeAll(stagedDeletions);
            resetCourse(rootModule, stagedDeletions);
            stagedDeletions.clear();
        }
        
        updateProgress();
        saveChanges.setDisable(true);
        saveProgressBtn.setDisable(false);
    }

    /**
     * Recursively goes through the modules and marks the courses in the given
     * set as completed in the UI. 
     * Checks if the given node is a course and if yes, compares it to the given
     * set. Otherwise goes recursively to the module's submodules. 
     * @param currentNode Node to process
     * @param coursesToAdd TreeSet that contains the ID's of the courses to be
     * marked as completed. 
     */
    private void setAsComplete(Node currentNode, TreeSet<String> coursesToAdd) {
        
        String id = currentNode.getId();
        
        // If the node is label, check if the course needs to be marked
        // as complete.
        if (currentNode instanceof Label && coursesToAdd.contains(id)) {
                
                String currText = ((Label) currentNode).getText();
                ((Label) currentNode).setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, null)));
                ((Label) currentNode).setText(currText + " (Completed)");
            
        } else 
            // Not a course, check children
            if (currentNode instanceof TitledPane) {
                var children = ((VBox) ((TitledPane) currentNode).getContent()).getChildren();

                children.forEach( node -> {
                    setAsComplete(node, coursesToAdd);
                });
                
                // Update the label if needed
                SisuModule instance = allModules.get(currentNode.getId());
                int credits = moduleCredits(instance);
                String instanceInfo = String.format("%s (%d/%d cr)", 
                instance.getName(), credits, instance.getCredits());
                
                if (! (((TitledPane) currentNode).getText().equals(instanceInfo))) {
                    ((TitledPane) currentNode).setText(instanceInfo);
                }
            }
    }
    
    /**
     * Recursively goes through the modules and marks deletes the completion
     * mark from the courses given in the set.
     * Checks if the given node is a course and if yes, compares it to the given
     * set. Otherwise goes recursively to the module's submodules. 
     * @param currentNode Node to process
     * @param coursesToDelete TreeSet that contains the ID's of the courses to be
     * marked as NOT completed. 
     */
    private void resetCourse(Node currentNode, TreeSet<String> coursesToDelete) {
        
        String id = currentNode.getId();
        // If the node is label, check if the course needs to be marked
        // as complete.
        if (currentNode instanceof Label && coursesToDelete.contains(id)) {

            ((Label) currentNode).setBackground(Background.EMPTY);
            ((Label) currentNode).setText(allCourses.get(id).toString());
            
        } else 
            // Not a course, check children
            if (currentNode instanceof TitledPane) {
                
                var children = ((VBox) ((TitledPane) currentNode).getContent()).getChildren();

                children.forEach( node -> {
                    resetCourse(node, coursesToDelete);
                });
            }
    }
    
    /**
     * Updates the progress bar, credit label and completion percentage label
     * to match the current situation. 
     */
    @FXML
    private void updateProgress() {
        if (currentDegree != null) {
            
            int maxCredits = currentDegree.getCredits();
            currCredits = 0;

            completedCourses.forEach( courseId -> {
                currCredits += allCourses.get(courseId).getCredits();
            });

            creditLabel.setText(currCredits + "/" + maxCredits + " cr" );

            double percentage = (( (double) currCredits)/ ( (double) maxCredits));
            progressBar.setProgress(percentage);

            String progressText = String.format("%.2f %% completed", percentage*100);
            progressLabel.setText(progressText);
            
        }
        
    }
    
    /**
     * Resets the progress in the UI.
     * Includes the progress bar, credits shown next to it and the percentage
     * label. 
     */
    @FXML
    private void resetProgress() {
        progressBar.setProgress(0.0);
        creditLabel.setText("?/? cr");
        progressLabel.setText("0 % completed");
    }
    
    @FXML
    private void setInitialProgress() {
        
        if (student.isDegreeSet() && currentDegree != null) {
            int maxCredits = currentDegree.getCredits();
            
            currCredits = 0;
            student.getCompletedCourses().forEach( courseId -> {
                
                currCredits += allCourses.get(courseId).getCredits();
            });
            
            creditLabel.setText(currCredits + "/" + maxCredits + " cr" );

            double percentage = (( (double) currCredits)/ ( (double) maxCredits));
            progressBar.setProgress(percentage);

            String progressText = String.format("%.2f %% completed", percentage*100);
            progressLabel.setText(progressText);
        }
        
    }
    
    /**
     * Saves the current progress to file. 
     * If previous file exists, this will overwrite it to make sure that all
     * the course information is up to date. 
     * This will also overwrite any courses that are marked completed that
     * belong to a different degree program.
     */
    @FXML
    private void saveProgress() {
        
        // Rewrite the student's completed courses with the current progress.
        student.clearCompletedCourses();
        
        completedCourses.forEach( courseId -> {
            student.addCompletedCourse(courseId);
        });
        
        try {
            // Create the new file.
            DegreeSaver.save(student);
        } catch (IOException ex) {
            System.out.println("Error while saving the progress");
        }
        saveProgressBtn.setDisable(true);
        emptyWarningLabels();
        
    }
    
}
