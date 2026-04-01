package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity for Factor de Corrección
 * @author Antigravity
 */
public class FactorCorreccion implements Serializable {

    private Date fecha;
    private Producto producto;
    private BigDecimal factor;
    private Comercializadora comercializadora;
    private Terminal terminal;

    public FactorCorreccion() {
    }

    public FactorCorreccion(Date fecha, Producto producto, BigDecimal factor) {
        this.fecha = fecha;
        this.producto = producto;
        this.factor = factor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public Comercializadora getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(Comercializadora comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return "FactorCorreccion{" + "fecha=" + fecha + ", producto=" + producto + ", factor=" + factor + '}';
    }
}
