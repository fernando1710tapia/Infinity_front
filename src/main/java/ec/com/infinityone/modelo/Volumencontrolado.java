package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Control volumétrico mensual por cliente (cupos autorizados por producto).
 * Un valor de -1 en autorizado significa "sin límite".
 *
 * NOTA: por ahora solo existe en el front (no hay backend). Cuando exista el
 * servicio REST se debe mapear contra la tabla volumencontrolado.
 */
public class Volumencontrolado implements Serializable {

    public static final BigDecimal SIN_LIMITE = new BigDecimal(-1);

    private String codigocliente;
    private String nombrecliente;
    /** Mes de control en formato yyyy-MM. */
    private String mes;

    private BigDecimal autorizadoext = SIN_LIMITE;
    private BigDecimal autorizadosup = SIN_LIMITE;
    private BigDecimal autorizadodie = SIN_LIMITE;

    public Volumencontrolado() {
    }

    public Volumencontrolado(String codigocliente, String nombrecliente, String mes) {
        this.codigocliente = codigocliente;
        this.nombrecliente = nombrecliente;
        this.mes = mes;
    }

    public Volumencontrolado copia() {
        Volumencontrolado v = new Volumencontrolado(codigocliente, nombrecliente, mes);
        v.autorizadoext = autorizadoext;
        v.autorizadosup = autorizadosup;
        v.autorizadodie = autorizadodie;
        return v;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public BigDecimal getAutorizadoext() {
        return autorizadoext;
    }

    public void setAutorizadoext(BigDecimal autorizadoext) {
        this.autorizadoext = autorizadoext;
    }

    public BigDecimal getAutorizadosup() {
        return autorizadosup;
    }

    public void setAutorizadosup(BigDecimal autorizadosup) {
        this.autorizadosup = autorizadosup;
    }

    public BigDecimal getAutorizadodie() {
        return autorizadodie;
    }

    public void setAutorizadodie(BigDecimal autorizadodie) {
        this.autorizadodie = autorizadodie;
    }
}
