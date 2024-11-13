/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.pensax.controladores;

import com.mycompany.pensax.modelos.User;
import com.mycompany.pensax.sessions.UserFacade;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author users
 */
@WebServlet(name = "usuarios",
        urlPatterns = {
            "/usuarios",
            "/usuarios/verificar"
        }
)
public class usuarios extends HttpServlet {

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private UserFacade userF;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathUsuario = request.getServletPath();
        System.out.println(pathUsuario);
        User user= (User) request.getSession().getAttribute("user");
        if(user==null || !user.getRol().equals("admin")){
            response.sendRedirect("/Pensax/");
            return;
        }
        String url=null;
        switch(pathUsuario){
            case "/usuarios":
                List<User> usuarios=userF.getUsuarios();
                request.setAttribute("usuarios", usuarios);
                url="/WEB-INF/usuarios/index.jsp";
                break;
        }
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException ex) {
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathUsuario = request.getServletPath();
        System.out.println(pathUsuario);
        User user= (User) request.getSession().getAttribute("user");
        if(user==null || !user.getRol().equals("admin")){
            response.sendRedirect("/Pensax/");
            return;
        }
        switch(pathUsuario){
            case "/usuarios/verificar":
                Integer userid= Integer.valueOf(request.getParameter("id"));
                User user1=userF.find(userid);
                if(user1==null){
                    request.getSession().setAttribute("error", "No existe el usuario");
                    response.sendRedirect("/Pensax/usuarios");
                }
                user1.setRol("redactor");
                userF.edit(user1);
                request.getSession().setAttribute("success", "Usuario Verificado");
                response.sendRedirect("/Pensax/usuarios");
                break;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
