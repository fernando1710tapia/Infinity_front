/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
public class ConsultaguiaremisionPK implements Serializable {

    private String codigocomercializadora;

    private String numero;

    private String fecha;

    private Date fecharecepcion;

    public ConsultaguiaremisionPK() {
    }

    public ConsultaguiaremisionPK(String codigocomercializadora, String numero, String fecha, Date fecharecepcion) {
        this.codigocomercializadora = codigocomercializadora;
        this.numero = numero;
        this.fecha = fecha;
        this.fecharecepcion = fecharecepcion;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFecharecepcion() {
        return fecharecepcion;
    }

    public void setFecharecepcion(Date fecharecepcion) {
        this.fecharecepcion = fecharecepcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (numero != null ? numero.hashCode() : 0);
        hash += (fecha != null ? fecha.hashCode() : 0);
        hash += (fecharecepcion != null ? fecharecepcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConsultaguiaremisionPK)) {
            return false;
        }
        ConsultaguiaremisionPK other = (ConsultaguiaremisionPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        if ((this.fecha == null && other.fecha != null) || (this.fecha != null && !this.fecha.equals(other.fecha))) {
            return false;
        }
        if ((this.fecharecepcion == null && other.fecharecepcion != null) || (this.fecharecepcion != null && !this.fecharecepcion.equals(other.fecharecepcion))) {
            return false;
        }
        return true;
    }
    
}
