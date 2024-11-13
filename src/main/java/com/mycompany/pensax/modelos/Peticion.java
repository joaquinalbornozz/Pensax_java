/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensax.modelos;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author users
 */
@Entity
@Table(catalog = "pensax", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idpeticion"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Peticion.findAll", query = "SELECT p FROM Peticion p"),
    @NamedQuery(name = "Peticion.findByIdpeticion", query = "SELECT p FROM Peticion p WHERE p.idpeticion = :idpeticion"),
    @NamedQuery(name = "Peticion.findByTitulo", query = "SELECT p FROM Peticion p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Peticion.findByImagen", query = "SELECT p FROM Peticion p WHERE p.imagen = :imagen"),
    @NamedQuery(name = "Peticion.findByVencimiento", query = "SELECT p FROM Peticion p WHERE p.vencimiento = :vencimiento"),
    @NamedQuery(name = "Peticion.findByPublicada", query = "SELECT p FROM Peticion p WHERE p.publicada = :publicada"),
    @NamedQuery(name = "Peticion.findByDeleted", query = "SELECT p FROM Peticion p WHERE p.deleted = :deleted"),
    @NamedQuery(name = "Peticion.findByRechazada", query = "SELECT p FROM Peticion p WHERE p.rechazada = :rechazada"),
    @NamedQuery(name = "Peticion.findByCreatedAt", query = "SELECT p FROM Peticion p WHERE p.createdAt = :createdAt"),
    @NamedQuery(name = "Peticion.findByUpdatedAt", query = "SELECT p FROM Peticion p WHERE p.updatedAt = :updatedAt"),
    @NamedQuery(name = "Peticion.findByPositivos", query = "SELECT p FROM Peticion p WHERE p.positivos = :positivos"),
    @NamedQuery(name = "Peticion.findByNegativos", query = "SELECT p FROM Peticion p WHERE p.negativos = :negativos")})
public class Peticion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idpeticion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(nullable = false, length = 65535)
    private String descripcion;
    @Size(max = 255)
    @Column(length = 255)
    private String imagen;
    @Temporal(TemporalType.DATE)
    private Date vencimiento;
    private Short publicada;
    private Short deleted;
    private Short rechazada;
    @Lob
    @Size(max = 65535)
    @Column(length = 65535)
    private String comentario;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private Integer positivos;
    private Integer negativos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "peticion")
    private Collection<Voto> votoCollection;
    @JoinColumn(name = "carrera_idCarrera", referencedColumnName = "idCarrera")
    @ManyToOne
    private Carrera carreraidCarrera;
    @JoinColumn(name = "user_idusers", referencedColumnName = "idusers", nullable = false)
    @ManyToOne(optional = false)
    private User userIdusers;

    public Peticion() {
    }

    public Peticion(Integer idpeticion) {
        this.idpeticion = idpeticion;
    }

    public Peticion(Integer idpeticion, String titulo, String descripcion) {
        this.idpeticion = idpeticion;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Integer getIdpeticion() {
        return idpeticion;
    }

    public void setIdpeticion(Integer idpeticion) {
        this.idpeticion = idpeticion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Short getPublicada() {
        return publicada;
    }

    public void setPublicada(Short publicada) {
        this.publicada = publicada;
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }

    public Short getRechazada() {
        return rechazada;
    }

    public void setRechazada(Short rechazada) {
        this.rechazada = rechazada;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getPositivos() {
        return positivos;
    }

    public void setPositivos(Integer positivos) {
        this.positivos = positivos;
    }

    public Integer getNegativos() {
        return negativos;
    }

    public void setNegativos(Integer negativos) {
        this.negativos = negativos;
    }

    @XmlTransient
    public Collection<Voto> getVotoCollection() {
        return votoCollection;
    }

    public void setVotoCollection(Collection<Voto> votoCollection) {
        this.votoCollection = votoCollection;
    }

    public Carrera getCarreraidCarrera() {
        return carreraidCarrera;
    }

    public void setCarreraidCarrera(Carrera carreraidCarrera) {
        this.carreraidCarrera = carreraidCarrera;
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
        hash += (idpeticion != null ? idpeticion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peticion)) {
            return false;
        }
        Peticion other = (Peticion) object;
        if ((this.idpeticion == null && other.idpeticion != null) || (this.idpeticion != null && !this.idpeticion.equals(other.idpeticion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pensax.modelos.Peticion[ idpeticion=" + idpeticion + " ]";
    }
    
    public boolean isImageUrl() {
        try {
            URL url = new URL(imagen);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
