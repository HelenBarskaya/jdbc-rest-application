package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.example.servlet.ClientServlet;
import org.example.servlet.CoachServlet;
import org.example.servlet.GroupServlet;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setPort(8081);

        Context context = tomcat.addContext("", null);

        Wrapper clientServlet = Tomcat.addServlet(context, "ClientServlet", new ClientServlet());
        clientServlet.addMapping("/client");

        Wrapper groupServlet = Tomcat.addServlet(context, "GroupServlet", new GroupServlet());
        groupServlet.addMapping("/group");

        Wrapper coachServlet = Tomcat.addServlet(context, "CoachServlet", new CoachServlet());
        coachServlet.addMapping("/coach");

        tomcat.start();
    }
}