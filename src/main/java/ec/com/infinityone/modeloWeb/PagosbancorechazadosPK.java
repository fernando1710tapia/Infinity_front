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
public class PagosbancorechazadosPK implements Serializable {
    
    private String bcoCodigobanco;

    private String bcoCodigocliente;

    private String bcoNumerofactura;

    private Date fechaactual;

    public PagosbancorechazadosPK() {
    }

    public PagosbancorechazadosPK(String bcoCodigobanco, String bcoCodigocliente, String bcoNumerofactura, Date fechaactual) {
        this.bcoCodigobanco = bcoCodigobanco;
        this.bcoCodigocliente = bcoCodigocliente;
        this.bcoNumerofactura = bcoNumerofactura;
        this.fechaactual = fechaactual;
    }

    public String getBcoCodigobanco() {
        return bcoCodigobanco;
    }

    public void setBcoCodigobanco(String bcoCodigobanco) {
        this.bcoCodigobanco = bcoCodigobanco;
    }

    public String getBcoCodigocliente() {
        return bcoCodigocliente;
    }

    public void setBcoCodigocliente(String bcoCodigocliente) {
        this.bcoCodigocliente = bcoCodigocliente;
    }

    public String getBcoNumerofactura() {
        return bcoNumerofactura;
    }

    public void setBcoNumerofactura(String bcoNumerofactura) {
        this.bcoNumerofactura = bcoNumerofactura;
    }

    public Date getFechaactual() {
        return fechaactual;
    }

    public void setFechaactual(Date fechaactual) {
        this.fechaactual = fechaactual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bcoCodigobanco != null ? bcoCodigobanco.hashCode() : 0);
        hash += (bcoCodigocliente != null ? bcoCodigocliente.hashCode() : 0);
        hash += (bcoNumerofactura != null ? bcoNumerofactura.hashCode() : 0);
        hash += (fechaactual != null ? fechaactual.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagosbancorechazadosPK)) {
            return false;
        }
        PagosbancorechazadosPK other = (PagosbancorechazadosPK) object;
        if ((this.bcoCodigobanco == null && other.bcoCodigobanco != null) || (this.bcoCodigobanco != null && !this.bcoCodigobanco.equals(other.bcoCodigobanco))) {
            return false;
        }
        if ((this.bcoCodigocliente == null && other.bcoCodigocliente != null) || (this.bcoCodigocliente != null && !this.bcoCodigocliente.equals(other.bcoCodigocliente))) {
            return false;
        }
        if ((this.bcoNumerofactura == null && other.bcoNumerofactura != null) || (this.bcoNumerofactura != null && !this.bcoNumerofactura.equals(other.bcoNumerofactura))) {
            return false;
        }
        if ((this.fechaactual == null && other.fechaactual != null) || (this.fechaactual != null && !this.fechaactual.equals(other.fechaactual))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.PagosbancorechazadosPK[ bcoCodigobanco=" + bcoCodigobanco + ", bcoCodigocliente=" + bcoCodigocliente + ", bcoNumerofactura=" + bcoNumerofactura + ", fechaactual=" + fechaactual + " ]";
    }

}
