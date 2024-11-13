/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.pensax.controladores;

import com.mycompany.pensax.modelos.Carrera;
import com.mycompany.pensax.modelos.User;
import com.mycompany.pensax.sessions.CarreraFacade;
import com.mycompany.pensax.sessions.UserFacade;
import jakarta.ejb.EJB;
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
@WebServlet(name = "carreras",
        urlPatterns = {
            "/carreras",
            "/carreras/editar",
            "/carreras/create",
            "/carreras/eliminar"
        }
)
public class carreras extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
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
    @EJB
    private CarreraFacade carreraF;
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
            case "/carreras":
                List<Carrera> carreras=carreraF.findAll();
                request.setAttribute("carreras", carreras);
                url="/WEB-INF/carreras/index.jsp";
                break;
            case "/carreras/editar":
                try {
                    Integer carreraid = Integer.valueOf(request.getParameter("id"));
                    Carrera carrera = carreraF.find(carreraid);
                    System.out.println(carrera);
                    if (carrera == null) {
                        request.getSession().setAttribute("error", "No existe la carrera");
                        response.sendRedirect("/Pensax/carreras");
                        return;
                    }
                    request.setAttribute("carrera", carrera);
                    url = "/WEB-INF/carreras/editar.jsp";
                } catch (NumberFormatException numberFormatException) {
                } catch (IOException iOException) {
                }
                break;
            case "/carreras/create":
                url="/WEB-INF/carreras/create.jsp";
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
            case "/carreras/editar":
                Integer carreraid= Integer.valueOf(request.getParameter("id"));
                Carrera carrera=carreraF.find(carreraid);
                if(carrera==null){
                    request.getSession().setAttribute("error", "No existe la carrera");
                    response.sendRedirect("/Pensax/carreras");
                }
                String titulo=request.getParameter("titulo");
                if(titulo==null || titulo.isEmpty()){
                    request.getSession().setAttribute("error", "No existe la carrera");
                    response.sendRedirect("./editar?id=" + carreraid);
                }
                carrera.setTitulo(titulo);
                carreraF.edit(carrera);
                request.getSession().setAttribute("success", "Carrera editada");
                response.sendRedirect("/Pensax/carreras");
                break;
            case "/carreras/create":
                titulo=request.getParameter("titulo");
                if(titulo==null || titulo.isEmpty()){
                    request.getSession().setAttribute("error", "No existe la carrera");
                    response.sendRedirect("./create");
                }
                carrera=new Carrera();
                carrera.setTitulo(titulo);
                carreraF.create(carrera);
                request.getSession().setAttribute("success", "Carrera creada");
                response.sendRedirect("/Pensax/carreras");
                break;
            case "/carreras/eliminar":
                carreraid= Integer.valueOf(request.getParameter("id"));
                carrera=carreraF.find(carreraid);
                if(carrera==null){
                    request.getSession().setAttribute("error", "No existe la carrera a eliminar");
                    response.sendRedirect("/Pensax/carreras");
                }
                carreraF.remove(carrera);
                request.getSession().setAttribute("success", "Carrera eliminada");
                response.sendRedirect("/Pensax/carreras");
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
