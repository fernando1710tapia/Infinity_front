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
 * @author SonyVaio
 */
@Embeddable
public class ConsultagarantiaPK implements Serializable {
    
    private String codigocomercializadora;
    
    private Date fecharecepcion;

    public ConsultagarantiaPK() {
    }

    public ConsultagarantiaPK(String codigocomercializadora, Date fecharecepcion) {
        this.codigocomercializadora = codigocomercializadora;
        this.fecharecepcion = fecharecepcion;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public Date getFecharecepcion() {
        return fecharecepcion;
    }

    public void setFecharecepcion(Date fecharecepcion) {
        this.fecharecepcion = fecharecepcion;
    }   
    
}
