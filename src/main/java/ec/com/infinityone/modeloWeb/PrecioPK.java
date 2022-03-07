/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Paul
 */
@Embeddable
public class PrecioPK implements Serializable {
    
    private String codigocomercializadora;
    
    private String codigoterminal;
   
    private String codigoproducto;
    
    private String codigomedida;
    
    private long codigolistaprecio;
    
    private Date fechainicio;
    
    private int secuencial;
    
    private Long codigoPrecio;

    public PrecioPK() {
    }

    public PrecioPK(String codigocomercializadora, String codigoterminal, String codigoproducto, String codigomedida, long codigolistaprecio, Date fechainicio, int secuencial, Long codigoPrecio) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminal = codigoterminal;
        this.codigoproducto = codigoproducto;
        this.codigomedida = codigomedida;
        this.codigolistaprecio = codigolistaprecio;
        this.fechainicio = fechainicio;
        this.secuencial = secuencial;
        this.codigoPrecio = codigoPrecio;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public String getCodigomedida() {
        return codigomedida;
    }

    public void setCodigomedida(String codigomedida) {
        this.codigomedida = codigomedida;
    }

    public long getCodigolistaprecio() {
        return codigolistaprecio;
    }

    public void setCodigolistaprecio(long codigolistaprecio) {
        this.codigolistaprecio = codigolistaprecio;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public Long getCodigoPrecio() {
        return codigoPrecio;
    }

    public void setCodigoPrecio(Long codigoPrecio) {
        this.codigoPrecio = codigoPrecio;
    }
  
}
