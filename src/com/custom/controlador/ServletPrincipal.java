package com.custom.controlador;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class ServletPrincipal extends HttpServlet{

	Map<String, Method> controllers = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        Set<Method> metodosGet = ClassFinder.findAllMethodsAnnotatedWith(Get.class);
        for (Method method : metodosGet) {
        	controllers.put("/CustomMVC"+ method.getAnnotation(Get.class).url(), method);
		}
        Set<Method> metodosPost = ClassFinder.findAllMethodsAnnotatedWith(Post.class);
        for (Method method : metodosPost) {
        	controllers.put("/CustomMVC"+ method.getAnnotation(Post.class).url(), method);
		}
    }
    
    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    	String requestURI = ((HttpServletRequest)request).getRequestURI().toString();
    	Method method = controllers.get(requestURI);
    	
    	try {
    		Object retornoMetodo;
    		Class<?> classe = method.getDeclaringClass();
    		Object instanciaDaClasse = classe.newInstance();
    		
	    	if ("GET".equals(((HttpServletRequest)request).getMethod())) {
	    		retornoMetodo = method.invoke(instanciaDaClasse);
	    	} else {
	    		retornoMetodo = method.invoke(instanciaDaClasse, request.getParameter("nome"), request.getParameter("email"));
	    	}
	    	
	    	if (retornoMetodo != null) {
	    		RequestDispatcher requestDispatcher = request.getRequestDispatcher(retornoMetodo.toString());
	    		requestDispatcher.forward(request, response);
	    	}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
