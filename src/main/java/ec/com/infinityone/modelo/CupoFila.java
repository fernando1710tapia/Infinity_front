package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Fila del modal "Administrar cupos": un producto de un Volumencontrolado.
 * Lee/escribe directamente sobre el Volumencontrolado padre.
 */
public class CupoFila implements Serializable {

    private final Volumencontrolado padre;
    /** EXTRA, SUPER o DIESEL. */
    private final String codigo;
    private final String producto;

    public CupoFila(Volumencontrolado padre, String codigo, String producto) {
        this.padre = padre;
        this.codigo = codigo;
        this.producto = producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getProducto() {
        return producto;
    }

    public BigDecimal getAutorizado() {
        if ("EXTRA".equals(codigo)) {
            return padre.getAutorizadoext();
        } else if ("SUPER".equals(codigo)) {
            return padre.getAutorizadosup();
        }
        return padre.getAutorizadodie();
    }

    public void setAutorizado(BigDecimal valor) {
        if ("EXTRA".equals(codigo)) {
            padre.setAutorizadoext(valor);
        } else if ("SUPER".equals(codigo)) {
            padre.setAutorizadosup(valor);
        } else {
            padre.setAutorizadodie(valor);
        }
    }
}
