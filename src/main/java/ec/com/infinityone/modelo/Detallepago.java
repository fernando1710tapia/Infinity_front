/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

    private String constanteCabe_10;
    private String motivo_bancario;
    private String constanteCabe_2;
    private String fechaCrea;
    //private String fechaIniProc;
    private String cantItemsCarga;
    private String cantItemsOk;
    private String valItemsOk;
    private String valTotRetRentBienes;
    private String valTotRetRentServicios;
    private String valTotRetIvaBienes;
    private String valTotRetIvaServicios;
    private String constanteCabe_6;

    private String constanteDet_2;
    //private String tipCuenta;
    //private String numCuenta;
    //private String valProcesado;
    private String motBancario;
    private String constanteDet_3;
    //private String contrapartida;
    //private String codProceso;
    private String valRetRentBienes;
    private String valRetRentServicios;
    private String valRetIvaBienes;
    private String valRetIvaServicios;   

    private Date fechaventa;
    private Date fechaacreditacionprorrogada;
    private BigDecimal valorconrubros;
    private BigDecimal tasainteres;
    private int diasretraso;
    private BigDecimal valormora;
    private String claveacceso;

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

    public String getConstanteCabe_10() {
        return constanteCabe_10;
    }

    public void setConstanteCabe_10(String constanteCabe_10) {
        this.constanteCabe_10 = constanteCabe_10;
    }

    public String getMotivo_bancario() {
        return motivo_bancario;
    }

    public void setMotivo_bancario(String motivo_bancario) {
        this.motivo_bancario = motivo_bancario;
    }

    public String getConstanteCabe_2() {
        return constanteCabe_2;
    }

    public void setConstanteCabe_2(String constanteCabe_2) {
        this.constanteCabe_2 = constanteCabe_2;
    }

    public String getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(String fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    public String getCantItemsCarga() {
        return cantItemsCarga;
    }

    public void setCantItemsCarga(String cantItemsCarga) {
        this.cantItemsCarga = cantItemsCarga;
    }

    public String getCantItemsOk() {
        return cantItemsOk;
    }

    public void setCantItemsOk(String cantItemsOk) {
        this.cantItemsOk = cantItemsOk;
    }

    public String getValItemsOk() {
        return valItemsOk;
    }

    public void setValItemsOk(String valItemsOk) {
        this.valItemsOk = valItemsOk;
    }

    public String getValTotRetRentBienes() {
        return valTotRetRentBienes;
    }

    public void setValTotRetRentBienes(String valTotRetRentBienes) {
        this.valTotRetRentBienes = valTotRetRentBienes;
    }

    public String getValTotRetRentServicios() {
        return valTotRetRentServicios;
    }

    public void setValTotRetRentServicios(String valTotRetRentServicios) {
        this.valTotRetRentServicios = valTotRetRentServicios;
    }

    public String getValTotRetIvaBienes() {
        return valTotRetIvaBienes;
    }

    public void setValTotRetIvaBienes(String valTotRetIvaBienes) {
        this.valTotRetIvaBienes = valTotRetIvaBienes;
    }

    public String getValTotRetIvaServicios() {
        return valTotRetIvaServicios;
    }

    public void setValTotRetIvaServicios(String valTotRetIvaServicios) {
        this.valTotRetIvaServicios = valTotRetIvaServicios;
    }

    public String getConstanteCabe_6() {
        return constanteCabe_6;
    }

    public void setConstanteCabe_6(String constanteCabe_6) {
        this.constanteCabe_6 = constanteCabe_6;
    }

    public String getConstanteDet_2() {
        return constanteDet_2;
    }

    public void setConstanteDet_2(String constanteDet_2) {
        this.constanteDet_2 = constanteDet_2;
    }

    public String getMotBancario() {
        return motBancario;
    }

    public void setMotBancario(String motBancario) {
        this.motBancario = motBancario;
    }

    public String getConstanteDet_3() {
        return constanteDet_3;
    }

    public void setConstanteDet_3(String constanteDet_3) {
        this.constanteDet_3 = constanteDet_3;
    }

    public String getValRetRentBienes() {
        return valRetRentBienes;
    }

    public void setValRetRentBienes(String valRetRentBienes) {
        this.valRetRentBienes = valRetRentBienes;
    }

    public String getValRetRentServicios() {
        return valRetRentServicios;
    }

    public void setValRetRentServicios(String valRetRentServicios) {
        this.valRetRentServicios = valRetRentServicios;
    }

    public String getValRetIvaBienes() {
        return valRetIvaBienes;
    }

    public void setValRetIvaBienes(String valRetIvaBienes) {
        this.valRetIvaBienes = valRetIvaBienes;
    }

    public String getValRetIvaServicios() {
        return valRetIvaServicios;
    }

    public void setValRetIvaServicios(String valRetIvaServicios) {
        this.valRetIvaServicios = valRetIvaServicios;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public Date getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(Date fechaventa) {
        this.fechaventa = fechaventa;
    }

    public Date getFechaacreditacionprorrogada() {
        return fechaacreditacionprorrogada;
    }

    public void setFechaacreditacionprorrogada(Date fechaacreditacionprorrogada) {
        this.fechaacreditacionprorrogada = fechaacreditacionprorrogada;
    }

    public BigDecimal getValorconrubros() {
        return valorconrubros;
    }

    public void setValorconrubros(BigDecimal valorconrubros) {
        this.valorconrubros = valorconrubros;
    }

    public BigDecimal getTasainteres() {
        return tasainteres;
    }

    public void setTasainteres(BigDecimal tasainteres) {
        this.tasainteres = tasainteres;
    }

    public int getDiasretraso() {
        return diasretraso;
    }

    public void setDiasretraso(int diasretraso) {
        this.diasretraso = diasretraso;
    }

    public BigDecimal getValormora() {
        return valormora;
    }

    public void setValormora(BigDecimal valormora) {
        this.valormora = valormora;
    }

}
