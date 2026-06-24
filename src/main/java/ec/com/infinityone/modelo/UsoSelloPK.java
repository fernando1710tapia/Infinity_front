/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;

/**
 *
 * @author david
 */
public class UsoSelloPK implements Serializable{

    private String codigocomercializadora;

    private String codigoterminal;

    private String codigocliente;

    private String placa;

    private String np1;

    public UsoSelloPK() {
    }

    public UsoSelloPK(String codigocomercializadora, String codigoterminal, String codigocliente, String placa, String np1) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminal = codigoterminal;
        this.codigocliente = codigocliente;
        this.placa = placa;
        this.np1 = np1;
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

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNp1() {
        return np1;
    }

    public void setNp1(String np1) {
        this.np1 = np1;
    }

}
