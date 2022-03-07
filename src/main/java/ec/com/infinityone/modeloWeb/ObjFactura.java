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
 * @author HP
 */
public class ObjFactura implements Serializable{
    
    private String codigobanco;

    private String fechavencimiento;  
    
    private BigDecimal valortotal;
    
    private Integer numerofacturas;   

    public ObjFactura() {
    }

    public ObjFactura(String codigobanco) {
        this.codigobanco = codigobanco;
    }

    public ObjFactura(String codigobanco, String fechavencimiento, BigDecimal valortotal, Integer numerofacturas) {
        this.codigobanco = codigobanco;
        this.fechavencimiento = fechavencimiento;
        this.valortotal = valortotal;
        this.numerofacturas = numerofacturas;
    }

    public String getCodigobanco() {
        return codigobanco;
    }

    public void setCodigobanco(String codigobanco) {
        this.codigobanco = codigobanco;
    }

    public String getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(String fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public BigDecimal getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigDecimal valortotal) {
        this.valortotal = valortotal;
    }

    public Integer getNumerofacturas() {
        return numerofacturas;
    }

    public void setNumerofacturas(Integer numerofacturas) {
        this.numerofacturas = numerofacturas;
    }
    
    
    
}
