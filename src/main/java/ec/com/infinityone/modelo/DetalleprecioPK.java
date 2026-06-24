/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
public class DetalleprecioPK implements Serializable{

    private String codigocomercializadora;

    private String codigoterminal;

    private String codigoproducto;

    private String codigomedida;

    private long codigolistaprecio;

    private Date fechainicio;

    private int secuencial;

    private String codigo;

    private String codigogravamen;

    public DetalleprecioPK() {
    }

    public DetalleprecioPK(String codigocomercializadora, String codigoterminal, String codigoproducto, String codigomedida, long codigolistaprecio, Date fechainicio, int secuencial, String codigo, String codigogravamen) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminal = codigoterminal;
        this.codigoproducto = codigoproducto;
        this.codigomedida = codigomedida;
        this.codigolistaprecio = codigolistaprecio;
        this.fechainicio = fechainicio;
        this.secuencial = secuencial;
        this.codigo = codigo;
        this.codigogravamen = codigogravamen;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public String getCodigomedida() {
        return codigomedida;
    }

    public void setCodigomedida(String codigomedida) {
        this.codigomedida = codigomedida;
    }

    public long getCodigolistaprecio() {
        return codigolistaprecio;
    }

    public void setCodigolistaprecio(long codigolistaprecio) {
        this.codigolistaprecio = codigolistaprecio;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public int getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(int secuencial) {
        this.secuencial = secuencial;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigogravamen() {
        return codigogravamen;
    }

    public void setCodigogravamen(String codigogravamen) {
        this.codigogravamen = codigogravamen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigocomercializadora != null ? codigocomercializadora.hashCode() : 0);
        hash += (codigoterminal != null ? codigoterminal.hashCode() : 0);
        hash += (codigoproducto != null ? codigoproducto.hashCode() : 0);
        hash += (codigomedida != null ? codigomedida.hashCode() : 0);
        hash += ((int) codigolistaprecio);
        hash += (fechainicio != null ? fechainicio.hashCode() : 0);
        hash += (int) secuencial;
        hash += (codigo != null ? codigo.hashCode() : 0);
        hash += (codigogravamen != null ? codigogravamen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleprecioPK)) {
            return false;
        }
        DetalleprecioPK other = (DetalleprecioPK) object;
        if ((this.codigocomercializadora == null && other.codigocomercializadora != null) || (this.codigocomercializadora != null && !this.codigocomercializadora.equals(other.codigocomercializadora))) {
            return false;
        }
        if ((this.codigoterminal == null && other.codigoterminal != null) || (this.codigoterminal != null && !this.codigoterminal.equals(other.codigoterminal))) {
            return false;
        }
        if ((this.codigoproducto == null && other.codigoproducto != null) || (this.codigoproducto != null && !this.codigoproducto.equals(other.codigoproducto))) {
            return false;
        }
        if ((this.codigomedida == null && other.codigomedida != null) || (this.codigomedida != null && !this.codigomedida.equals(other.codigomedida))) {
            return false;
        }
        if (this.codigolistaprecio != other.codigolistaprecio ) {
            return false;
        }
        if ((this.fechainicio == null && other.fechainicio != null) || (this.fechainicio != null && !this.fechainicio.equals(other.fechainicio))) {
            return false;
        }
        if (this.secuencial != other.secuencial) {
            return false;
        }
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        if ((this.codigogravamen == null && other.codigogravamen != null) || (this.codigogravamen != null && !this.codigogravamen.equals(other.codigogravamen))) {
            return false;
        }
        return true;
    }
}
