/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SonyVaio
 */

public class Facturadordespachador implements Serializable {
    
    protected FacturadordespachadorPK facturadordespachadorPK;
    
    private Boolean activo;
    
    private String usuarioactual;
    
    private Terminal terminal;

    public Facturadordespachador() {
    }

    public Facturadordespachador(FacturadordespachadorPK facturadordespachadorPK) {
        this.facturadordespachadorPK = facturadordespachadorPK;
    }

    public Facturadordespachador(FacturadordespachadorPK facturadordespachadorPK, String usuarioactual) {
        this.facturadordespachadorPK = facturadordespachadorPK;
        this.usuarioactual = usuarioactual;
    }

    public Facturadordespachador(String codigocomercializadora, String codigoterminal, String codigousuario) {
        this.facturadordespachadorPK = new FacturadordespachadorPK(codigocomercializadora, codigoterminal, codigousuario);
    }

    public FacturadordespachadorPK getFacturadordespachadorPK() {
        return facturadordespachadorPK;
    }

    public void setFacturadordespachadorPK(FacturadordespachadorPK facturadordespachadorPK) {
        this.facturadordespachadorPK = facturadordespachadorPK;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturadordespachadorPK != null ? facturadordespachadorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturadordespachador)) {
            return false;
        }
        Facturadordespachador other = (Facturadordespachador) object;
        if ((this.facturadordespachadorPK == null && other.facturadordespachadorPK != null) || (this.facturadordespachadorPK != null && !this.facturadordespachadorPK.equals(other.facturadordespachadorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Facturadordespachador[ facturadordespachadorPK=" + facturadordespachadorPK + " ]";
    }
    
}
