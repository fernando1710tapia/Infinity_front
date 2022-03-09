/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Detalleprecio {

    protected DetalleprecioPK detalleprecioPK;

    private BigDecimal valor;

    private String usuarioactual;

    private Gravamen gravamen;

    private Precio precio;

    public Detalleprecio() {
    }

    public Detalleprecio(DetalleprecioPK detalleprecioPK) {
        this.detalleprecioPK = detalleprecioPK;
    }

    public Detalleprecio(DetalleprecioPK detalleprecioPK, BigDecimal valor, String usuarioactual) {
        this.detalleprecioPK = detalleprecioPK;
        this.valor = valor;
        this.usuarioactual = usuarioactual;
    }

    public Detalleprecio(String codigocomercializadora, String codigoterminal, String codigoproducto, String codigomedida, long codigolistaprecio, Date fechainicio, int secuencial, String codigo, String codigogravamen) {
        this.detalleprecioPK = new DetalleprecioPK(codigocomercializadora, codigoterminal, codigoproducto, codigomedida, codigolistaprecio, fechainicio, secuencial, codigo, codigogravamen);
    }

    public DetalleprecioPK getDetalleprecioPK() {
        return detalleprecioPK;
    }

    public void setDetalleprecioPK(DetalleprecioPK detalleprecioPK) {
        this.detalleprecioPK = detalleprecioPK;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Gravamen getGravamen() {
        return gravamen;
    }

    public void setGravamen(Gravamen gravamen) {
        this.gravamen = gravamen;
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleprecioPK != null ? detalleprecioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalleprecio)) {
            return false;
        }
        Detalleprecio other = (Detalleprecio) object;
        if ((this.detalleprecioPK == null && other.detalleprecioPK != null) || (this.detalleprecioPK != null && !this.detalleprecioPK.equals(other.detalleprecioPK))) {
            return false;
        }
        return true;
    }

}
