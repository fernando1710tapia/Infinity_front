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
 * @author Paul
 */

public class Detallepago implements Serializable {
   
    protected DetallepagoPK detallepagoPK;
  
    private BigDecimal valor;
    
    private boolean activo;
    
    private String usuarioactual;
    
    private Factura factura;
    
    private Pagofactura pagofactura;
    
    private String activoS;

    private String id_sobre;
    private String id_item;
    private String contrapartida;
    private String moneda;
    private String valorEnviado;
    private String valorProcesado;
    private String formPago;
    private String codBancoProc;
    private String tipCuenta;
    private String numCuenta;
    private String tipIdCliente;
    private String numIdCliente;
    private String nomBeneficiario;
    private String referencia;
    private String fechaProc;
    private String horaProc;
    private String condProc;
    private String mensProc;
    private String numDocu;
    private String canal;
    private String numSRI;

    public Detallepago() {
    }

    public Detallepago(DetallepagoPK detallepagoPK) {
        this.detallepagoPK = detallepagoPK;
    }

    public Detallepago(DetallepagoPK detallepagoPK, BigDecimal valor, boolean activo, String usuarioactual) {
        this.detallepagoPK = detallepagoPK;
        this.valor = valor;
        this.activo = activo;
        this.usuarioactual = usuarioactual;
    }

    public Detallepago(String codigoabastecedora, String codigocomercializadora, String numeronotapedido, String numero, String codigobanco, String numerofactura) {
        this.detallepagoPK = new DetallepagoPK(codigoabastecedora, codigocomercializadora, numeronotapedido, numero, codigobanco, numerofactura);
    }

    public DetallepagoPK getDetallepagoPK() {
        return detallepagoPK;
    }

    public void setDetallepagoPK(DetallepagoPK detallepagoPK) {
        this.detallepagoPK = detallepagoPK;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean getActivo() {
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

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Pagofactura getPagofactura() {
        return pagofactura;
    }

    public void setPagofactura(Pagofactura pagofactura) {
        this.pagofactura = pagofactura;
    }

    public String getActivoS() {
        return activoS;
    }

    public void setActivoS(String activoS) {
        this.activoS = activoS;
    }

    public String getId_sobre() {
        return id_sobre;
    }

    public void setId_sobre(String id_sobre) {
        this.id_sobre = id_sobre;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getContrapartida() {
        return contrapartida;
    }

    public void setContrapartida(String contrapartida) {
        this.contrapartida = contrapartida;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getValorEnviado() {
        return valorEnviado;
    }

    public void setValorEnviado(String valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getValorProcesado() {
        return valorProcesado;
    }

    public void setValorProcesado(String valorProcesado) {
        this.valorProcesado = valorProcesado;
    }

    public String getFormPago() {
        return formPago;
    }

    public void setFormPago(String formPago) {
        this.formPago = formPago;
    }

    public String getCodBancoProc() {
        return codBancoProc;
    }

    public void setCodBancoProc(String codBancoProc) {
        this.codBancoProc = codBancoProc;
    }

    public String getTipCuenta() {
        return tipCuenta;
    }

    public void setTipCuenta(String tipCuenta) {
        this.tipCuenta = tipCuenta;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public String getTipIdCliente() {
        return tipIdCliente;
    }

    public void setTipIdCliente(String tipIdCliente) {
        this.tipIdCliente = tipIdCliente;
    }

    public String getNumIdCliente() {
        return numIdCliente;
    }

    public void setNumIdCliente(String numIdCliente) {
        this.numIdCliente = numIdCliente;
    }

    public String getNomBeneficiario() {
        return nomBeneficiario;
    }

    public void setNomBeneficiario(String nomBeneficiario) {
        this.nomBeneficiario = nomBeneficiario;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFechaProc() {
        return fechaProc;
    }

    public void setFechaProc(String fechaProc) {
        this.fechaProc = fechaProc;
    }

    public String getHoraProc() {
        return horaProc;
    }

    public void setHoraProc(String horaProc) {
        this.horaProc = horaProc;
    }

    public String getCondProc() {
        return condProc;
    }

    public void setCondProc(String condProc) {
        this.condProc = condProc;
    }

    public String getMensProc() {
        return mensProc;
    }

    public void setMensProc(String mensProc) {
        this.mensProc = mensProc;
    }

    public String getNumDocu() {
        return numDocu;
    }

    public void setNumDocu(String numDocu) {
        this.numDocu = numDocu;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getNumSRI() {
        return numSRI;
    }

    public void setNumSRI(String numSRI) {
        this.numSRI = numSRI;
    }

}
