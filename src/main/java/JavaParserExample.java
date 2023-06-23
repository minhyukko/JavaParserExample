import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.VariableDeclarator;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class JavaParserExample {

    public static void main(String[] args) {
        // Specify the path to the Java source code file
        String filePath = "C:\\Users\\minhy\\Desktop\\Main.java";

        try {
            // Create a JavaParser instance
            JavaParser javaParser = new JavaParser();

            // Load the Java source code file
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);

            // Parse the Java source code
            CompilationUnit compilationUnit = javaParser.parse(fis).getResult().orElseThrow();

//            // Create a visitor to traverse the AST
//            VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<>() {
//                @Override
//                public void visit(MethodDeclaration methodDeclaration, Void arg) {
//                    // Perform actions on method declarations
//                    // Example: Extract method names
//                    System.out.println("Method name: " + methodDeclaration.getNameAsString());
//                    super.visit(methodDeclaration, arg);
//                }
//            };

            // Visit the AST using the visitor
//            visitor.visit(compilationUnit, null);

            // Generate Abstract Syntax Tree
            ParserConfiguration parserConfiguration = new ParserConfiguration();
//            CompilationUnit compilationUnit = StaticJavaParser.parse(new File("C:\\Users\\minhy\\Desktop\\Main.java"));

            VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<>() {
                @Override
                public void visit(MethodDeclaration methodDeclaration, Void arg) {
                    System.out.println("Method name: " + methodDeclaration.getName());
                    super.visit(methodDeclaration, arg);
                }
            };

//            VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<>() {
//                @Override
//                public void visit(FieldDeclaration fieldDeclaration, Void arg) {
//                    // Check if the field declaration matches the desired condition
//                    if (fieldDeclaration.getVariable(0).getNameAsString().equals("obj")) {
//                        System.out.println("Variable pb found at line " + fieldDeclaration.getRange().get().begin.line);
//                    }
//                    super.visit(fieldDeclaration, arg);
//                }
//            };

// Parse the Java file
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(new File(filePath));

            // Retrieve the CompilationUnit from the ParseResult
            if (parseResult.isSuccessful()) {

                // Find all field declarations
                List<FieldDeclaration> fieldDeclarations = compilationUnit.findAll(FieldDeclaration.class);

                // Set to track variable names already printed
                Set<String> printedVariableNames = new HashSet<>();

                // Print variable names from field declarations
                for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
                    List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
                    for (VariableDeclarator variableDeclarator : variableDeclarators) {
                        String variableName = variableDeclarator.getNameAsString();
                        if (!printedVariableNames.contains(variableName)) {
                            System.out.println(variableName);
                            System.out.println("aa" + fieldDeclaration.getRange());
                            printedVariableNames.add(variableName);
                        }
                    }
                }

                // Create a map to store variable names and their initial values
//                Map<String, String> initialValues = new HashMap<>();
//
//                // Create a visitor to traverse the AST
//                VoidVisitorAdapter<Void> visitor = new VoidVisitorAdapter<>() {
//                    @Override
//                    public void visit(VariableDeclarator variableDeclarator, Void arg) {
//                        // Visit variable declarators
//                        String variableName = variableDeclarator.getNameAsString();
//                        String initialValue = variableDeclarator.getInitializer().map(Object::toString).orElse("");
//                        initialValues.put(variableName, initialValue);
//
//                        super.visit(variableDeclarator, arg);
//                    }
//
//                    @Override
//                    public void visit(AssignExpr assignExpr, Void arg) {
//                        // Visit assignment expressions
//                        if (assignExpr.getTarget() instanceof NameExpr) {
//                            String variableName = ((NameExpr) assignExpr.getTarget()).getNameAsString();
//                            String modifiedValue = assignExpr.getValue().toString();
//
//                            // Compare the initial value with the modified value
//                            String initialValue = initialValues.get(variableName);
//                            if (initialValue != null && !initialValue.equals(modifiedValue)) {
//                                System.out.println("Variable '" + variableName + "' has been modified!");
//                            }
//                        }
//
//                        super.visit(assignExpr, arg);
//                    }
//                };

                // Visit the AST using the visitor
                visitor.visit(compilationUnit, null);

                // Visitor to extract variable names from method bodies
                VoidVisitorAdapter<Void> methodVisitor = new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(VariableDeclarator variableDeclarator, Void arg) {
                        String variableName = variableDeclarator.getNameAsString();
                        if (!printedVariableNames.contains(variableName)) {
//                            System.out.println(variableName);
//                            System.out.println(variableDeclarator.getRange());
                            printedVariableNames.add(variableName);

                            // Insert print statement after the variable declaration

                        }
                        super.visit(variableDeclarator, arg);
                    }
                };

                // Visit all methods in the compilation unit to extract variable names
                methodVisitor.visit(compilationUnit, null);


            } else {
                // Handle parse errors if necessary
                List<Problem> parseErrors = parseResult.getProblems();
                for (Problem error : parseErrors) {
                    System.err.println("Parse error: " + error.getMessage());
                }
            }



//            visitor.visit(compilationUnit, null);

            // Finds when method is declared
            compilationUnit.findAll(MethodDeclaration.class).forEach(methodDeclaration -> {
                // Add a print statement to each method
                methodDeclaration.getBody().ifPresent(body -> {
                    body.addStatement(StaticJavaParser.parseStatement("System.out.println(\"Hello, World!\");"));
                });


            });

            Queue<Integer> assignExprLineNum = new LinkedList<Integer>(); // An ArrayList of line numbers where the variable assignment occurs.
            Queue<String> assignExprVarName = new LinkedList<String>(); // An ArrayList of variable names to be printed.
             //Finds when variable is assigned.
            compilationUnit.findAll(AssignExpr.class).forEach(variableDeclarator -> {
                System.out.println("Apple: " + variableDeclarator);
                System.out.println(variableDeclarator.getRange().get().begin.line);
                assignExprLineNum.add(variableDeclarator.getRange().get().begin.line);
                assignExprVarName.add(variableDeclarator.getTarget().toString());
            });

            // Retrieve the list of lines in the compilation unit
            List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);

            // Locate the desired line number where you want to insert the code
            int targetLineNumber = 2;


//            NodeList<Expression> arguments = new NodeList<>();
//            Expression ex = new NameExpr("1");
//            arguments.add(ex);
//
//            // Create the code snippet to be inserted
//            Statement codeSnippet = new ExpressionStmt(new MethodCallExpr(new NameExpr("System.err"),"println", arguments));
            Statement codeSnippet = new ExpressionStmt(new NameExpr("System.out.println(" + "variable" + ")"));

            // Find the method declaration at the target line
            for (int i = 0; i < methods.size(); i++) {
//                MethodDeclaration targetMethod = methods.get(0); // Example: Get the first method declaration
                MethodDeclaration targetMethod = methods.get(i); // Example: Get the first method declaration
                BlockStmt methodBody = targetMethod.getBody().orElseThrow();

                // Checks whether the print statement that is about to be inserted is within the range of the method or not.
                if (assignExprLineNum.peek() >= targetMethod.getRange().get().begin.line && assignExprLineNum.peek() <= targetMethod.getRange().get().end.line) {
                    int lineNum = targetMethod.getRange().get().begin.line;
                    parseStatements(targetMethod, methodBody, assignExprLineNum, assignExprVarName, codeSnippet);
//                    for (int j = 0; j < methodBody.getStatements().size(); j++) {
//                        int gap = methodBody.getStatement(j).getRange().get().end.line - methodBody.getStatement(j).getRange().get().begin.line + 1;
//                        if (assignExprLineNum.peek() >= methodBody.getStatement(j).getRange().get().begin.line && assignExprLineNum.peek() <= methodBody.getStatement(j).getRange().get().end.line) {
//                            if (methodBody.getStatement(j).isExpressionStmt()) {
//                                methodBody.getStatements().add(j, codeSnippet);
//                            } else if (methodBody.getStatement(j).isTryStmt()) {
//                                TryStmt ts = (TryStmt) methodBody.getStatement(j);
//                                BlockStmt bs = ts.getTryBlock();
//                                // A recursive method?
//                                bs.getStatements().add();
//                            }
//                        }
//                        lineNum += gap;
//                    }
                    // Insert the code snippet at the desired line number
//                    methodBody.getStatements().add(assignExprLineNum.poll() - targetMethod.getRange().get().begin.line, codeSnippet);
                }
            }

            // Print the modified source code
//            System.out.println(compilationUnit.toString());

            // Can handle increment/decrement operations.
            compilationUnit.findAll(UnaryExpr.class).forEach(variableDeclarator -> {
                System.out.println(1);
            });

//            compilationUnit.findAll(BinaryExpr.class).forEach(variableDeclarator -> {
//                System.out.println(1);
//            });

//            // Finds a class named "Main
//            Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("Main");
//            System.out.println(classA);

//            compilationUnit.findAll(FieldDeclaration.class).stream()
////                    .filter(f -> f.isPublic())
//                    .forEach(f -> System.out.println("Check field at line " +
//                            f.getRange().map(r -> r.begin.line).orElse(-1)));

            compilationUnit.findAll(FieldDeclaration.class).stream()
                    .filter(f -> f.isPublic() && f.isStatic())
                    .forEach(f -> System.out.println("Check static field at line " + f.getRange().map(r -> r.begin.line).orElse(-1)));

//            // Creates a source code
//            ClassOrInterfaceDeclaration myClass = compilationUnit
//                    .addClass("MyClass")
//                    .setPublic(true);
//            myClass.addField(int.class, "A_CONSTANT", Modifier.Keyword.PUBLIC, Modifier.Keyword.STATIC);
//            myClass.addField(String.class, "name", Modifier.Keyword.PRIVATE);
//            String code = myClass.toString();
//            System.out.println(code);

            MethodDeclaration toStringMethod = new MethodDeclaration()
                    .setModifiers(Modifier.Keyword.PUBLIC)
                    .setType("String")
                    .setName("toString")
                    .addAnnotation(Override.class);
            BlockStmt body = new BlockStmt()
                    .addStatement("return \"Your custom implementation of toString() here\";");
            toStringMethod.setBody(body);
            compilationUnit.getClassByName("Main")
                    .ifPresent(classDeclaration -> classDeclaration.addMember(toStringMethod));
//            String modifiedCode = compilationUnit.toString();
//            System.out.println(modifiedCode);


// Generate modified code from the modified AST
            String modifiedCode = compilationUnit.toString();
            System.out.println(modifiedCode);

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
    }

    public static void parseStatements(MethodDeclaration targetMethod, BlockStmt methodBody, Queue<Integer> assignExprLineNum, Queue<String> assignExprVarName, Statement codeSnippet) {
        for (int j = 0; j < methodBody.getStatements().size(); j++) {
//            int gap = methodBody.getStatement(j).getRange().get().end.line - methodBody.getStatement(j).getRange().get().begin.line + 1;
            if (assignExprLineNum.size() > 0 && assignExprLineNum.peek() >= methodBody.getStatement(j).getRange().get().begin.line && assignExprLineNum.peek() <= methodBody.getStatement(j).getRange().get().end.line) {
                if (methodBody.getStatement(j).isExpressionStmt()) {
//                    String variableName = methodBody.getStatement(j).getTokenRange().get().getBegin().getText();
                    methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                    assignExprLineNum.poll();
                } else if (methodBody.getStatement(j).isTryStmt()) {
                    TryStmt ts = (TryStmt) methodBody.getStatement(j);
                    BlockStmt bs = ts.getTryBlock();
                    // A recursive method?
                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                } else if (methodBody.getStatement(j).isWhileStmt()) {
                    if (methodBody.getStatement(j).getRange().get().begin.line == assignExprLineNum.peek()) {
                        TokenRange variableName = methodBody.getStatement(j).getTokenRange().get();
                        BlockStmt bs = (BlockStmt) methodBody.getStatement(j).getChildNodes().get(1);
                        bs.getStatements().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                        assignExprLineNum.poll();
//                        methodBody.getStatements().add(j, codeSnippet);
//                        assignExprLineNum.poll();
                    }
                    System.out.println("Hello");
                }
            }
        }
    }


}
