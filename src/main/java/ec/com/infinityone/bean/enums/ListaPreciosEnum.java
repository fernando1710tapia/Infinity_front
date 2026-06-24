package ec.com.infinityone.bean.enums;

/**
 *
 * @author SonyVaio
 */
public enum ListaPreciosEnum {

    MPO("MPO - Margen sobre el precio en terminal SIN iva"), 
    MTI("MTI - Margen sobre el precio en terminal CON iva"),
    MCO("MCO - Margen sobre el margen de comercialización");

    private final String codigo;

    private ListaPreciosEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
