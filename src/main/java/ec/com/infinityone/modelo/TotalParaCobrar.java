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
 * @author HP
 */
public class TotalParaCobrar implements Serializable{
    private String banco;

    private String fechavencimiento;

    private BigDecimal valortotal;

    private BigDecimal valortotalconrubro;

    private Integer facturas;
    
    public TotalParaCobrar() {
    }

    public TotalParaCobrar(String banco) {
        this.banco = banco;
    }

    public TotalParaCobrar(String banco, String fechavencimiento, BigDecimal valortotal, BigDecimal valortotalconrubro, Integer facturas) {
        
        this.banco = banco;
        this.fechavencimiento = fechavencimiento;
        this.valortotal = valortotal;
        this.valortotalconrubro = valortotalconrubro;
        this.facturas = facturas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (banco != null ? banco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TotalParaCobrar)) {
            return false;
        }
        TotalParaCobrar other = (TotalParaCobrar) object;
        if ((this.banco == null && other.banco != null) || (this.banco != null && !this.banco.equals(other.banco))) {
            return false;
        }
        return true;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
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

    public BigDecimal getValortotalconrubro() {
        return valortotalconrubro;
    }

    public void setValortotalconrubro(BigDecimal valortotalconrubro) {
        this.valortotalconrubro = valortotalconrubro;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas(Integer facturas) {
        this.facturas = facturas;
    }
    
    

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.TotalParaCobrar[ banco= " + banco + "F. vencimiento= " + fechavencimiento + " ]";
    }
    
}
