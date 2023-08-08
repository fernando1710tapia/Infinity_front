/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;

/**
 *
 * @author SonyVaio
 */
@Embeddable
public class FechafestivaPK implements Serializable {
    
    private String codigocomercializadora;
    
    private Date festivo;

    public FechafestivaPK() {
    }

    public FechafestivaPK(String codigocomercializadora, Date festivo) {
        this.codigocomercializadora = codigocomercializadora;
        this.festivo = festivo;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public Date getFestivo() {
        return festivo;
    }

    public void setFestivo(Date festivo) {
        this.festivo = festivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (festivo != null ? festivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechafestivaPK)) {
            return false;
        }
        FechafestivaPK other = (FechafestivaPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if ((this.festivo == null && other.festivo != null) || (this.festivo != null && !this.festivo.equals(other.festivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.FechafestivaPK[ codigocomercializadora=" + codigocomercializadora + ", festivo=" + festivo + " ]";
    }
    
}
