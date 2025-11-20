/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Carton;
import Modelo.EnumModoJuego;
import Modelo.EnumTipoJuego;
import Modelo.EstrategiaNormal;
import Modelo.GestorMemoria;
import Modelo.IObservadorJuego;
import Modelo.Servicio;
import Modelo.ServicioCartones;
import Modelo.ServicioJuego;
import Modelo.ServicioTombola;
import Vista.IVista;
import java.util.List;

/**
 *
 * @author Braya
 */
public class Controlador implements IObservadorJuego {

    private IVista vista;
    private Servicio servicio;
    private ServicioCartones servicioCartones;
    private ServicioJuego servicioJuego;
    private ServicioTombola servicioTombola;

    public Controlador(IVista vista) {
        this.vista = vista;
        this.servicio = new Servicio();
        this.servicio.agregarObservador(this);

        // Conectar servicios especializados con el Gestor de Memoria
        GestorMemoria gm = Modelo.GestorMemoria.obtenerInstancia();
        this.servicioCartones = new ServicioCartones(gm);
        this.servicioJuego = new ServicioJuego(gm, new EstrategiaNormal());
        this.servicioTombola = new ServicioTombola(gm);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void crearCarton(String id, EnumModoJuego modo, EnumTipoJuego tipo) {
        //  Validación de la Regla de Negocio
        if (id.isEmpty()) {
            vista.mostrarError("Debe ingresar un ID válido para crear el cartón.");
            return; 
        }

        // Configurar Juego (Lógica de Negocio)
        // Actualizar estado del juego (tipo/modo) en el servicio central
        servicio.establecerTipoJuego(tipo);
        servicio.establecerModoJuego(modo);

        // Crear Cartón usando el Servicio especializado de cartones
        Carton carton = servicioCartones.crearCarton(id, modo);

        vista.mostrarCarton(carton);
        vista.mostarMensaje("Cartón " + id + " creado y Tipo de Juego establecido.");

    }

    public void solicitarIngresoManual(String idCarton, int fila, int columna, String valor) {
        // VALIDACIÓN DE VISTA (Modo y Cartón Activo)
        EnumModoJuego modoActual = servicio.getModoJuego();

        if (modoActual != EnumModoJuego.MANUAL) {
            vista.mostrarError("La edición manual solo está permitida en Modo Manual.");
            return;
        }
        
        if (idCarton == null || idCarton.isEmpty()) {
            vista.mostrarError("Debe crear o seleccionar un cartón antes de ingresar números.");
            return;
        }
        
        if (fila == 2 && columna == 2) {
            vista.mostrarError("La celda central no se puede modificar (es 'LIBRE').");
            return;
        }

        int numeroIngresado;
        try {
            numeroIngresado = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            vista.mostrarError("El valor ingresado no es un número válido.");
            return;
        }

        // VALIDACIÓN DE RANGO / LÓGICA DE NEGOCIO**
        if (numeroIngresado > 0) { // Si el usuario ingresa un número, se valida el rango.
            if (!servicio.validarNumeroCarton(numeroIngresado, columna)) {
                int rangoMin = columna * 15 + 1;
                int rangoMax = columna * 15 + 15;
                vista.mostrarError("Número fuera de rango (" + rangoMin + "-" + rangoMax + ") para la columna.");
                return;
            }
        }

        solicitarIngresoManual(idCarton, fila, columna, numeroIngresado);
    }

    public void solicitarIngresoManual(String idCarton, int fila, int columna, int numero) {
        GestorMemoria gestor = GestorMemoria.obtenerInstancia();
        Carton carton = gestor.obtenerCarton(idCarton);

        if (carton == null) {
            vista.mostrarError("Error interno: Cartón no encontrado para el ID: " + idCarton);
            return;
        }

        // Validación de Repetición (delegada al Servicio)
        if (numero > 0) {
            if (servicio.numeroExisteEnOtraCelda(carton, numero, fila, columna)) { 
                 vista.mostrarError("El número " + numero + " ya existe en otra posición de este cartón.");
                 return;
            }
        }

        // Actualización del Modelo (Guarda el número o 0 si se está borrando)
        int[][] numeros = carton.getNumerosCarton();
        numeros[fila][columna] = numero;

        // Notificación a la Vista (para actualizar el JLabel)
        vista.insertarNumeroCarton(carton);
    }
   
    public void marcarNumeroJuego(int numero){
        servicio.marcarNumero(numero);
    }
    
    public void generarNumeroAutomatico() {
        Integer numero = servicioTombola.generarNumeroAleatorio();
        if (numero == null) {
            vista.mostarMensaje("No hay más números disponibles");
            return;
        }
        servicio.marcarNumero(numero);
    }
    
    public void desmarcarNumero(){
        servicio.desmarcarUltimoNumero();
        vista.actualizarVista();
    }
    
    public void reiniciarJuego(){
        servicio.reiniciar();
    }
    
    public List<Carton> obtenerCartones(){
        return servicioCartones.obtenerCartones();
    }

    public void eliminarCarton(String id) {
        if (id == null || id.isEmpty()) {
            vista.mostrarError("No hay un cartón seleccionado en la vista para eliminar.");
            return;
        }

        boolean eliminado;
        try {
            servicioCartones.eliminarCarton(id);
            eliminado = true;
        } catch (Exception e) {
            eliminado = false;
        }

        if (eliminado) {
            vista.eliminarCarton(id);
            vista.mostarMensaje("Cartón '" + id + "' eliminado correctamente.");
        } else {
            vista.mostrarError("Error: No se pudo encontrar o eliminar el cartón con ID: " + id);
        }
    }
    
    public void finalizarCarton(String idCarton) {
        GestorMemoria gestor = GestorMemoria.obtenerInstancia();
        Carton carton = gestor.obtenerCarton(idCarton);

        if (carton == null) {
            vista.mostrarError("No se encontró el cartón con ID: " + idCarton);
            return;
        }

        try {
            servicio.validarYGuardarCartonManual(carton);

            vista.mostarMensaje("¡Cartón " + idCarton + " completado y listo para jugar!");

        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    @Override
    public void onNumeroMarcado(int numero) {
        vista.actualizarTablero(numero);
        vista.mostarUltimoNumero(numero);
        vista.actualizarVista();
    }

    @Override
    public void onCartonGanador(String id, String tipoVictoria) {
        vista.actualizarVista();
        vista.mostarGanador(id, tipoVictoria);
    }

    @Override
    public void onJuegoReiniciado() {
        vista.reiniciarVista();
    } 
}
