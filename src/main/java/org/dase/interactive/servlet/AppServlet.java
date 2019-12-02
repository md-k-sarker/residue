package org.dase.interactive.servlet;
/*
Written by sarker.
Written at 9/5/19.
*/

import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        name = "AppServlet",
        urlPatterns = {"/test"}
)

public class AppServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getRequestURL());
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().println("Response from the server");
        resp.getWriter().println("ip: "+ req.getRemoteAddr());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
