/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import ec.com.infinityone.preciosyfacturacion.bean.ListaPrecioBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul
 */

public class Precio implements Serializable {

    protected PrecioPK precioPK;

    private Date fechafin;

    private boolean activo;

    private String observacion;

    private BigDecimal precioproducto;

    private String usuarioactual;

    private Comercializadoraproducto comercializadoraproducto;

    private Listaprecio listaprecio;

    private Terminal terminal;
    
    private List<Detalleprecio> detalleprecioList;
    
    //private String activoS;
    

    public Precio() {
    }

    public Precio(PrecioPK precioPK) {
        this.precioPK = precioPK;
    }

    public Precio(PrecioPK precioPK, boolean activo, BigDecimal precioproducto, String usuarioactual) {
        this.precioPK = precioPK;
        this.activo = activo;
        this.precioproducto = precioproducto;
        this.usuarioactual = usuarioactual;
    }

    public Precio(String codigocomercializadora, String codigoterminal, String codigoproducto, String codigomedida, long codigolistaprecio, Date fechainicio, int secuencial, Long codigoPrecio) {
        this.precioPK = new PrecioPK(codigocomercializadora, codigoterminal, codigoproducto, codigomedida, codigolistaprecio, fechainicio, secuencial, codigoPrecio);
    }

    public PrecioPK getPrecioPK() {
        return precioPK;
    }

    public void setPrecioPK(PrecioPK precioPK) {
        this.precioPK = precioPK;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getPrecioproducto() {
        return precioproducto;
    }

    public void setPrecioproducto(BigDecimal precioproducto) {
        this.precioproducto = precioproducto;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Comercializadoraproducto getComercializadoraproducto() {
        return comercializadoraproducto;
    }

    public void setComercializadoraproducto(Comercializadoraproducto comercializadoraproducto) {
        this.comercializadoraproducto = comercializadoraproducto;
    }

    public Listaprecio getListaprecio() {
        return listaprecio;
    }

    public void setListaprecio(Listaprecio listaprecio) {
        this.listaprecio = listaprecio;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
    
    

//    public String getActivoS() {
//        return activoS;
//    }
//
//    public void setActivoS(String activoS) {
//        this.activoS = activoS;
//    }

    public List<Detalleprecio> getDetalleprecioList() {
        return detalleprecioList;
    }

    public void setDetalleprecioList(List<Detalleprecio> detalleprecioList) {
        this.detalleprecioList = detalleprecioList;
    }
    
}
