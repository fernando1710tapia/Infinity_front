/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author David Ayala
 */
public class DetalleterminalselloPK implements Serializable {

    private String codigocomercializadora;

    private String codigoterminalentrega;

    private String codigoterminalrecibe;
    
    private String nombreTerminalEntrega;

    private BigInteger selloinicial;

    private BigInteger sellofinal;
    
    private BigInteger sello;

    public DetalleterminalselloPK() {
    }

    public DetalleterminalselloPK(String codigocomercializadora, String codigoterminalentrega, String codigoterminalrecibe, BigInteger selloinicial, BigInteger sellofinal, BigInteger sello) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminalentrega = codigoterminalentrega;
        this.codigoterminalrecibe = codigoterminalrecibe;
        this.selloinicial = selloinicial;
        this.sellofinal = sellofinal;
        this.sello = sello;
    }
    
      public DetalleterminalselloPK(DetalleterminalselloPK other) {
        this.codigocomercializadora = other.codigocomercializadora;
        this.codigoterminalentrega = other.codigoterminalentrega;
        this.codigoterminalrecibe = other.codigoterminalrecibe;
        this.selloinicial = other.selloinicial;
        this.sellofinal = other.sellofinal;
        this.sello = other.sello;
    }

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getCodigoterminalentrega() {
        return codigoterminalentrega;
    }

    public void setCodigoterminalentrega(String codigoterminalentrega) {
        this.codigoterminalentrega = codigoterminalentrega;
    }

    public String getCodigoterminalrecibe() {
        return codigoterminalrecibe;
    }

    public void setCodigoterminalrecibe(String codigoterminalrecibe) {
        this.codigoterminalrecibe = codigoterminalrecibe;
    }

    public BigInteger getSelloinicial() {
        return selloinicial;
    }

    public void setSelloinicial(BigInteger selloinicial) {
        this.selloinicial = selloinicial;
    }

    public BigInteger getSellofinal() {
        return sellofinal;
    }

    public void setSellofinal(BigInteger sellofinal) {
        this.sellofinal = sellofinal;
    }

    public BigInteger getSello() {
        return sello;
    }

    public void setSello(BigInteger sello) {
        this.sello = sello;
    }

    public String getNombreTerminalEntrega() {
        return nombreTerminalEntrega;
    }

    public void setNombreTerminalEntrega(String nombreTerminalEntrega) {
        this.nombreTerminalEntrega = nombreTerminalEntrega;
    }
    
    

}
