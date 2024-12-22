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
public class TerminalSelloPK implements Serializable {

    private String codigocomercializadora;

    private String codigoterminalentrega;

    private String codigoterminalrecibe;

    private BigInteger selloinicial;

    private BigInteger sellofinal;

    public TerminalSelloPK() {
    }

    public TerminalSelloPK(String codigocomercializadora, String codigoterminalentrega, String codigoterminalrecibe, BigInteger selloinicial, BigInteger sellofinal) {
        this.codigocomercializadora = codigocomercializadora;
        this.codigoterminalentrega = codigoterminalentrega;
        this.codigoterminalrecibe = codigoterminalrecibe;
        this.selloinicial = selloinicial;
        this.sellofinal = sellofinal;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (codigoterminalentrega != null ? codigoterminalentrega.hashCode() : 0);
        hash = 31 * hash + (selloinicial != null ? selloinicial.hashCode() : 0);
        hash = 31 * hash + (sellofinal != null ? sellofinal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TerminalSelloPK other = (TerminalSelloPK) obj;
        return codigoterminalentrega != null && codigoterminalentrega.equals(other.codigoterminalentrega)
                && selloinicial != null && selloinicial.equals(other.selloinicial)
                && sellofinal != null && sellofinal.equals(other.sellofinal);
    }

}
