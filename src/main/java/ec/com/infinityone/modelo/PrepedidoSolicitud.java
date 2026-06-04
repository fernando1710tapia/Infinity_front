/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author HP
 */
public class PrepedidoSolicitud {
    private Prepedido prepedido;
    private List<Detalleprepedido> detalle;
    private boolean autorizar;

    public PrepedidoSolicitud() {
    }

    /**
     * @return the prepedido
     */
    public Prepedido getPrepedido() {
        return prepedido;
    }

    /**
     * @param prepedido the prepedido to set
     */
    public void setPrepedido(Prepedido prepedido) {
        this.prepedido = prepedido;
    }

    /**
     * @return the detalle
     */
    public List<Detalleprepedido> getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(List<Detalleprepedido> detalle) {
        this.detalle = detalle;
    }

    @JsonIgnore
    public boolean isAutorizar() {
        return autorizar;
    }

    public void setAutorizar(boolean autorizar) {
        this.autorizar = autorizar;
    }

    @JsonIgnore
    public String getEstadoAutorizado() {
        if (detalle != null && !detalle.isEmpty()) {
            for (Detalleprepedido d : detalle) {
                if (d.getVolumennaturalautorizado() != null && d.getVolumennaturalautorizado().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    return "SI";
                }
            }
        }
        return "NO";
    }
}
