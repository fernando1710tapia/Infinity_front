/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author HP
 */
public class ObjetoPrecio implements Serializable{
    
    private Precio precio;
    
    private BigDecimal pvpsugerido;
    
    private BigDecimal precioepp;
    
    private BigDecimal margenporcentaje;

    private BigDecimal margenvalorcomercializadora;

    public ObjetoPrecio() {
    }

    public ObjetoPrecio(Precio precio, BigDecimal pvpsugerido, BigDecimal precioepp, BigDecimal margenporcentaje, BigDecimal margenvalorcomercializadora) {
        this.precio = precio;
        this.pvpsugerido = pvpsugerido;
        this.precioepp = precioepp;
        this.margenporcentaje = margenporcentaje;
        this.margenvalorcomercializadora = margenvalorcomercializadora;
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
    
    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }

    public BigDecimal getPvpsugerido() {
        return pvpsugerido;
    }

    public void setPvpsugerido(BigDecimal pvpsugerido) {
        this.pvpsugerido = pvpsugerido;
    }

    public BigDecimal getPrecioepp() {
        return precioepp;
    }

    public void setPrecioepp(BigDecimal precioepp) {
        this.precioepp = precioepp;
    }
    
    
    
}
