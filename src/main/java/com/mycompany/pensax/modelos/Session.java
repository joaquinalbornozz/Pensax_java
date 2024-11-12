/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.modelos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author users
 */
@Entity
@Table(catalog = "pensax", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idsession"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"),
    @NamedQuery(name = "Session.findByIdsession", query = "SELECT s FROM Session s WHERE s.idsession = :idsession"),
    @NamedQuery(name = "Session.findBySessionToken", query = "SELECT s FROM Session s WHERE s.sessionToken = :sessionToken"),
    @NamedQuery(name = "Session.findByExperationTime", query = "SELECT s FROM Session s WHERE s.experationTime = :experationTime"),
    @NamedQuery(name = "Session.findByCreatedAt", query = "SELECT s FROM Session s WHERE s.createdAt = :createdAt")})
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(nullable = false, length = 128)
    private String idsession;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "session_token", nullable = false, length = 255)
    private String sessionToken;
    @Column(name = "experation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date experationTime;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "user_idusers", referencedColumnName = "idusers", nullable = false)
    @ManyToOne(optional = false)
    private User userIdusers;

    public Session() {
    }

    public Session(String idsession) {
        this.idsession = idsession;
    }

    public Session(String idsession, String sessionToken) {
        this.idsession = idsession;
        this.sessionToken = sessionToken;
    }

    public String getIdsession() {
        return idsession;
    }

    public void setIdsession(String idsession) {
        this.idsession = idsession;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Date getExperationTime() {
        return experationTime;
    }

    public void setExperationTime(Date experationTime) {
        this.experationTime = experationTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUserIdusers() {
        return userIdusers;
    }

    public void setUserIdusers(User userIdusers) {
        this.userIdusers = userIdusers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsession != null ? idsession.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.idsession == null && other.idsession != null) || (this.idsession != null && !this.idsession.equals(other.idsession))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pensax.modelos.Session[ idsession=" + idsession + " ]";
    }
    
}
