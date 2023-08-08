/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author SonyVaio
 */

public class Fechafestiva implements Serializable {
    
    protected FechafestivaPK fechafestivaPK;
    
    private Boolean activo;
    
    private String descripcion;
    
    private String usuarioactual;

    public Fechafestiva() {
    }

    public Fechafestiva(FechafestivaPK fechafestivaPK) {
        this.fechafestivaPK = fechafestivaPK;
    }

    public Fechafestiva(String codigocomercializadora, Date festivo) {
        this.fechafestivaPK = new FechafestivaPK(codigocomercializadora, festivo);
    }

    public FechafestivaPK getFechafestivaPK() {
        return fechafestivaPK;
    }

    public void setFechafestivaPK(FechafestivaPK fechafestivaPK) {
        this.fechafestivaPK = fechafestivaPK;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fechafestivaPK != null ? fechafestivaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fechafestiva)) {
            return false;
        }
        Fechafestiva other = (Fechafestiva) object;
        if ((this.fechafestivaPK == null && other.fechafestivaPK != null) || (this.fechafestivaPK != null && !this.fechafestivaPK.equals(other.fechafestivaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Fechafestiva[ fechafestivaPK=" + fechafestivaPK + " ]";
    }
    
}
