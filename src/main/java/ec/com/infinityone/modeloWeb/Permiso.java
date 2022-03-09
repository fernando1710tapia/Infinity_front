/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;

/**
 *
 * @author SonyVaio
 */

public class Permiso implements Serializable {

    protected PermisoPK permisoPK;

    private String usuarioactual;

    private Menu menu;

    public Permiso() {
    }

    public Permiso(PermisoPK permisoPK) {
        this.permisoPK = permisoPK;
    }

    public Permiso(PermisoPK permisoPK, String usuarioactual) {
        this.permisoPK = permisoPK;
        this.usuarioactual = usuarioactual;
    }

    public Permiso(String niveloperacion, String codigomenu) {
        this.permisoPK = new PermisoPK(niveloperacion, codigomenu);
    }

    public PermisoPK getPermisoPK() {
        return permisoPK;
    }

    public void setPermisoPK(PermisoPK permisoPK) {
        this.permisoPK = permisoPK;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisoPK != null ? permisoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.permisoPK == null && other.permisoPK != null) || (this.permisoPK != null && !this.permisoPK.equals(other.permisoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Permiso[ permisoPK=" + permisoPK + " ]";
    }
    
}
