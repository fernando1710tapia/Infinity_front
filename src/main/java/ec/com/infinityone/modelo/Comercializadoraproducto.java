/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Paul
 */

public class Comercializadoraproducto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected ComercializadoraproductoPK comercializadoraproductoPK;
    
    private boolean activo;
    
    private BigDecimal margencomercializacion;
    
    private BigDecimal precioepp;
    
    private BigDecimal pvpsugerido;
    
    private boolean soloaplicaiva;
    
    private String usuarioactual;
    
    private Medida medida;
    
    private Producto producto;
    
    private boolean procesar;
    
    //private BigDecimal margenValorAux;
    
    private List<Precio> precioList;
       

    public Comercializadoraproducto() {
    }

    public Comercializadoraproducto(ComercializadoraproductoPK comercializadoraproductoPK) {
        this.comercializadoraproductoPK = comercializadoraproductoPK;
    }

    public Comercializadoraproducto(ComercializadoraproductoPK comercializadoraproductoPK, boolean activo, BigDecimal margencomercializacion, BigDecimal precioepp, BigDecimal pvpsugerido, boolean soloaplicaiva, String usuarioactual) {
        this.comercializadoraproductoPK = comercializadoraproductoPK;
        this.activo = activo;
        this.margencomercializacion = margencomercializacion;
        this.precioepp = precioepp;
        this.pvpsugerido = pvpsugerido;
        this.soloaplicaiva = soloaplicaiva;
        this.usuarioactual = usuarioactual;
    }

    public Comercializadoraproducto(String codigocomercializadora, String codigoproducto, String codigomedida) {
        this.comercializadoraproductoPK = new ComercializadoraproductoPK(codigocomercializadora, codigoproducto, codigomedida);
    }

    public ComercializadoraproductoPK getComercializadoraproductoPK() {
        return comercializadoraproductoPK;
    }

    public void setComercializadoraproductoPK(ComercializadoraproductoPK comercializadoraproductoPK) {
        this.comercializadoraproductoPK = comercializadoraproductoPK;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getMargencomercializacion() {
        return margencomercializacion;
    }

    public void setMargencomercializacion(BigDecimal margencomercializacion) {
        this.margencomercializacion = margencomercializacion;
    }

    public BigDecimal getPrecioepp() {
        return precioepp;
    }

    public void setPrecioepp(BigDecimal precioepp) {
        this.precioepp = precioepp;
    }

    public BigDecimal getPvpsugerido() {
        return pvpsugerido;
    }

    public void setPvpsugerido(BigDecimal pvpsugerido) {
        this.pvpsugerido = pvpsugerido;
    }

    public boolean getSoloaplicaiva() {
        return soloaplicaiva;
    }

    public void setSoloaplicaiva(boolean soloaplicaiva) {
        this.soloaplicaiva = soloaplicaiva;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }
    
    public List<Precio> getPrecioList() {
        return precioList;
    }

    public void setPrecioList(List<Precio> precioList) {
        this.precioList = precioList;
    }

//    public BigDecimal getMargenValorAux() {
//        return margenValorAux;
//    }
//
//    public void setMargenValorAux(BigDecimal margenValorAux) {
//        this.margenValorAux = margenValorAux;
//    }        

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public boolean isProcesar() {
        return procesar;
    }

    public void setProcesar(boolean procesar) {
        this.procesar = procesar;
    }
    
    
    
}
