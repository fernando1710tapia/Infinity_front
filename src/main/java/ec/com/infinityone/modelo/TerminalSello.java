/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
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

    public TerminalSello() {
    }

    public TerminalSello(TerminalSelloPK terminalselloPK, String fecha, String usuarioactual, List<Detalleterminalsello> detalleterminalselloList) {
        this.terminalselloPK = terminalselloPK;
        this.fecha = fecha;
        this.usuarioactual = usuarioactual;
        this.detalleterminalselloList = detalleterminalselloList;
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
}
