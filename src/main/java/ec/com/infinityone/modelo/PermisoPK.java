/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SonyVaio
 */
@Embeddable
public class PermisoPK implements Serializable {
    
    private String niveloperacion;
    
    private String codigomenu;

    public PermisoPK() {
    }

    public PermisoPK(String niveloperacion, String codigomenu) {
        this.niveloperacion = niveloperacion;
        this.codigomenu = codigomenu;
    }

    public String getNiveloperacion() {
        return niveloperacion;
    }

    public void setNiveloperacion(String niveloperacion) {
        this.niveloperacion = niveloperacion;
    }

    public String getCodigomenu() {
        return codigomenu;
    }

    public void setCodigomenu(String codigomenu) {
        this.codigomenu = codigomenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (niveloperacion != null ? niveloperacion.hashCode() : 0);
        hash += (codigomenu != null ? codigomenu.hashCode() : 0);
        return hash;
    }

    

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.PermisoPK[ niveloperacion=" + niveloperacion + ", codigomenu=" + codigomenu + " ]";
    }

    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoPK)) {
            return false;
        }
        PermisoPK other = (PermisoPK) object;
        if ((this.niveloperacion == null && other.niveloperacion != null) || (this.niveloperacion != null && !this.niveloperacion.equals(other.niveloperacion))) {
            return false;
        }
        if ((this.codigomenu == null && other.codigomenu != null) || (this.codigomenu != null && !this.codigomenu.equals(other.codigomenu))) {
            return false;
        }
        return true;
    }

   
    
}
