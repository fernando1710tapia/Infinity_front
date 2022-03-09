/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author HP
 */
public class Gravamen implements Serializable {

    protected GravamenPK gravamenPK;

    private String nombre;

    private Boolean activo;

    private Boolean seimprime;

    private String formulavalor;

    private BigDecimal valordefecto;

    private String usuarioactual;
    
    private int secuencial;

    private List<Detalleprecio> detalleprecioList;

    public Gravamen() {
    }

    public Gravamen(GravamenPK gravamenPK) {
        this.gravamenPK = gravamenPK;
    }

    public Gravamen(GravamenPK gravamenPK, String usuarioactual) {
        this.gravamenPK = gravamenPK;
        this.usuarioactual = usuarioactual;
    }

    public Gravamen(String codigocomercializadora, String codigo) {
        this.gravamenPK = new GravamenPK(codigocomercializadora, codigo);
    }

    public GravamenPK getGravamenPK() {
        return gravamenPK;
    }

    public void setGravamenPK(GravamenPK gravamenPK) {
        this.gravamenPK = gravamenPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getSeimprime() {
        return seimprime;
    }

    public void setSeimprime(Boolean seimprime) {
        this.seimprime = seimprime;
    }

    public String getFormulavalor() {
        return formulavalor;
    }

    public void setFormulavalor(String formulavalor) {
        this.formulavalor = formulavalor;
    }

    public BigDecimal getValordefecto() {
        return valordefecto;
    }

    public void setValordefecto(BigDecimal valordefecto) {
        this.valordefecto = valordefecto;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gravamenPK != null ? gravamenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gravamen)) {
            return false;
        }
        Gravamen other = (Gravamen) object;
        if ((this.gravamenPK == null && other.gravamenPK != null) || (this.gravamenPK != null && !this.gravamenPK.equals(other.gravamenPK))) {
            return false;
        }
        return true;
    }

}
