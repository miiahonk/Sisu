package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * A class for maintainting Tampere University study modules
 */
public class StudyModule extends SisuModule{
    
    private final String name;
    private final String id;
    private final int credits;
    private ArrayList<StudyModule> submodules = new ArrayList<>();
    private ArrayList<GroupingModule> groupingmodules = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<ArrayList<? extends SisuModule>> allChildren;
    
   /**
     * Constructs a StudyModule object.
     * @param name the name of the study module
     * @param id the ID of the study module
     * @param credits the extent of the study module
     */
    public StudyModule(String name, String id, int credits) {
        this.name = name;
        this.id = id;
        this.credits = credits;
    }

    /**
     * Returns the name of the study module
     * @return the name of the study module
     */
    @Override
    public String getName() {
        return name;
    }
    
   /**
     * Returns the ID of the study module
     * @return the ID of the study module
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the extent of the study module
     * @return the extent of the study module
     */
    @Override
    public int getCredits() {
        return credits;
    }
    
    /**
     * Returns the list of submodules under the study module
     * @return the list of submodules under the study module
     */
    public ArrayList<StudyModule> getSubmodules() {
        return submodules;
    }
    
   /**
     * Returns the list of grouping modules directly under the study module
     * @return the list of grouping modules directly under the study module
     */
    public ArrayList<GroupingModule> getGroupingmodules() {
        return groupingmodules;
    }
    
    /**
     * Returns the list of courses directly under the study module
     * @return the list of courses directly under the study module
     */
    @Override
    public ArrayList<Course> getCourses() {
        return courses;
    }
    
    /**
     * Adds a module to the correct list
     * @param module the module to be added, the type can be either StudyModule, GroupingModule or Course
     */
    @Override
    public void addItem(SisuModule module) {
        if (module instanceof StudyModule) {
            submodules.add((StudyModule)module);
        }
        else if (module instanceof GroupingModule) {
            groupingmodules.add((GroupingModule)module);
        }
        else if (module instanceof Course) {
            courses.add((Course)module);
        }
    }

    /**
     * Returns a list of study module's children
     * @return a list of study module's children
     */
    @Override
    public ArrayList<ArrayList<? extends SisuModule>> getChildren() {
        allChildren = new ArrayList<>();

        if (!submodules.isEmpty()) {
            allChildren.add(submodules);
        }
        if (!groupingmodules.isEmpty()) {
            allChildren.add(groupingmodules);
        }
        if (!courses.isEmpty()) {
            allChildren.add(courses);
        }
        return allChildren;
    }
    
}
