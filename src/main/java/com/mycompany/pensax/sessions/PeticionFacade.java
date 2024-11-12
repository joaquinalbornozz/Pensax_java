/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.sessions;

import com.mycompany.pensax.modelos.Peticion;
import com.mycompany.pensax.modelos.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *
 * @author users
 */
@Stateless
public class PeticionFacade extends AbstractFacade<Peticion> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeticionFacade() {
        super(Peticion.class);
    }
    
    public List<Peticion> getPeticionesPulicadas(String carreraId){
        EntityManager emm = getEntityManager();
        CriteriaBuilder cb = emm.getCriteriaBuilder();
        
        // Crear la consulta de Peticion
        CriteriaQuery<Peticion> cq = cb.createQuery(Peticion.class);
        Root<Peticion> peticion = cq.from(Peticion.class);

        // Construir predicados para los filtros
        Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Predicate publicada = cb.equal(peticion.get("publicada"), true);
        Predicate noVencida = cb.greaterThanOrEqualTo(peticion.get("vencimiento"), now);

        // Filtro de carrera (null o específico)
        Predicate carreraPredicate;
        if ("0".equals(carreraId)) {
            carreraPredicate = cb.isNull(peticion.get("carreraidCarrera"));
        } else if (carreraId != null && !carreraId.isEmpty()) {
            carreraPredicate = cb.equal(peticion.get("carreraidCarrera").get("idCarrera"), carreraId);
        } else {
            carreraPredicate = cb.conjunction(); // Sin filtro de carrera
        }

        // Combinar los predicados
        cq.where(cb.and(publicada, noVencida, carreraPredicate));
        cq.orderBy(cb.asc(peticion.get("vencimiento"))); // Ordenar por fecha de creación

        return emm.createQuery(cq)
                 .getResultList();

    }
    
    public List<Peticion> obtenerPeticionesPorUsuario(User user, String filter) {
        EntityManager emm = getEntityManager();
        CriteriaBuilder cb = emm.getCriteriaBuilder();
        CriteriaQuery<Peticion> query = cb.createQuery(Peticion.class);
        Root<Peticion> peticionRoot = query.from(Peticion.class);

        
        Predicate userPredicate = cb.equal(peticionRoot.get("userIdusers"), user);
        Predicate filterPredicate = cb.conjunction();

        // Aplicar filtro específico
        switch (filter) {
            case "publicadas":
                filterPredicate = cb.equal(peticionRoot.get("publicada"),1);
                break;
            case "rechazadas":
                filterPredicate = cb.equal(peticionRoot.get("rechazada"),1);
                break;
            case "pendientes":
                filterPredicate = cb.and(
                    cb.equal(peticionRoot.get("rechazada"),0),
                    cb.equal(peticionRoot.get("publicada"),0),
                    cb.greaterThan(peticionRoot.get("vencimiento"), new Date())
                );
                break;
            case "eliminadas":
                filterPredicate = cb.equal(peticionRoot.get("deleted"),1);
                break;
            case "vencidas":
                filterPredicate = cb.lessThan(peticionRoot.get("vencimiento"), new Date());
                break;
            default:
                // Traer todas las peticiones, incluyendo eliminadas si existe esa configuración
                return emm.createQuery(query.select(peticionRoot).where(userPredicate).orderBy(cb.desc(peticionRoot.get("createdAt")))).getResultList();
        }

        // Combinar filtros de usuario y condición
        query.select(peticionRoot)
             .where(cb.and(userPredicate, filterPredicate))
             .orderBy(cb.desc(peticionRoot.get("createdAt")));

        // Ejecutar la consulta
        return emm.createQuery(query).getResultList();
    }
    
}
