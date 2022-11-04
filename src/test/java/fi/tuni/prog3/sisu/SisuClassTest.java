package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A class to perform unit tests on Sisu classes
 */
public class SisuClassTest {

    /**
     *  Unit tests for DegreeProgramme class
     */

    /**
     * Verify that getName gives the correct name for DegreeProgramme object
     */
    @org.junit.Test
    public void testGetDegreeName() {
        DegreeProgramme degree = new DegreeProgramme("Architecture", "123", 120);
        String result = degree.getName();
        String expResult = "Architecture";
        
        System.out.println("getDegreeName");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that getId gives the correct ID for DegreeProgramme object
     */
    @org.junit.Test
    public void testGetDegreeId() {
        DegreeProgramme degree = new DegreeProgramme("Architecture", "123", 120);
        String result = degree.getId();
        String expResult = "123";
        
        System.out.println("getDegreeId");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that getCredits gives the correct number of credits for DegreeProgramme object
     */
    @org.junit.Test
    public void testGetDegreeCredits() {
        DegreeProgramme degree = new DegreeProgramme("Architecture", "123", 120);
        int result = degree.getCredits();
        int expResult = 120;
        
        System.out.println("getDegreeCredits");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that addItem adds the modules to the correct ArrayList
     */
    @org.junit.Test
    public void testDegreeAddItem() {
        DegreeProgramme degree = new DegreeProgramme("Architecture", "123", 120);
        
        Specialisation spec1 = new Specialisation("Tiny homes and yards", "246", 30);
        Specialisation spec2 = new Specialisation("Large houses and yards", "468", 60);
        
        degree.addItem(spec1);
        degree.addItem(spec2);
     
        ArrayList<Specialisation> specs = degree.getSpecialisations();
        int result = specs.size();
        int expResult = 2;
        
        System.out.println("degreeAddSpecs");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that getChildren returns all the ArrayLists
     */
    @org.junit.Test
    public void testDegreeGetChildren() {
        DegreeProgramme degree = new DegreeProgramme("Architecture", "123", 120);
        
        Specialisation spec1 = new Specialisation("Tiny homes and yards", "246", 30);
        Specialisation spec2 = new Specialisation("Large houses and yards", "468", 60);
        
        Course course = new Course("Walk-in-closets in tiny homes", "tiny123", "2244", "Course gives an overview of how to design tiny homes with walk-in-closets", 4);

        degree.addItem(spec1);
        degree.addItem(spec2);
        degree.addItem(course);
     
        ArrayList<ArrayList<? extends SisuModule>> allChildren = degree.getChildren();
        int result = allChildren.size();
        int expResult = 2;
        
        System.out.println("degreegetChildren");
        assertEquals(expResult, result);
    }
    
    
    /**
     *  Unit tests for StudyModule class
     */

    /**
     * Verify that getName gives the correct name for StudyModule object
     */
    @org.junit.Test
    public void testGetStudyModuleName() {
        StudyModule module = new StudyModule("Buildings and bridges", "3123", 20);
        String result = module.getName();
        String expResult = "Buildings and bridges";
        
        System.out.println("getStudyModuleName");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that addItem adds the modules to the correct ArrayList
     */
    @org.junit.Test
    public void testStudyModuleAddItem() {
        StudyModule module = new StudyModule("Buildings and bridges", "3123", 20);

        StudyModule submodule = new StudyModule("Steel bridge design", "303", 10);
        
        module.addItem(submodule);
     
        ArrayList<StudyModule> submodules = module.getSubmodules();
        int result = submodules.size();
        int expResult = 1;
        
        System.out.println("StudymoduleAddSubmodules");
        assertEquals(expResult, result);
    }
    
    
    /**
     *  Tests for Student class
     */
    
    /**
     * Verify that isDegreeSet returns true if student has chosen a degree programme
     */
    @org.junit.Test
    public void testStudentIsDegreeSet1() {
        Student stu = new Student("Sisko Sisu", "ss123", "");
        
        stu.setDegreeProgramme("123");
        
        boolean result = stu.isDegreeSet();
        boolean expResult = true;
        
        System.out.println("studentIsDegreeSetTrue");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that isDegreeSet returns false if student has not chosen a degree programme
     */
    @org.junit.Test
    public void testStudentIsDegreeSet2() {
        Student stu = new Student("Sisko Sisu", "ss123", "");
        
        boolean result = stu.isDegreeSet();
        boolean expResult = false;
        
        System.out.println("studentIsDegreeSetFalse");
        assertEquals(expResult, result);
    }
    
    /**
     * Verify that getCompletedCourses returns all completed courses
     */
    @org.junit.Test
    public void testStudentGetCompletedCourses() {
        Student stu = new Student("Sisko Sisu", "ss123", "");
        
        stu.addCompletedCourse("2468");
        stu.addCompletedCourse("2246");
        stu.addCompletedCourse("1208");
        
        int result = stu.getCompletedCourses().size();
        int expResult = 3;
        
        System.out.println("studentGetCompletedCourses");
        assertEquals(expResult, result);
    }

    /**
     * Verify that clearCompletedCourses clears all completed courses from Student object
     */
    @org.junit.Test
    public void testStudentClearCompletedCourses() {
        Student stu = new Student("Sisko Sisu", "ss123", "");
        
        stu.addCompletedCourse("2468");
        stu.addCompletedCourse("2246");
        stu.addCompletedCourse("1208");
        
        stu.clearCompletedCourses();
        
        int result = stu.getCompletedCourses().size();
        int expResult = 0;
        
        System.out.println("studentGetCompletedCourses");
        assertEquals(expResult, result);
    }
    
    
}