/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SonyVaio
 */
public class Listaprecio implements Serializable {

    protected ListaprecioPK listaprecioPK;

    private String nombre;

    private String tipo;

    private Boolean activo;

    private String usuarioactual;

    private List<Precio> precioList;

    //private List<Clientelistaprecio> clientelistaprecioList;

    private List<Listaprecioterminalproducto> listaprecioterminalproductoList;

    public Listaprecio() {
    }

    public Listaprecio(ListaprecioPK listaprecioPK) {
        this.listaprecioPK = listaprecioPK;
    }

    public Listaprecio(ListaprecioPK listaprecioPK, String nombre, String tipo, String usuarioactual) {
        this.listaprecioPK = listaprecioPK;
        this.nombre = nombre;
        this.tipo = tipo;
        this.usuarioactual = usuarioactual;
    }

    public Listaprecio(String codigocomercializadora, long codigo) {
        this.listaprecioPK = new ListaprecioPK(codigocomercializadora, codigo);
    }

    public ListaprecioPK getListaprecioPK() {
        return listaprecioPK;
    }

    public void setListaprecioPK(ListaprecioPK listaprecioPK) {
        this.listaprecioPK = listaprecioPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    @XmlTransient
    public List<Precio> getPrecioList() {
        return precioList;
    }

    public void setPrecioList(List<Precio> precioList) {
        this.precioList = precioList;
    }

//    @XmlTransient
//    public List<Clientelistaprecio> getClientelistaprecioList() {
//        return clientelistaprecioList;
//    }
//
//    public void setClientelistaprecioList(List<Clientelistaprecio> clientelistaprecioList) {
//        this.clientelistaprecioList = clientelistaprecioList;
//    }

    @XmlTransient
    public List<Listaprecioterminalproducto> getListaprecioterminalproductoList() {
        return listaprecioterminalproductoList;
    }

    public void setListaprecioterminalproductoList(List<Listaprecioterminalproducto> listaprecioterminalproductoList) {
        this.listaprecioterminalproductoList = listaprecioterminalproductoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listaprecioPK != null ? listaprecioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Listaprecio)) {
            return false;
        }
        Listaprecio other = (Listaprecio) object;
        if ((this.listaprecioPK == null && other.listaprecioPK != null) || (this.listaprecioPK != null && !this.listaprecioPK.equals(other.listaprecioPK))) {
            return false;
        }
        return true;
    } 

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Listaprecio[ listaprecioPK=" + listaprecioPK + " ]";
    }
    
    /*
     public String getCodigo() {
        return getListaprecioPK().getCodigo();
    }
     public String getCodigocomercializadora() {
        return getListaprecioPK().getCodigocomercializadora();
    }
     
    public void setCodigo(String codigo) {
         new ListaprecioPK().setCodigo(codigo);
    }
     public void setCodigocomercializadora(String codigoComercializadora) {
         new ListaprecioPK().setCodigocomercializadora(codigoComercializadora);
    } 
     */ 
}
