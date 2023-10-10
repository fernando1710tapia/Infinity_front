/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;

/**
 *
 * @author SonyVaio
 */
public class FacturaComercialDto implements Serializable {

    private FacturaComercialPKDto facturaComercialPKDto;

    private String dcodigocomercializadora;

    private String dcodigobanco;

    private String dtiporegistro;

    private String dnumeroorden;

    private String dcodigocliente;

    private String dcodigoestablecimiento;

    private String dcodigoproducto;

    private String dcantidad;

    private String dunidadmedida;

    private String dpreciounitario;

    private String dtotal;

    private String dtiporubro;

    public FacturaComercialDto() {
    }

    public FacturaComercialPKDto getFacturaComercialPKDto() {
        return facturaComercialPKDto;
    }

    public void setFacturaComercialPKDto(FacturaComercialPKDto facturaComercialPKDto) {
        this.facturaComercialPKDto = facturaComercialPKDto;
    }

    public String getDcodigocomercializadora() {
        return dcodigocomercializadora;
    }

    public void setDcodigocomercializadora(String dcodigocomercializadora) {
        this.dcodigocomercializadora = dcodigocomercializadora;
    }

    public String getDcodigobanco() {
        return dcodigobanco;
    }

    public void setDcodigobanco(String dcodigobanco) {
        this.dcodigobanco = dcodigobanco;
    }

    public String getDtiporegistro() {
        return dtiporegistro;
    }

    public void setDtiporegistro(String dtiporegistro) {
        this.dtiporegistro = dtiporegistro;
    }

    public String getDnumeroorden() {
        return dnumeroorden;
    }

    public void setDnumeroorden(String dnumeroorden) {
        this.dnumeroorden = dnumeroorden;
    }

    public String getDcodigocliente() {
        return dcodigocliente;
    }

    public void setDcodigocliente(String dcodigocliente) {
        this.dcodigocliente = dcodigocliente;
    }

    public String getDcodigoestablecimiento() {
        return dcodigoestablecimiento;
    }

    public void setDcodigoestablecimiento(String dcodigoestablecimiento) {
        this.dcodigoestablecimiento = dcodigoestablecimiento;
    }

    public String getDcodigoproducto() {
        return dcodigoproducto;
    }

    public void setDcodigoproducto(String dcodigoproducto) {
        this.dcodigoproducto = dcodigoproducto;
    }

    public String getDcantidad() {
        return dcantidad;
    }

    public void setDcantidad(String dcantidad) {
        this.dcantidad = dcantidad;
    }

    public String getDunidadmedida() {
        return dunidadmedida;
    }

    public void setDunidadmedida(String dunidadmedida) {
        this.dunidadmedida = dunidadmedida;
    }

    public String getDpreciounitario() {
        return dpreciounitario;
    }

    public void setDpreciounitario(String dpreciounitario) {
        this.dpreciounitario = dpreciounitario;
    }

    public String getDtotal() {
        return dtotal;
    }

    public void setDtotal(String dtotal) {
        this.dtotal = dtotal;
    }

    public String getDtiporubro() {
        return dtiporubro;
    }

    public void setDtiporubro(String dtiporubro) {
        this.dtiporubro = dtiporubro;
    }
    
}
