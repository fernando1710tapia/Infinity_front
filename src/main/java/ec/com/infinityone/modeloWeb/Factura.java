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
public class Factura implements Serializable{

    private String fechaventa;

    private String fechavencimiento;

    private String fechaacreditacion;

    private String fechadespacho;

    private boolean activa;

    private BigDecimal valortotal;
    
    private BigDecimal valorconrubro;

    private BigDecimal ivatotal;

    private String observacion;

    private boolean pagada;

    private boolean oeenpetro;

    private String codigocliente;

    private String codigoterminal;

    private String codigobanco;

    private String usuarioactual;

    private String nombrecomercializadora;

    private String ruccomercializadora;

    private String direccionmatrizcomercializadora;

    private String nombrecliente;

    private String ruccliente;

    private BigDecimal valorsinimpuestos;

    private String correocliente;

    private String direccioncliente;

    private String telefonocliente;

    private String numeroautorizacion;

    private String fechaautorizacion;

    private String clienteformapago;

    private Integer plazocliente;

    private String claveacceso;

    private String campoadicionalCampo1;

    private String campoadicionalCampo2;

    private String campoadicionalCampo3;

    private String campoadicionalCampo4;

    private String campoadicionalCampo5;

    private String campoadicionalCampo6;

    private String estado;

    private Short errordocumento;

    private Short hospedado;

    private Character ambientesri;

    private Character tipoemision;

    private String codigodocumento;

    private Boolean esagenteretencion;

    private String escontribuyenteespacial;

    private String obligadocontabilidad;

    private String tipocomprador;

    private String moneda;

    private String seriesri;

    protected FacturaPK facturaPK;
    
    private Boolean adelantar;
    
    private String tipoplazocredito;
    
    private Boolean oeanuladaenpetro;
    
    private Boolean refacturada;
    
    private Boolean reliquidada;
    
    private boolean seleccionar;

    private String fechaacreditacionprorrogada;

    private String clienteformapagonosri;

    private Boolean despachada;

    private Boolean enviadaxcobrar;

    public Factura() {
    }
    
    public Factura(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }
    
    public Factura(FacturaPK facturaPK, String fechaventa, String fechavencimiento, String fechaacreditacion, String fechadespacho, boolean activa, BigDecimal valortotal, BigDecimal valorconrubro, BigDecimal ivatotal, boolean pagada, boolean oeenpetro, String codigocliente, String codigoterminal, String codigobanco, String usuarioactual) {
        this.facturaPK = facturaPK;
        this.fechaventa = fechaventa;
        this.fechavencimiento = fechavencimiento;
        this.fechaacreditacion = fechaacreditacion;
        this.fechadespacho = fechadespacho;
        this.activa = activa;
        this.valortotal = valortotal;
        this.valorconrubro = valorconrubro;
        this.ivatotal = ivatotal;
        this.pagada = pagada;
        this.oeenpetro = oeenpetro;
        this.codigocliente = codigocliente;
        this.codigoterminal = codigoterminal;
        this.codigobanco = codigobanco;
        this.usuarioactual = usuarioactual;
    }


    public FacturaPK getFacturaPK() {
        return facturaPK;
    }

    public void setFacturaPK(FacturaPK facturaPK) {
        this.facturaPK = facturaPK;
    }
    public Boolean getAdelantar() {
        return adelantar;
    }
    public void setAdelantar(Boolean adelantar) {
        this.adelantar = adelantar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facturaPK != null ? facturaPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.facturaPK == null && other.facturaPK != null) || (this.facturaPK != null && !this.facturaPK.equals(other.facturaPK))) {
            return false;
        }
        return true;
    }

    public String getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(String fechaventa) {
        this.fechaventa = fechaventa;
    }

    public String getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(String fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

    public String getFechaacreditacion() {
        return fechaacreditacion;
    }

    public void setFechaacreditacion(String fechaacreditacion) {
        this.fechaacreditacion = fechaacreditacion;
    }

    public String getFechadespacho() {
        return fechadespacho;
    }

    public void setFechadespacho(String fechadespacho) {
        this.fechadespacho = fechadespacho;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public BigDecimal getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigDecimal valortotal) {
        this.valortotal = valortotal;
    }

    public BigDecimal getIvatotal() {
        return ivatotal;
    }

    public void setIvatotal(BigDecimal ivatotal) {
        this.ivatotal = ivatotal;
    }

    public BigDecimal getValorconrubro() {
        return valorconrubro;
    }

    public void setValorconrubro(BigDecimal valorconrubro) {
        this.valorconrubro = valorconrubro;
    }
    

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean getPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }

    public boolean getOeenpetro() {
        return oeenpetro;
    }

    public void setOeenpetro(boolean oeenpetro) {
        this.oeenpetro = oeenpetro;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getCodigobanco() {
        return codigobanco;
    }

    public void setCodigobanco(String codigobanco) {
        this.codigobanco = codigobanco;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getNombrecomercializadora() {
        return nombrecomercializadora;
    }

    public void setNombrecomercializadora(String nombrecomercializadora) {
        this.nombrecomercializadora = nombrecomercializadora;
    }

    public String getRuccomercializadora() {
        return ruccomercializadora;
    }

    public void setRuccomercializadora(String ruccomercializadora) {
        this.ruccomercializadora = ruccomercializadora;
    }

    public String getDireccionmatrizcomercializadora() {
        return direccionmatrizcomercializadora;
    }

    public void setDireccionmatrizcomercializadora(String direccionmatrizcomercializadora) {
        this.direccionmatrizcomercializadora = direccionmatrizcomercializadora;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getRuccliente() {
        return ruccliente;
    }

    public void setRuccliente(String ruccliente) {
        this.ruccliente = ruccliente;
    }

    public BigDecimal getValorsinimpuestos() {
        return valorsinimpuestos;
    }

    public void setValorsinimpuestos(BigDecimal valorsinimpuestos) {
        this.valorsinimpuestos = valorsinimpuestos;
    }

    public String getCorreocliente() {
        return correocliente;
    }

    public void setCorreocliente(String correocliente) {
        this.correocliente = correocliente;
    }

    public String getDireccioncliente() {
        return direccioncliente;
    }

    public void setDireccioncliente(String direccioncliente) {
        this.direccioncliente = direccioncliente;
    }

    public String getTelefonocliente() {
        return telefonocliente;
    }

    public void setTelefonocliente(String telefonocliente) {
        this.telefonocliente = telefonocliente;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public String getFechaautorizacion() {
        return fechaautorizacion;
    }

    public void setFechaautorizacion(String fechaautorizacion) {
        this.fechaautorizacion = fechaautorizacion;
    }

    public String getClienteformapago() {
        return clienteformapago;
    }

    public void setClienteformapago(String clienteformapago) {
        this.clienteformapago = clienteformapago;
    }

    public Integer getPlazocliente() {
        return plazocliente;
    }

    public void setPlazocliente(Integer plazocliente) {
        this.plazocliente = plazocliente;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getCampoadicionalCampo1() {
        return campoadicionalCampo1;
    }

    public void setCampoadicionalCampo1(String campoadicionalCampo1) {
        this.campoadicionalCampo1 = campoadicionalCampo1;
    }

    public String getCampoadicionalCampo2() {
        return campoadicionalCampo2;
    }

    public void setCampoadicionalCampo2(String campoadicionalCampo2) {
        this.campoadicionalCampo2 = campoadicionalCampo2;
    }

    public String getCampoadicionalCampo3() {
        return campoadicionalCampo3;
    }

    public void setCampoadicionalCampo3(String campoadicionalCampo3) {
        this.campoadicionalCampo3 = campoadicionalCampo3;
    }

    public String getCampoadicionalCampo4() {
        return campoadicionalCampo4;
    }

    public void setCampoadicionalCampo4(String campoadicionalCampo4) {
        this.campoadicionalCampo4 = campoadicionalCampo4;
    }

    public String getCampoadicionalCampo5() {
        return campoadicionalCampo5;
    }

    public void setCampoadicionalCampo5(String campoadicionalCampo5) {
        this.campoadicionalCampo5 = campoadicionalCampo5;
    }

    public String getCampoadicionalCampo6() {
        return campoadicionalCampo6;
    }

    public void setCampoadicionalCampo6(String campoadicionalCampo6) {
        this.campoadicionalCampo6 = campoadicionalCampo6;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Short getErrordocumento() {
        return errordocumento;
    }

    public void setErrordocumento(Short errordocumento) {
        this.errordocumento = errordocumento;
    }

    public Short getHospedado() {
        return hospedado;
    }

    public void setHospedado(Short hospedado) {
        this.hospedado = hospedado;
    }

    public Character getAmbientesri() {
        return ambientesri;
    }

    public void setAmbientesri(Character ambientesri) {
        this.ambientesri = ambientesri;
    }

    public Character getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(Character tipoemision) {
        this.tipoemision = tipoemision;
    }

    public String getCodigodocumento() {
        return codigodocumento;
    }

    public void setCodigodocumento(String codigodocumento) {
        this.codigodocumento = codigodocumento;
    }

    public Boolean getEsagenteretencion() {
        return esagenteretencion;
    }

    public void setEsagenteretencion(Boolean esagenteretencion) {
        this.esagenteretencion = esagenteretencion;
    }

    public String getEscontribuyenteespacial() {
        return escontribuyenteespacial;
    }

    public void setEscontribuyenteespacial(String escontribuyenteespacial) {
        this.escontribuyenteespacial = escontribuyenteespacial;
    }

    public String getObligadocontabilidad() {
        return obligadocontabilidad;
    }
   
    public void setObligadocontabilidad(String obligadocontabilidad) {
        this.obligadocontabilidad = obligadocontabilidad;
    }

    public String getTipocomprador() {
        return tipocomprador;
    }

    public void setTipocomprador(String tipocomprador) {
        this.tipocomprador = tipocomprador;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
     
    public String getSeriesri() {
        return seriesri;
    }

    public void setSeriesri(String seriesri) {
        this.seriesri = seriesri;
    }

    public String getTipoplazocredito() {
        return tipoplazocredito;
    }

    public void setTipoplazocredito(String tipoplazocredito) {
        this.tipoplazocredito = tipoplazocredito;
    }

    public Boolean getOeanuladaenpetro() {
        return oeanuladaenpetro;
    }

    public void setOeanuladaenpetro(Boolean oeanuladaenpetro) {
        this.oeanuladaenpetro = oeanuladaenpetro;
    }

    public Boolean getRefacturada() {
        return refacturada;
    }

    public void setRefacturada(Boolean refacturada) {
        this.refacturada = refacturada;
    }

    public Boolean getReliquidada() {
        return reliquidada;
    }

    public void setReliquidada(Boolean reliquidada) {
        this.reliquidada = reliquidada;
    }    

    public boolean isSeleccionar() {
        return seleccionar;
    }

    public void setSeleccionar(boolean seleccionar) {
        this.seleccionar = seleccionar;
    }

    public String getFechaacreditacionprorrogada() {
        return fechaacreditacionprorrogada;
    }

    public void setFechaacreditacionprorrogada(String fechaacreditacionprorrogada) {
        this.fechaacreditacionprorrogada = fechaacreditacionprorrogada;
    }

    public String getClienteformapagonosri() {
        return clienteformapagonosri;
    }

    public void setClienteformapagonosri(String clienteformapagonosri) {
        this.clienteformapagonosri = clienteformapagonosri;
    }

    public Boolean getDespachada() {
        return despachada;
    }

    public void setDespachada(Boolean despachada) {
        this.despachada = despachada;
    }

    public Boolean getEnviadaxcobrar() {
        return enviadaxcobrar;
    }

    public void setEnviadaxcobrar(Boolean enviadaxcobrar) {
        this.enviadaxcobrar = enviadaxcobrar;
    }

}
