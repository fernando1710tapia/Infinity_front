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

public class Clientegarantia implements Serializable {

    protected ClientegarantiaPK clientegarantiaPK;

    private Boolean activo;

    private Date fechainiciovigencia;

    private Date fechafinvigencia;

    private BigDecimal valor;

    private String observacion;

    private String usuarioactual;

    private Banco banco;

    private Cliente cliente;

    public Clientegarantia() {
    }

    public Clientegarantia(ClientegarantiaPK clientegarantiaPK) {
        this.clientegarantiaPK = clientegarantiaPK;
    }

    public Clientegarantia(String codigocomercializadora, String codigocliente, String codigobanco, String numero, int secuencial) {
        this.clientegarantiaPK = new ClientegarantiaPK(codigocomercializadora, codigocliente, codigobanco, numero, secuencial);
    }

    public ClientegarantiaPK getClientegarantiaPK() {
        return clientegarantiaPK;
    }

    public void setClientegarantiaPK(ClientegarantiaPK clientegarantiaPK) {
        this.clientegarantiaPK = clientegarantiaPK;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechainiciovigencia() {
        return fechainiciovigencia;
    }

    public void setFechainiciovigencia(Date fechainiciovigencia) {
        this.fechainiciovigencia = fechainiciovigencia;
    }

    public Date getFechafinvigencia() {
        return fechafinvigencia;
    }

    public void setFechafinvigencia(Date fechafinvigencia) {
        this.fechafinvigencia = fechafinvigencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientegarantiaPK != null ? clientegarantiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clientegarantia)) {
            return false;
        }
        Clientegarantia other = (Clientegarantia) object;
        if ((this.clientegarantiaPK == null && other.clientegarantiaPK != null) || (this.clientegarantiaPK != null && !this.clientegarantiaPK.equals(other.clientegarantiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Clientegarantia[ clientegarantiaPK=" + clientegarantiaPK + " ]";
    }
    
}
