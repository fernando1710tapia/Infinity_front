/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Paul
 */

public class Detallepago implements Serializable {
   
    protected DetallepagoPK detallepagoPK;
  
    private BigDecimal valor;
    
    private boolean activo;
    
    private String usuarioactual;
    
    private Factura factura;
    
    private Pagofactura pagofactura;
    
    private String activoS;

    public Detallepago() {
    }

    public Detallepago(DetallepagoPK detallepagoPK) {
        this.detallepagoPK = detallepagoPK;
    }

    public Detallepago(DetallepagoPK detallepagoPK, BigDecimal valor, boolean activo, String usuarioactual) {
        this.detallepagoPK = detallepagoPK;
        this.valor = valor;
        this.activo = activo;
        this.usuarioactual = usuarioactual;
    }

    public Detallepago(String codigoabastecedora, String codigocomercializadora, String numeronotapedido, String numero, String codigobanco, String numerofactura) {
        this.detallepagoPK = new DetallepagoPK(codigoabastecedora, codigocomercializadora, numeronotapedido, numero, codigobanco, numerofactura);
    }

    public DetallepagoPK getDetallepagoPK() {
        return detallepagoPK;
    }

    public void setDetallepagoPK(DetallepagoPK detallepagoPK) {
        this.detallepagoPK = detallepagoPK;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Pagofactura getPagofactura() {
        return pagofactura;
    }

    public void setPagofactura(Pagofactura pagofactura) {
        this.pagofactura = pagofactura;
    }

    public String getActivoS() {
        return activoS;
    }

    public void setActivoS(String activoS) {
        this.activoS = activoS;
    }

}
