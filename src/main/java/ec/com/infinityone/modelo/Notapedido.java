/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class Notapedido implements Serializable{

    protected NotapedidoPK notapedidoPK;

    private String fechaventa;

    private String fechadespacho;

    private boolean activa;

    private String codigoautotanque;

    private String cedulaconductor;

    private String numerofacturasri;

    private String respuestageneracionoeepp;

    private String observacion;

    private boolean adelantar;
    
    private boolean procesar;

    private String respuestaanulacionoeepp;

    private String tramaenviadagoe;
    
    private String tramarenviadaaoe;

    private String tramarecibidagoe;

    private String tramarecibidaaoe;

    private String usuarioactual;
   
    private String prefijo;

    private Cliente codigocliente;
    
    private String codigoclienteId;
    
    private Terminal codigoterminal;
    
    private Banco codigobanco;
    
    private Comercializadora comercializadora;
    
    private Abastecedora abastecedora;
    

    
    
    public Notapedido() {
    }

    public Notapedido(NotapedidoPK notapedidoPK) {
        this.notapedidoPK = notapedidoPK;
    }
    
    public Notapedido(NotapedidoPK notapedidoPK, String fechaventa, String fechadespacho, boolean activa, boolean adelantar, String usuarioactual) {
        this.notapedidoPK = notapedidoPK;
        this.fechaventa = fechaventa;
        this.fechadespacho = fechadespacho;
        this.activa = activa;
        this.adelantar = adelantar;
        this.usuarioactual = usuarioactual;
    }

    public Notapedido(String codigoabastecedora, String codigocomercializadora, String numero) {
        this.notapedidoPK = new NotapedidoPK(codigoabastecedora, codigocomercializadora, numero);
    }

    public NotapedidoPK getNotapedidoPK() {
        return notapedidoPK;
    }

    public void setNotapedidoPK(NotapedidoPK notapedidoPK) {
        this.notapedidoPK = notapedidoPK;
    }

    public String getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(String fechaventa) {
        this.fechaventa = fechaventa;
    }

    public String getFechadespacho() {
        return fechadespacho;
    }

    public void setFechadespacho(String fechadespacho) {
        this.fechadespacho = fechadespacho;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getCodigoautotanque() {
        return codigoautotanque;
    }

    public void setCodigoautotanque(String codigoautotanque) {
        this.codigoautotanque = codigoautotanque;
    }

    public String getCedulaconductor() {
        return cedulaconductor;
    }

    public void setCedulaconductor(String cedulaconductor) {
        this.cedulaconductor = cedulaconductor;
    }

    public String getNumerofacturasri() {
        return numerofacturasri;
    }

    public void setNumerofacturasri(String numerofacturasri) {
        this.numerofacturasri = numerofacturasri;
    }

    public String getRespuestageneracionoeepp() {
        return respuestageneracionoeepp;
    }

    public void setRespuestageneracionoeepp(String respuestageneracionoeepp) {
        this.respuestageneracionoeepp = respuestageneracionoeepp;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public boolean isAdelantar() {
        return adelantar;
    }

    public void setAdelantar(boolean adelantar) {
        this.adelantar = adelantar;
    }

    public String getRespuestaanulacionoeepp() {
        return respuestaanulacionoeepp;
    }

    public void setRespuestaanulacionoeepp(String respuestaanulacionoeepp) {
        this.respuestaanulacionoeepp = respuestaanulacionoeepp;
    }

    public String getTramaenviadagoe() {
        return tramaenviadagoe;
    }

    public void setTramaenviadagoe(String tramaenviadagoe) {
        this.tramaenviadagoe = tramaenviadagoe;
    }

    public String getTramarecibidagoe() {
        return tramarecibidagoe;
    }

    public void setTramarecibidagoe(String tramarecibidagoe) {
        this.tramarecibidagoe = tramarecibidagoe;
    }

    public String getTramarecibidaaoe() {
        return tramarecibidaaoe;
    }

    public void setTramarecibidaaoe(String tramarecibidaaoe) {
        this.tramarecibidaaoe = tramarecibidaaoe;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    public Cliente getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(Cliente codigocliente) {
        this.codigocliente = codigocliente;
    }

    public Terminal getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(Terminal codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public Banco getCodigobanco() {
        return codigobanco;
    }

    public void setCodigobanco(Banco codigobanco) {
        this.codigobanco = codigobanco;
    }

    public Comercializadora getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(Comercializadora comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Abastecedora getAbastecedora() {
        return abastecedora;
    }

    public void setAbastecedora(Abastecedora abastecedora) {
        this.abastecedora = abastecedora;
    }

    public String getTramarenviadaaoe() {
        return tramarenviadaaoe;
    }

    public void setTramarenviadaaoe(String tramarenviadaaoe) {
        this.tramarenviadaaoe = tramarenviadaaoe;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notapedidoPK != null ? notapedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notapedido)) {
            return false;
        }
        Notapedido other = (Notapedido) object;
        if ((this.notapedidoPK == null && other.notapedidoPK != null) || (this.notapedidoPK != null && !this.notapedidoPK.equals(other.notapedidoPK))) {
            return false;
        }
        return true;
    }

    public boolean isProcesar() {
        return procesar;
    }

    public void setProcesar(boolean procesar) {
        this.procesar = procesar;
    }    

    public String getCodigoclienteId() {
        return codigoclienteId;
    }

    public void setCodigoclienteId(String codigoclienteId) {
        this.codigoclienteId = codigoclienteId;
    }
    
    
}
