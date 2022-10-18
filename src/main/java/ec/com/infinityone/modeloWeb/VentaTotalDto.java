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

public class VentaTotalDto implements Serializable {
    
    private String nombreTerminal;
   
    private String nombreProducto;
  
    private BigDecimal volumenTotal;

    private Integer facturas;

    public VentaTotalDto() {
    }

    public String getNombreTerminal() {
        return nombreTerminal;
    }

    public void setNombreTerminal(String nombreTerminal) {
        this.nombreTerminal = nombreTerminal;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getVolumenTotal() {
        return volumenTotal;
    }

    public void setVolumenTotal(BigDecimal volumenTotal) {
        this.volumenTotal = volumenTotal;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas(Integer facturas) {
        this.facturas = facturas;
    }

    
}