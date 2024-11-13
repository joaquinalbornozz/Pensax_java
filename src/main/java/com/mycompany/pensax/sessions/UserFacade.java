/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.sessions;

import com.mycompany.pensax.modelos.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author users
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    public User findlogin(String email, String password){
        EntityManager emm = getEntityManager();
        CriteriaBuilder cb = emm.getCriteriaBuilder();
        CriteriaQuery cq=cb.createQuery();
        Root<User> c=cq.from(User.class);
        cq.select(c);
        cq.where(
                cb.and(
                        cb.equal(c.get("email"),email),
                        cb.equal(c.get("password"),password)
                )
        );
        Query q= emm.createQuery(cq);
        return  !q.getResultList().isEmpty() ? (User) q.getSingleResult():null;
    }
    public User findByEmail(String email) {
        TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    
    public List<User> getUsuarios(){
        EntityManager emm = getEntityManager();
        CriteriaBuilder cb = emm.getCriteriaBuilder();
        CriteriaQuery cq=cb.createQuery();
        Root<User> c=cq.from(User.class);
        cq.select(c);
        cq.where(cb.or(cb.isNull(c.get("rol")),cb.equal(c.get("rol"),"")) );
        return emm.createQuery(cq).getResultList();
    }
    
}
