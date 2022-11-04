package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * A class for maintaining Tampere University degree programmes.
 * 
 */
public class DegreeProgramme extends SisuModule{
    
    private final String name;
    private final String id;
    private final int credits;
    private ArrayList<Specialisation> specialisations = new ArrayList<>();
    private ArrayList<StudyModule> studymodules = new ArrayList<>();
    private ArrayList<GroupingModule> groupingmodules = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<ArrayList<? extends SisuModule>> allChildren;

    /**
     * Constructs a DegreeProgramme object.
     * @param name the name of the degree programme
     * @param id the ID of the degree programme
     * @param credits the extent of the degree programme
     */
    public DegreeProgramme(String name, String id, int credits) {
        this.name = name;
        this.id = id;
        this.credits = credits;
    }

    /**
     * Returns the name of the degree programme
     * @return the name of the degree programme
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the ID of the degree programme
     * @return the ID of the degree programme
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns the extent of the degree programme
     * @return the extent of the degree programme
     */
    @Override
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the list of specialisations under the degree programme
     * @return the list of specialisations under the degree programme
     */
    public ArrayList<Specialisation> getSpecialisations() {
        return specialisations;
    }

    /**
     * Returns the list of study modules directly under the degree programme
     * @return the list of study modules directly under the degree programme
     */
    public ArrayList<StudyModule> getStudymodules() {
        return studymodules;
    }

    /** 
     * Returns the list of grouping modules directly under the degree programme
     * @return the list of grouping modules directly under the degree programme
     */
    public ArrayList<GroupingModule> getGroupingmodules() {
        return groupingmodules;
    }
    

    /**
     * Returns the list of courses directly under the degree programme
     * @return the list of courses directly under the degree programme
     */
    @Override
    public ArrayList<Course> getCourses() {
        return courses;
    }
    
    /**
     * Adds a module to the correct list
     * @param module the module to be added, the type can be either Specialisation, StudyModule, GroupingModule or Course
     */
    @Override
    public void addItem(SisuModule module) {
        if (module instanceof Specialisation) {
            specialisations.add((Specialisation)module);
        }
        else if (module instanceof StudyModule) {
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
     * Returns a list of degree programme's children
     * @return a list of degree programme's children
     */
    @Override
    public ArrayList<ArrayList<? extends SisuModule>> getChildren() {
        allChildren = new ArrayList<>();
        
        if (!specialisations.isEmpty()) {
            allChildren.add(specialisations);
        }
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