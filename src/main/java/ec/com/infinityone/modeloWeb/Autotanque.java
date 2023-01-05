/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author HP
 */
public class Autotanque implements Serializable {

    private String placa;

    private BigDecimal volumentotal;

    private BigDecimal compartimento1;

    private BigDecimal compartimento2;

    private BigDecimal compartimento3;
    
    private BigDecimal compartimento4;

    private BigDecimal compartimento5;

    private BigDecimal compartimento6;

    private BigDecimal compartimento7;

    private BigDecimal compartimento8;

    private BigDecimal compartimento9;

    private BigDecimal compartimento10;

    private boolean activo;

    private String usuarioactual;

    private Conductor cedularuc;

    private int compartimentos;

    public Autotanque() {
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public BigDecimal getVolumentotal() {
        return volumentotal;
    }

    public void setVolumentotal(BigDecimal volumentotal) {
        this.volumentotal = volumentotal;
    }

    public BigDecimal getCompartimento1() {
        return compartimento1;
    }

    public void setCompartimento1(BigDecimal compartimento1) {
        this.compartimento1 = compartimento1;
    }

    public BigDecimal getCompartimento2() {
        return compartimento2;
    }

    public void setCompartimento2(BigDecimal compartimento2) {
        this.compartimento2 = compartimento2;
    }

    public BigDecimal getCompartimento3() {
        return compartimento3;
    }

    public void setCompartimento3(BigDecimal compartimento3) {
        this.compartimento3 = compartimento3;
    }

    public BigDecimal getCompartimento4() {
        return compartimento4;
    }

    public void setCompartimento4(BigDecimal compartimento4) {
        this.compartimento4 = compartimento4;
    }

    public BigDecimal getCompartimento5() {
        return compartimento5;
    }

    public void setCompartimento5(BigDecimal compartimento5) {
        this.compartimento5 = compartimento5;
    }

    public BigDecimal getCompartimento6() {
        return compartimento6;
    }

    public void setCompartimento6(BigDecimal compartimento6) {
        this.compartimento6 = compartimento6;
    }

    public BigDecimal getCompartimento7() {
        return compartimento7;
    }

    public void setCompartimento7(BigDecimal compartimento7) {
        this.compartimento7 = compartimento7;
    }

    public BigDecimal getCompartimento8() {
        return compartimento8;
    }

    public void setCompartimento8(BigDecimal compartimento8) {
        this.compartimento8 = compartimento8;
    }

    public BigDecimal getCompartimento9() {
        return compartimento9;
    }

    public void setCompartimento9(BigDecimal compartimento9) {
        this.compartimento9 = compartimento9;
    }

    public BigDecimal getCompartimento10() {
        return compartimento10;
    }

    public void setCompartimento10(BigDecimal compartimento10) {
        this.compartimento10 = compartimento10;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Conductor getCedularuc() {
        return cedularuc;
    }

    public void setCedularuc(Conductor cedularuc) {
        this.cedularuc = cedularuc;
    }

    public int getCompartimentos() {
        return compartimentos;
    }

    public void setCompartimentos(int compartimentos) {
        this.compartimentos = compartimentos;
    }
    
}
