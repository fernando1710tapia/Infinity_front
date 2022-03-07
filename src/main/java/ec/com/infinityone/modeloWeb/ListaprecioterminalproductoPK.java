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
 * @author Paul
 */
@Embeddable
public class ListaprecioterminalproductoPK implements Serializable {
    
    private String codigocomercializadora;
    
    private long codigolistaprecio;
    
    private String codigoterminal;
    
    private String codigoproducto;
    
    private String codigomedida;

    public ListaprecioterminalproductoPK() {
    }

    public ListaprecioterminalproductoPK(String codigocomercializadora, long codigolistaprecio, String codigoterminal, String codigoproducto, String codigomedida) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigolistaprecio = codigolistaprecio;
        this.codigoterminal = codigoterminal;
        this.codigoproducto = codigoproducto;
        this.codigomedida = codigomedida;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public long getCodigolistaprecio() {
        return codigolistaprecio;
    }

    public void setCodigolistaprecio(long codigolistaprecio) {
        this.codigolistaprecio = codigolistaprecio;
    }

    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public String getCodigomedida() {
        return codigomedida;
    }

    public void setCodigomedida(String codigomedida) {
        this.codigomedida = codigomedida;
    }
    
}
