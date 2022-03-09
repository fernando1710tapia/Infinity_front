/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Paul
 */

public class Listaprecioterminalproducto implements Serializable {

    protected ListaprecioterminalproductoPK listaprecioterminalproductoPK;

    private BigDecimal margenporcentaje;

    private BigDecimal margenvalorcomercializadora;

    private String usuarioactual;

    private Comercializadoraproducto comercializadoraproducto;

    private Terminal terminal;

    public Listaprecioterminalproducto() {
    }

    public Listaprecioterminalproducto(ListaprecioterminalproductoPK listaprecioterminalproductoPK) {
        this.listaprecioterminalproductoPK = listaprecioterminalproductoPK;
    }

    public Listaprecioterminalproducto(ListaprecioterminalproductoPK listaprecioterminalproductoPK, String usuarioactual) {
        this.listaprecioterminalproductoPK = listaprecioterminalproductoPK;
        this.usuarioactual = usuarioactual;
    }

    public Listaprecioterminalproducto(String codigocomercializadora, long codigolistaprecio, String codigoterminal, String codigoproducto, String codigomedida) {
        this.listaprecioterminalproductoPK = new ListaprecioterminalproductoPK(codigocomercializadora, codigolistaprecio, codigoterminal, codigoproducto, codigomedida);
    }

    public ListaprecioterminalproductoPK getListaprecioterminalproductoPK() {
        return listaprecioterminalproductoPK;
    }

    public void setListaprecioterminalproductoPK(ListaprecioterminalproductoPK listaprecioterminalproductoPK) {
        this.listaprecioterminalproductoPK = listaprecioterminalproductoPK;
    }

    public BigDecimal getMargenporcentaje() {
        return margenporcentaje;
    }

    public void setMargenporcentaje(BigDecimal margenporcentaje) {
        this.margenporcentaje = margenporcentaje;
    }

    public BigDecimal getMargenvalorcomercializadora() {
        return margenvalorcomercializadora;
    }

    public void setMargenvalorcomercializadora(BigDecimal margenvalorcomercializadora) {
        this.margenvalorcomercializadora = margenvalorcomercializadora;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Comercializadoraproducto getComercializadoraproducto() {
        return comercializadoraproducto;
    }

    public void setComercializadoraproducto(Comercializadoraproducto comercializadoraproducto) {
        this.comercializadoraproducto = comercializadoraproducto;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }    
    
}
