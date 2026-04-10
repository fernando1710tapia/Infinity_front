/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.estilos;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class Estilos implements Serializable {

    public String tipoActivo(String _tipo) {
        String result;
        switch (_tipo) {
            case "S":
                result = "instock";
                break;
            case "N":
                result = "outofstock";
                break;
            default:
                result = "lowstock";
        }
        return result;
    }

    public String tipoActivoBool(boolean _tipo) {
        String result;
        if (_tipo) {
            result = "instock";
        } else if (_tipo == false) {
            result = "outofstock";
        } else {
            result = "lowstock";
        }
        return result;
    }

    public String valorActivo(String _tipo) {
        String result;
        switch (_tipo) {
            case "S":
                result = "ACTIVO";
                break;
            case "N":
                result = "INACTIVO";
                break;
            default:
                result = "DESCONOCIDO";
        }
        return result;
    }

    public String valorActivoFactura(String _tipo) {
        String result;
        switch (_tipo) {
            case "S":
                result = "SI";
                break;
            case "N":
                result = "NO";
                break;
            case "P":
                result = "PE";
                break;
            default:
                result = "DESCONOCIDO";
        }
        return result;
    }

    public String valorBoolActivo(boolean _tipo) {
        String result;
        if (_tipo) {
            result = "ACTIVO";
        } else {
            result = "INACTIVO";
        }
        return result;
    }

    public String valorBoolActivoFact(boolean _tipo) {
        String result;
        if (_tipo) {
            result = "ACT";
        } else {
            result = "ANU";
        }
        return result;
    }

    public String valorBoolActivoIni(boolean _tipo) {
        String result;
        if (_tipo) {
            result = "A";
        } else {
            result = "I";
        }
        return result;
    }

    public String valorBoolActivoPago(boolean _tipo) {
        String result;
        if (_tipo) {
            result = "SI";
        } else {
            result = "NO";
        }
        return result;
    }

    public String valorEstadoPetro(String _tipo) {
        if (_tipo == null) return "NO";
        if (_tipo.equals("00") || _tipo.equals("20")) {
            return "SI";
        } else {
            return "NO";
        }
    }

    public String valorEstadoAnulacionPetro(String _tipo) {
        if (_tipo == null) return "NO";
        if (_tipo.equals("00") || _tipo.equals("01") || _tipo.equals("03")) {
            return "SI";
        } else {
            return "NO";
        }
    }

    public String estiloEstadoPetro(String _tipo) {
        if (_tipo == null) return "outofstock";
        if (_tipo.equals("00") || _tipo.equals("20")) {
            return "instock";
        } else {
            return "outofstock";
        }
    }

    public String estiloEstadoAnulacionPetro(String _tipo) {
        if (_tipo == null) return "outofstock";
        if (_tipo.equals("00") || _tipo.equals("01") || _tipo.equals("03")) {
            return "instock";
        } else {
            return "outofstock";
        }
    }
}
