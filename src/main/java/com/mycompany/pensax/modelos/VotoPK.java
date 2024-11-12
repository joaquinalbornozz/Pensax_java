/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.modelos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author users
 */
@Embeddable
public class VotoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_idusers", nullable = false)
    private int userIdusers;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peticion_idpeticion", nullable = false)
    private int peticionIdpeticion;

    public VotoPK() {
    }

    public VotoPK(int userIdusers, int peticionIdpeticion) {
        this.userIdusers = userIdusers;
        this.peticionIdpeticion = peticionIdpeticion;
    }

    public int getUserIdusers() {
        return userIdusers;
    }

    public void setUserIdusers(int userIdusers) {
        this.userIdusers = userIdusers;
    }

    public int getPeticionIdpeticion() {
        return peticionIdpeticion;
    }

    public void setPeticionIdpeticion(int peticionIdpeticion) {
        this.peticionIdpeticion = peticionIdpeticion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userIdusers;
        hash += (int) peticionIdpeticion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VotoPK)) {
            return false;
        }
        VotoPK other = (VotoPK) object;
        if (this.userIdusers != other.userIdusers) {
            return false;
        }
        if (this.peticionIdpeticion != other.peticionIdpeticion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pensax.modelos.VotoPK[ userIdusers=" + userIdusers + ", peticionIdpeticion=" + peticionIdpeticion + " ]";
    }
    
}
