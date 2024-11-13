/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.pensax.controladores;

import com.mycompany.pensax.modelos.Carrera;
import com.mycompany.pensax.modelos.Peticion;
import com.mycompany.pensax.modelos.User;
import com.mycompany.pensax.modelos.Voto;
import com.mycompany.pensax.modelos.VotoPK;
import com.mycompany.pensax.sessions.CarreraFacade;
import com.mycompany.pensax.sessions.PeticionFacade;
import com.mycompany.pensax.sessions.UserFacade;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author users
 */
@WebServlet(
        name = "peticiones", 
        loadOnStartup = 1,
        urlPatterns={
            "/peticiones",
            "/peticiones/editar",
            "/peticiones/votar",
            "/peticiones/create",
            "/peticiones/mis",
            "/peticiones/show",
            "/peticiones/pendientes",
            "/peticiones/eliminar",
            "/peticiones/publicar",
            "/peticiones/aprobar",
            "/peticiones/rechazar",
            "/peticiones/vencidas"
        }
        )
@MultipartConfig
public class peticiones extends HttpServlet {

    @EJB
    private PeticionFacade petF;
    @EJB
    private UserFacade userF;
    @EJB
    private CarreraFacade carreraF;
    @PersistenceUnit 
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathUsuario = request.getServletPath();
        EntityManager em = null;
        String url= null;
        System.out.println(pathUsuario);
        User user= (User) request.getSession().getAttribute("user");
        if(user==null){
            response.sendRedirect("/Pensax/");
            return;
        }
        try{
            switch(pathUsuario){
                case"/peticiones":
                    String carreraId = request.getParameter("carrera_id");

                    List<Peticion> peticiones = petF.getPeticionesPulicadas(carreraId);

                    List<Carrera> carreras = carreraF.findAll();
                    for(Peticion p : peticiones){
                        String imagePath = p.getImagen();

                        // Replace backslashes with forward slashes
                        if (imagePath != null) {
                            imagePath = imagePath.replace("\\", "/");
                            imagePath = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                        }
                        p.setImagen(imagePath);
                    }

                    request.setAttribute("peticiones", peticiones);
                    request.setAttribute("carreras", carreras);
                    url="/WEB-INF/peticiones/index.jsp";
                    break;
                case"/peticiones/votar":
                    Integer peticionId= Integer.valueOf(request.getParameter("id")) ;
                    Peticion p= petF.find(peticionId);
                    if((p.getRechazada()==1) || (p.getPublicada()==0)){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }else{
                        User user1= (User) request.getSession().getAttribute("user");
                        Carrera c= p.getCarreraidCarrera();
                        Voto uservote = p.getVotoCollection()
                                .stream()
                                .filter(voto -> voto.getUser().getIdusers().equals(user1.getIdusers()))
                                .findFirst()
                                .orElse(null);
                        System.out.println(uservote);
                        boolean uv;
                        uv = uservote != null;
                        String imagePath = p.getImagen();

                        // Replace backslashes with forward slashes
                        if (imagePath != null) {
                            imagePath = imagePath.replace("\\", "/");
                            imagePath = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                        }

                        // Set the modified image path back to the peticion object if needed or pass it as an attribute
                        p.setImagen(imagePath);
                        request.setAttribute("peticion", p);
                        request.setAttribute("usuario", user);
                        request.setAttribute("carrera", c);
                        request.setAttribute("uservote", uv);
                        url="/WEB-INF/peticiones/votar.jsp";
                    }
                    break;
                case "/peticiones/create":
                    if(!user.getRol().equals("redactor")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    carreras = carreraF.findAll();
                    request.setAttribute("carreras", carreras);
                    url="/WEB-INF/peticiones/create.jsp";
                    break;
                case "/peticiones/mis":
                    user = (User) request.getSession().getAttribute("user");
                    if(!user.getRol().equals("redactor")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    System.out.println(user);
                    
                    String filter = request.getParameter("filter");
                    if(filter==null){
                        filter="";
                    }
                    peticiones = petF.getPeticionesPorUsuario(user, filter);

                    request.setAttribute("peticiones", peticiones);
                    url="/WEB-INF/peticiones/mispeticiones.jsp";
                    break;
                case "/peticiones/editar":
                    peticionId= Integer.valueOf(request.getParameter("id")) ;
                    p= petF.find(peticionId);
                    user = (User) request.getSession().getAttribute("user");
                    if(!user.getRol().equals("redactor")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    System.out.println(p);
                    System.out.println(user);
                    System.out.println(new Date().toString());
                    
                    if(p==null || p.getPublicada()==1 || p.getVencimiento().before(new Date()) || !p.getUserIdusers().equals(user)){
                        request.setAttribute("successMessage", "No puede editar esta peticion");
                        url="/WEB-INF/peticiones/mispeticiones.jsp";
                    }else{
                        carreras= carreraF.findAll();
                        request.setAttribute("carreras",carreras);
                        request.setAttribute("peticion", p);
                        url="/WEB-INF/peticiones/editar.jsp";
                    }
                    break;
                case "/peticiones/show":
                    peticionId = Integer.valueOf(request.getParameter("id"));
                    user = (User) request.getSession().getAttribute("user");
                    
                    if (user.getRol()==null ||(!user.getRol().equals("admin")&& !user.getRol().equals("redactor"))) {
                        request.getSession().setAttribute("error", "Vista no autorizada");
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }

                    Peticion peticion = petF.find(peticionId); 
                    if (peticion == null) {
                        request.getSession().setAttribute("error", "Petición no encontrada");
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }

                    if (!user.getRol().equals("admin") && !peticion.getUserIdusers().equals(user)) {
                        request.getSession().setAttribute("error", "Vista no autorizada");
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }

                    String usuario = peticion.getUserIdusers().getApellido()+ peticion.getUserIdusers().getNombre();

                    String carrera;
                    if (peticion.getCarreraidCarrera() != null) {
                        Carrera carreraEntity = carreraF.find(peticion.getCarreraidCarrera().getIdCarrera());
                        carrera = carreraEntity != null ? carreraEntity.getTitulo() : "Sin carrera especificada";
                    } else {
                        carrera = "Sin carrera especificada";
                    }
                    
                    String imagePath = peticion.getImagen();

                    // Replace backslashes with forward slashes
                    if (imagePath != null) {
                        imagePath = imagePath.replace("\\", "/");
                        imagePath = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                    }

                    // Set the modified image path back to the peticion object if needed or pass it as an attribute
                    peticion.setImagen(imagePath);

                    request.setAttribute("peticion", peticion);
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("carrera", carrera);
                    url="/WEB-INF/peticiones/show.jsp";
                    break;
                case "/peticiones/pendientes":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    peticiones=petF.getPendientes();
                    request.setAttribute("peticiones", peticiones);
                    url="/WEB-INF/peticiones/pendientes.jsp";
                    break;
                case "/peticiones/aprobar":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    peticionId = Integer.valueOf(request.getParameter("id"));
                    peticion=petF.find(peticionId);
                    if(peticion==null || peticion.getRechazada()==1 || peticion.getPublicada()==1 || peticion.getVencimiento().before(new Date())){
                        request.getSession().setAttribute("error", "No se puede aprobar la petición");
                        response.sendRedirect("./pendientes");
                        return;
                    }
                    user= peticion.getUserIdusers();
                    String nombre= user.getApellido() +" " + user.getNombre();
                    String carreraTitulo;
                    if (peticion.getCarreraidCarrera()!= null) {
                        carreraTitulo = peticion.getCarreraidCarrera().getTitulo();
                    } else {
                        carreraTitulo = "Sin carrera especificada";
                    }
                    imagePath = peticion.getImagen();

                    // Replace backslashes with forward slashes
                    if (imagePath != null) {
                        imagePath = imagePath.replace("\\", "/");
                        imagePath = imagePath.substring(imagePath.lastIndexOf('/') + 1);
                    }

                    // Set the modified image path back to the peticion object if needed or pass it as an attribute
                    peticion.setImagen(imagePath);
                    // Set attributes for JSP
                    request.setAttribute("peticion", peticion);
                    request.setAttribute("usuario", nombre);
                    request.setAttribute("carrera", carreraTitulo);
                    url="/WEB-INF/peticiones/aprobar.jsp";
                    break;
                case "/peticiones/vencidas":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    peticiones= petF.getVencidas();
                    request.setAttribute("peticiones", peticiones);
                    url="/WEB-INF/peticiones/vencidas.jsp";
                    break;
                
                
            }
            try {
                request.getRequestDispatcher(url).forward(request, response);
            } catch (ServletException | IOException ex) {
            }
        }catch(IOException | NumberFormatException e){
            
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
        EntityManager em = null;
        System.out.println(pathUsuario);
        User user= (User) request.getSession().getAttribute("user");
        if(user==null){
            response.sendRedirect("/Pensax/");
            return;
        }
        try{
            switch(pathUsuario){
                case "/peticiones/votar":
                    System.out.println("entra en el post");
                    try {
                        Integer peticionId = Integer.valueOf(request.getParameter("id"));
                        Short voto = Short.valueOf(request.getParameter("voto"));
                        Short anonimo;
                        anonimo = Short.valueOf(request.getParameter("anonimo")==null?"0":"1");
                        //System.out.println(voto);
                        //System.out.println(anonimo);
                        User user1 = (User) request.getSession().getAttribute("user");
                        //System.out.println(user);
                        utx.begin();
                        em= emf.createEntityManager();
                        Peticion peticion = petF.find(peticionId);
                        if (peticion == null || peticion.getRechazada()==1 || peticion.getPublicada()==0) {
                            response.sendRedirect("./peticiones?error=La petición no puede ser votada.");
                            return;
                        }
                        //System.out.println(peticion);

                        // el usuario ya votó?
                        Voto userVote = peticion.getVotoCollection().stream()
                                                 .filter(v -> v.getUser().getIdusers().equals(user1.getIdusers()))
                                                 .findFirst()
                                                 .orElse(null);

                        if (userVote != null) {
                            response.sendRedirect("/Pensax/peticiones?error=Ya has votado esta petición.");
                            return;
                        }

                        Voto nuevoVoto = new Voto();
                        VotoPK nuevoVotoPK= new VotoPK();
                        nuevoVotoPK.setPeticionIdpeticion(peticionId);
                        nuevoVotoPK.setUserIdusers(user.getIdusers());
                        nuevoVoto.setPeticion(peticion);
                        nuevoVoto.setUser(user); 
                        nuevoVoto.setVoto(voto);
                        nuevoVoto.setAnonimo(anonimo);
                        nuevoVoto.setVotoPK(nuevoVotoPK);
                        //System.out.println(nuevoVoto);
                        em.persist(nuevoVoto);

                        if (voto==1) {
                            peticion.setPositivos(peticion.getPositivos()+ 1);
                        } else {
                            peticion.setNegativos(peticion.getNegativos() + 1);
                        }

                        petF.edit(peticion);

                        utx.commit();
                        response.sendRedirect("Pensax/peticiones/votar?id=" + peticionId);

                    } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | NumberFormatException | SecurityException e) {
                        try {
                            // Revertir la transacción en caso de error
                            utx.rollback();
                        } catch (SystemException | IllegalStateException | SecurityException rollbackException) {
                            rollbackException.printStackTrace();
                        }
                        e.printStackTrace();
                        response.sendRedirect("./peticiones?error=Error al registrar el voto.");
                    }
                    break;
                case "/peticiones/create":
                    try {
                        System.out.println("Creando petición");

                        String titulo = request.getParameter("titulo");
                        String descripcion = request.getParameter("descripcion");
                        String vencimientoStr = request.getParameter("vencimiento");
                        String carreraId = request.getParameter("carrera_id");
                        String imagenUrl = request.getParameter("imagen_url");
                        //System.out.println(titulo);
                        if (!isValidTitulo(titulo) || !isValidDescripcion(descripcion) || !isValidVencimiento(vencimientoStr)) {
                            request.setAttribute("error", "Los datos no son válidos.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/create.jsp").forward(request, response);
                            return;
                        }

                        Date vencimiento = java.sql.Date.valueOf(vencimientoStr);

                        user = (User) request.getSession().getAttribute("user");
                        if(!user.getRol().equals("redactor")){
                            response.sendRedirect("/Pensax/peticiones");
                            return;
                        }

                        Peticion peticion = new Peticion();
                        peticion.setTitulo(titulo);
                        peticion.setDescripcion(descripcion);
                        peticion.setVencimiento(vencimiento);
                        peticion.setCarreraidCarrera(null != carreraId && !carreraId.trim().isEmpty() ? carreraF.find(Integer.valueOf(carreraId)) : null);
                        peticion.setPositivos(0);
                        peticion.setNegativos(0);
                        peticion.setUserIdusers(user);
                        peticion.setComentario("");
                        peticion.setPublicada(Short.valueOf("0"));
                        peticion.setRechazada(Short.valueOf("0"));
                        peticion.setCreatedAt(new Date());
                        peticion.setDeleted(Short.valueOf("0"));
                        peticion.setUpdatedAt(new Date());

                        Part imagen = null;
                        if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
                            imagen = request.getPart("imagen");
                        }
                        if (imagen != null && imagen.getSize() > 0 && isImageFile(imagen)) {
                            String filePath = saveImageFile(imagen);
                            peticion.setImagen(filePath);
                        }else if (isValidUrl(imagenUrl)) {
                            peticion.setImagen(imagenUrl);
                        } else {
                            peticion.setImagen("");
                        }

                        utx.begin();
                        em = emf.createEntityManager();
                        em.persist(peticion);
                        utx.commit();

                        request.setAttribute("successMessage", "Petición creada correctamente!");
                        response.sendRedirect("./mis");

                    } catch (IOException e) {
                        System.err.println("Error de IO: " + e.getMessage());
                        request.setAttribute("error", "Ocurrió un error al procesar la solicitud.");
                        request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                    } catch (ServletException e) {
                        System.err.println("Error en el servlet: " + e.getMessage());
                        request.setAttribute("error", "Ocurrió un error al procesar la solicitud.");
                        request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                    } catch (NumberFormatException e) {
                        System.err.println("Error en el formato de número: " + e.getMessage());
                        request.setAttribute("error", "Número inválido en la solicitud.");
                        request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                    } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException e) {
                        System.err.println("Error general: " + e.getMessage());
                        request.setAttribute("error", "Ocurrió un error inesperado.");
                        request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);
                    }
                    break;
                case "/peticiones/editar":
                    try {
                        if(!user.getRol().equals("redactor")){
                            response.sendRedirect("/Pensax/peticiones");
                            return;
                        }
                        System.out.println("Editando petición");

                        String peticionId = request.getParameter("id");
                        if (peticionId == null || peticionId.trim().isEmpty()) {
                            request.setAttribute("error", "ID de la petición no proporcionado.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/mispeticiones.jsp").forward(request, response);
                            return;
                        }

                        Peticion peticion = petF.find(Integer.valueOf(peticionId));
                        if (peticion == null) {
                            request.setAttribute("successMessage", "La petición no existe.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/mispeticiones.jsp").forward(request, response);
                            return;
                        }

                        String titulo = request.getParameter("titulo");
                        String descripcion = request.getParameter("descripcion");
                        String vencimientoStr = request.getParameter("vencimiento");
                        String carreraId = request.getParameter("carrera_id");
                        String imagenUrl = request.getParameter("imagen_url");

                        if (!isValidTitulo(titulo) || !isValidDescripcion(descripcion) || !isValidVencimiento(vencimientoStr)) {
                            request.setAttribute("error", "Los datos no son válidos.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/edit.jsp").forward(request, response);
                            return;
                        }

                        Date vencimiento = java.sql.Date.valueOf(vencimientoStr);

                        peticion.setTitulo(titulo);
                        peticion.setDescripcion(descripcion);
                        peticion.setVencimiento(vencimiento);
                        if(null != carreraId && !carreraId.trim().isEmpty()) peticion.setCarreraidCarrera(carreraF.find(Integer.valueOf(carreraId)) );
                        peticion.setUpdatedAt(new Date());
                        
                        String oldPath = peticion.getImagen();

                        if (oldPath != null && !oldPath.isEmpty()) {
                            File oldFile = new File(oldPath);
                            if (oldFile.exists()) {
                                if (oldFile.delete()) {
                                    System.out.println("Old image deleted successfully.");
                                } else {
                                    System.err.println("Failed to delete the old image.");
                                }
                            }
                        }
                        Part imagen = null;
                        if (request.getContentType() != null && request.getContentType().startsWith("multipart/")) {
                            imagen = request.getPart("imagen");
                        }
                        if (imagen != null && imagen.getSize() > 0 && isImageFile(imagen)) {
                            String filePath = saveImageFile(imagen);
                            peticion.setImagen(filePath);
                        } else if (isValidUrl(imagenUrl)) {
                            peticion.setImagen(imagenUrl);
                        }
                        
                        if(peticion.getRechazada()==1){
                            peticion.setRechazada(Short.valueOf("0"));
                            peticion.setComentario("");
                        }

                        petF.edit(peticion);

                        request.setAttribute("success", "Petición actualizada correctamente!");
                        response.sendRedirect("./mis");

                        } catch (IOException e) {
                            System.err.println("Error de IO: " + e.getMessage());
                            request.setAttribute("error", "Ocurrió un error al procesar la solicitud.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                        } catch (ServletException e) {
                            System.err.println("Error en el servlet: " + e.getMessage());
                            request.setAttribute("error", "Ocurrió un error al procesar la solicitud.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                        } catch (NumberFormatException e) {
                            System.err.println("Error en el formato de número: " + e.getMessage());
                            request.setAttribute("error", "Número inválido en la solicitud.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);

                        } catch (IllegalStateException | SecurityException e) {
                            System.err.println("Error general: " + e.getMessage());
                            request.setAttribute("error", "Ocurrió un error inesperado.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);
                        }
                case "/peticiones/publicar":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    Integer peticionId = Integer.valueOf(request.getParameter("id"));
                    Peticion peticion=petF.find(peticionId);
                    if(peticion==null || peticion.getRechazada()==1 || peticion.getPublicada()==1 || peticion.getVencimiento().before(new Date())){
                        request.getSession().setAttribute("error", "No se puede publicar la petición");
                        response.sendRedirect("./pendientes");
                        return;
                    }
                    peticion.setPublicada(Short.valueOf("1"));
                    petF.edit(peticion);
                    request.getSession().setAttribute("success", "Petición Publicada");
                    response.sendRedirect("./pendientes");
                    break;
                case "/peticiones/eliminar":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    peticionId = Integer.valueOf(request.getParameter("id"));
                    peticion=petF.find(peticionId);
                    if(peticion==null){
                        request.getSession().setAttribute("error", "No existe la peticion");
                        response.sendRedirect("./pendientes");
                        return;
                    }
                    String oldPath = peticion.getImagen();

                    if (oldPath != null && !oldPath.isEmpty()) {
                        File oldFile = new File(oldPath);
                        if (oldFile.exists()) {
                            if (oldFile.delete()) {
                                System.out.println("Old image deleted successfully.");
                            } else {
                                System.err.println("Failed to delete the old image.");
                            }
                        }
                    }
                    petF.remove(peticion);
                    request.getSession().setAttribute("success", "Petición eliminada");
                    response.sendRedirect("./pendientes");
                    break;
                case "/peticiones/rechazar":
                    if(!user.getRol().equals("admin")){
                        response.sendRedirect("/Pensax/peticiones");
                        return;
                    }
                    peticionId = Integer.valueOf(request.getParameter("id"));
                    String comentario = request.getParameter("comentario");

                    // Validación del comentario
                    if (comentario == null || comentario.length() < 3 || comentario.length() > 100) {
                        request.getSession().setAttribute("error", "El comentario es obligatorio y debe tener entre 3 y 100 caracteres.");
                        response.sendRedirect("./pendientes");
                        return;
                    }

                    try {
                        // Buscar la petición por ID
                        peticion = petF.find(peticionId);

                        if (peticion == null) {
                            request.getSession().setAttribute("error", "Petición no encontrada.");
                            response.sendRedirect("./pendientes");
                            return;
                        }

                        // Verificar si la petición ya está publicada
                        if (peticion.getPublicada()==1) {
                            request.getSession().setAttribute("error", "¡La petición no puede ser rechazada!");
                            response.sendRedirect("./pendientes");
                            return;
                        }

                        // Marcar como rechazada y guardar el comentario
                        peticion.setRechazada(Short.valueOf("1"));
                        peticion.setComentario(comentario);
                        petF.edit(peticion);
                        request.getSession().setAttribute("success", "Petición rechazada.");
                        response.sendRedirect("./pendientes");
                    } catch (IOException | NumberFormatException e) {
                        request.getSession().setAttribute("error", "Ocurrió un error al procesar la petición.");
                        response.sendRedirect("./pendientes");
                    }


            }
        }catch(IOException | IllegalStateException | NumberFormatException | SecurityException e){
            
        }
    }
    
    
    private boolean isValidTitulo(String titulo) {
        boolean a=titulo != null && titulo.length() <= 255;
        System.out.println(a);
        return a;
    }

    private boolean isValidDescripcion(String descripcion) {
        boolean a=descripcion != null && descripcion.length() >= 10;
        System.out.println(a);
        return a;
    }

    private boolean isValidVencimiento(String vencimientoStr) {
        try {
            Date vencimiento = java.sql.Date.valueOf(vencimientoStr);
            boolean a=vencimiento.after(new Date());
            System.out.println(a);
            return a;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidUrl(String url) {
        String urlRegex = "^(http|https)://.*$";
        return url != null && Pattern.matches(urlRegex, url);
    }

    private boolean isImageFile(Part filePart) {
        String mimeType = filePart.getContentType();
        return mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/jpg") || mimeType.equals("image/gif") || mimeType.equals("image/svg+xml");
    }

    private String saveImageFile(Part filePart) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uploadDir ="C:\\Users\\users\\Documents\\Cristiannika\\TERCER AnO FACULTAD\\PROGRAMACION WEB III\\Pensax\\src\\main\\webapp\\WEB-INF\\uploads\\imagenes";

        // Crear el directorio si no existe
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Crear la ruta completa del archivo
        File file = new File(dir, fileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return file.getAbsolutePath(); 
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
