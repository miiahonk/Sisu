package fi.tuni.prog3.sisu;

import org.json.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A class to construct data of degree programmes and contents for a single 
 * degree programme of Tampere University. 
 */
public class DegreeBuilder {
    private HashMap<String, String> requestAddresses = new HashMap<>();
    private HashMap<String, DegreeProgramme> degrees = new LinkedHashMap<>();
    private static HttpURLConnection connection;
    
    /**
     * Builds a container of http-request addresses to access Sisu Kori API. 
     */
    private void setRequestAddresses() {
        requestAddresses.put("rootDataReq", "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        requestAddresses.put("idReq", "https://sis-tuni.funidata.fi/kori/api/modules/");
        requestAddresses.put("groupIdReq", "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?&universityId=tuni-university-root-id&groupId=");
        requestAddresses.put("courseGroupIdReq", "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?&universityId=tuni-university-root-id&groupId=");
    }
    
    /**
     * Returns a container "degrees" of degree programmes identified by their 
     * IDs. The container is empty if Kori API http-request fails.
     * @return A HashMap "degrees" with degree programmes of type DegreePogramme
     * and ID keys of type String. "Degrees" is empty if data fetching fails.
     */
    public HashMap<String, DegreeProgramme> getDegrees() {
        setRequestAddresses();
        String rootData = fetchDegreeData("rootDataReq", "");
        if(!rootData.equals("")) {
            parseRootData(rootData);
        }        
        return degrees;
    }
    
    /**
     * Updates the contents of a given degree programme and returns the updated 
     * degree programme.
     * @param degree A DegreeProgramme object.
     * @return The received DegreeProgramme object "degree" with updated 
     * content.
     */
    public DegreeProgramme getDegreeContent(DegreeProgramme degree) {
        setRequestAddresses();
        String degProgStr = fetchDegreeData("idReq", degree.getId());
        if(!degProgStr.equals("")) {
            parseDegreeProgramme(degProgStr, degree);
        }
        return degree;
    }

    /**
     * Fetches and returns degree programme related data in JSON form from Sisu 
     * Kori API.
     * @param reqType A String key for http-request address type.
     * @param idStr An ID or a group ID value in String form of a module or 
     * course unit.
     * @return The fetched JSON data of a module or a course unit in String. If 
     * fetching fails, an empty String value is returned.
     */
    private String fetchDegreeData(String reqType, String idStr) {
        String reqAddress = requestAddresses.get(reqType) + idStr;
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL(reqAddress); 
            connection = (HttpURLConnection) url.openConnection();           
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int status = connection.getResponseCode();            
            if(status > 299) {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream())
                );
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            connection.disconnect();
            return responseContent.toString();
        }
        catch(IOException e) {
            e.printStackTrace();
            connection.disconnect();
            return "";
        }   
    }
    
    /**
     * Receives and parses root data of degree programmes to create and save
     * DegreeProgramme objects.
     * @param responseBody The fetched JSON-formatted root data of degree 
     * programmes in String form.
     */
    private void parseRootData(String responseBody) {
        try {
            JSONObject bodyContents = new JSONObject(responseBody);
            JSONArray uniDegrees = bodyContents.getJSONArray("searchResults");
            
            for(int i = 0; i < uniDegrees.length(); i++) {
                try {
                    JSONObject degreeObj = uniDegrees.getJSONObject(i);
                    String degreeName = degreeObj.getString("name");
                    int degreeCredits = degreeObj
                            .getJSONObject("credits")
                            .getInt("min");
                    String degreeId = degreeObj.getString("id");
                    DegreeProgramme degreeProgramme = new DegreeProgramme(
                            degreeName, 
                            degreeId, 
                            degreeCredits
                    );
                    degrees.put(degreeId, degreeProgramme); 
                }
                catch(JSONException e) {
                    // Invalid degree programme modules are ignored.
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }                           
    }
    
    /**
     * Parses the given degree programme data to update the contents of the 
     * related DegreeProgramme object. 
     * @param responseBody String type data of the given degree programme in 
     * JSON format.
     * @param degreeProgramme The given DegreeProgramme object to be updated.
     */
    private void parseDegreeProgramme(
            String responseBody, 
            DegreeProgramme degreeProgramme
    ) {
        try {
            JSONObject bodyContents = new JSONObject(responseBody);
            JSONObject ruleObj = bodyContents.getJSONObject("rule");
            findObjects(ruleObj, degreeProgramme, true);
        }
        catch(JSONException e) {
            // Invalid degree programme modules are ignored.
        }
    }
    
    /**
     * Parses the given course data to create and save the course in the given 
     * supermodule.
     * @param superModule The SisuModule extending supermodule of a higher 
     * hierarchy, under which the course is placed.
     * @param courseStr The String type and JSON-formatted data of the course.
     */
    public void createCourse(SisuModule superModule, String courseStr) {
        try {
            JSONArray courseArr = new JSONArray(courseStr);           
            for(int i = 0; i < courseArr.length(); i++) {
                try {
                    JSONObject courseObj = courseArr.getJSONObject(i);

                    String name = "";
                    if(courseObj.has("name") && !courseObj.isNull("name")) {
                        JSONObject nameObj = courseObj.getJSONObject("name");
                        if(nameObj.has("en") && !nameObj.isNull("en")) {
                            name = nameObj.getString("en");
                        }
                        else if(nameObj.has("fi") && !nameObj.isNull("fi")) {
                            name = nameObj.getString("fi");
                        }
                        else if(nameObj.has("sv") && !nameObj.isNull("sv")) {
                            name = nameObj.getString("sv");
                        }
                    }

                    String code = "";
                    if(courseObj.has("code") && !courseObj.isNull("code")) {
                        code = courseObj.getString("code");
                    }

                    String id = "";
                    if(courseObj.has("id") && !courseObj.isNull("id")) {
                        id = courseObj.getString("id");
                    }

                    String content = "";
                    if(courseObj.has("content") && 
                            !courseObj.isNull("content")) {                
                        JSONObject contentObj = courseObj
                                .getJSONObject("content");
                        if(contentObj.has("en") && !contentObj.isNull("en")) {
                            content = contentObj.getString("en");
                        }
                        else if(contentObj.has("fi") && 
                                !contentObj.isNull("fi")) {
                            content = contentObj.getString("fi");
                        }
                        else if(contentObj.has("sv") && 
                                !contentObj.isNull("sv")) {
                            content = contentObj.getString("sv");
                        }
                    }

                    int credits = 0;
                    if(courseObj.has("credits") && 
                            !courseObj.isNull("credits")) {
                        JSONObject creditsObj = courseObj
                                .getJSONObject("credits");
                        if(creditsObj.has("min") && 
                                !creditsObj.isNull("min")) {
                            credits = creditsObj.getInt("min");
                        }
                        else if(creditsObj.has("max") && 
                                !creditsObj.isNull("max")) {
                            credits = creditsObj.getInt("max");
                        }
                    }
                    if(!id.equals("")) {
                        Course newCourse = new Course(
                                name, 
                                code, 
                                id, 
                                content, 
                                credits
                        );
                        superModule.addItem(newCourse);
                    }
                }
                catch(JSONException e) {
                    // Invalid course units within 
                    // the course unit list are ignored.
                }
            }
        }
        catch(JSONException e) {
            // Invalid course units are ignored.
        }
    }
    
    /**
     * Creates a new GroupingModule object from the given module name and ID, 
     * adds it to the given supermodule, and initiates a new search for 
     * submodules or course units under the created GroupingModule object using 
     * the given rule object.
     * @param newRuleObj A JSONObject type rule containing a possible subrule or 
     * multiple rules.
     * @param superModule A supermodule that extends SisuModule and represents 
     * a module of a higher hierachy to the submodules to be found.
     * @param name The String type name of the given module.
     * @param id The String type ID of the given module.
     * @throws JSONException 
     */
    private void createGroupingModule(
            JSONObject newRuleObj, 
            SisuModule superModule, 
            String name, 
            String id
    ) throws JSONException {
        GroupingModule newGroMod = new GroupingModule(name, id, 0);
        superModule.addItem(newGroMod);
        findObjects(newRuleObj, newGroMod, false);
    }
    
    /**
     * Creates a new Specialisation object from the given module name, ID, and 
     * credits, adds it to the given supermodule, and initiates a new search for
     * submodules or course units under the created Specialisation object using 
     * the given rule object.
     * @param newRuleObj A JSONObject type rule containing a possible subrule or 
     * multiple rules.
     * @param superModule A supermodule that extends SisuModule and represents 
     * a module of a higher hierachy to the submodules to be found.
     * @param name The String type name of the given module.
     * @param id The String type ID of the given module.
     * @param credits The int type credits of the given module.
     * @throws JSONException 
     */
    private void createSpecialisation(
            JSONObject newRuleObj, 
            SisuModule superModule, 
            String name, 
            String id, 
            int credits
    ) throws JSONException {
        Specialisation newSpec = new Specialisation(name, id, credits);
        superModule.addItem(newSpec);
        findObjects(newRuleObj, newSpec, false);
    }    
    
    /**
     * Receives module data and information on whether it refers to a new 
     * Specialisation or StudyModule object to be created, and based on the 
     * latter case creates a new StudyModule object from the given module name, 
     * ID, and credits, adds it to the given supermodule, and initiates a new 
     * search for submodules or course units under the created StudyModule 
     * object using the given rule object. Otherwise calls a function 
     * createSpecialisation to create a Specialisation object from the given 
     * data. 
     * @param specialisationsIncomplete A boolean variable to indicate whether 
     * all specialisations on the current hierarchical level have been created 
     * for the updateable degree programme.
     * @param newRuleObj A JSONObject type rule containing a possible subrule or 
     * multiple rules.
     * @param superModule A supermodule that extends SisuModule and represents 
     * a module of a higher hierachy to the submodules to be found.
     * @param name The String type name of the given module.
     * @param id The String type ID of the given module.
     * @param credits The int type credits of the given module.
     * @throws JSONException 
     */
    private void createStudyModule(
            boolean specialisationsIncomplete, 
            JSONObject newRuleObj, 
            SisuModule superModule, 
            String name, 
            String id, 
            int credits
    ) throws JSONException {
        if(specialisationsIncomplete) {
            createSpecialisation(newRuleObj, superModule, name, id, credits);
        }
        else {
            StudyModule newStudyMod = new StudyModule(name, id, credits);
            superModule.addItem(newStudyMod);
            findObjects(newRuleObj, newStudyMod, false);
        }
    }
    
    /**
     * Searches a list of rules recursively and returns the first encountered 
     * list of rules, which is empty if no such list is found.
     * @param ruleContents The contents of the given rule of JSONObject type.
     * @return A JSONArray type "rules" including possible module and course 
     * unit rules. "Rules" is empty, if no such list is found.
     * @throws JSONException 
     */
    public JSONArray getRules(JSONObject ruleContents) throws JSONException {
        JSONArray rules = new JSONArray();
        if(ruleContents.has("rule")) {
            rules = getRules(ruleContents.getJSONObject("rule"));
        }
        else if(ruleContents.has("rules")) {
            rules = ruleContents.getJSONArray("rules");
        }
        return rules;
    }
    
    /**
     * Searches a rule of either type "ModuleRule" or "CourseUnitRule" 
     * recursively and returns the first encountered valid rule. Otherwise 
     * returns an empty rule object.
     * @param ruleContents The contents of the given rule of JSONObject type.
     * @return A JSONObject type "rule", which either represents a "ModuleRule"
     * or "CourseUnitRule". "Rule" is empty, if no valid rule is found.
     * @throws JSONException 
     */
    public JSONObject getRule(JSONObject ruleContents) throws JSONException {
        JSONObject rule = new JSONObject();
        if(ruleContents.has("rule")) {
            rule = getRule(ruleContents.getJSONObject("rule"));
        }
        else if(ruleContents.has("type")) {
            if(ruleContents.getString("type").equals("ModuleRule") || 
                ruleContents.getString("type").equals("CourseUnitRule")) {
                rule = ruleContents;
            }
        }
        return rule;
    }
    
    /**
     * Parses the given submodule, that can be either of type "StudyModule" or 
     * "GroupingModule" and belongs under the given supermodule, and passes on 
     * the information about whether the list of possible Specialisation objects
     * on the current hierarchical level is incomplete under the updateable 
     * DegreeProgramme object.
     * @param moduleStr A JSON-formatted and String type module that can be 
     * either "StudyModule" or "GroupingModule".
     * @param superModule A supermodule that extends SisuModule and represents 
     * a module of a higher hierachy to the submodules to be found.
     * @param specialisationsIncomplete A boolean variable to indicate whether 
     * all specialisations on the current hierarchical level have been created 
     * for the updateable degree programme.
     */
    private void parseModuleRule(
            String moduleStr, 
            SisuModule superModule, 
            boolean specialisationsIncomplete
    ) {
        try {
            JSONArray moduleArr = new JSONArray(moduleStr);
            for(int i = 0; i < moduleArr.length(); i++) {
                try {
                    JSONObject moduleObj = moduleArr.getJSONObject(i);
                    String name = "";
                    if(moduleObj.has("name") && !moduleObj.isNull("name")) {
                        JSONObject nameObj = moduleObj.getJSONObject("name");
                        if(nameObj.has("en") && !nameObj.isNull("en")) {
                            name = nameObj.getString("en");
                        }
                        else if(nameObj.has("fi") && !nameObj.isNull("fi")) {
                            name = nameObj.getString("fi");
                        }
                        else if(nameObj.has("sv") && !nameObj.isNull("sv")) {
                            name = nameObj.getString("sv");
                        }
                    }
                    String id = "";
                    if(moduleObj.has("id") && !moduleObj.isNull("id")) {
                        id = moduleObj.getString("id"); 
                    }
                    JSONObject newRuleObj = moduleObj.getJSONObject("rule");

                    if(moduleObj.getString("type").equals("StudyModule")) {
                        int credits = 0;
                        if(moduleObj.has("targetCredits") && 
                                !moduleObj.isNull("targetCredits")) {
                            JSONObject creditsObj = moduleObj
                                    .getJSONObject("targetCredits");
                            if(creditsObj.has("min") && 
                                    !creditsObj.isNull("min")) {
                                credits = creditsObj.getInt("min");
                            }
                            else if(creditsObj.has("max") && 
                                    !creditsObj.isNull("max")) {
                                credits = creditsObj.getInt("max");
                            }
                        }                        
                        createStudyModule(
                                specialisationsIncomplete, 
                                newRuleObj, 
                                superModule, 
                                name, 
                                id, 
                                credits
                        );        
                    }
                    else if(
                            moduleObj.getString("type").equals("GroupingModule")
                    ) {
                        createGroupingModule(newRuleObj, superModule, name, id); 
                    }
                }
                catch(JSONException e) {
                    // Invalid modules within the module list are ignored.
                }
            }
        }
        catch(JSONException e) {
            // Invalid modules are ignored.
        }
    }
        
    /**
     * Looks for all the submodules and course units recursively under the given
     * supermodule and passes on the information about whether the list of 
     * possible Specialisation objects on the current hierarchical level is 
     * incomplete under the updateable DegreeProgramme object.
     * @param ruleObj A JSONObject type rule containing a possible subrule or 
     * multiple rules. 
     * @param superModule A supermodule that extends SisuModule and represents 
     * a module of a higher hierachy to the submodules or course units to be 
     * found.
     * @param specialisationsIncomplete A boolean variable to indicate whether 
     * all specialisations on the current hierarchical level have been created 
     * for the updateable degree programme.
     * @throws JSONException
     */
    private void findObjects(
            JSONObject ruleObj, 
            SisuModule superModule, 
            boolean specialisationsIncomplete
    ) throws JSONException {
        if(ruleObj.getString("type").equals("ModuleRule")) {
            String moduleStr = fetchDegreeData(
                    "groupIdReq", 
                    ruleObj.getString("moduleGroupId")
            );
            if(!moduleStr.equals("")) {
                parseModuleRule(
                        moduleStr, 
                        superModule, 
                        specialisationsIncomplete
                );
            }                                        
        }
        else if(ruleObj.getString("type").equals("CourseUnitRule")) {
            String courseStr = fetchDegreeData(
                    "courseGroupIdReq", 
                    ruleObj.getString("courseUnitGroupId")
            );
            if(!courseStr.equals("")) {
                createCourse(superModule, courseStr);
            }   
        }
        else {
            JSONArray rules = getRules(ruleObj);
            if(rules.isEmpty()) {
                if(!getRule(ruleObj).isEmpty()) {
                    findObjects(
                            getRule(ruleObj), 
                            superModule, 
                            specialisationsIncomplete
                    );
                }  
            }
            else {
                for(int i = 0; i < rules.length(); i++) {
                    JSONObject newRuleObj = rules.getJSONObject(i);
                    findObjects(
                            newRuleObj, 
                            superModule, 
                            specialisationsIncomplete
                    );
                }
            } 
        }   
    }
}
