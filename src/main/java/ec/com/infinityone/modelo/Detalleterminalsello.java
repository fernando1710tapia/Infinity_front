/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author David Ayala
 */
public class Detalleterminalsello implements Serializable {

    private static final long serialVersionUID = 1L;

    protected DetalleterminalselloPK detalleterminalselloPK;

    private String observacion;

    private Boolean activo;

    private String fecha;

    private String usuarioactual;

    private TerminalSello terminalsello;

    public Detalleterminalsello() {
    }

    public Detalleterminalsello(DetalleterminalselloPK detalleterminalselloPK, String observacion, Boolean activo, String fecha, String usuarioactual, TerminalSello terminalsello) {
        this.detalleterminalselloPK = detalleterminalselloPK;
        this.observacion = observacion;
        this.activo = activo;
        this.fecha = fecha;
        this.usuarioactual = usuarioactual;
        this.terminalsello = terminalsello;
    }

    public DetalleterminalselloPK getDetalleterminalselloPK() {
        return detalleterminalselloPK;
    }

    public void setDetalleterminalselloPK(DetalleterminalselloPK detalleterminalselloPK) {
        this.detalleterminalselloPK = detalleterminalselloPK;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public TerminalSello getTerminalsello() {
        return terminalsello;
    }

    public void setTerminalsello(TerminalSello terminalsello) {
        this.terminalsello = terminalsello;
    }

    @Override
    public Detalleterminalsello clone() {
        Detalleterminalsello cloned = new Detalleterminalsello();
        cloned.setDetalleterminalselloPK(new DetalleterminalselloPK(this.getDetalleterminalselloPK()));
        cloned.setActivo(this.getActivo());
        cloned.setObservacion(this.getObservacion());
        cloned.setFecha(this.getFecha());
        cloned.setUsuarioactual(this.getUsuarioactual());
        cloned.setTerminalsello(this.terminalsello);
        return cloned;
    }

}
