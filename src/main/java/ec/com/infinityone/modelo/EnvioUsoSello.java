/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

/**
 *
 * @author david
 */
public class EnvioUsoSello {
    
    private String codigocomercializadora; 
    private String codigoterminal; 
    private String codigocliente;
    private String placa; 
    private String fecha;
    private String nombreconductor;
    private String nombrecliente;
    private String np1;
    private String selloconcatenado;
    private String npconcatenada;
    
    public EnvioUsoSello() {
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    
    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    
    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreconductor() {
        return nombreconductor;
    }

    public void setNombreconductor(String nombreconductor) {
        this.nombreconductor = nombreconductor;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getNp1() {
        return np1;
    }

    public void setNp1(String np1) {
        this.np1 = np1;
    }

    public String getSelloconcatenado() {
        return selloconcatenado;
    }

    public void setSelloconcatenado(String selloconcatenado) {
        this.selloconcatenado = selloconcatenado;
    }

    public String getNpconcatenada() {
        return npconcatenada;
    }

    public void setNpconcatenada(String npconcatenada) {
        this.npconcatenada = npconcatenada;
    }
}
