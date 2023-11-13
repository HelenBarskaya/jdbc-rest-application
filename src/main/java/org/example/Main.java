package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.example.servlet.MainServlet;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(8081);

        Context context = tomcat.addContext("", null);
        Wrapper servletWrapper = Tomcat.addServlet(context, "MainServlet", new MainServlet());
        servletWrapper.addMapping("/");
        tomcat.start();
    }
}