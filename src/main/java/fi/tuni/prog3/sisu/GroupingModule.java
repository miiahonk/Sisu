package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * A class for maintaining Tampere University grouping modules
 */
public class GroupingModule extends SisuModule{
    
    private final String name;
    private final String id;
    private final int credits;
    private ArrayList<StudyModule> studymodules = new ArrayList<>();
    private ArrayList<GroupingModule> subgroupingmodules = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<ArrayList<? extends SisuModule>> allChildren;
    
    /**
     * Constructs a GroupingModule object.
     * @param name the name of the grouping module
     * @param id the ID of the grouping module
     * @param credits the extent of the grouping module
     */
    public GroupingModule(String name, String id, int credits) {
        this.name = name;
        this.id = id;
        this.credits = credits;
    }

    /**
     * Returns the name of the grouping module
     * @return the name of the grouping module
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Returns the ID of the grouping module
     * @return the ID of the grouping module
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the extent of the grouping module
     * @return the extent of the grouping module
     */
    @Override
    public int getCredits() {
        return credits;
    }
    
    /**
     * Returns the list of study modules directly under the grouping module
     * @return the list of study modules directly under the grouping module
     */
    public ArrayList<StudyModule> getSubmodules() {
        return studymodules;
    }
    
    /**
     * Returns the list of grouping modules directly under the grouping module
     * @return the list of grouping modules directly under the grouping module
     */
    public ArrayList<GroupingModule> getSubgroupingmodules() {
        return subgroupingmodules;
    }
    
    /**
     * Returns the list of courses directly  under the study module
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
            studymodules.add((StudyModule)module);
        }
        else if (module instanceof GroupingModule) {
            subgroupingmodules.add((GroupingModule)module);
        }
        else if (module instanceof Course) {
            courses.add((Course)module);
        }
    }
    
    /**
     * Returns a list of grouping module's children
     * @return a list of grouping module's children
     */
    @Override
    public ArrayList<ArrayList<? extends SisuModule>> getChildren() {
        allChildren = new ArrayList<>();

        if (!studymodules.isEmpty()) {
            allChildren.add(studymodules);
        }
        if (!subgroupingmodules.isEmpty()) {
            allChildren.add(subgroupingmodules);
        }
        if (!courses.isEmpty()) {
            allChildren.add(courses);
        }
        return allChildren;
    }
}
