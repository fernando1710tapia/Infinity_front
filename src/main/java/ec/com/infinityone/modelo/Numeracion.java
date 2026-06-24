/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul
 */

public class Numeracion implements Serializable {
    
    private Long id;
    
    private String tipodocumento;
   
    private boolean activo;
    
    private int ultimonumero;
   
    private Integer version;
    
    private Comercializadora codigocomercializadora;
    
    private String usuarioactual;

    public Numeracion() {
    }

    public Numeracion(Long id) {
        this.id = id;
    }

    public Numeracion(Long id, String tipodocumento, boolean activo, int ultimonumero) {
        this.id = id;
        this.tipodocumento = tipodocumento;
        this.activo = activo;
        this.ultimonumero = ultimonumero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getUltimonumero() {
        return ultimonumero;
    }

    public void setUltimonumero(int ultimonumero) {
        this.ultimonumero = ultimonumero;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Comercializadora getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(Comercializadora codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

}
