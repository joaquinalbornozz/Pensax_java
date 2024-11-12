/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.modelos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author users
 */
@Entity
@Table(catalog = "pensax", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Voto.findAll", query = "SELECT v FROM Voto v"),
    @NamedQuery(name = "Voto.findByUserIdusers", query = "SELECT v FROM Voto v WHERE v.votoPK.userIdusers = :userIdusers"),
    @NamedQuery(name = "Voto.findByPeticionIdpeticion", query = "SELECT v FROM Voto v WHERE v.votoPK.peticionIdpeticion = :peticionIdpeticion"),
    @NamedQuery(name = "Voto.findByVoto", query = "SELECT v FROM Voto v WHERE v.voto = :voto"),
    @NamedQuery(name = "Voto.findByAnonimo", query = "SELECT v FROM Voto v WHERE v.anonimo = :anonimo")})
public class Voto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VotoPK votoPK;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private short voto;
    private Short anonimo;
    @JoinColumn(name = "peticion_idpeticion", referencedColumnName = "idpeticion", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Peticion peticion;
    @JoinColumn(name = "user_idusers", referencedColumnName = "idusers", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Voto() {
    }

    public Voto(VotoPK votoPK) {
        this.votoPK = votoPK;
    }

    public Voto(VotoPK votoPK, short voto) {
        this.votoPK = votoPK;
        this.voto = voto;
    }

    public Voto(int userIdusers, int peticionIdpeticion) {
        this.votoPK = new VotoPK(userIdusers, peticionIdpeticion);
    }

    public VotoPK getVotoPK() {
        return votoPK;
    }

    public void setVotoPK(VotoPK votoPK) {
        this.votoPK = votoPK;
    }

    public short getVoto() {
        return voto;
    }

    public void setVoto(short voto) {
        this.voto = voto;
    }

    public Short getAnonimo() {
        return anonimo;
    }

    public void setAnonimo(Short anonimo) {
        this.anonimo = anonimo;
    }

    public Peticion getPeticion() {
        return peticion;
    }

    public void setPeticion(Peticion peticion) {
        this.peticion = peticion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (votoPK != null ? votoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Voto)) {
            return false;
        }
        Voto other = (Voto) object;
        if ((this.votoPK == null && other.votoPK != null) || (this.votoPK != null && !this.votoPK.equals(other.votoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pensax.modelos.Voto[ votoPK=" + votoPK + " ]";
    }
    
}
