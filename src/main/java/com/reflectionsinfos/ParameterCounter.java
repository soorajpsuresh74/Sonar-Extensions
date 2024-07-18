package com.reflectionsinfos;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.python.api.PythonSubscriptionCheck;
import org.sonar.plugins.python.api.SubscriptionContext;
import org.sonar.plugins.python.api.tree.FunctionDef;
import org.sonar.plugins.python.api.tree.LambdaExpression;
import org.sonar.plugins.python.api.tree.ParameterList;
import org.sonar.plugins.python.api.tree.Tree;

@Rule(key = ParameterCounter.RULE_KEY,
        priority = Priority.INFO,
        name = "A Too Many Parameters Accepted",
        description = "This rule checks for functions or methods that use an excessive number of parameters."
)
public class ParameterCounter extends PythonSubscriptionCheck {
    public static final String MESSAGE = "Usage of excess number of Parameters in the function";
    public int max = 13;
    public static final String RULE_KEY = "Parametes";

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.FUNCDEF, this::checkFunctionDef);
        context.registerSyntaxNodeConsumer(Tree.Kind.LAMBDA, ctx -> {
            LambdaExpression tree = (LambdaExpression) ctx.syntaxNode();
            ParameterList parameters = tree.parameters();
            if (parameters != null) {
                int nbParameters = parameters.all().size();
                if (nbParameters > max) {
                    String name = "Lambda";
                    String message = String.format(MESSAGE, name, nbParameters, max);
                    ctx.addIssue(parameters, message);
                }
            }
        });
    }

    private void checkFunctionDef(SubscriptionContext ctx) {
        FunctionDef functionDef = (FunctionDef) ctx.syntaxNode();
        ParameterList parameters = functionDef.parameters();

        if (parameters != null) {
            long nbParameters = parameters.all().size();
            boolean isMethod = functionDef.isMethodDefinition();
            if (isMethod) {
                // First parameter is implicitly passed: either "self" or "cls"
                nbParameters -= 1;
            }
            if (nbParameters > max) {
                String typeName = isMethod ? "Method" : "Function";
                String name = String.format("%s \"%s\"", typeName, functionDef.name().name());
                String message = String.format(MESSAGE, name, nbParameters, max);
                ctx.addIssue(parameters, message);
            }
        }
    }
}
