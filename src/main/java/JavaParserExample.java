import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.body.MethodDeclaration;

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

                // Visit all cd in the compilation unit to extract variable names
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

            List<ConstructorDeclaration> c = compilationUnit.findAll(ConstructorDeclaration.class);
            // Retrieve the list of lines in the compilation unit
            List<MethodDeclaration> method = compilationUnit.findAll(MethodDeclaration.class);


            List<CallableDeclaration> cd = new ArrayList<CallableDeclaration>();
            cd.addAll(c);
            cd.addAll(method);

            cd = cdSort(cd);

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
            for (int i = 0; i < cd.size(); i++) {
//                MethodDeclaration targetMethod = cd.get(0); // Example: Get the first method declaration
                CallableDeclaration targetMethod = cd.get(i); // Example: Get the first method declaration
//                BlockStmt methodBody = targetMethod.getBody().orElseThrow();
                BlockStmt methodBody = null;
                if (cd.get(i).isConstructorDeclaration()) {
                    ConstructorDeclaration targetMethod1 = (ConstructorDeclaration) cd.get(i); // Example: Get the first method declaration
                    if (!targetMethod1.getBody().isEmpty())
                        methodBody = targetMethod1.getBody();
                } else if (cd.get(i).isMethodDeclaration()) {
                    MethodDeclaration targetMethod1 = (MethodDeclaration) cd.get(i); // Example: Get the first method declaration
                    if (!targetMethod1.getBody().isEmpty())
                        methodBody = targetMethod1.getBody().orElseThrow();
                }

                // Checks whether the print statement that is about to be inserted is within the range of the method or not.
                System.out.println(assignExprLineNum.size() > 0 && assignExprLineNum.peek() >= targetMethod.getRange().get().begin.line && assignExprLineNum.peek() <= targetMethod.getRange().get().end.line);
                System.out.println(assignExprLineNum.peek());
                System.out.println(targetMethod.getRange().get().begin.line);
                System.out.println(targetMethod.getRange().get().end.line);
                while (assignExprLineNum.size() > 0 && assignExprLineNum.peek() >= targetMethod.getRange().get().begin.line && assignExprLineNum.peek() <= targetMethod.getRange().get().end.line) {
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
//
//    public static void parseStatements(CallableDeclaration targetMethod, Statement methodBody, Queue<Integer> assignExprLineNum, Queue<String> assignExprVarName, Statement codeSnippet) {
//        System.out.println(methodBody);
//        Node s = methodBody.getChildNodes().get(0);
//        s.getClass().getName().equals("com.github.javaparser.ast.stmt.ExpressionStmt");
//        System.out.println(s);
//        for (int j = 0; j < methodBody.getChildNodes().size(); j++) {
//            if (assignExprLineNum.size() > 0 && !methodBody.getChildNodes().get(j).getRange().isEmpty() && assignExprLineNum.peek() >= methodBody.getChildNodes().get(j).getRange().get().begin.line && assignExprLineNum.peek() <= methodBody.getChildNodes().get(j).getRange().get().end.line) {
//                if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.TryStmt")) {
//                    TryStmt ts = (TryStmt) methodBody.getChildNodes().get(j);
//                    Statement bs = ts.getTryBlock();
//                    // A recursive method?
//                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.WhileStmt")) {
//                    BlockStmt bs = (BlockStmt) methodBody.getChildNodes().get(j).getChildNodes().get(1);
//                    if (methodBody.getChildNodes().get(j).getRange().get().begin.line == assignExprLineNum.peek()) {
//                        bs.getChildNodes().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                        assignExprLineNum.poll();
//                    }
//                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.ForStmt")) {
//                    ForStmt fs = (ForStmt) methodBody.getChildNodes().get(j);
//                    System.out.println("bool:" + fs.getBody().isExpressionStmt());
//                    if (fs.getBody().isExpressionStmt()) { // Handles the case of one line if statement
//                        if (assignExprLineNum.peek() <= fs.getBody().getRange().get().begin.line) {
//                            if (assignExprVarName.peek().contains("[")) { // Checks whether an entire array has to be printed or not.
//                                int index = assignExprVarName.peek().indexOf('[');
//                                // Retrieve the substring until "["
//                                String substring = assignExprVarName.poll().substring(0, index);
//                                methodBody.getChildNodes().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + substring + ": \" + Arrays.toString(" + substring + "))")));
//                            } else {
//                                methodBody.getChildNodes().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                            }
//                            assignExprLineNum.poll();
//                        }
//                    } else {
//                        BlockStmt bs = (BlockStmt) fs.getBody();
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.BlockStmt")) {
//                    parseStatements(targetMethod, (BlockStmt) methodBody.getChildNodes().get(j), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.IfStmt")) {
//                    j = ifStmt(j, assignExprLineNum, assignExprVarName, targetMethod, codeSnippet, methodBody, methodBody.getChildNodes().get(j));
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.LabeledStmt")) {
//                    if (methodBody.getChildNodes().get(j).getChildNodes().get(1) instanceof ForStmt) {
//                        ForStmt fs = (ForStmt) methodBody.getChildNodes().get(j).getChildNodes().get(1);
//                        BlockStmt bs = (BlockStmt) fs.getBody();
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    } else if (methodBody.getChildNodes().get(j).getChildNodes().get(1) instanceof WhileStmt) {
//                        BlockStmt bs = (BlockStmt) methodBody.getChildNodes().get(j).getChildNodes().get(1).getChildNodes().get(1);
//                        if (methodBody.getChildNodes().get(j).getRange().get().begin.line == assignExprLineNum.peek()) {
//                            bs.getChildNodes().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                            assignExprLineNum.poll();
//                        }
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.DoStmt")) {
//                    DoStmt ds = (DoStmt) methodBody.getChildNodes().get(j);
//                    parseStatements(targetMethod, (BlockStmt) ds.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.ForEachStmt")) {
//                    ForEachStmt fes = (ForEachStmt) methodBody.getChildNodes().get(j);
//                    parseStatements(targetMethod, (BlockStmt) fes.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getChildNodes().get(j).getClass().getName().equals("com.github.javaparser.ast.stmt.SwitchStmt")) {
//                    SwitchStmt ss = (SwitchStmt) methodBody.getChildNodes().get(j);
//                    for (int k = 1; k < ss.getChildNodes().size(); k++) {
//                        Statement se = (Statement) ss.getChildNodes().get(k);
////                        BlockStmt bs = new BlockStmt(se.getChildNodes().get(k));
//                        parseStatements(targetMethod, se, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else { // Expression, Assert
//                    methodBody.getChildNodes().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                    assignExprLineNum.poll();
//                }
//            }
//        }
//    }

//    public static void parseStatements(CallableDeclaration targetMethod, Statement methodBody, Queue<Integer> assignExprLineNum, Queue<String> assignExprVarName, Statement codeSnippet) {
//        System.out.println(methodBody);
//        Node s = methodBody.getChildNodes().get(0);
//        System.out.println(s);
//        for (int j = 0; j < methodBody.getStatements().size(); j++) {
//            if (assignExprLineNum.size() > 0 && !methodBody.getStatement(j).getRange().isEmpty() && assignExprLineNum.peek() >= methodBody.getStatement(j).getRange().get().begin.line && assignExprLineNum.peek() <= methodBody.getStatement(j).getRange().get().end.line) {
//                if (methodBody.getStatement(j).isTryStmt()) {
//                    TryStmt ts = (TryStmt) methodBody.getStatement(j);
//                    BlockStmt bs = ts.getTryBlock();
//                    // A recursive method?
//                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getStatement(j).isWhileStmt()) {
//                    BlockStmt bs = (BlockStmt) methodBody.getStatement(j).getChildNodes().get(1);
//                    if (methodBody.getStatement(j).getRange().get().begin.line == assignExprLineNum.peek()) {
//                        bs.getStatements().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                        assignExprLineNum.poll();
//                    }
//                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getStatement(j).isForStmt()) {
//                    ForStmt fs = (ForStmt) methodBody.getStatement(j);
//                    System.out.println("bool:" + fs.getBody().isExpressionStmt());
//                    if (fs.getBody().isExpressionStmt()) { // Handles the case of one line if statement
//                        if (assignExprLineNum.peek() <= fs.getBody().getRange().get().begin.line) {
//                            if (assignExprVarName.peek().contains("[")) { // Checks whether an entire array has to be printed or not.
//                                int index = assignExprVarName.peek().indexOf('[');
//                                // Retrieve the substring until "["
//                                String substring = assignExprVarName.poll().substring(0, index);
//                                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + substring + ": \" + Arrays.toString(" + substring + "))")));
//                            } else {
//                                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                            }
//                            assignExprLineNum.poll();
//                        }
//                    } else {
//                        BlockStmt bs = (BlockStmt) fs.getBody();
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else if (methodBody.getStatement(j).isBlockStmt()) {
//                    parseStatements(targetMethod, (BlockStmt) methodBody.getStatement(j), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getStatement(j).isIfStmt()) {
//                    j = ifStmt(j, assignExprLineNum, assignExprVarName, targetMethod, codeSnippet, methodBody, methodBody.getStatement(j));
//                } else if (methodBody.getStatement(j).isLabeledStmt()) {
//                    if (methodBody.getStatement(j).getChildNodes().get(1) instanceof ForStmt) {
//                        ForStmt fs = (ForStmt) methodBody.getStatement(j).getChildNodes().get(1);
//                        BlockStmt bs = (BlockStmt) fs.getBody();
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    } else if (methodBody.getStatement(j).getChildNodes().get(1) instanceof WhileStmt) {
//                        BlockStmt bs = (BlockStmt) methodBody.getStatement(j).getChildNodes().get(1).getChildNodes().get(1);
//                        if (methodBody.getStatement(j).getRange().get().begin.line == assignExprLineNum.peek()) {
//                            bs.getStatements().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                            assignExprLineNum.poll();
//                        }
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else if (methodBody.getStatement(j).isDoStmt()) {
//                    DoStmt ds = (DoStmt) methodBody.getStatement(j);
//                    parseStatements(targetMethod, (BlockStmt) ds.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getStatement(j).isForEachStmt()) {
//                    ForEachStmt fes = (ForEachStmt) methodBody.getStatement(j);
//                    parseStatements(targetMethod, (BlockStmt) fes.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
//                } else if (methodBody.getStatement(j).isSwitchStmt()) {
//                    SwitchStmt ss = (SwitchStmt) methodBody.getStatement(j);
//                    for (int k = 1; k < ss.getChildNodes().size(); k++) {
//                        SwitchEntry se = (SwitchEntry) ss.getChildNodes().get(k);
//                        BlockStmt bs = new BlockStmt(se.getStatements());
//                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                } else { // Expression, Assert
//                    methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                    assignExprLineNum.poll();
//                }
//            }
//        }
//    }

    public static void parseStatements(CallableDeclaration targetMethod, BlockStmt methodBody, Queue<Integer> assignExprLineNum, Queue<String> assignExprVarName, Statement codeSnippet) {
        for (int j = 0; j < methodBody.getStatements().size(); j++) {
            if (assignExprLineNum.size() != 0 && assignExprLineNum.peek() <= methodBody.getRange().get().begin.line) { // Checks whether the value being printed is in the statement declaration or not/
                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                assignExprLineNum.poll();
            }
//            int gap = methodBody.getStatement(j).getRange().get().end.line - methodBody.getStatement(j).getRange().get().begin.line + 1;
//            System.out.println(methodBody.getStatement(j).getRange().get().begin.line);
            if (assignExprLineNum.size() > 0 && !methodBody.getStatement(j).getRange().isEmpty() && assignExprLineNum.peek() >= methodBody.getStatement(j).getRange().get().begin.line && assignExprLineNum.peek() <= methodBody.getStatement(j).getRange().get().end.line) {
                if (methodBody.getStatement(j).isTryStmt()) {
                    TryStmt ts = (TryStmt) methodBody.getStatement(j);
                    BlockStmt bs = ts.getTryBlock();
                    // A recursive method?
                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                    if (ts.getCatchClauses().size() > 0) { // 1 or more catch block exists
                        NodeList<CatchClause> catchClauses = ts.getCatchClauses();
                        for (CatchClause cc: ts.getCatchClauses()) {
                            BlockStmt bc = cc.getBody();
                            parseStatements(targetMethod, bc, assignExprLineNum, assignExprVarName, codeSnippet);
                        }
                    }
                } else if (methodBody.getStatement(j).isWhileStmt()) {
                    BlockStmt bs = (BlockStmt) methodBody.getStatement(j).getChildNodes().get(1);
                    if (methodBody.getStatement(j).getRange().get().begin.line == assignExprLineNum.peek()) {
                        bs.getStatements().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                        assignExprLineNum.poll();
//                        methodBody.getStatements().add(j, codeSnippet);
//                        assignExprLineNum.poll();
                    }
                    parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                } else if (methodBody.getStatement(j).isForStmt()) {
                    ForStmt fs = (ForStmt) methodBody.getStatement(j);
//                    if (assignExprLineNum.peek() <= fs.getRange().get().begin.line) { // Checks whether the value being printed is in the if statement of not (The value might be in the else statement)
//                        methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                        assignExprLineNum.poll();
//                    }
                    System.out.println("bool:" + fs.getBody().isExpressionStmt());
                    if (fs.getBody().isExpressionStmt()) { // Handles the case of one line if statement
                        if (assignExprLineNum.peek() <= fs.getBody().getRange().get().begin.line) {
                            if (assignExprVarName.peek().contains("[")) { // Checks whether an entire array has to be printed or not.
                                int index = assignExprVarName.peek().indexOf('[');
                                // Retrieve the substring until "["
                                String substring = assignExprVarName.poll().substring(0, index);
                                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + substring + ": \" + Arrays.toString(" + substring + "))")));
                            } else {
                                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                            }
                            assignExprLineNum.poll();
                        }
                    } else {
                        Statement s = fs.getBody();
//                        BlockStmt bs = new BlockStmt(new NodeList<>(s)); // An example of resolving cast exception.
//                        BlockStmt bs = new BlockStmt();
                        NodeList<Statement> statements = new NodeList<>();
                        statements.add(s);
                        BlockStmt bs = new BlockStmt();
//                        bs.addStatement(statements.get(0));
                        // Find the parent node containing the Statement
//                        bs.setParentNode(statements.get(0));
//                        bs.setStatements(statements);
                        System.out.println(s.getChildNodes().get(0));


                        if (fs.isForStmt() || fs.isIfStmt()) {
                            int erf = bs.getStatements().size();
                            if (statements.size() > 0 && statements.get(0).toString().substring(0, 3).equals("{\r\n")) { // getStatement(0).toString().substring(0, 1).equals("{\r\n")
                                BlockStmt bstmt = (BlockStmt) statements.get(0);
                                bs.setStatements(bstmt.getStatements());
                            } else {
                                bs.setStatements(statements);
                            }
                            if (bs.getStatements().size() == 1) { // We modify the range only when bs has one statement contained because in this case, we can guarantee the parent's range is equal to the child's range.
                                bs.getStatement(0).setRange(s.getRange().get());
                            }
                        } else {
                            for (Node node : s.getChildNodes()) {
//                                if (s.isForStmt()) {
//                                    ForStmt forstmt = (ForStmt) s;
//                                    String i = forstmt.getInitialization().get(0).toString();
//                                    String c = forstmt.getCompare().get().toString();
//                                    String u = forstmt.getUpdate().get(0).toString();
//                                    String combined = "for (" + i + "; " + c + "; " + u + ")";
//                                    bs.addStatement(combined + forstmt.getBody());
//                                    System.out.println();
//                                }
                                String str = node.toString();
                                bs.addStatement(str);
                                System.out.println(1);
                            }
                        }
                        bs.setRange(s.getRange().get());
//                        Node n = s.getChildNodes().get(0);
                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
//                        bs.addStatement(s.getChildNodes().indexOf(0));
                        fs.setBody(bs);
                    }
                } else if (methodBody.getStatement(j).isBlockStmt()) {
                    if (methodBody.getStatement(j).getRange().get().begin.line <= assignExprLineNum.peek()) { // Statement inside the loop declaration
                        methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                        assignExprLineNum.poll();
                    } else
                        if (methodBody.getStatement(j).getClass().getName().equals("com.github.javaparser.ast.stmt.BlockStmt")) {
                        parseStatements(targetMethod, (BlockStmt) methodBody.getStatement(j), assignExprLineNum, assignExprVarName, codeSnippet);
                    }
                } else if (methodBody.getStatement(j).isIfStmt()) {
                    j = ifStmt(j, assignExprLineNum, assignExprVarName, targetMethod, codeSnippet, methodBody, methodBody.getStatement(j));
//                    IfStmt is = (IfStmt) methodBody.getStatement(j);
//
//                    System.out.println(is.getThenStmt().getClass().getName().equals("com.github.javaparser.ast.stmt.ExpressionStmt"));
//                    if (is.getThenStmt().getClass().getName().equals("com.github.javaparser.ast.stmt.ExpressionStmt")) { // Handles the case of one line if statement
//                        if (assignExprLineNum.peek() <= is.getThenStmt().getRange().get().begin.line) { // Checks whether the value being printed is in the if statement of not (The value might be in the else statement)
//                            methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
//                            assignExprLineNum.poll();
//                        }
//                    } else {
//                        parseStatements(targetMethod, (BlockStmt) is.getThenStmt(), assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
//                    boolean bo = is.hasElseBranch();
//                    if (is.hasElseBranch()) {
//                        Statement s = is.getElseStmt().get();
//                        if (s.isIfStmt()) {
//                            // Recursion of if.
//                        }
//                        parseStatements(targetMethod, (BlockStmt) is.getElseStmt().get(), assignExprLineNum, assignExprVarName, codeSnippet);
//                    }
                } else if (methodBody.getStatement(j).isLabeledStmt()) {
                    if (methodBody.getStatement(j).getChildNodes().get(1) instanceof ForStmt) {
                        ForStmt fs = (ForStmt) methodBody.getStatement(j).getChildNodes().get(1);
                        BlockStmt bs = (BlockStmt) fs.getBody();
                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                    } else if (methodBody.getStatement(j).getChildNodes().get(1) instanceof WhileStmt) {
                        BlockStmt bs = (BlockStmt) methodBody.getStatement(j).getChildNodes().get(1).getChildNodes().get(1);
                        if (methodBody.getStatement(j).getRange().get().begin.line == assignExprLineNum.peek()) {
                            bs.getStatements().add(0, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                            assignExprLineNum.poll();
                        }
                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                    }
                } else if (methodBody.getStatement(j).isDoStmt()) {
                    DoStmt ds = (DoStmt) methodBody.getStatement(j);
                    parseStatements(targetMethod, (BlockStmt) ds.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
                } else if (methodBody.getStatement(j).isForEachStmt()) {
                    ForEachStmt fes = (ForEachStmt) methodBody.getStatement(j);
                    if (fes.getBody().isIfStmt()) {
                        j = ifStmt(j, assignExprLineNum, assignExprVarName, targetMethod, codeSnippet, methodBody, fes.getBody());
                    } else {
                        parseStatements(targetMethod, (BlockStmt) fes.getBody(), assignExprLineNum, assignExprVarName, codeSnippet);
                    }
                } else if (methodBody.getStatement(j).isSwitchStmt()) {
                    SwitchStmt ss = (SwitchStmt) methodBody.getStatement(j);
                    for (int k = 1; k < ss.getChildNodes().size(); k++) {
                        SwitchEntry se = (SwitchEntry) ss.getChildNodes().get(k);
                        BlockStmt bs = new BlockStmt(se.getStatements());
                        parseStatements(targetMethod, bs, assignExprLineNum, assignExprVarName, codeSnippet);
                    }
                } else { // Expression, Assert
                    methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                    assignExprLineNum.poll();
                }
            }
        }
    }

    public static int ifStmt(int j, Queue<Integer> assignExprLineNum, Queue<String> assignExprVarName, CallableDeclaration targetMethod, Statement codeSnippet, BlockStmt methodBody, Statement s) {
//        Statement s = methodBody.getStatement(j);
        IfStmt is = (IfStmt) s;
        System.out.println(is.getThenStmt().getClass().getName().equals("com.github.javaparser.ast.stmt.ExpressionStmt"));
        if (is.getThenStmt().getClass().getName().equals("com.github.javaparser.ast.stmt.ExpressionStmt")) { // Handles the case of one line if statement
            if (assignExprLineNum.peek() <= is.getThenStmt().getRange().get().begin.line) { // Checks whether the value being printed is in the if statement of not (The value might be in the else statement)
                methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                assignExprLineNum.poll();
            }
        } else {
            parseStatements(targetMethod, (BlockStmt) is.getThenStmt(), assignExprLineNum, assignExprVarName, codeSnippet);
        }
        boolean bo = is.hasElseBranch();
        if (is.hasElseBranch()) {
            s = is.getElseStmt().get();
            if (s.isIfStmt()) {
                // Recursion of if.
                j = ifStmt(j, assignExprLineNum, assignExprVarName, targetMethod, codeSnippet, methodBody, s);
            } else {
                if (is.getElseStmt().get().isExpressionStmt()) { // Handles the case of one line else statement
                    if (assignExprLineNum.peek() <= is.getElseStmt().get().getRange().get().begin.line) {
                        methodBody.getStatements().add(++j, new ExpressionStmt(new NameExpr("System.out.println(" + "\"" + assignExprVarName.peek() + ": \" + " + assignExprVarName.poll() + ")")));
                        assignExprLineNum.poll();
                    }
                } else {
                    parseStatements(targetMethod, (BlockStmt) is.getElseStmt().get(), assignExprLineNum, assignExprVarName, codeSnippet);
                }
            }
        }
        return j;
    }

    public static List<CallableDeclaration> cdSort(List<CallableDeclaration> cd) {
        for (int j = 1; j < cd.size(); j++) {
            CallableDeclaration current = cd.get(j);
            int i = j-1;
                while ((i > -1) && (cd.get(i).getRange().get().begin.line > current.getRange().get().begin.line)) {
                cd.set(i+1, cd.get(i));
                i--;
            }
            cd.set(i+1, current);
        }
        return cd;
    }


}
