/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SonyVaio
 */
@Embeddable
public class CuotarubrotercerosPK implements Serializable {
    
    private String codigocomercializadora;
    
    private long codigorubrotercero;
    
    private String codigocliente;
    
    private int cuota;

    public CuotarubrotercerosPK() {
    }

    public CuotarubrotercerosPK(String codigocomercializadora, long codigorubrotercero, String codigocliente, int cuota) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigorubrotercero = codigorubrotercero;
        this.codigocliente = codigocliente;
        this.cuota = cuota;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public long getCodigorubrotercero() {
        return codigorubrotercero;
    }

    public void setCodigorubrotercero(long codigorubrotercero) {
        this.codigorubrotercero = codigorubrotercero;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (int) codigorubrotercero;
        hash += (codigocliente != null ? codigocliente.hashCode() : 0);
        hash += (int) cuota;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuotarubrotercerosPK)) {
            return false;
        }
        CuotarubrotercerosPK other = (CuotarubrotercerosPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if (this.codigorubrotercero != other.codigorubrotercero) {
            return false;
        }
        if ((this.codigocliente == null && other.codigocliente != null) || (this.codigocliente != null && !this.codigocliente.equals(other.codigocliente))) {
            return false;
        }
        if (this.cuota != other.cuota) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.CuotarubrotercerosPK[ codigocomercializadora=" + codigocomercializadora + ", codigorubrotercero=" + codigorubrotercero + ", codigocliente=" + codigocliente + ", cuota=" + cuota + " ]";
    }
    
}
