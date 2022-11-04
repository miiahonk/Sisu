package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * An abstract base class for Sisu modules
 */
public abstract class SisuModule {
    
    private String name;
    private String id;
    private int credits;
    private ArrayList<Course> courses = new ArrayList<>();

    /**
     * Returns the name of the module
     * @return the name of the module
     */
    abstract String getName();
    
    /**
     * Returns the ID of the module
     * @return the ID of the module
     */
    abstract String getId();
    
    /**
     * Returns the extent of the module
     * @return the extent of the module
     */
    abstract int getCredits();
    
    /**
     * Returns the list of courses directly under the module
     * @return the list of courses directly under the module
     */
    abstract ArrayList<Course> getCourses();
    
    /**
     * Abstract function to add module to the correct list
     */
    abstract void addItem(SisuModule module);
    
    /**
     * Abstract function for getting all children modules
     * @return ArrayList of all children modules
     */
    abstract ArrayList<ArrayList<? extends SisuModule>> getChildren();
    
    /**
     * Returns the name and credits of the module as String
     * @return the name and credits of the module as String
     */
    @Override
    public String toString() {
        return getName() + " (" + getCredits() + " cr)";
    }
    
}