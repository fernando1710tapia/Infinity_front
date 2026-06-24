/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author HP
 */
public class ObjetoDetallePrecioAux implements Serializable {

    private BigDecimal pvpsugerido;

    private BigDecimal precioepp;

    private BigDecimal precioTerminalEpp;

    private BigDecimal iva;

    private BigDecimal margenxcliente;

    private BigDecimal ivaPresuntivo;

    private BigDecimal tresPorMil;

    private BigDecimal precioProducto;

    private String codigoTerminal;

    private String codigoProd;

    private String codigoMedida;

    private String codigoLisPrecio;

    public ObjetoDetallePrecioAux() {
    }

    public ObjetoDetallePrecioAux(BigDecimal pvpsugerido, BigDecimal precioepp, BigDecimal precioTerminalEpp, BigDecimal iva, BigDecimal margenxcliente, BigDecimal ivaPresuntivo, BigDecimal tresPorMil, BigDecimal precioProducto) {
        this.pvpsugerido = pvpsugerido;
        this.precioepp = precioepp;
        this.precioTerminalEpp = precioTerminalEpp;
        this.iva = iva;
        this.margenxcliente = margenxcliente;
        this.ivaPresuntivo = ivaPresuntivo;
        this.tresPorMil = tresPorMil;
        this.precioProducto = precioProducto;
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

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getMargenxcliente() {
        return margenxcliente;
    }

    public void setMargenxcliente(BigDecimal margenxcliente) {
        this.margenxcliente = margenxcliente;
    }

    public BigDecimal getIvaPresuntivo() {
        return ivaPresuntivo;
    }

    public void setIvaPresuntivo(BigDecimal ivaPresuntivo) {
        this.ivaPresuntivo = ivaPresuntivo;
    }

    public BigDecimal getTresPorMil() {
        return tresPorMil;
    }

    public void setTresPorMil(BigDecimal tresPorMil) {
        this.tresPorMil = tresPorMil;
    }

    public BigDecimal getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(BigDecimal precioProducto) {
        this.precioProducto = precioProducto;
    }

    public BigDecimal getPrecioTerminalEpp() {
        return precioTerminalEpp;
    }

    public void setPrecioTerminalEpp(BigDecimal precioTerminalEpp) {
        this.precioTerminalEpp = precioTerminalEpp;
    }

    public String getCodigoTerminal() {
        return codigoTerminal;
    }

    public void setCodigoTerminal(String codigoTerminal) {
        this.codigoTerminal = codigoTerminal;
    }

    public String getCodigoProd() {
        return codigoProd;
    }

    public void setCodigoProd(String codigoProd) {
        this.codigoProd = codigoProd;
    }

    public String getCodigoMedida() {
        return codigoMedida;
    }

    public void setCodigoMedida(String codigoMedida) {
        this.codigoMedida = codigoMedida;
    }

    public String getCodigoLisPrecio() {
        return codigoLisPrecio;
    }

    public void setCodigoLisPrecio(String codigoLisPrecio) {
        this.codigoLisPrecio = codigoLisPrecio;
    }

}
