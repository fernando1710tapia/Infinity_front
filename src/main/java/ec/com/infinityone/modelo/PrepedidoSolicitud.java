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

    private String numeroNotaPedidoGenerada = "0";

    @JsonIgnore
    public String getNumeroNotaPedidoGenerada() {
        return numeroNotaPedidoGenerada;
    }

    public void setNumeroNotaPedidoGenerada(String numeroNotaPedidoGenerada) {
        this.numeroNotaPedidoGenerada = numeroNotaPedidoGenerada;
    }

    private String estadoForzado = null;

    @JsonIgnore
    public String getEstadoForzado() {
        return estadoForzado;
    }

    public void setEstadoForzado(String estadoForzado) {
        this.estadoForzado = estadoForzado;
    }

    @JsonIgnore
    public String getEstadoAutorizado() {
        if (estadoForzado != null) {
            return estadoForzado;
        }
        if (detalle != null && !detalle.isEmpty()) {
            for (Detalleprepedido d : detalle) {
                if (d.getVolumennaturalautorizado() != null && d.getVolumennaturalautorizado().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    return "SI";
                }
            }
        }
        return "NO";
    }

    @JsonIgnore
    public boolean isRegistroActivo() {
        if (detalle != null && !detalle.isEmpty() && detalle.get(0) != null) {
            Boolean act = detalle.get(0).getActivo();
            return act != null ? act : true;
        }
        return prepedido != null ? prepedido.isActiva() : true;
    }
}
