package org.dase.interactive.launcher;
/*
Written by sarker.
Written at 9/5/19.
*/

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyAppServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("Response from the server");
    }
}
