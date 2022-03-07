/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul
 */

public class Pagofactura implements Serializable {
   
    protected PagofacturaPK pagofacturaPK;
    
    private Date fecha;
    
    private boolean activo;
    
    private BigDecimal valor;
    
    private String observacion;
    
    private Date fecharegistro;
    
    private String usuarioactual;
    
    private List<Detallepago> detallepagoList;
    
    private Abastecedora abastecedora;
    
    private Banco banco;
    
    private String activoS;

    public Pagofactura() {
    }

    public Pagofactura(PagofacturaPK pagofacturaPK) {
        this.pagofacturaPK = pagofacturaPK;
    }

    public Pagofactura(PagofacturaPK pagofacturaPK, Date fecha, boolean activo, BigDecimal valor, String observacion, Date fecharegistro, String usuarioactual) {
        this.pagofacturaPK = pagofacturaPK;
        this.fecha = fecha;
        this.activo = activo;
        this.valor = valor;
        this.observacion = observacion;
        this.fecharegistro = fecharegistro;
        this.usuarioactual = usuarioactual;
    }

    public Pagofactura(String codigoabastecedora, String codigocomercializadora, String numero, String codigobanco) {
        this.pagofacturaPK = new PagofacturaPK(codigoabastecedora, codigocomercializadora, numero, codigobanco);
    }

    public PagofacturaPK getPagofacturaPK() {
        return pagofacturaPK;
    }

    public void setPagofacturaPK(PagofacturaPK pagofacturaPK) {
        this.pagofacturaPK = pagofacturaPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public List<Detallepago> getDetallepagoList() {
        return detallepagoList;
    }

    public void setDetallepagoList(List<Detallepago> detallepagoList) {
        this.detallepagoList = detallepagoList;
    }

    public Abastecedora getAbastecedora() {
        return abastecedora;
    }

    public void setAbastecedora(Abastecedora abastecedora) {
        this.abastecedora = abastecedora;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getActivoS() {
        return activoS;
    }

    public void setActivoS(String activoS) {
        this.activoS = activoS;
    }

}
