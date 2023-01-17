/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SonyVaio
 */
public class Pagosbancorechazados implements Serializable {

    private static final long serialVersionUID = 1L;

    protected PagosbancorechazadosPK pagosbancorechazadosPK;

    private BigDecimal bcoValorconrubro;

    private String bcoRuccliente;

    private String bcoNombrecliente;

    private String bcoNombrebanco;

    private Date pysFechaacreditacionprorrogada;

    private String bcoFechaproceso;

    private String pysCodigobanco;

    private String pysCodigocliente;

    private String pysNumerofactura;

    private BigDecimal pysValorconrubro;

    private String pysRuccliente;

    private String pysNombrecliente;

    private String pysNombrebanco;

    private Boolean registrook;

    private String pysNumeronotapedido;

    private String bcoCondicionProceso;

    private String bcoMensajeProceso;

    private String fechaventa;
    private String fechaacreditacionprorrogada;
    private BigDecimal valorconrubros;
    private String tasainteres;
    private long diasretraso;
    private BigDecimal valormora;
    private String claveacceso;

    public Pagosbancorechazados() {
    }

    public Pagosbancorechazados(PagosbancorechazadosPK pagosbancorechazadosPK) {
        this.pagosbancorechazadosPK = pagosbancorechazadosPK;
    }

    public Pagosbancorechazados(PagosbancorechazadosPK pagosbancorechazadosPK, String pysCodigobanco, String pysCodigocliente, String pysNumerofactura) {
        this.pagosbancorechazadosPK = pagosbancorechazadosPK;
        this.pysCodigobanco = pysCodigobanco;
        this.pysCodigocliente = pysCodigocliente;
        this.pysNumerofactura = pysNumerofactura;
    }

    public Pagosbancorechazados(String bcoCodigobanco, String bcoCodigocliente, String bcoNumerofactura, Date fechaactual) {
        this.pagosbancorechazadosPK = new PagosbancorechazadosPK(bcoCodigobanco, bcoCodigocliente, bcoNumerofactura, fechaactual);
    }

    public PagosbancorechazadosPK getPagosbancorechazadosPK() {
        return pagosbancorechazadosPK;
    }

    public void setPagosbancorechazadosPK(PagosbancorechazadosPK pagosbancorechazadosPK) {
        this.pagosbancorechazadosPK = pagosbancorechazadosPK;
    }

    public BigDecimal getBcoValorconrubro() {
        return bcoValorconrubro;
    }

    public void setBcoValorconrubro(BigDecimal bcoValorconrubro) {
        this.bcoValorconrubro = bcoValorconrubro;
    }

    public String getBcoRuccliente() {
        return bcoRuccliente;
    }

    public void setBcoRuccliente(String bcoRuccliente) {
        this.bcoRuccliente = bcoRuccliente;
    }

    public String getBcoNombrecliente() {
        return bcoNombrecliente;
    }

    public void setBcoNombrecliente(String bcoNombrecliente) {
        this.bcoNombrecliente = bcoNombrecliente;
    }

    public String getBcoNombrebanco() {
        return bcoNombrebanco;
    }

    public void setBcoNombrebanco(String bcoNombrebanco) {
        this.bcoNombrebanco = bcoNombrebanco;
    }

    public Date getPysFechaacreditacionprorrogada() {
        return pysFechaacreditacionprorrogada;
    }

    public void setPysFechaacreditacionprorrogada(Date pysFechaacreditacionprorrogada) {
        this.pysFechaacreditacionprorrogada = pysFechaacreditacionprorrogada;
    }

    public String getBcoFechaproceso() {
        return bcoFechaproceso;
    }

    public void setBcoFechaproceso(String bcoFechaproceso) {
        this.bcoFechaproceso = bcoFechaproceso;
    }

    public String getPysCodigobanco() {
        return pysCodigobanco;
    }

    public void setPysCodigobanco(String pysCodigobanco) {
        this.pysCodigobanco = pysCodigobanco;
    }

    public String getPysCodigocliente() {
        return pysCodigocliente;
    }

    public void setPysCodigocliente(String pysCodigocliente) {
        this.pysCodigocliente = pysCodigocliente;
    }

    public String getPysNumerofactura() {
        return pysNumerofactura;
    }

    public void setPysNumerofactura(String pysNumerofactura) {
        this.pysNumerofactura = pysNumerofactura;
    }

    public BigDecimal getPysValorconrubro() {
        return pysValorconrubro;
    }

    public void setPysValorconrubro(BigDecimal pysValorconrubro) {
        this.pysValorconrubro = pysValorconrubro;
    }

    public String getPysRuccliente() {
        return pysRuccliente;
    }

    public void setPysRuccliente(String pysRuccliente) {
        this.pysRuccliente = pysRuccliente;
    }

    public String getPysNombrecliente() {
        return pysNombrecliente;
    }

    public void setPysNombrecliente(String pysNombrecliente) {
        this.pysNombrecliente = pysNombrecliente;
    }

    public String getPysNombrebanco() {
        return pysNombrebanco;
    }

    public void setPysNombrebanco(String pysNombrebanco) {
        this.pysNombrebanco = pysNombrebanco;
    }

    public Boolean getRegistrook() {
        return registrook;
    }

    public void setRegistrook(Boolean registrook) {
        this.registrook = registrook;
    }

    public String getPysNumeronotapedido() {
        return pysNumeronotapedido;
    }

    public void setPysNumeronotapedido(String pysNumeronotapedido) {
        this.pysNumeronotapedido = pysNumeronotapedido;
    }

    public String getBcoCondicionProceso() {
        return bcoCondicionProceso;
    }

    public void setBcoCondicionProceso(String bcoCondicionProceso) {
        this.bcoCondicionProceso = bcoCondicionProceso;
    }

    public String getBcoMensajeProceso() {
        return bcoMensajeProceso;
    }

    public void setBcoMensajeProceso(String bcoMensajeProceso) {
        this.bcoMensajeProceso = bcoMensajeProceso;
    }

    public String getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(String fechaventa) {
        this.fechaventa = fechaventa;
    }

    public String getFechaacreditacionprorrogada() {
        return fechaacreditacionprorrogada;
    }

    public void setFechaacreditacionprorrogada(String fechaacreditacionprorrogada) {
        this.fechaacreditacionprorrogada = fechaacreditacionprorrogada;
    }

    public BigDecimal getValorconrubros() {
        return valorconrubros;
    }

    public void setValorconrubros(BigDecimal valorconrubros) {
        this.valorconrubros = valorconrubros;
    }

    public String getTasainteres() {
        return tasainteres;
    }

    public void setTasainteres(String tasainteres) {
        this.tasainteres = tasainteres;
    }

    public long getDiasretraso() {
        return diasretraso;
    }

    public void setDiasretraso(long diasretraso) {
        this.diasretraso = diasretraso;
    }

    public BigDecimal getValormora() {
        return valormora;
    }

    public void setValormora(BigDecimal valormora) {
        this.valormora = valormora;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagosbancorechazadosPK != null ? pagosbancorechazadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagosbancorechazados)) {
            return false;
        }
        Pagosbancorechazados other = (Pagosbancorechazados) object;
        if ((this.pagosbancorechazadosPK == null && other.pagosbancorechazadosPK != null) || (this.pagosbancorechazadosPK != null && !this.pagosbancorechazadosPK.equals(other.pagosbancorechazadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Pagosbancorechazados[ pagosbancorechazadosPK=" + pagosbancorechazadosPK + " ]";
    }

}
