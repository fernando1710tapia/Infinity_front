/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.bean.enums;

/**
 *
 * @author David Ayala
 */
public enum CodigoComerEnum {
    
    PETROL_RIOS("0008"),
    PYS("0002");
    
    private final String codigo;

    private CodigoComerEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
