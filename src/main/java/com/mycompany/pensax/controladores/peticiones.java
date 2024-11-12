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
import java.util.Collection;
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
            "/peticiones/mis"
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
        try{
            switch(pathUsuario){
                case"/peticiones":
                    String carreraId = request.getParameter("carrera_id");

                    // Obtener las peticiones filtradas
                    List<Peticion> peticiones = petF.getPeticionesPulicadas(carreraId);

                    // Obtener todas las carreras para el filtro
                    List<Carrera> carreras = carreraF.findAll();

                    // Configurar los atributos para la vista
                    request.setAttribute("peticiones", peticiones);
                    request.setAttribute("carreras", carreras);
                    url="/WEB-INF/peticiones/index.jsp";
                    break;
                case"/peticiones/votar":
                    Integer peticionId= Integer.valueOf(request.getParameter("id")) ;
                    Peticion p= petF.find(peticionId);
                    if((p.getRechazada()==1) || (p.getPublicada()==0)){
                        response.sendRedirect("./");
                        return;
                    }else{
                        User user= (User) request.getSession().getAttribute("user");
                        Carrera c= p.getCarreraidCarrera();
                        Voto uservote = p.getVotoCollection()
                                .stream()
                                .filter(voto -> voto.getUser().getIdusers().equals(user.getIdusers()))
                                .findFirst()
                                .orElse(null);
                        System.out.println(uservote);
                        boolean uv;
                        uv = uservote != null;
                        request.setAttribute("peticion", p);
                        request.setAttribute("usuario", user);
                        request.setAttribute("carrera", c);
                        request.setAttribute("uservote", uv);
                        url="/WEB-INF/peticiones/votar.jsp";
                    }
                    break;
                case "/peticiones/create":
                    carreras = carreraF.findAll();
                    request.setAttribute("carreras", carreras);
                    url="/WEB-INF/peticiones/create.jsp";
                    break;
                case "/peticiones/mis":
                    User user = (User) request.getSession().getAttribute("user");
                    System.out.println(user);
                    if(user==null){
                        response.sendRedirect("/Pensax/login");
                        return;
                    }
                    String filter = request.getParameter("filter");
                    if(filter==null){
                        filter="";
                    }
                    // Llamar al EJB para obtener las peticiones del usuario
                    peticiones = petF.obtenerPeticionesPorUsuario(user, filter);

                    // Enviar la lista de peticiones a la vista JSP
                    request.setAttribute("peticiones", peticiones);
                    url="/WEB-INF/peticiones/mispeticiones.jsp";
                    break;
                case "/peticiones/editar":
                    
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
                        User user = (User) request.getSession().getAttribute("user");
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
                                                 .filter(v -> v.getUser().getIdusers().equals(user.getIdusers()))
                                                 .findFirst()
                                                 .orElse(null);

                        if (userVote != null) {
                            response.sendRedirect("./peticiones?error=Ya has votado esta petición.");
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

                        // Obtener parámetros del formulario
                        String titulo = request.getParameter("titulo");
                        String descripcion = request.getParameter("descripcion");
                        String vencimientoStr = request.getParameter("vencimiento");
                        String carreraId = request.getParameter("carrera_id");
                        String imagenUrl = request.getParameter("imagen_url");
                        System.out.println(titulo);
                        // Validación de campos
                        if (!isValidTitulo(titulo) || !isValidDescripcion(descripcion) || !isValidVencimiento(vencimientoStr)) {
                            request.setAttribute("error", "Los datos no son válidos.");
                            request.getRequestDispatcher("/WEB-INF/peticiones/index.jsp").forward(request, response);
                            return;
                        }

                        // Convertir la fecha de vencimiento
                        Date vencimiento = java.sql.Date.valueOf(vencimientoStr);

                        // Validar sesión del usuario
                        User user = (User) request.getSession().getAttribute("user");
                        if (user == null) {
                            response.sendRedirect("/Pensax/login");
                            return;
                        }

                        // Crear y configurar la instancia de Peticion
                        Peticion peticion = new Peticion();
                        peticion.setTitulo(titulo);
                        peticion.setDescripcion(descripcion);
                        peticion.setVencimiento(vencimiento);
                        peticion.setCarreraidCarrera(carreraId != null ? carreraF.find(Integer.valueOf(carreraId)) : null);
                        peticion.setPositivos(0);
                        peticion.setNegativos(0);
                        peticion.setUserIdusers(user);
                        peticion.setComentario("");
                        peticion.setPublicada(Short.valueOf("0"));
                        peticion.setRechazada(Short.valueOf("0"));
                        peticion.setCreatedAt(new Date());
                        peticion.setDeleted(Short.valueOf("0"));
                        peticion.setUpdatedAt(new Date());

                        // Manejo de la imagen (archivo o URL)
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

                        // Guardar la petición en la base de datos
                        utx.begin();
                        em = emf.createEntityManager();
                        em.persist(peticion);
                        utx.commit();

                        // Redirección después de guardar
                        request.setAttribute("success", "Petición creada correctamente!");
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
        String uploadDir = getServletContext().getRealPath("/uploads/imagenes");

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

        return file.getAbsolutePath(); // Retorna la ruta completa del archivo guardado
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
