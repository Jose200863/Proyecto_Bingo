/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Carton;
import Modelo.EnumModoJuego;
import Modelo.EnumTipoJuego;
import Modelo.GestorMemoria;
import Modelo.IObservadorJuego;
import Modelo.Servicio;
import Vista.IVista;
import java.util.List;

/**
 *
 * @author Braya
 */
public class Controlador implements IObservadorJuego {

    private IVista vista;
    private Servicio servicio;
    private Modelo.ServicioCartones servicioCartones;
    private Modelo.ServicioJuego servicioJuego;
    private Modelo.ServicioTombola servicioTombola;

    public Controlador(IVista vista) {
        this.vista = vista;
        this.servicio = new Servicio();
        this.servicio.agregarObservador(this);

        // Conectar servicios especializados con el Gestor de Memoria
        Modelo.GestorMemoria gm = Modelo.GestorMemoria.obtenerInstancia();
        this.servicioCartones = new Modelo.ServicioCartones(gm);
        this.servicioJuego = new Modelo.ServicioJuego(gm, new Modelo.EstrategiaNormal());
        this.servicioTombola = new Modelo.ServicioTombola(gm);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void crearCarton(String id, EnumModoJuego modo, EnumTipoJuego tipo) {
        // 1. Validación de la Regla de Negocio
        if (id.isEmpty()) {
            // Pide a la IVista (VentanaPrincipal) que muestre el error
            vista.mostrarError("Debe ingresar un ID válido para crear el cartón.");
            return; // Detiene la ejecución
        }

        // 2. Configurar Juego (Lógica de Negocio)
        // Actualizar estado del juego (tipo/modo) en el servicio central
        servicio.establecerTipoJuego(tipo);
        servicio.establecerModoJuego(modo);

        // 3. Crear Cartón usando el Servicio especializado de cartones
        Carton carton = servicioCartones.crearCarton(id, modo);

        // 4. Mostrar Éxito y Actualizar Vistas
        vista.mostrarCarton(carton);
        vista.mostarMensaje("Cartón " + id + " creado y Tipo de Juego establecido.");

        // La VentanaPrincipal recibirá la llamada a 'mostrarCarton(carton)' 
        // y DELEGARÁ ese cartón a la VentanaCartones para que se muestre en la tabla.
    }

    public void solicitarIngresoManual(String idCarton, int fila, int columna, String valor) {
        // 1. **VALIDACIÓN DE VISTA (Modo y Cartón Activo)**
        EnumModoJuego modoActual = servicio.getModoJuego();

        if (modoActual != EnumModoJuego.MANUAL) {
            vista.mostrarError("La edición manual solo está permitida en Modo Manual.");
            return;
        }
        
        if (idCarton == null || idCarton.isEmpty()) {
            vista.mostrarError("Debe crear o seleccionar un cartón antes de ingresar números.");
            return;
        }
        
        // 2. **VALIDACIÓN DE MODELO (Celda Central "LIBRE")**
        if (fila == 2 && columna == 2) {
            vista.mostrarError("La celda central no se puede modificar (es 'LIBRE').");
            return;
        }

        // 3. **VALIDACIÓN DE ENTRADA (Conversión a Numérico)**
        int numeroIngresado;
        try {
            numeroIngresado = Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            vista.mostrarError("El valor ingresado no es un número válido.");
            return;
        }

        // 4. **VALIDACIÓN DE RANGO / LÓGICA DE NEGOCIO**
        if (numeroIngresado > 0) { // Si el usuario ingresa un número, se valida el rango.
            if (!servicio.validarNumeroCarton(numeroIngresado, columna)) {
                int rangoMin = columna * 15 + 1;
                int rangoMax = columna * 15 + 15;
                vista.mostrarError("Número fuera de rango (" + rangoMin + "-" + rangoMax + ") para la columna.");
                return;
            }
        }

        // 5. **DELEGACIÓN AL MÉTODO FINAL**
        // Si pasa todas las validaciones, actualizamos el modelo.
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
            // Usa el nuevo método de Servicio que excluye la celda actual
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
        // Usar el servicio central para marcar y notificar
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
        // 1. Validación de Regla de Negocio (El Controlador maneja el ID vacío/nulo)
        if (id == null || id.isEmpty()) {
            vista.mostrarError("No hay un cartón seleccionado en la vista para eliminar.");
            return;
        }

        // 2. Lógica del Modelo
        // Usar servicio de cartones para eliminar
        boolean eliminado;
        try {
            servicioCartones.eliminarCarton(id);
            eliminado = true;
        } catch (Exception e) {
            eliminado = false;
        }

        if (eliminado) {
            // Notificar a la Vista Principal, que delegará la limpieza visual a VentanaCartones.
            vista.eliminarCarton(id);
            vista.mostarMensaje("Cartón '" + id + "' eliminado correctamente.");
        } else {
            vista.mostrarError("Error: No se pudo encontrar o eliminar el cartón con ID: " + id);
        }
    }
    
    public void finalizarCarton(String idCarton) {
        // 1. EL CONTROLADOR valida que el cartón existe antes de llamar al Servicio
        GestorMemoria gestor = GestorMemoria.obtenerInstancia();
        Carton carton = gestor.obtenerCarton(idCarton);

        if (carton == null) {
            vista.mostrarError("No se encontró el cartón con ID: " + idCarton);
            return;
        }

        try {
            // 2. EL CONTROLADOR LLAMA AL SERVICIO para validar y actualizar el Modelo
            // El servicio lanzará una excepción si la validación falla
            servicio.validarYGuardarCartonManual(carton);

            // 3. Éxito: EL CONTROLADOR ORDENA a la Vista un mensaje de éxito.
            vista.mostarMensaje("¡Cartón " + idCarton + " completado y listo para jugar!");

            // 4. EL CONTROLADOR ORDENA a la Vista deshabilitar la tabla
            // Asumo que tienes un método así en IVista (si no, créalo)
            // vista.habilitarEdicionCarton(false);
        } catch (IllegalArgumentException e) {
            // 5. Fallo: EL CONTROLADOR ORDENA a la Vista un mensaje de error.
            vista.mostrarError(e.getMessage());
        }
    }

    @Override
    public void onNumeroMarcado(int numero) {
        // 1. La Vista (VentanaPrincipal) actualiza el Tablero
        vista.actualizarTablero(numero);

        // 2. La Vista muestra el número en la Tómbola
        vista.mostarUltimoNumero(numero);

        // 3. MUY IMPORTANTE: Se asume que VentanaCartones y VentanaPrincipal
        // tienen la lógica para que al llamar a actualizarTablero() o
        // actualizarVista(), se repinten los cartones con las nuevas marcas.
        vista.actualizarVista();
    }

    @Override
    public void onCartonGanador(String id, String tipoVictoria) {
        // 1. **¡NUEVO!** Forzar el repintado visual de todos los cartones.
        // Esto asegura que la última marca que causó la victoria se pinte de verde.
        vista.actualizarVista();

        // 2. Mostrar el mensaje de ganador (Esta parte ya te funciona)
        vista.mostarGanador(id, tipoVictoria);
    }

    @Override
    public void onJuegoReiniciado() {
        vista.reiniciarVista();
    }
    
    
    
}
