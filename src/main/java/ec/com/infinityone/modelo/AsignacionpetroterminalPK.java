/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Clave primaria para Asignacionpetroterminal
 */
public class AsignacionpetroterminalPK implements Serializable {

    private String codigocomercializadora;
    private String codigoterminal;
    private Date fecha;

    public AsignacionpetroterminalPK() {
    }

    public AsignacionpetroterminalPK(String codigocomercializadora, String codigoterminal, Date fecha) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminal = codigoterminal;
        this.fecha = fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.codigocomercializadora);
        hash = 97 * hash + Objects.hashCode(this.codigoterminal);
        hash = 97 * hash + Objects.hashCode(this.fecha);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AsignacionpetroterminalPK other = (AsignacionpetroterminalPK) obj;
        if (!Objects.equals(this.codigocomercializadora, other.codigocomercializadora)) {
            return false;
        }
        if (!Objects.equals(this.codigoterminal, other.codigoterminal)) {
            return false;
        }
        return Objects.equals(this.fecha, other.fecha);
    }

    @Override
    public String toString() {
        return "AsignacionpetroterminalPK{" + "codigocomercializadora=" + codigocomercializadora + ", codigoterminal=" + codigoterminal + ", fecha=" + fecha + '}';
    }
}
