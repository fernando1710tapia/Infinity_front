/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ec.com.infinityone.bean.enums;

/**
 *
 * @author David Ayala
 */
public enum TerminalEnum {
    
    TERMINAL_ENTREGA("00"),
    TERMINAL_RECIBE("00");
    
    private final String codigoTeminal;

    private TerminalEnum(String codigoTeminal) {
        this.codigoTeminal = codigoTeminal;
    }

    public String getCodigo() {
        return codigoTeminal;
    }
}
