/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class DetallefacturaPK implements Serializable{
    private String codigoabastecedora;

    private String codigocomercializadora;

    private String numeronotapedido;

    private String numero;

    private String codigoproducto;
    
     public DetallefacturaPK() {
    }

    public DetallefacturaPK(String codigoabastecedora, String codigocomercializadora, String numeronotapedido, String numero, String codigoproducto) {
        this.codigoabastecedora = codigoabastecedora;
        this.codigocomercializadora = codigocomercializadora;
        this.numeronotapedido = numeronotapedido;
        this.numero = numero;
        this.codigoproducto = codigoproducto;
    }

    public String getCodigoabastecedora() {
        return codigoabastecedora;
    }

    public void setCodigoabastecedora(String codigoabastecedora) {
        this.codigoabastecedora = codigoabastecedora;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getNumeronotapedido() {
        return numeronotapedido;
    }

    public void setNumeronotapedido(String numeronotapedido) {
        this.numeronotapedido = numeronotapedido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoabastecedora != null ? codigoabastecedora.hashCode() : 0);
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (numeronotapedido != null ? numeronotapedido.hashCode() : 0);
        hash += (numero != null ? numero.hashCode() : 0);
        hash += (codigoproducto != null ? codigoproducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallefacturaPK)) {
            return false;
        }
        DetallefacturaPK other = (DetallefacturaPK) object;
        if ((this.codigoabastecedora == null && other.codigoabastecedora != null) || (this.codigoabastecedora != null && !this.codigoabastecedora.equals(other.codigoabastecedora))) {
            return false;
        }
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if ((this.numeronotapedido == null && other.numeronotapedido != null) || (this.numeronotapedido != null && !this.numeronotapedido.equals(other.numeronotapedido))) {
            return false;
        }
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        if ((this.codigoproducto == null && other.codigoproducto != null) || (this.codigoproducto != null && !this.codigoproducto.equals(other.codigoproducto))) {
            return false;
        }
        return true;
    }
    
}
