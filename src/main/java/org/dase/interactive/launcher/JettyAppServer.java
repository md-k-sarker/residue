package org.dase.interactive.launcher;
/*
Written by sarker.
Written at 9/5/19.
*/

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyAppServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(7071);
        ServletContextHandler handler = new ServletContextHandler(server, "/example");
        handler.addServlet(JettyAppServlet.class, "/");
        server.start();

    }

}
