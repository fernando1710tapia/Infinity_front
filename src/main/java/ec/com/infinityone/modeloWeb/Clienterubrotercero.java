/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import ec.com.infinityone.modeloWeb.Cliente;
import ec.com.infinityone.modeloWeb.ClienterubroterceroPK;
import ec.com.infinityone.modeloWeb.Cuotarubroterceros;
import ec.com.infinityone.modeloWeb.Rubrotercero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SonyVaio
 */

public class Clienterubrotercero implements Serializable {

    protected ClienterubroterceroPK clienterubroterceroPK;

    private BigDecimal valor;

    private int cuotas;

    private String usuarioactual;

    private String tipocobro;

    private Cliente cliente1;

    private Date fechainiciocobro;

    private Rubrotercero rubrotercero;
    
    private Boolean activo;

    private List<Cuotarubroterceros> cuotarubrotercerosList;

    public Clienterubrotercero() {
    }

    public Clienterubrotercero(ClienterubroterceroPK clienterubroterceroPK) {
        this.clienterubroterceroPK = clienterubroterceroPK;
    }

    public Clienterubrotercero(ClienterubroterceroPK clienterubroterceroPK, BigDecimal valor, int cuotas) {
        this.clienterubroterceroPK = clienterubroterceroPK;
        this.valor = valor;
        this.cuotas = cuotas;
    }

    public Clienterubrotercero(String codigocomercializadora, long codigorubrotercero, String codigocliente) {
        this.clienterubroterceroPK = new ClienterubroterceroPK(codigocomercializadora, codigorubrotercero, codigocliente);
    }

    public ClienterubroterceroPK getClienterubroterceroPK() {
        return clienterubroterceroPK;
    }

    public void setClienterubroterceroPK(ClienterubroterceroPK clienterubroterceroPK) {
        this.clienterubroterceroPK = clienterubroterceroPK;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Cliente getCliente1() {
        return cliente1;
    }

    public void setCliente1(Cliente cliente1) {
        this.cliente1 = cliente1;
    }

    public Rubrotercero getRubrotercero() {
        return rubrotercero;
    }

    public void setRubrotercero(Rubrotercero rubrotercero) {
        this.rubrotercero = rubrotercero;
    }

    @XmlTransient
    public List<Cuotarubroterceros> getCuotarubrotercerosList() {
        return cuotarubrotercerosList;
    }

    public void setCuotarubrotercerosList(List<Cuotarubroterceros> cuotarubrotercerosList) {
        this.cuotarubrotercerosList = cuotarubrotercerosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienterubroterceroPK != null ? clienterubroterceroPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clienterubrotercero)) {
            return false;
        }
        Clienterubrotercero other = (Clienterubrotercero) object;
        if ((this.clienterubroterceroPK == null && other.clienterubroterceroPK != null) || (this.clienterubroterceroPK != null && !this.clienterubroterceroPK.equals(other.clienterubroterceroPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Clienterubrotercero[ clienterubroterceroPK=" + clienterubroterceroPK + " ]";
    }

    public String getTipocobro() {
        return tipocobro;
    }

    public void setTipocobro(String tipocobro) {
        this.tipocobro = tipocobro;
    }

    public Date getFechainiciocobro() {
        return fechainiciocobro;
    }

    public void setFechainiciocobro(Date fechainiciocobro) {
        this.fechainiciocobro = fechainiciocobro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
        
    
}
