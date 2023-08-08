/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author SonyVaio
 */
@Embeddable
public class ClientegarantiaPK implements Serializable {
    
    private String codigocomercializadora;
    
    private String codigocliente;
    
    private String codigobanco;
    
    private String numero;
    
    private int secuencial;

    public ClientegarantiaPK() {
    }

    public ClientegarantiaPK(String codigocomercializadora, String codigocliente, String codigobanco, String numero, int secuencial) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigocliente = codigocliente;
        this.codigobanco = codigobanco;
        this.numero = numero;
        this.secuencial = secuencial;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public String getCodigobanco() {
        return codigobanco;
    }

    public void setCodigobanco(String codigobanco) {
        this.codigobanco = codigobanco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (codigocliente != null ? codigocliente.hashCode() : 0);
        hash += (codigobanco != null ? codigobanco.hashCode() : 0);
        hash += (numero != null ? numero.hashCode() : 0);
        hash += (int) secuencial;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientegarantiaPK)) {
            return false;
        }
        ClientegarantiaPK other = (ClientegarantiaPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if ((this.codigocliente == null && other.codigocliente != null) || (this.codigocliente != null && !this.codigocliente.equals(other.codigocliente))) {
            return false;
        }
        if ((this.codigobanco == null && other.codigobanco != null) || (this.codigobanco != null && !this.codigobanco.equals(other.codigobanco))) {
            return false;
        }
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        if (this.secuencial != other.secuencial) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.ClientegarantiaPK[ codigocomercializadora=" + codigocomercializadora + ", codigocliente=" + codigocliente + ", codigobanco=" + codigobanco + ", numero=" + numero + ", secuencial=" + secuencial + " ]";
    }
    
}
