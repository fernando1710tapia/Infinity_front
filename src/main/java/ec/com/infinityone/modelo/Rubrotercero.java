/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import ec.com.infinityone.modelo.Comercializadora;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SonyVaio
 */

public class Rubrotercero implements Serializable {
    
    protected RubroterceroPK rubroterceroPK;
    
    private String nombre;
    
    private boolean activo;
    
    private String codigocontable;
    
    private String tipo;
    
    private String usuarioactual;
    
    private List<Clienterubrotercero> clienterubroterceroList;
    
    private Comercializadora comercializadora;

    public Rubrotercero() {
    }

    public Rubrotercero(RubroterceroPK rubroterceroPK) {
        this.rubroterceroPK = rubroterceroPK;
    }

    public Rubrotercero(RubroterceroPK rubroterceroPK, String nombre, boolean activo, String tipo) {
        this.rubroterceroPK = rubroterceroPK;
        this.nombre = nombre;
        this.activo = activo;
        this.tipo = tipo;
    }

    public Rubrotercero(String codigocomercializadora, long codigo) {
        this.rubroterceroPK = new RubroterceroPK(codigocomercializadora, codigo);
    }

    public RubroterceroPK getRubroterceroPK() {
        return rubroterceroPK;
    }

    public void setRubroterceroPK(RubroterceroPK rubroterceroPK) {
        this.rubroterceroPK = rubroterceroPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodigocontable() {
        return codigocontable;
    }

    public void setCodigocontable(String codigocontable) {
        this.codigocontable = codigocontable;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    @XmlTransient
    public List<Clienterubrotercero> getClienterubroterceroList() {
        return clienterubroterceroList;
    }

    public void setClienterubroterceroList(List<Clienterubrotercero> clienterubroterceroList) {
        this.clienterubroterceroList = clienterubroterceroList;
    }

    public Comercializadora getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(Comercializadora comercializadora) {
        this.comercializadora = comercializadora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rubroterceroPK != null ? rubroterceroPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rubrotercero)) {
            return false;
        }
        Rubrotercero other = (Rubrotercero) object;
        if ((this.rubroterceroPK == null && other.rubroterceroPK != null) || (this.rubroterceroPK != null && !this.rubroterceroPK.equals(other.rubroterceroPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Rubrotercero[ rubroterceroPK=" + rubroterceroPK + " ]";
    }
    
}