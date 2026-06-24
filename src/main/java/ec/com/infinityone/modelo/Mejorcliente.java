/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Paul
 */

public class Mejorcliente implements Serializable {
    
    private String nombrecliente;
   
    private BigDecimal sumatotal;
  
    private Integer facturas;

    public Mejorcliente() {
    }

    public Mejorcliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public Mejorcliente(String nombrecliente, BigDecimal sumatotal, Integer facturas) {
        
        this.nombrecliente = nombrecliente;
        this.sumatotal = sumatotal;
        this.facturas = facturas;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public BigDecimal getSumatotal() {
        return sumatotal;
    }

    public void setSumatotal(BigDecimal sumatotal) {
        this.sumatotal = sumatotal;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas(Integer facturas) {
        this.facturas = facturas;
    }  
    
}