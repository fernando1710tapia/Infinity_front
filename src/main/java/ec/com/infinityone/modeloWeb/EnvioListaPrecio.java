/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

/**
 *
 * @author SonyVaio
 */
public class EnvioListaPrecio {
    private Listaprecio listaprecio;
    private Listaprecioterminalproducto terminalproducto;

    public EnvioListaPrecio() {
    }

    /**
     * @return the listaprecio
     */
    public Listaprecio getListaprecio() {
        return listaprecio;
    }

    /**
     * @param listaprecio the notapedido to set
     */
    public void setListaprecio(Listaprecio listaprecio) {
        this.listaprecio = listaprecio;
    }

    /**
     * @return the terminalproducto
     */
    public Listaprecioterminalproducto getTerminalproducto() {
        return terminalproducto;
    }

    /**
     * @param terminalproducto the detalle to set
     */
    public void setTerminalproducto(Listaprecioterminalproducto terminalproducto) {
        this.terminalproducto = terminalproducto;
    }

}
