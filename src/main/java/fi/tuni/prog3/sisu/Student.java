package fi.tuni.prog3.sisu;

import java.util.TreeSet;

/**
 * A class for maintaining student data
 * 
 */
public class Student {
    
    private final String name;
    private final String id;
    private String degreeProgramme = "";
    private TreeSet<String> completedCourses = new TreeSet<>();

    /**
     * Constructs a Student object.
     * @param name the name of the student
     * @param id the ID of the student
     */
    public Student(String name, String id) {
        this.name = name;
        this.id = id;
    }
   
    /**
     * Constructs a Student object.
     * @param name the name of the student
     * @param id the ID of the student
     * @param degreeProgramme the ID of the chosen degreeProgramme
     */
    public Student(String name, String id, String degreeProgramme) {
        this.name = name;
        this.id = id;
        this.degreeProgramme = degreeProgramme;
    }
    
    /**
     * Constructs a Student object.
     * @param name the name of the student
     * @param id the ID of the student
     * @param degreeProgramme the ID of the chosen degreeProgramme
     * @param completedCourses the IDs of the completed courses
     */
    public Student(String name, String id, String degreeProgramme, TreeSet<String> completedCourses) {
        this.name = name;
        this.id = id;
        this.degreeProgramme = degreeProgramme;
        this.completedCourses = completedCourses;
    }

    /**
     * Returns the name of the student
     * @return the name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ID of the student
     * @return the ID of the student
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the chosen degree programme
     * @param degreeProgramme the ID for the chosen degree programme
     */
    public void setDegreeProgramme(String degreeProgramme) {
        this.degreeProgramme = degreeProgramme;
    }
    
    /**
     * Returns the ID of the chosen degree progamme
     * @return the ID of the chosen degree progamme
     */
    public String getDegreeProgramme() {
        return degreeProgramme;
    }
    
    /**
     * Returns true if the student has set a degree programme, otherwise false
     * @return true if the student has set a degree programme, otherwise false
     */
    public boolean isDegreeSet() {
        return degreeProgramme.length() > 0;
    }
    
    /**
     * Adds a new course to the set of completed courses
     * @param courseId the id of the course to be added
     */
    public void addCompletedCourse(String courseId) {
        completedCourses.add(courseId);
    }
    
    /**
     * Adds a set of completed courses
     * @param courses the set of the completed courses
     */
    public void addCompletedCourses(TreeSet<String> courses) {
        completedCourses = courses;
    }
    
    /**
     * Returns the set of completed courses
     * @return the set of completed courses
     */
    public TreeSet<String> getCompletedCourses() {
        return completedCourses;
    }
    
    /**
     * Removes all courses from completed courses
     */
    public void clearCompletedCourses() {
        completedCourses.clear();
    }
}