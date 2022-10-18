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

public class DespachoTotalDto implements Serializable {
    
    private String nombreTerminal;
   
    private String nombreProducto;
  
    private BigDecimal volumenTotal;

    private Integer guias;

    public DespachoTotalDto() {
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

    public Integer getGuias() {
        return guias;
    }

    public void setGuias(Integer guias) {
        this.guias = guias;
    }
  
}