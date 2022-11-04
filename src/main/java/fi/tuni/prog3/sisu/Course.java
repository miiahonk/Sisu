package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * A class for maintaining Tampere University courses.
 * 
 */
public class Course extends SisuModule{
    
    private final String name;
    private final String code;
    private final String id;
    private final String content;
    private final int credits;

    /**
     * Constructs a Course object.
     * @param name the name of the course
     * @param code the code of the course
     * @param id the ID of the course
     * @param content the content description of the course
     * @param credits the extent of the course
     */
    public Course(String name, String code, String id, String content, int credits) {
        this.name = name;
        this.code = code;
        this.id = id;
        this.content = content;
        this.credits = credits;
    }

    /**
     * Returns the name of the course
     * @return the name of the course
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the code of the course
     * @return the code of the course
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Returns the ID of the course
     * @return the ID of the course
     */
    @Override
    public String getId() {
        return id;
    } 

    /**
     * Returns the content of the course
     * @return the content of the course
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Returns the extent of the course
     * @return the extent of the course
     */
    @Override
    public int getCredits() {
        return credits;
    }
    
    /**
     * Returns null
     * @return null
     */
    @Override
    ArrayList<Course> getCourses() {
        return null;
    }
    
    /**
     * Unsupported method with Course objects
     * @param module 
     */
    @Override
    public void addItem(SisuModule module) {
        throw new UnsupportedOperationException("This operation is not supported");
    }
    
    /**
     * Returns null
     * @return null
     */
    @Override
    ArrayList<ArrayList<? extends SisuModule>> getChildren() {
        return null;
    }
}