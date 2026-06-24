/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Temporalparacobrar implements Serializable{
    private static final long serialVersionUID = 1L;

    protected TemporalparacobrarPK temporalparacobrarPK;

    private String codigobanco;

    private String fechaventa;

    private String fechavencimiento;

    private BigDecimal valortotal;

    private BigDecimal valorconrubro;

    private String codigocliente;
    
    public Temporalparacobrar() {
    }

    public Temporalparacobrar(TemporalparacobrarPK temporalparacobrarPK) {
        this.temporalparacobrarPK = temporalparacobrarPK;
    }

    public Temporalparacobrar(String fechahoraproceso, String usuarioactual, String codigocomercializadora, String numerofactura) {
        this.temporalparacobrarPK = new TemporalparacobrarPK(fechahoraproceso, usuarioactual, codigocomercializadora, numerofactura);
    }

    public TemporalparacobrarPK getTemporalparacobrarPK() {
        return temporalparacobrarPK;
    }

    public void setTemporalparacobrarPK(TemporalparacobrarPK temporalparacobrarPK) {
        this.temporalparacobrarPK = temporalparacobrarPK;
    }

    public String getCodigobanco() {
        return codigobanco;
    }

    public void setCodigobanco(String codigobanco) {
        this.codigobanco = codigobanco;
    }

    public String getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(String fechaventa) {
        this.fechaventa = fechaventa;
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

    public BigDecimal getValorconrubro() {
        return valorconrubro;
    }

    public void setValorconrubro(BigDecimal valorconrubro) {
        this.valorconrubro = valorconrubro;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (temporalparacobrarPK != null ? temporalparacobrarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Temporalparacobrar)) {
            return false;
        }
        Temporalparacobrar other = (Temporalparacobrar) object;
        if ((this.temporalparacobrarPK == null && other.temporalparacobrarPK != null) || (this.temporalparacobrarPK != null && !this.temporalparacobrarPK.equals(other.temporalparacobrarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Temporalparacobrar[ temporalparacobrarPK=" + temporalparacobrarPK + " ]";
    }
}
