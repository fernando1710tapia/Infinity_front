package ec.com.infinityone.bean.actorcomercial;

import ec.com.infinityone.modelo.FactorCorreccion;
import ec.com.infinityone.modelo.Producto;
import ec.com.infinityone.modelo.Terminal;
import ec.com.infinityone.reusable.ReusableBean;
import ec.com.infinityone.serivicio.actorcomercial.ComercializadoraServicio;
import ec.com.infinityone.serivicio.actorcomercial.FactorCorreccionServicio;
import ec.com.infinityone.servicio.catalogo.ProductoServicio;
import ec.com.infinityone.servicio.catalogo.TerminalServicio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 * Managed Bean for Factor de Corrección screen
 * 
 * @author Antigravity
 */
@Named
@ViewScoped
public class FactorCorreccionBean extends ReusableBean implements Serializable {

    @Inject
    private ComercializadoraServicio comerServicio;
    @Inject
    private TerminalServicio termServicio;
    @Inject
    private ProductoServicio prodServicio;

    @Inject
    private FactorCorreccionServicio factorServicio;

    private List<ComercializadoraBean> listaComercializadora;
    private List<Terminal> listaTerminales;
    private List<Producto> listaProductos;
    private List<FactorCorreccion> listaFactores;
    private List<FactorCorreccion> listaFactoresGuardada;
    private List<FactorCorreccion> listaProductosNuevos;

    private ComercializadoraBean comercializadora;
    private Terminal terminal;
    private Date fechaInicial;
    private Date fechaFinal;
    private FactorCorreccion factorSeleccionado;

    // User Permissions
    private boolean habilitarComer;
    private boolean habilitarTerminal;

    // Creation View Fields
    private boolean panelNuevo;
    private ComercializadoraBean comerNuevo;
    private Terminal termNuevo;
    private Date fechaActual;

    // Switch filter
    private boolean mostrarSoloVigentes;

    @PostConstruct
    public void init() {
        if (dataUser == null) {
            dataUser = (ec.com.infinityone.bean.login.DatosUsuario) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("dataUser");
        }

        if (dataUser != null && dataUser.getUser() != null) {
            x = dataUser.getUser();
        }

        obtenerComercializadoras();
        obtenerTerminales();
        obtenerProductos();
        listaFactores = new ArrayList<>();
        listaFactoresGuardada = new ArrayList<>();
        fechaInicial = new Date();
        fechaFinal = new Date();
        panelNuevo = false;
        mostrarSoloVigentes = false;

        habilitarBusqueda();
    }

    public void obtenerComercializadoras() {
        listaComercializadora = comerServicio.obtenerComercializadorasActivas();
    }

    public void obtenerTerminales() {
        listaTerminales = termServicio.obtenerTerminal();
    }

    public void obtenerProductos() {
        listaProductos = prodServicio.obtenerProducto();
    }

    public void buscar() {
        if (comercializadora == null) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_WARN, "Debe seleccionar Comercializadora");
            return;
        }
        String codTerm = (terminal != null) ? terminal.getCodigo() : "-1";
        listaFactoresGuardada = factorServicio.obtenerFactores(comercializadora.getCodigo(), codTerm, fechaInicial,
                fechaFinal);
        aplicarFiltroVigentes();
        if (listaFactores.isEmpty()) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_INFO,
                    "No se encontraron factores para los filtros seleccionados");
        }
    }

    public void aplicarFiltroVigentes() {
        if (listaFactoresGuardada == null) {
            listaFactoresGuardada = new ArrayList<>();
        }
        listaFactores = new ArrayList<>();
        if (!mostrarSoloVigentes) {
            listaFactores.addAll(listaFactoresGuardada);
        } else {
            Map<String, FactorCorreccion> p = new HashMap<>();
            for (FactorCorreccion f : listaFactoresGuardada) {
                String key = (f.getTerminal() != null ? f.getTerminal().getCodigo() : "") + "-" +
                        (f.getProducto() != null ? f.getProducto().getCodigo() : "");
                if (!p.containsKey(key)) {
                    p.put(key, f);
                } else {
                    if (f.getFecha() != null && p.get(key).getFecha() != null) {
                        if (f.getFecha().after(p.get(key).getFecha())) {
                            p.put(key, f);
                        }
                    }
                }
            }
            listaFactores.addAll(p.values());
            // Sort by Date Descending
            listaFactores.sort((f1, f2) -> {
                if (f1.getFecha() == null || f2.getFecha() == null)
                    return 0;
                return f2.getFecha().compareTo(f1.getFecha());
            });
        }
    }

    public void habilitarBusqueda() {
        if (dataUser.getUser() != null) {
            switch (dataUser.getUser().getNiveloperacion()) {
                case "cero":
                    habilitarComer = true;
                    habilitarTerminal = true;
                    break;
                case "adco":
                    habilitarComer = false;
                    habilitarTerminal = true;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    break;
                case "usac":
                    habilitarComer = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                        }
                    }
                    if (comercializadora != null && comercializadora.getCodigo() == null) {
                        this.dialogo(javax.faces.application.FacesMessage.SEVERITY_FATAL,
                                "La comercializadora se encuentra deshabilitada");
                    }
                    break;
                case "agco":
                    habilitarComer = false;
                    habilitarTerminal = false;
                    for (int i = 0; i < listaComercializadora.size(); i++) {
                        if (listaComercializadora.get(i).getCodigo()
                                .equals(dataUser.getUser().getCodigocomercializadora())) {
                            this.comercializadora = listaComercializadora.get(i);
                            break;
                        }
                    }
                    for (int i = 0; i < listaTerminales.size(); i++) {
                        if (listaTerminales.get(i).getCodigo().equals(dataUser.getUser().getCodigoterminal())) {
                            this.terminal = listaTerminales.get(i);
                            break;
                        }
                    }
                    break;
                default:
                    habilitarComer = true;
                    habilitarTerminal = true;
                    break;
            }
        }
    }

    public void nuevo() {
        panelNuevo = true;
        comerNuevo = comercializadora;
        termNuevo = terminal;
        fechaActual = new Date();
        listaProductosNuevos = new ArrayList<>();
    }

    public void cancelar() {
        panelNuevo = false;
        listaProductosNuevos = new ArrayList<>();
    }

    public void buscarProductos() {
        if (comerNuevo == null || termNuevo == null) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_WARN,
                    "Debe seleccionar Comercializadora y Terminal");
            return;
        }
        listaProductosNuevos = factorServicio.buscarProductosPorTerminal(termNuevo.getCodigo());
        if (listaProductosNuevos.isEmpty()) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_INFO,
                    "No se encontraron productos para los filtros seleccionados");
        }
    }

    public void guardar() {
        if (listaProductosNuevos == null || listaProductosNuevos.isEmpty()) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_WARN, "No hay datos para guardar");
            return;
        }

        boolean ok = factorServicio.guardar(listaProductosNuevos, comerNuevo.getCodigo(), termNuevo.getCodigo(),
                fechaActual, x != null ? x.getCodigo() : "SISTEMA");
        if (ok) {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_INFO, "Factores guardados correctamente");
            cancelar();
            buscar(); // Refresh main list if possible with current filters
        } else {
            this.dialogo(javax.faces.application.FacesMessage.SEVERITY_ERROR, "Error al guardar los factores");
        }
    }

    public void eliminar(FactorCorreccion f) {
        // Implementation for single delete if needed
    }

    // Getters and Setters
    public List<ComercializadoraBean> getListaComercializadora() {
        return listaComercializadora;
    }

    public void setListaComercializadora(List<ComercializadoraBean> listaComercializadora) {
        this.listaComercializadora = listaComercializadora;
    }

    public List<Terminal> getListaTerminales() {
        return listaTerminales;
    }

    public void setListaTerminales(List<Terminal> listaTerminales) {
        this.listaTerminales = listaTerminales;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<FactorCorreccion> getListaFactores() {
        return listaFactores;
    }

    public void setListaFactores(List<FactorCorreccion> listaFactores) {
        this.listaFactores = listaFactores;
    }

    public List<FactorCorreccion> getListaProductosNuevos() {
        return listaProductosNuevos;
    }

    public void setListaProductosNuevos(List<FactorCorreccion> listaProductosNuevos) {
        this.listaProductosNuevos = listaProductosNuevos;
    }

    public ComercializadoraBean getComercializadora() {
        return comercializadora;
    }

    public void setComercializadora(ComercializadoraBean comercializadora) {
        this.comercializadora = comercializadora;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
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

    public FactorCorreccion getFactorSeleccionado() {
        return factorSeleccionado;
    }

    public void setFactorSeleccionado(FactorCorreccion factorSeleccionado) {
        this.factorSeleccionado = factorSeleccionado;
    }

    public boolean isPanelNuevo() {
        return panelNuevo;
    }

    public void setPanelNuevo(boolean panelNuevo) {
        this.panelNuevo = panelNuevo;
    }

    public boolean isMostrarSoloVigentes() {
        return mostrarSoloVigentes;
    }

    public void setMostrarSoloVigentes(boolean mostrarSoloVigentes) {
        this.mostrarSoloVigentes = mostrarSoloVigentes;
    }

    public ComercializadoraBean getComerNuevo() {
        return comerNuevo;
    }

    public void setComerNuevo(ComercializadoraBean comerNuevo) {
        this.comerNuevo = comerNuevo;
    }

    public Terminal getTermNuevo() {
        return termNuevo;
    }

    public void setTermNuevo(Terminal termNuevo) {
        this.termNuevo = termNuevo;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public boolean isHabilitarComer() {
        return habilitarComer;
    }

    public void setHabilitarComer(boolean habilitarComer) {
        this.habilitarComer = habilitarComer;
    }

    public boolean isHabilitarTerminal() {
        return habilitarTerminal;
    }

    public void setHabilitarTerminal(boolean habilitarTerminal) {
        this.habilitarTerminal = habilitarTerminal;
    }
}
