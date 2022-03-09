/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SonyVaio
 */

public class Menu implements Serializable {
    
    private String codigo;
    
    private String nombre;
    
    private String nivel;
    
    private String menupadre;
    
    private String urlaccion;
    
    private String usuarioactual;
    
    private List<Permiso> permisoList;

    public Menu() {
    }

    public Menu(String codigo) {
        this.codigo = codigo;
    }

    public Menu(String codigo, String nivel, String usuarioactual) {
        this.codigo = codigo;
        this.nivel = nivel;
        this.usuarioactual = usuarioactual;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getMenupadre() {
        return menupadre;
    }

    public void setMenupadre(String menupadre) {
        this.menupadre = menupadre;
    }

    public String getUrlaccion() {
        return urlaccion;
    }

    public void setUrlaccion(String urlaccion) {
        this.urlaccion = urlaccion;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    @XmlTransient
    public List<Permiso> getPermisoList() {
        return permisoList;
    }

    public void setPermisoList(List<Permiso> permisoList) {
        this.permisoList = permisoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Menu[ codigo=" + codigo + " ]";
    }
    
}
