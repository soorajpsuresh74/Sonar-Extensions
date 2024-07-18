package com.reflectionsinfos;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.python.api.PythonCustomRuleRepository;
import org.sonar.plugins.python.api.tree.FunctionDef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TooManyParametersCheck implements RulesDefinition, PythonCustomRuleRepository {

    @Override
    public void define(Context context) {
        NewRepository newRepository = context.createRepository(repositoryKey(), "py").setName("UNIQUE TESTING REPO");
        new RulesDefinitionAnnotationLoader().load(newRepository, checkClasses().toArray(new Class[]{}));

        Map<String, String> remediationCosts = new HashMap<>();
        remediationCosts.put(ParameterCounter.RULE_KEY, "5 min");

//        String compliantHtmlDescription = readFileContent("rab.txt");
//        String nonCompliantHtmlDescription = readFileContent("src/main/resources/templates/non_compliant_description.html");


        String compliantHtmlDescription = "<pre><code>def example_function(param1, param2, ..., param13):\n" +
                "    # Function logic here\n" +
                "    pass</code></pre>";

        String nonCompliantHtmlDescription = "<pre><code>def example_function(param1, param2, ..., param14):\n" +
                "    # Function logic here\n" +
                "    pass</code></pre>";

        System.out.println("Compliant HTML: " + compliantHtmlDescription);
        System.out.println("Non-Compliant HTML: " + nonCompliantHtmlDescription);

        newRepository.rules().forEach(rule -> {
            rule.setDebtRemediationFunction(
                    rule.debtRemediationFunctions().constantPerIssue(remediationCosts.get(rule.key()))
            );
            rule.setHtmlDescription(
                    "<h2>Compliant Code:</h2>" + compliantHtmlDescription +
                            "<h2>Non-Compliant Code:</h2>" + nonCompliantHtmlDescription
            );
        });
        newRepository.done();
    }

    @Override
    public String repositoryKey() {
        return "Too_many_parameters";
    }

    @Override
    public List<Class> checkClasses() {
        return Arrays.asList(ParameterCounter.class);
    }

    private String readFileContent(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.println("Read file content from: " + filePath);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
