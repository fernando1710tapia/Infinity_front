/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Fernando Tapia
 */

public class Cuotarubroterceros implements Serializable {

    protected CuotarubrotercerosPK cuotarubrotercerosPK;

    private boolean pagada;

    private Date fechacobro;

    private BigDecimal valor;

    private String usuarioactual;

    private String tipocobro;   

    private Date fechainiciocobro;
    
    private Clienterubrotercero clienterubrotercero;

    public Cuotarubroterceros() {
    }

    public Cuotarubroterceros(CuotarubrotercerosPK cuotarubrotercerosPK) {
        this.cuotarubrotercerosPK = cuotarubrotercerosPK;
    }

    public Cuotarubroterceros(CuotarubrotercerosPK cuotarubrotercerosPK, boolean pagada, Date fechacobro, BigDecimal valor) {
        this.cuotarubrotercerosPK = cuotarubrotercerosPK;
        this.pagada = pagada;
        this.fechacobro = fechacobro;
        this.valor = valor;
    }

    public Cuotarubroterceros(String codigocomercializadora, long codigorubrotercero, String codigocliente, int cuota) {
        this.cuotarubrotercerosPK = new CuotarubrotercerosPK(codigocomercializadora, codigorubrotercero, codigocliente, cuota);
    }

    public CuotarubrotercerosPK getCuotarubrotercerosPK() {
        return cuotarubrotercerosPK;
    }

    public void setCuotarubrotercerosPK(CuotarubrotercerosPK cuotarubrotercerosPK) {
        this.cuotarubrotercerosPK = cuotarubrotercerosPK;
    }

    public boolean getPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    public Date getFechacobro() {
        return fechacobro;
    }

    public void setFechacobro(Date fechacobro) {
        this.fechacobro = fechacobro;
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

    public Clienterubrotercero getClienterubrotercero() {
        return clienterubrotercero;
    }

    public void setClienterubrotercero(Clienterubrotercero clienterubrotercero) {
        this.clienterubrotercero = clienterubrotercero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuotarubrotercerosPK != null ? cuotarubrotercerosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuotarubroterceros)) {
            return false;
        }
        Cuotarubroterceros other = (Cuotarubroterceros) object;
        if ((this.cuotarubrotercerosPK == null && other.cuotarubrotercerosPK != null) || (this.cuotarubrotercerosPK != null && !this.cuotarubrotercerosPK.equals(other.cuotarubrotercerosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Cuotarubroterceros[ cuotarubrotercerosPK=" + cuotarubrotercerosPK + " ]";
    }

    public String getTipocobro() {
        return tipocobro;
    }

    public void setTipocobro(String tipocobro) {
        this.tipocobro = tipocobro;
    }

    public Date getFechainiciocobro() {
        return fechainiciocobro;
    }

    public void setFechainiciocobro(Date fechainiciocobro) {
        this.fechainiciocobro = fechainiciocobro;
    }
    
    
    
}
