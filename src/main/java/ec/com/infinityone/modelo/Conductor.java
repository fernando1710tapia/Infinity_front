/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author HP
 */
public class Conductor implements Serializable{
    
    private String cedularuc;

    private String nombre;

    private boolean activo;

    private String usuarioactual;   
    
    public Conductor() {
    }

    public String getCedularuc() {
        return cedularuc;
    }

    public void setCedularuc(String cedularuc) {
        this.cedularuc = cedularuc;
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

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedularuc != null ? cedularuc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conductor)) {
            return false;
        }
        Conductor other = (Conductor) object;
        if ((this.cedularuc == null && other.cedularuc != null) || (this.cedularuc != null && !this.cedularuc.equals(other.cedularuc))) {
            return false;
        }
        return true;
    }
}
