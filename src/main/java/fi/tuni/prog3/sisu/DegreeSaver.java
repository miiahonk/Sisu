package fi.tuni.prog3.sisu;
        
import java.util.TreeSet;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileWriter;

/**
 * A class for saving student's degree
 */
public class DegreeSaver {
    
    /**
     * The ID's of the completed courses
     */
    TreeSet<String> completedCourses = new TreeSet<>();

    /**
     * Saves student's degree plan to JSON file
     * @param student the Student object of the user
     * @throws java.io.IOException
     */
    public static void save(Student student) throws IOException {
        
        String filename = student.getId() + ".json";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject studentJson = new JsonObject();

        studentJson.addProperty("name", student.getName());
        studentJson.addProperty("id", student.getId());
        studentJson.addProperty("degree programme", student.getDegreeProgramme());

        if (student.getCompletedCourses().size() > 0) {
            JsonArray courses = new JsonArray();
            for (var c : student.getCompletedCourses()) {
                courses.add(c);
            }
            studentJson.add("courses", courses);
        }

        FileWriter fileWriter = new FileWriter(filename);

        gson.toJson(studentJson, fileWriter);

        fileWriter.close();
        
        
    }
    
    /**
     * Returns the Student object of the user
     * @param filename the saved JSON file
     * @return the Student object of the user or null
     * @throws java.io.FileNotFoundException
     */
    public static Student getStudent(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        

        JsonObject root = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();

        JsonElement name = root.get("name");
        String sName = name.toString().substring(1, name.toString().length() - 1);
        JsonElement id = root.get("id");
        String sId = id.toString().substring(1, id.toString().length() - 1);
        JsonElement degreeProgramme = root.get("degree programme");
        String sDegree = degreeProgramme.toString().substring(1, degreeProgramme.toString().length() - 1);

        Student student = new Student(sName, sId, sDegree);

        if (root.getAsJsonArray("courses") != null) {
            JsonArray courses = root.getAsJsonArray("courses");

            TreeSet<String> completedCourses = new TreeSet<>();

            for (var c : courses) {
                completedCourses.add(c.toString().substring(1, c.toString().length() - 1));
            }

            student.addCompletedCourses(completedCourses);
        }

        return student; 

     }  
}
