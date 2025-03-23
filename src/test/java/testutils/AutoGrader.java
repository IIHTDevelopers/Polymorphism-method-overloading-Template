package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class AutoGrader {

	// Test if the code implements polymorphism method overloading correctly
	public boolean testPolymorphismMethodOverloading(String filePath) throws IOException {
		System.out.println("Starting testPolymorphismMethodOverloading with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Use AtomicBoolean to allow modifications inside lambda expressions
		AtomicBoolean animalClassFound = new AtomicBoolean(false);
		AtomicBoolean dogClassFound = new AtomicBoolean(false);
		AtomicBoolean speakMethodFound = new AtomicBoolean(false);
		AtomicBoolean speakMethodOverloaded = new AtomicBoolean(false);

		// Check for class implementation (Dog class and its methods)
		System.out.println("------ Class and Method Check ------");
		for (TypeDeclaration<?> typeDecl : cu.findAll(TypeDeclaration.class)) {
			if (typeDecl instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;

				if (classDecl.getNameAsString().equals("Animal")) {
					System.out.println("Class 'Animal' found.");
					animalClassFound.set(true);
				}

				if (classDecl.getNameAsString().equals("Dog")) {
					System.out.println("Class 'Dog' found.");
					dogClassFound.set(true);
				}
			}
		}

		// Ensure Animal and Dog classes exist
		if (!animalClassFound.get() || !dogClassFound.get()) {
			System.out.println("Error: Class 'Animal' or 'Dog' not found.");
			return false;
		}

		// Check if method overloading is implemented correctly
		System.out.println("------ Method Overloading Check ------");
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals("speak")) {
				speakMethodFound.set(true);
				if (method.getParameters().size() > 0) {
					speakMethodOverloaded.set(true);
					System.out.println("Method 'speak' is overloaded.");
				}
				System.out.println("Method 'speak' found in the class.");
			}
		}

		if (!speakMethodFound.get()) {
			System.out.println("Error: 'speak' method not found.");
			return false;
		}

		if (!speakMethodOverloaded.get()) {
			System.out.println("Error: 'speak' method is not overloaded correctly.");
			return false;
		}

		// Check if methods are executed in main
		System.out.println("------ Method Execution Check in Main ------");
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals("main")) {
				if (method.getBody().isPresent()) {
					method.getBody().get().findAll(MethodCallExpr.class).forEach(callExpr -> {
						if (callExpr.getNameAsString().equals("speak")) {
							System.out.println("Method 'speak' is executed in main method.");
						}
					});
				}
			}
		}

		// If method overloading is implemented and executed in main
		System.out.println("Test passed: Polymorphism method overloading is correctly implemented.");
		return true;
	}
}
