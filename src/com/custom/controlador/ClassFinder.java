package com.custom.controlador;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class ClassFinder {
	
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
	
	public static Set<Class<?>> findAllClassesAnnotatedWith(Class<? extends Annotation> annotationType) {
		Scanner scanner = new TypeAnnotationsScanner();
		Reflections reflections = getReflectionsBy(scanner);

		return reflections.getTypesAnnotatedWith(annotationType);
	}
	
	public static Set<Method> findAllMethodsAnnotatedWith(Class<? extends Annotation> annotationType) {
		Scanner scanner = new MethodAnnotationsScanner();
		Reflections reflections = getReflectionsBy(scanner);

		return reflections.getMethodsAnnotatedWith(annotationType);
	}
	
	private static Reflections getReflectionsBy(Scanner scanner) {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forPackage("com.custom"))
				.setScanners(scanner));
		return reflections;
	}
}
