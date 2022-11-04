package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * A class for maintaining Tampere University specialisations
 */
public class Specialisation extends SisuModule {
    
    private final String name;
    private final String id;
    private final int credits;
    private ArrayList<StudyModule> studymodules = new ArrayList<>();
    private ArrayList<GroupingModule> groupingmodules = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<ArrayList<? extends SisuModule>> allChildren;
    
   /**
     * Constructs a Specialisation object.
     * @param name the name of the specialisation
     * @param id the ID of the specialisation
     * @param credits the extent of the spesialisation
     */
    public Specialisation(String name, String id, int credits) {
        this.name = name;
        this.id = id;
        this.credits = credits;
    }

   /**
     * Returns the name of the spesialisation
     * @return the name of the spesialisation
     */
    @Override
    public String getName() {
        return name;
    }

   /**
     * Returns the ID of the specialisation
     * @return the ID of the specialisation
     */
    @Override
    public String getId() {
        return id;
    }
    
    /**
     * Returns the extent of the spesialisation
     * @return the extent of the spesialisation
     */
    @Override
    public int getCredits() {
        return credits;
    }
    
    /**
     * Returns the list of study modules directly under the specialisation
     * @return the list of study modules directly under the specialisation
     */
    public ArrayList<StudyModule> getStudyModules() {
        return studymodules;
    }
    
    /**
     * Returns the list of grouping modules directly under the specialisation
     * @return the list of grouping modules directly under the specialisation
     */
    public ArrayList<GroupingModule> getGroupingmodules() {
        return groupingmodules;
    }
    
    /**
     * Returns the list of courses directly under the specialisation
     * @return the list of courses directly under the specialisation
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
            groupingmodules.add((GroupingModule)module);
        }
        else if (module instanceof Course) {
            courses.add((Course)module);
        }
    }
    
    /**
     * Returns a list of specialisation's children
     * @return a list of specialisation's children
     */
    @Override
    public ArrayList<ArrayList<? extends SisuModule>> getChildren() {
        allChildren = new ArrayList<>();

        if (!studymodules.isEmpty()) {
            allChildren.add(studymodules);
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