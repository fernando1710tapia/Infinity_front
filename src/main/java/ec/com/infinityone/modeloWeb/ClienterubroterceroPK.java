/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;

/**
 *
 * @author SonyVaio
 */

public class ClienterubroterceroPK implements Serializable {
    
    private String codigocomercializadora;
    
    private long codigo;
    
    private String codigocliente;

    public ClienterubroterceroPK() {
    }

    public ClienterubroterceroPK(String codigocomercializadora, long codigo, String codigocliente) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigo = codigo;
        this.codigocliente = codigocliente;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
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
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (int) codigo;
        hash += (codigocliente != null ? codigocliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienterubroterceroPK)) {
            return false;
        }
        ClienterubroterceroPK other = (ClienterubroterceroPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if (this.codigo != other.codigo) {
            return false;
        }
        if ((this.codigocliente == null && other.codigocliente != null) || (this.codigocliente != null && !this.codigocliente.equals(other.codigocliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.ClienterubroterceroPK[ codigocomercializadora=" + codigocomercializadora + ", codigo=" + codigo + ", codigocliente=" + codigocliente + " ]";
    }
    
}