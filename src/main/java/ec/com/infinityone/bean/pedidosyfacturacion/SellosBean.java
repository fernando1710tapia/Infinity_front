/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.bean.pedidosyfacturacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.infinityone.bean.actorcomercial.ComercializadoraBean;
import ec.com.infinityone.bean.enums.CodigoComerEnum;
import ec.com.infinityone.bean.enums.TerminalEnum;
import ec.com.infinityone.modelo.Cliente;
import ec.com.infinityone.modelo.Detalleterminalsello;
import ec.com.infinityone.modelo.DetalleterminalselloPK;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.modelo.TerminalSello;
import ec.com.infinityone.modelo.TerminalSelloPK;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import ec.com.infinityone.servicio.pedidosyfacturacion.SellosServicio;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author David Ayala
 */
@Named
@ViewScoped
public class SellosBean extends ReusableBean implements Serializable {

    /*
    Variable para acceder a los servicios de Comercialziadora
     */
    @Inject
    private ComercializadoraServicio comerServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    private TerminalServicio termServicio;
    /*
    Variable para acceder a los servicios de Terminal
     */
    @Inject
    private SellosServicio sellosServicio;
    /*
    Variable Comercializadora
     */
    private ComercializadoraBean comercializadora;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<ComercializadoraBean> listaComercializadora;
    /*
    Variable Terminal
     */
    private Terminal terminal;
    /*
    Variable para almacenar los datos comercializadora
     */
    private List<Terminal> listaTermianles;
    /*
    Variable para almacenar los datos teminalSello
     */
    private TerminalSello terminalSello;
    /*
    Variable para almacenar litsa de los datos teminalSello
     */
    private List<TerminalSello> terminalesSellos;
    /*
    Variable para almacenar los datos teminalSelloPK
     */
    private TerminalSelloPK terminalSelloPk;
    /*
    Variable para almacenar los datos de detalleTerminalSello
     */
    private Detalleterminalsello detalleTerminalSello;
    /*
    Variable para almacenar los datos de detalleTerminalSello
     */
    private List<Detalleterminalsello> detallesTerminalesSellos;
    /*
    Variable para almacenar los datos de detalleTerminalSello temporal
     */
    private List<Detalleterminalsello> detalleAux = new ArrayList<>();

    private List<Detalleterminalsello> filasEditadas = new ArrayList<>();

    private List<Detalleterminalsello> detalleEntregaRecepcion = new ArrayList<>();
    /*
    Variable para almacenar los datos de detalleTerminalSello
     */
    private DetalleterminalselloPK detalleTerminalSelloPK;
    /*
    Variable que almacena el código de la comercializadora
     */
    private String codComer;
    /*
    Variable que almacena el código del terminal
     */
    private String codTerminal;
    /**
     * Variable que permite establecer la fecha
     */
    private Date fecha;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarSellos;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarPantallaInicial;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarNuevosSellos;
    /*
    Variable para renderizar la pantalla
     */
    private boolean mostrarDetalleSellos;
    /*
    Variable para validar si es guardar o editar
     */
    private boolean editarSellos;
    /*
    variable para establecer la fecha inicial para la busqueda 
     */
    private Date fechaActual;
    /*
    variable para establecer la fecha inicial para la busqueda 
     */
    private Date fechaInicial;
    /*
    variable para establecer la fecha final para la busqueda 
     */
    private Date fechaFinal;
    /*
    variable para establecer el sello inicial
     */
    private int selloInicial;
    /*
    variable para establecer el sello inicial
     */
    private int selloFinal;
    /*
    variable para establecer el sello inicial entrega
     */
    private int selloInicialEntrega;
    /*
    variable para establecer el sello final entrega
     */
    private int selloFinalEntrega;

    private boolean permitirEdicionActivo = false; // Condición para "Activo"
    private boolean permitirEdicionObservacion = false; // Condición para "Observación"
    private boolean rangoInicialValido = true;
    private boolean rangosValidos = false;

    private TerminalSello selectedItem;

    /**
     * Constructor por defecto
     */
    public SellosBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        reestablecer();
        fechaActual = new Date();
        mostrarSellos = false;
        mostrarPantallaInicial = true;
        mostrarDetalleSellos = false;
        obtenerTerminales();
        obtenerComercializadora();
    }

    public void nuevosSellos() {
        reestablecer();
        if (habilitarComer) {
            comercializadora = new ComercializadoraBean();
        } else {
            seleccionarComerc(1);
        }
        mostrarNuevosSellos = true;
        mostrarPantallaInicial = false;
        mostrarDetalleSellos = false;
    }

    public void detalleSellos(TerminalSello termSello, Boolean isEdit) {
        if (isEdit) {
            permitirEdicionActivo = true;
            permitirEdicionObservacion = true;
        } else {
            permitirEdicionActivo = false;
            permitirEdicionObservacion = false;
        }
        reestablecer();
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        mostrarNuevosSellos = false;
        mostrarPantallaInicial = false;
        mostrarDetalleSellos = true;
        JSONArray retorno = sellosServicio.cargarDetalleSellos(termSello.getTerminalselloPK().getCodigocomercializadora(),
                termSello.getTerminalselloPK().getCodigoterminalentrega(),
                termSello.getTerminalselloPK().getCodigoterminalrecibe(),
                termSello.getTerminalselloPK().getSelloinicial(),
                termSello.getTerminalselloPK().getSellofinal());
        if (retorno.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCOTRO DETALLES DEL SELLO SELECCIONADO");
        } else {
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject detTerminalS = retorno.getJSONObject(indice);
                    JSONObject detTerminalSPK = detTerminalS.getJSONObject("detalleterminalselloPK");
                    JSONObject terminalS = detTerminalS.getJSONObject("terminalsello");
                    JSONObject terminalSPK = terminalS.getJSONObject("terminalselloPK");
                    Long dateStrTerminal = detTerminalS.getLong("fecha");
                    Date dateFecha = new Date(dateStrTerminal);
                    String fecha = date.format(dateFecha);

                    detalleTerminalSelloPK.setCodigocomercializadora(detTerminalSPK.getString("codigocomercializadora"));
                    detalleTerminalSelloPK.setCodigoterminalentrega(detTerminalSPK.getString("codigoterminalentrega"));
                    detalleTerminalSelloPK.setCodigoterminalrecibe(detTerminalSPK.getString("codigoterminalrecibe"));
                    detalleTerminalSelloPK.setSelloinicial(detTerminalSPK.getBigInteger("selloinicial"));
                    detalleTerminalSelloPK.setSellofinal(detTerminalSPK.getBigInteger("sellofinal"));
                    detalleTerminalSelloPK.setSello(detTerminalSPK.getBigInteger("sello"));

                    detalleTerminalSello.setObservacion(detTerminalS.getString("observacion"));
                    detalleTerminalSello.setActivo(detTerminalS.getBoolean("activo"));
                    detalleTerminalSello.setFecha(fecha);
                    detalleTerminalSello.setUsuarioactual(detTerminalS.getString("usuarioactual"));

                    detalleTerminalSello.setDetalleterminalselloPK(detalleTerminalSelloPK);

                    terminalSelloPk.setCodigocomercializadora(terminalSPK.getString("codigocomercializadora"));
                    terminalSelloPk.setCodigoterminalentrega(terminalSPK.getString("codigoterminalentrega"));
                    terminalSelloPk.setCodigoterminalrecibe(terminalSPK.getString("codigoterminalrecibe"));
                    terminalSelloPk.setSelloinicial(terminalSPK.getBigInteger("selloinicial"));
                    terminalSelloPk.setSellofinal(terminalSPK.getBigInteger("sellofinal"));

                    terminalSello.setTerminalselloPK(terminalSelloPk);
                    terminalSello.setUsuarioactual(terminalS.getString("usuarioactual"));
                    terminalSello.setFecha(fecha);

                    detallesTerminalesSellos.add(detalleTerminalSello);

                    detalleTerminalSello = new Detalleterminalsello();
                    detalleTerminalSelloPK = new DetalleterminalselloPK();
                    terminalSello = new TerminalSello();
                    terminalSelloPk = new TerminalSelloPK();
                }
            }
            if (detallesTerminalesSellos.size() > 0) {
                for (Detalleterminalsello detalle : detallesTerminalesSellos) {
                    detalleAux.add(detalle.clone());
                }
            }
        }
    }

    public void reestablecer() {
        selloInicial = 0;
        selloFinal = 0;
        selloInicialEntrega = 0;
        selloFinalEntrega = 0;
        editarSellos = false;
        codComer = "";
        //codTerminal = "";
        fecha = new Date();
        comercializadora = new ComercializadoraBean();
        terminal = new Terminal();
        terminalSello = new TerminalSello();
        terminalSelloPk = new TerminalSelloPK();
        terminalesSellos = new ArrayList<>();
        detallesTerminalesSellos = new ArrayList<>();
        detalleEntregaRecepcion = new ArrayList<>();
        filasEditadas = new ArrayList<>();
        detalleAux = new ArrayList<>();
        detalleTerminalSello = new Detalleterminalsello();
        detalleTerminalSelloPK = new DetalleterminalselloPK();
        selectedItem = new TerminalSello();
        //listaProductos = new ArrayList<>();
    }

    public void regresar() {
        mostrarDetalleSellos = false;
        mostrarPantallaInicial = true;
        mostrarNuevosSellos = false;
        reestablecer();
    }

    public void obtenerComercializadora() {
        listaComercializadora = new ArrayList<>();
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
        if (!listaComercializadora.isEmpty()) {
            habilitarBusqueda();
        }
    }

    public void seleccionarComerc(int busqueda) {
        if (comercializadora != null) {
            codComer = comercializadora.getCodigo();
        }
    }

    public void obtenerTerminales() {
        listaTermianles = new ArrayList<>();
        listaTermianles = termServicio.obtenerTerminal();
    }

    public void seleccionarTerminal(int busqueda) {
        if (terminal != null) {
            codTerminal = terminal.getCodigo();
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            if (dataUser.getUser().getNiveloperacion().equals("cero")) {
                habilitarComer = true;
            }
            if (dataUser.getUser().getNiveloperacion().equals("adco")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            codComer = comercializadora.getCodigo();
                        }
                    }
                }
            }
            if (dataUser.getUser().getNiveloperacion().equals("usac")) {
                habilitarComer = false;
                for (int i = 0; i < listaComercializadora.size(); i++) {
                    if (listaComercializadora.get(i).getCodigo().equals(dataUser.getUser().getCodigocomercializadora())) {
                        this.comercializadora = listaComercializadora.get(i);
                        if (comercializadora != null) {
                            codComer = comercializadora.getCodigo();
                        }
                    }
                }
            }
        }
    }

    public void obtenerSellos() {
        DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        /*fechas para hacer la consulta*/
        String fechaI = date.format(fechaInicial);
        String fechaF = date.format(fechaFinal);
        terminalesSellos = new ArrayList<>();

        JSONArray retorno = sellosServicio.buscarSellos(codComer, codTerminal, "e", fechaI, fechaF);
        if (retorno.isEmpty()) {
            this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON SELLOS");
        } else {
            for (int indice = 0; indice < retorno.length(); indice++) {
                if (!retorno.isNull(indice)) {
                    JSONObject terminal = retorno.getJSONObject(indice);
                    JSONObject terminalPk = terminal.getJSONObject("terminalselloPK");
                    Long dateStrTerminal = terminal.getLong("fecha");
                    Date dateFecha = new Date(dateStrTerminal);
                    String fecha = date.format(dateFecha);

                    terminalSelloPk.setCodigocomercializadora(terminalPk.getString("codigocomercializadora"));
                    terminalSelloPk.setCodigoterminalentrega(terminalPk.getString("codigoterminalentrega"));
                    terminalSelloPk.setCodigoterminalrecibe(terminalPk.getString("codigoterminalrecibe"));
                    terminalSelloPk.setSelloinicial(terminalPk.getBigInteger("selloinicial"));
                    terminalSelloPk.setSellofinal(terminalPk.getBigInteger("sellofinal"));

                    terminalSello.setTerminalselloPK(terminalSelloPk);
                    terminalSello.setUsuarioactual(terminal.getString("usuarioactual"));
                    terminalSello.setFecha(fecha);
                    terminalesSellos.add(terminalSello);

                    terminalSello = new TerminalSello();
                    terminalSelloPk = new TerminalSelloPK();
                }
            }
        }
    }

    public void dialogoAdquision() {
        PrimeFaces.current().executeScript("PF('adquirirSellos').show()");
    }

    public void comprarSellos() {
        JSONObject teminalSello = new JSONObject();
        JSONObject teminalSelloPK = new JSONObject();
        LocalDate fechaActual = LocalDate.now();
        long timestamp = fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        teminalSelloPK.put("codigocomercializadora", CodigoComerEnum.PETROL_RIOS.getCodigo());
        teminalSelloPK.put("codigoterminalentrega", TerminalEnum.TERMINAL_ENTREGA.getCodigo());
        teminalSelloPK.put("codigoterminalrecibe", TerminalEnum.TERMINAL_RECIBE.getCodigo());
        teminalSelloPK.put("selloinicial", BigInteger.valueOf(selloInicial));
        teminalSelloPK.put("sellofinal", BigInteger.valueOf(selloFinal));
        teminalSello.put("terminalselloPK", teminalSelloPK);
        teminalSello.put("fecha", timestamp);
        teminalSello.put("usuarioactual", dataUser.getUser().getNombrever());

        sellosServicio.comprarSellos(teminalSello);
        PrimeFaces.current().executeScript("PF('adquirirSellos').hide()");
    }

    private boolean hasChanges(Detalleterminalsello original, Detalleterminalsello current) {
        if (original == null || current == null) {
            return false;
        }
        return !Objects.equals(original.getActivo(), current.getActivo())
                || !Objects.equals(original.getObservacion(), current.getObservacion());
    }

    public void onRowEdit(Detalleterminalsello filaEditada) {
        // Identificar la fila actual
        //List<Detalleterminalsello> filasEditadas = new ArrayList<>();
        for (int i = 0; i < detalleAux.size(); i++) {
            Detalleterminalsello original = detalleAux.get(i);
            Detalleterminalsello current = detallesTerminalesSellos.get(i);

            if (filaEditada.equals(current) && hasChanges(original, current)) {
                if (!filasEditadas.contains(current)) {
                    filasEditadas.add(current);
                }
                break;
            }
        }
    }

    public void actualizarDetalleSellos() {
        List<JSONObject> detallesTerminalSelloEditJSONObjects = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        long timestamp = fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (filasEditadas.size() > 0) {
            for (Detalleterminalsello element : filasEditadas) {
                JSONObject detallesTerminalSelloEdit = new JSONObject();
                detallesTerminalSelloEdit.put("codigocomercializadora", element.getDetalleterminalselloPK().getCodigocomercializadora());
                detallesTerminalSelloEdit.put("codigoterminalentrega", element.getDetalleterminalselloPK().getCodigoterminalentrega());
                detallesTerminalSelloEdit.put("codigoterminalrecibe", element.getDetalleterminalselloPK().getCodigoterminalrecibe());
                detallesTerminalSelloEdit.put("selloinicial", element.getDetalleterminalselloPK().getSelloinicial());
                detallesTerminalSelloEdit.put("sellofinal", element.getDetalleterminalselloPK().getSellofinal());
                detallesTerminalSelloEdit.put("sello", element.getDetalleterminalselloPK().getSello());
                detallesTerminalSelloEdit.put("observacion", element.getObservacion());
                detallesTerminalSelloEdit.put("activo", element.getActivo());
                detallesTerminalSelloEdit.put("fecha", fechaActual);
                detallesTerminalSelloEdit.put("usuarioactual", dataUser.getUser().getNombrever());
                detallesTerminalSelloEditJSONObjects.add(detallesTerminalSelloEdit);
            }
            sellosServicio.actualizarSellos(detallesTerminalSelloEditJSONObjects);
            mostrarDetalleSellos = false;
            mostrarPantallaInicial = true;
        }
    }

    public void onRowSelect(SelectEvent<TerminalSello> event) {
        this.selectedItem = event.getObject(); // Obtener el objeto seleccionado
        System.out.println("Item seleccionado: " + selectedItem);
        System.out.println("Hola");
    }

    public void entregaRecepcionSellos(TerminalSello filaSeleccionada) {
        if (filaSeleccionada != null) {
            DateFormat date = new SimpleDateFormat("yyyy/MM/dd");
            detalleEntregaRecepcion = new ArrayList<>();
            selloInicialEntrega = filaSeleccionada.getTerminalselloPK().getSelloinicial().intValue();
            selloFinalEntrega = filaSeleccionada.getTerminalselloPK().getSellofinal().intValue();
            PrimeFaces.current().executeScript("PF('entregaRecepcion').show()");
            JSONArray retorno = sellosServicio.entregaRecepcionConsulta(
                    filaSeleccionada.getTerminalselloPK().getCodigocomercializadora(),
                    true,
                    false,
                    selloInicialEntrega,
                    selloFinalEntrega
            );
            if (retorno.isEmpty()) {
                this.dialogo(FacesMessage.SEVERITY_ERROR, "NO SE ENCONTRARON SELLOS");
            } else {
                for (int indice = 0; indice < retorno.length(); indice++) {
                    if (!retorno.isNull(indice)) {
                        JSONObject detTerminalSello = retorno.getJSONObject(indice);
                        JSONObject detTerminalSelloPk = detTerminalSello.getJSONObject("detalleterminalselloPK");
                        JSONObject termSello = detTerminalSello.getJSONObject("terminalsello");
                        JSONObject termlSelloPk = termSello.getJSONObject("terminalselloPK");
                        Long dateStrTerminal = detTerminalSello.getLong("fecha");
                        Date dateFecha = new Date(dateStrTerminal);
                        String fecha = date.format(dateFecha);

                        detalleTerminalSelloPK.setCodigocomercializadora(detTerminalSelloPk.getString("codigocomercializadora"));
                        detalleTerminalSelloPK.setCodigoterminalentrega(detTerminalSelloPk.getString("codigoterminalentrega"));
                        detalleTerminalSelloPK.setCodigoterminalrecibe(detTerminalSelloPk.getString("codigoterminalrecibe"));
                        detalleTerminalSelloPK.setSelloinicial(detTerminalSelloPk.getBigInteger("selloinicial"));
                        detalleTerminalSelloPK.setSellofinal(detTerminalSelloPk.getBigInteger("sellofinal"));
                        detalleTerminalSelloPK.setSello(detTerminalSelloPk.getBigInteger("sello"));

                        terminalSelloPk.setCodigocomercializadora(termlSelloPk.getString("codigocomercializadora"));
                        terminalSelloPk.setCodigoterminalentrega(termlSelloPk.getString("codigoterminalentrega"));
                        terminalSelloPk.setCodigoterminalrecibe(termlSelloPk.getString("codigoterminalrecibe"));
                        terminalSelloPk.setSelloinicial(termlSelloPk.getBigInteger("selloinicial"));
                        terminalSelloPk.setSellofinal(termlSelloPk.getBigInteger("sellofinal"));
                        terminalSello.setTerminalselloPK(terminalSelloPk);
                        terminalSello.setFecha(fecha);
                        terminalSello.setUsuarioactual(termSello.getString("usuarioactual"));

                        detalleTerminalSello.setDetalleterminalselloPK(detalleTerminalSelloPK);
                        detalleTerminalSello.setTerminalsello(terminalSello);
                        detalleTerminalSello.setActivo(detTerminalSello.getBoolean("activo"));
                        detalleTerminalSello.setFecha(fecha);
                        detalleTerminalSello.setUsuarioactual(detTerminalSello.getString("usuarioactual"));
                        detalleTerminalSello = new Detalleterminalsello();
                        detalleTerminalSelloPK = new DetalleterminalselloPK();
                        terminalSello = new TerminalSello();
                        terminalSelloPk = new TerminalSelloPK();
                        detalleEntregaRecepcion.add(detalleTerminalSello);
                    }
                }
            }
        }
    }

    public void validarRangoInicial() {
        if (selloInicial <= selloInicialEntrega || selloInicial >= selloFinalEntrega) {
            this.dialogo(FacesMessage.SEVERITY_WARN, "El sello inicial se encuentra fuera del rango seleccionado");
            rangoInicialValido = false;
            rangosValidos = false;
        } else {
            rangoInicialValido = true;
        }
    }

    public void validarRangoFinal() {
        if (selloFinal <= selloInicialEntrega || selloFinal >= selloFinalEntrega) {
            this.dialogo(FacesMessage.SEVERITY_WARN, "El sello final se encuentra fuera del rango seleccionado");
            rangosValidos = false;
        } else {
            if (rangoInicialValido) {
                rangosValidos = true;
                if (detalleEntregaRecepcion.size() > 0) {
                    for (Detalleterminalsello element : detalleEntregaRecepcion) {
                        int selloEncontrado = element.getDetalleterminalselloPK().getSello().intValue();
                        if (selloEncontrado >= selloInicial && selloEncontrado <= selloFinal) {
                            List<JSONObject> detallesTerminalSelloERJSON = new ArrayList<>();
                            LocalDate fechaActual = LocalDate.now();
                            long timestamp = fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                            JSONObject detallesTerminalSelloER = new JSONObject();
                            detallesTerminalSelloER.put("codigocomercializadora", element.getDetalleterminalselloPK().getCodigocomercializadora());
                            detallesTerminalSelloER.put("codigoterminalentrega", element.getDetalleterminalselloPK().getCodigoterminalentrega());
                            detallesTerminalSelloER.put("codigoterminalrecibe", element.getDetalleterminalselloPK().getCodigoterminalrecibe());
                            detallesTerminalSelloER.put("selloinicial", element.getDetalleterminalselloPK().getSelloinicial());
                            detallesTerminalSelloER.put("sellofinal", element.getDetalleterminalselloPK().getSellofinal());
                            detallesTerminalSelloER.put("sello", element.getDetalleterminalselloPK().getSello());
                            detallesTerminalSelloER.put("observacion", element.getObservacion());
                            detallesTerminalSelloER.put("activo", element.getActivo());
                            detallesTerminalSelloER.put("fecha", fechaActual);
                            detallesTerminalSelloER.put("usuarioactual", dataUser.getUser().getNombrever());
                            detallesTerminalSelloERJSON.add(detallesTerminalSelloER);
                            System.out.println("hola");
                        }
                    }
                }
            }
        }
    }

    public ComercializadoraServicio getComerServicio() {
        return comerServicio;
    }

    public void setComerServicio(ComercializadoraServicio comerServicio) {
        this.comerServicio = comerServicio;
    }

    public TerminalServicio getTermServicio() {
        return termServicio;
    }

    public void setTermServicio(TerminalServicio termServicio) {
        this.termServicio = termServicio;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public List<Terminal> getListaTermianles() {
        return listaTermianles;
    }

    public void setListaTermianles(List<Terminal> listaTermianles) {
        this.listaTermianles = listaTermianles;
    }

    public String getCodComer() {
        return codComer;
    }

    public void setCodComer(String codComer) {
        this.codComer = codComer;
    }

    public String getCodTerminal() {
        return codTerminal;
    }

    public void setCodTerminal(String codTerminal) {
        this.codTerminal = codTerminal;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isMostrarSellos() {
        return mostrarSellos;
    }

    public void setMostrarSellos(boolean mostrarSellos) {
        this.mostrarSellos = mostrarSellos;
    }

    public boolean isMostrarPantallaInicial() {
        return mostrarPantallaInicial;
    }

    public void setMostrarPantallaInicial(boolean mostrarPantallaInicial) {
        this.mostrarPantallaInicial = mostrarPantallaInicial;
    }

    public boolean isEditarSellos() {
        return editarSellos;
    }

    public void setEditarSellos(boolean editarSellos) {
        this.editarSellos = editarSellos;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public TerminalSello getTerminalSello() {
        return terminalSello;
    }

    public void setTerminalSello(TerminalSello terminalSello) {
        this.terminalSello = terminalSello;
    }

    public List<TerminalSello> getTerminalesSellos() {
        return terminalesSellos;
    }

    public void setTerminalesSellos(List<TerminalSello> terminalesSellos) {
        this.terminalesSellos = terminalesSellos;
    }

    public TerminalSelloPK getTerminalSelloPk() {
        return terminalSelloPk;
    }

    public void setTerminalSelloPk(TerminalSelloPK terminalSelloPk) {
        this.terminalSelloPk = terminalSelloPk;
    }

    public boolean isMostrarNuevosSellos() {
        return mostrarNuevosSellos;
    }

    public void setMostrarNuevosSellos(boolean mostrarNuevosSellos) {
        this.mostrarNuevosSellos = mostrarNuevosSellos;
    }

    public boolean isMostrarDetalleSellos() {
        return mostrarDetalleSellos;
    }

    public void setMostrarDetalleSellos(boolean mostrarDetalleSellos) {
        this.mostrarDetalleSellos = mostrarDetalleSellos;
    }

    public Detalleterminalsello getDetalleTerminalSello() {
        return detalleTerminalSello;
    }

    public void setDetalleTerminalSello(Detalleterminalsello detalleTerminalSello) {
        this.detalleTerminalSello = detalleTerminalSello;
    }

    public List<Detalleterminalsello> getDetallesTerminalesSellos() {
        return detallesTerminalesSellos;
    }

    public void setDetallesTerminalesSellos(List<Detalleterminalsello> detallesTerminalesSellos) {
        this.detallesTerminalesSellos = detallesTerminalesSellos;
    }

    public DetalleterminalselloPK getDetalleTerminalSelloPK() {
        return detalleTerminalSelloPK;
    }

    public void setDetalleTerminalSelloPK(DetalleterminalselloPK detalleTerminalSelloPK) {
        this.detalleTerminalSelloPK = detalleTerminalSelloPK;
    }

    public int getSelloInicial() {
        return selloInicial;
    }

    public void setSelloInicial(int selloInicial) {
        this.selloInicial = selloInicial;
    }

    public int getSelloFinal() {
        return selloFinal;
    }

    public void setSelloFinal(int selloFinal) {
        this.selloFinal = selloFinal;
    }

    public boolean isPermitirEdicionActivo() {
        return permitirEdicionActivo;
    }

    public void setPermitirEdicionActivo(boolean permitirEdicionActivo) {
        this.permitirEdicionActivo = permitirEdicionActivo;
    }

    public boolean isPermitirEdicionObservacion() {
        return permitirEdicionObservacion;
    }

    public void setPermitirEdicionObservacion(boolean permitirEdicionObservacion) {
        this.permitirEdicionObservacion = permitirEdicionObservacion;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public TerminalSello getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(TerminalSello selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelloInicialEntrega() {
        return selloInicialEntrega;
    }

    public void setSelloInicialEntrega(int selloInicialEntrega) {
        this.selloInicialEntrega = selloInicialEntrega;
    }

    public int getSelloFinalEntrega() {
        return selloFinalEntrega;
    }

    public void setSelloFinalEntrega(int selloFinalEntrega) {
        this.selloFinalEntrega = selloFinalEntrega;
    }

    public boolean isRangosValidos() {
        return rangosValidos;
    }

    public void setRangosValidos(boolean rangosValidos) {
        this.rangosValidos = rangosValidos;
    }
}
