/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.pensax.controladores;

import java.sql.Timestamp;
import java.util.UUID;
import com.mycompany.pensax.modelos.Session;
import com.mycompany.pensax.modelos.User;
import com.mycompany.pensax.sessions.SessionFacade;
import com.mycompany.pensax.sessions.UserFacade;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author users
 */
@WebServlet(
        name = "auth", 
        loadOnStartup = 1,
        urlPatterns={"/login",
                    "/registrar",
                    "/logout"
        }
)
public class auth extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private UserFacade userF;
    @EJB
    private SessionFacade sesF;
    @PersistenceUnit 
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            String pathUsuario = request.getServletPath();
            System.out.println("path = "+ pathUsuario);	    
            String url = null;
            EntityManager em = null;
            switch(pathUsuario){
                case "/login":
                    url="/WEB-INF/auth/login.jsp";
                    break;
                case "/registrar":
                    url="/WEB-INF/auth/register.jsp";
                    break;
                case "/logout":
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        String sessionId = session.getId();
                        session.invalidate();
                        sesF.remove(sesF.find(sessionId));
                    }

                    url="/WEB-INF/index.jsp";
                    break;
            }
            try {
                request.getRequestDispatcher(url).forward(request, response);
            } catch (ServletException | IOException ex) {
            }
        }catch(SecurityException | IllegalStateException  ex) {
            Logger.getLogger(auth.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param password
     * @return 
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            System.out.println(password);
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathUsuario = request.getServletPath();
        EntityManager em = null;
        System.out.println(pathUsuario);
        String url=null;
        try {
            switch(pathUsuario){
                case "/login":
                    String email = request.getParameter("email");
                    System.out.println(email);
                    String password =request.getParameter("password");
                    System.out.println(password);
                    password = hashPassword(password);
                    User user = userF.findlogin(email, password);
                    if (user != null) {
                        int userId = user.getIdusers();

                        HttpSession session = request.getSession();
                        session.setAttribute("userId", userId);
                        session.setAttribute("user", user);

                        session.setAttribute("rol", user.getRol());

                        Session s = new Session();
                        String sessionToken = UUID.randomUUID().toString();
                        Date expirationTime = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // 30 minutes

                        s.setUserIdusers(user);
                        s.setIdsession(session.getId());
                        s.setSessionToken(sessionToken);
                        s.setExperationTime(expirationTime);

                        utx.begin();
                        em = emf.createEntityManager();
                        em.persist(s);
                        utx.commit();

                        session.setAttribute("sessionToken", sessionToken);
                        session.setAttribute("userFullName", user.getApellido().toUpperCase() +", " + user.getNombre());
                        response.sendRedirect("./peticiones");
                    } else {
                        request.setAttribute("error","invalid_credentials");
                        url="/WEB-INF/auth/login.jsp";
                        try {
                            request.getRequestDispatcher(url).forward(request, response);
                        } catch (ServletException | IOException ex) {
                        }
                    }
                    break;
                case "/registrar":
                    email = request.getParameter("email");
                    password = hashPassword(request.getParameter("password"));
                    String c_password = hashPassword(request.getParameter("c_password"));
                    String nombre = request.getParameter("nombre");
                    String apellido= request.getParameter("apellido");
                    String dni = request.getParameter("dni");
                    // Check if user with email already exists
                    if (userF.findByEmail(email) != null) {
                        response.sendRedirect("./registrar?error=email_exists");
                        return;
                    }else if(email.trim().isEmpty() || password.trim().isEmpty() || nombre.trim().isEmpty() || apellido.trim().isEmpty() || dni.trim().isEmpty() || !c_password.equals(password)){
                        response.sendRedirect("./registrar?error=void_fields");
                        return;
                    }

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    newUser.setNombre(nombre);
                    newUser.setApellido(apellido);
                    newUser.setDni(dni);
                    newUser.setCreatedAt(new Date());
                    newUser.setUpdatedAt(new Date());
                    utx.begin();
                    em = emf.createEntityManager();
                    em.persist(newUser);
                    utx.commit();
                    
                    HttpSession session = request.getSession();
                    session.setAttribute("user", newUser);

                    Session s = new Session();
                    String sessionToken = UUID.randomUUID().toString();
                    Date expirationTime = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // 30 minutos

                    s.setUserIdusers(newUser);
                    s.setIdsession(session.getId());
                    s.setSessionToken(sessionToken);
                    s.setExperationTime(expirationTime);

                    // Persistir la sesión en la base de datos
                    utx.begin();
                    em = emf.createEntityManager();
                    em.persist(s);
                    utx.commit();

                    // Establecer token en la sesión de HTTP
                    session.setAttribute("sessionToken", sessionToken);
                    session.setAttribute("userFullName", newUser.getApellido().toUpperCase() +", " + newUser.getNombre());
                    response.sendRedirect("./peticiones");
                    break;

            }
            
            
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | SecurityException e) {
            try {
                if (utx != null && utx.getStatus() == jakarta.transaction.Status.STATUS_ACTIVE) {
                    utx.rollback();
                }
            } catch (SystemException | IllegalStateException | SecurityException rollbackEx) {
                Logger.getLogger(auth.class.getName()).log(Level.SEVERE, "Rollback failed", rollbackEx);
            }
            throw new ServletException("Error during authentication", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
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
