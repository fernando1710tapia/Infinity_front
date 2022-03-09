/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

public class Consultagarantia implements Serializable {
   
    protected ConsultagarantiaPK consultagarantiaPK;
    
    private String codigomoneda;
    
    private BigDecimal valorcomercializadora;
    
    private BigDecimal sumaavalizada;
    
    private BigDecimal garantia98;
    
    private BigDecimal saldo;
   
    private BigDecimal saldodisponible;
    
    private BigDecimal porcentajeuso;
    
    private boolean activo;
   
    private String usuarioactual;

    public Consultagarantia() {
    }

    public Consultagarantia(ConsultagarantiaPK consultagarantiaPK) {
        this.consultagarantiaPK = consultagarantiaPK;
    }

    public Consultagarantia(ConsultagarantiaPK consultagarantiaPK, String codigomoneda, BigDecimal valorcomercializadora, BigDecimal sumaavalizada, BigDecimal garantia98, BigDecimal saldo, BigDecimal saldodisponible, BigDecimal porcentajeuso, boolean activo) {
        this.consultagarantiaPK = consultagarantiaPK;
        this.codigomoneda = codigomoneda;
        this.valorcomercializadora = valorcomercializadora;
        this.sumaavalizada = sumaavalizada;
        this.garantia98 = garantia98;
        this.saldo = saldo;
        this.saldodisponible = saldodisponible;
        this.porcentajeuso = porcentajeuso;
        this.activo = activo;
    }

    public Consultagarantia(String codigocomercializadora, Date fecharecepcion) {
        this.consultagarantiaPK = new ConsultagarantiaPK(codigocomercializadora, fecharecepcion);
    }

    public ConsultagarantiaPK getConsultagarantiaPK() {
        return consultagarantiaPK;
    }

    public void setConsultagarantiaPK(ConsultagarantiaPK consultagarantiaPK) {
        this.consultagarantiaPK = consultagarantiaPK;
    }

    public String getCodigomoneda() {
        return codigomoneda;
    }

    public void setCodigomoneda(String codigomoneda) {
        this.codigomoneda = codigomoneda;
    }

    public BigDecimal getValorcomercializadora() {
        return valorcomercializadora;
    }

    public void setValorcomercializadora(BigDecimal valorcomercializadora) {
        this.valorcomercializadora = valorcomercializadora;
    }

    public BigDecimal getSumaavalizada() {
        return sumaavalizada;
    }

    public void setSumaavalizada(BigDecimal sumaavalizada) {
        this.sumaavalizada = sumaavalizada;
    }

    public BigDecimal getGarantia98() {
        return garantia98;
    }

    public void setGarantia98(BigDecimal garantia98) {
        this.garantia98 = garantia98;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldodisponible() {
        return saldodisponible;
    }

    public void setSaldodisponible(BigDecimal saldodisponible) {
        this.saldodisponible = saldodisponible;
    }

    public BigDecimal getPorcentajeuso() {
        return porcentajeuso;
    }

    public void setPorcentajeuso(BigDecimal porcentajeuso) {
        this.porcentajeuso = porcentajeuso;
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

}
