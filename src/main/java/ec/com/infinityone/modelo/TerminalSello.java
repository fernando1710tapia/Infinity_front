/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author David Ayala
 */
public class TerminalSello implements Serializable {

    private static final long serialVersionUID = 1L;

    protected TerminalSelloPK terminalselloPK;

    private String fecha;

    private String usuarioactual;

    private List<Detalleterminalsello> detalleterminalselloList;
    
    private BigInteger sellosvalidos;
    
    private String observacion;

    public TerminalSello() {
    }

    public TerminalSello(TerminalSelloPK terminalselloPK, String fecha, String usuarioactual, List<Detalleterminalsello> detalleterminalselloList, BigInteger sellosvalidos) {
        this.terminalselloPK = terminalselloPK;
        this.fecha = fecha;
        this.usuarioactual = usuarioactual;
        this.detalleterminalselloList = detalleterminalselloList;
        this.sellosvalidos = sellosvalidos;
    }

    public TerminalSelloPK getTerminalselloPK() {
        return terminalselloPK;
    }

    public void setTerminalselloPK(TerminalSelloPK terminalselloPK) {
        this.terminalselloPK = terminalselloPK;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public List<Detalleterminalsello> getDetalleterminalselloList() {
        return detalleterminalselloList;
    }

    public void setDetalleterminalselloList(List<Detalleterminalsello> detalleterminalselloList) {
        this.detalleterminalselloList = detalleterminalselloList;
    }

    public BigInteger getSellosvalidos() {
        return sellosvalidos;
    }

    public void setSellosvalidos(BigInteger sellosvalidos) {
        this.sellosvalidos = sellosvalidos;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (terminalselloPK != null ? terminalselloPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TerminalSello other = (TerminalSello) obj;
        return terminalselloPK != null && terminalselloPK.equals(other.terminalselloPK);
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    
}
