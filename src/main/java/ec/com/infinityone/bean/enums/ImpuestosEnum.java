package ec.com.infinityone.bean.enums;

/**
 *
 * @author SonyVaio
 */
public enum ImpuestosEnum {

    IVA("0002"), 
    RETENCION_IVA_PRESUNTIVO("0004"),
    TRES_X_MIL("0328");

    private final String codigo;

    private ImpuestosEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
