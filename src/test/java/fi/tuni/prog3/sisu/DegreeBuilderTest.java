package fi.tuni.prog3.sisu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.*;
import org.junit.*;

/**
 * A class to perform unit tests on those DegreeBuilder methods which are 
 * considered viable for unit tests.
 */
public class DegreeBuilderTest {
    /**
     * Test of getRules method, of class DegreeBuilder.
     */
    @org.junit.Test
    public void testGetRules() {
        System.out.println("getRules");
        
        String ruleStr = "{\n" +
"    \"type\": \"CreditsRule\",\n" +
"    \"localId\": \"d120cb05-d3dd-41d7-bb3a-eec44edabea3\",\n" +
"    \"rule\": {\n" +
"      \"type\": \"CompositeRule\",\n" +
"      \"localId\": \"6bfe8c40-05fd-4fe4-befa-db6b6c97e74f\",\n" +
"      \"rules\": [\n" +
"        {\n" +
"          \"type\": \"CompositeRule\",\n" +
"          \"localId\": \"86b3f8cf-6413-416e-9360-ab2ace32acf5\",\n" +
"          \"rules\": [\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"243e2896-979c-4984-ba2e-e1ead0f98f1e\",\n" +
"              \"moduleGroupId\": \"otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"ebbda47e-c0d3-4385-8988-aa5dcd5fb041\",\n" +
"              \"moduleGroupId\": \"uta-ok-ykoodi-41176\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"7d7078d2-7565-4203-8fb5-17f6d2a0e3c9\",\n" +
"              \"moduleGroupId\": \"uta-ok-ykoodi-41177\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"otm-617e57b7-a17c-4238-8641-ddb063737914\",\n" +
"              \"moduleGroupId\": \"otm-6c36cb36-1507-44ff-baab-a30ac76ca786\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"otm-18f43e62-3b2a-4568-ac4e-e915f7bd2444\",\n" +
"              \"moduleGroupId\": \"otm-35d5a7e1-71c1-456a-8783-9cf8c34262f5\"\n" +
"            }\n" +
"          ],\n" +
"          \"require\": null,\n" +
"          \"description\": null,\n" +
"          \"allMandatory\": true\n" +
"        }\n" +
"      ],\n" +
"      \"require\": {\n" +
"        \"min\": 0,\n" +
"        \"max\": null\n" +
"      },\n" +
"      \"description\": null,\n" +
"      \"allMandatory\": false\n" +
"    },\n" +
"    \"credits\": {\n" +
"      \"min\": 180,\n" +
"      \"max\": null\n" +
"    }\n" +
"  }";
        String rulesStr = "[\n" +
"        {\n" +
"          \"type\": \"CompositeRule\",\n" +
"          \"localId\": \"86b3f8cf-6413-416e-9360-ab2ace32acf5\",\n" +
"          \"rules\": [\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"243e2896-979c-4984-ba2e-e1ead0f98f1e\",\n" +
"              \"moduleGroupId\": \"otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"ebbda47e-c0d3-4385-8988-aa5dcd5fb041\",\n" +
"              \"moduleGroupId\": \"uta-ok-ykoodi-41176\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"7d7078d2-7565-4203-8fb5-17f6d2a0e3c9\",\n" +
"              \"moduleGroupId\": \"uta-ok-ykoodi-41177\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"otm-617e57b7-a17c-4238-8641-ddb063737914\",\n" +
"              \"moduleGroupId\": \"otm-6c36cb36-1507-44ff-baab-a30ac76ca786\"\n" +
"            },\n" +
"            {\n" +
"              \"type\": \"ModuleRule\",\n" +
"              \"localId\": \"otm-18f43e62-3b2a-4568-ac4e-e915f7bd2444\",\n" +
"              \"moduleGroupId\": \"otm-35d5a7e1-71c1-456a-8783-9cf8c34262f5\"\n" +
"            }\n" +
"          ],\n" +
"          \"require\": null,\n" +
"          \"description\": null,\n" +
"          \"allMandatory\": true\n" +
"        }\n" +
"      ]";
        JSONObject firstInstance = new JSONObject(ruleStr);
        JSONArray firstExpResult = new JSONArray(rulesStr);
        JSONArray firstResult = new DegreeBuilder().getRules(firstInstance);
        assertEquals(firstExpResult.toString(), firstResult.toString());
        
        String noRulesStr = "{\n" +
"      \"type\": \"ModuleRule\",\n" +
"      \"localId\": \"otm-a8507846-404c-4216-abe1-ebbfe425da12\",\n" +
"      \"moduleGroupId\": \"otm-f69645a5-007b-4f4d-90e1-782494e3a84c\"\n" +
"    }";        
        JSONObject secondInstance = new JSONObject(noRulesStr);
        JSONArray secondExpResult = new JSONArray();
        JSONArray secondResult = new DegreeBuilder().getRules(secondInstance);
        assertEquals(secondExpResult.toString(), secondResult.toString());
    }
    
    /**
     * Test of getRules method, of class DegreeBuilder, throwing JSONException.
     */
    @org.junit.Test
    public void testWhenExceptionThrownGetRules() {
        System.out.println("whenExceptionThrownGetRules");
        
        String faultyRuleStr = "{\n" +
"    \"type\": \"CreditsRule\",\n" +
"    \"localId\": \"d120cb05-d3dd-41d7-bb3a-eec44edabea3\",\n" +
"    \"rule\": [\n" +
"    ]\n" +
"  }";
        JSONObject faultyRule = new JSONObject(faultyRuleStr);
        Exception firstException = assertThrows(JSONException.class, () -> {
           new  DegreeBuilder().getRules(faultyRule);
        });
        System.out.println(firstException.getMessage());
        
        String faultyRulesStr = "{\n" +
"          \"type\": \"CompositeRule\",\n" +
"          \"localId\": \"86b3f8cf-6413-416e-9360-ab2ace32acf5\",\n" +
"          \"rules\": {\n" +
"          },\n" +
"          \"require\": null,\n" +
"          \"description\": null,\n" +
"          \"allMandatory\": true\n" +
"        }";
        JSONObject faultyRules = new JSONObject(faultyRulesStr);
        Exception secondException = assertThrows(JSONException.class, () -> {
           new  DegreeBuilder().getRules(faultyRules);
        });
        System.out.println(secondException.getMessage());        
    }
    
    /**
     * Test of getRule method, of class DegreeBuilder.
     */
    @org.junit.Test
    public void testGetRule() {
        System.out.println("getRule");
        
        // The rule of type ModuleRule is returned correctly.
        String firstRuleStr = "{\n" +
"      \"type\": \"ModuleRule\",\n" +
"      \"localId\": \"otm-a8507846-404c-4216-abe1-ebbfe425da12\",\n" +
"      \"moduleGroupId\": \"otm-f69645a5-007b-4f4d-90e1-782494e3a84c\"\n" +
"    }";
        JSONObject firstInstance = new JSONObject(firstRuleStr);
        JSONObject firstExpResult = new JSONObject(firstRuleStr);
        JSONObject firstResult = new DegreeBuilder().getRule(firstInstance);
        assertEquals(firstExpResult.toString(), firstResult.toString());
        
        // The rule of type CourseUnitRule is returned correctly.
        String secondRuleStr = "{\n" +
"          \"type\": \"CourseUnitRule\",\n" +
"          \"localId\": \"otm-01211452-ad69-40ee-83b0-33ff500dea3e\",\n" +
"          \"courseUnitGroupId\": \"otm-00f5c0e9-e35e-4bd7-8a55-f41028d23820\"\n" +
"        }";
        JSONObject secondInstance = new JSONObject(secondRuleStr);
        JSONObject secondExpResult = new JSONObject(secondRuleStr);
        JSONObject secondResult = new DegreeBuilder().getRule(secondInstance);
        assertEquals(secondExpResult.toString(), secondResult.toString());
        
        // An empty rule is returned in case no valid rule is found.
        String thirdRuleStr = "{\n" +
"            \"type\": \"AnyCourseUnitRule\",\n" +
"            \"localId\": \"b1a4e4f2-a77c-4826-bb09-abd0b18b60b4\"\n" +
"          }";
        JSONObject thirdInstance = new JSONObject(thirdRuleStr);
        JSONObject thirdExpResult = new JSONObject();
        JSONObject thirdResult = new DegreeBuilder().getRule(thirdInstance);
        assertEquals(thirdExpResult.toString(), thirdResult.toString());   
    }
    
    /**
     * Test of getRule method, of class DegreeBuilder, throwing JSONException.
     */
    @org.junit.Test
    public void testWhenExceptionThrownGetRule() {
        System.out.println("whenExceptionThrownGetRule");
        
        String faultyRuleStr = "{\n" +
"    \"type\": \"CreditsRule\",\n" +
"    \"localId\": \"d120cb05-d3dd-41d7-bb3a-eec44edabea3\",\n" +
"    \"rule\": [\n" +
"    ]\n" +
"  }";
        JSONObject faultyRule = new JSONObject(faultyRuleStr);
        Exception exception = assertThrows(JSONException.class, () -> {
           new  DegreeBuilder().getRule(faultyRule);
        });
        System.out.println(exception.getMessage());
    }
    
    /**
     * Test of createCourse method, of class DegreeBuilder.
     */
    @org.junit.Test
    public void testCreateCourse() {
        System.out.println("createCourse");

        StudyModule firstTestModule = new StudyModule("fTestName", "fTestId", 0);
        String firstCourseStr = "[\n" +
"    {\n" +
"    \"id\": \"otm-94ffcfc5-0db4-4507-b475-63f290639e04\",\n" +
"    \"credits\": {\n" +
"      \"min\": 5,\n" +
"      \"max\": 5\n" +
"    },\n" +
"    \"name\": {\n" +
"      \"en\": \"Introduction to Analysis\",\n" +
"      \"fi\": \"Johdatus analyysiin\"\n" +
"    },\n" +
"    \"code\": \"MATH.MA.110\",\n" +
"    \"content\": {\n" +
"      \"fi\": \"kissa\"\n" +
"    },\n" +
"    }\n" +
"  ]";
        new DegreeBuilder().createCourse(firstTestModule, firstCourseStr);
        String firstExpStr = "Introduction to Analysis" + 
                "MATH.MA.110" + 
                "otm-94ffcfc5-0db4-4507-b475-63f290639e04" + 
                "kissa" + 
                5;
        Course firstCourseResult = firstTestModule.getCourses().get(0);
        String firstResultStr = firstCourseResult.getName() + 
                firstCourseResult.getCode() + 
                firstCourseResult.getId() + 
                firstCourseResult.getContent() + 
                firstCourseResult.getCredits();
        assertEquals(firstExpStr, firstResultStr);
                
        // When a course doesn't have an ID, a Course object isn't created.
        StudyModule secondTestModule = new StudyModule("sTestName", "sTestId", 0);
        String secondCourseStr = "[\n" +
"    {\n" +
"    \"credits\": {\n" +
"      \"min\": 5,\n" +
"      \"max\": 5\n" +
"    },\n" +
"    \"name\": {\n" +
"      \"en\": \"Introduction to Analysis\",\n" +
"      \"fi\": \"Johdatus analyysiin\"\n" +
"    },\n" +
"    \"code\": \"MATH.MA.110\",\n" +
"    \"content\": {\n" +
"      \"fi\": \"kissa\"\n" +
"    },\n" +
"    }\n" +
"  ]";
        new DegreeBuilder().createCourse(secondTestModule, firstCourseStr);
        new DegreeBuilder().createCourse(secondTestModule, secondCourseStr);
        assertEquals(1, secondTestModule.getCourses().size());
    }
}
