package com.yaksha.assignment;

// Animal class - Base class for demonstrating method overloading
class Animal {
	public void speak() {
		System.out.println("The animal makes a sound.");
	}

	// Overloaded method to handle specific animal sounds
	public void speak(String sound) {
		System.out.println("The animal says: " + sound);
	}

	public void speak(int times) {
		System.out.println("The animal speaks " + times + " times.");
	}
}

// Dog class - Inherits from Animal and demonstrates method overloading
class Dog extends Animal {

	@Override
	public void speak() {
		System.out.println("The dog barks.");
	}

	// Overloaded method to handle specific dog sounds
	@Override
	public void speak(String sound) {
		System.out.println("The dog says: " + sound);
	}

	@Override
	public void speak(int times) {
		System.out.println("The dog barks " + times + " times.");
	}
}

public class PolymorphismMethodOverloadingAssignment {
	public static void main(String[] args) {
		Dog dog = new Dog(); // Creating a Dog object
		dog.speak(); // Should print "The dog barks."
		dog.speak("Woof!"); // Should print "The dog says: Woof!"
		dog.speak(3); // Should print "The dog barks 3 times."
	}
}
