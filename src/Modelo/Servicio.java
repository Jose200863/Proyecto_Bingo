/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Braya
 */
public class Servicio extends SujetoJuegoObserver {
    private GestorMemoria gestor;
    private IEstrategiaGanador estrategia;
    private Stack<IComando> historial;
    private EnumTipoJuego tipoJuego;
    private EnumModoJuego modoJuego;

    public Servicio() {
        this.gestor = GestorMemoria.obtenerInstancia();
        this.estrategia = new EstrategiaNormal();
        this.historial = new Stack<>();
        this.tipoJuego = tipoJuego.NORMAL;
        this.modoJuego = modoJuego.MANUAL;
    }
    
    public void establecerTipoJuego(EnumTipoJuego tipo){
        this.tipoJuego = tipo;
        switch(tipo){
            case NORMAL:
                estrategia = new EstrategiaNormal();
                break;
            case CUATRO_ESQUINAS:
                estrategia = new EstrategiaCuatroEsquinas();
                break;
            case CARTON_LLENO:
                estrategia = new EstrategiaCartonLleno();
        }
    }
    
    public void establecerModoJuego(EnumModoJuego modo){
        this.modoJuego = modo;
    }
    
    public EnumTipoJuego getTipoJuego(){
        return tipoJuego;
    }
    
    public EnumModoJuego getModoJuego(){
        return modoJuego;
    }
    
    public Carton crearCarton(String id, EnumModoJuego modo){
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del cartón no puede estar vacío.");
        }

        // Evitar ID duplicados
        if (gestor.obtenerCarton(id) != null) {
            throw new IllegalArgumentException("Ya existe un cartón con el ID: " + id);
        }

        ICartonFactory factory = modo == EnumModoJuego.AUTOMATICO ?
                new CartonAutomaticoFactory() : new CartonManualFactory();

        Carton carton = factory.crearCarton(id);

        // Agregar inmediatamente al gestor para que la UI pueda editarlo (también para automático)
        gestor.agregarCarton(carton);
        return carton;
    }

    public void validarYGuardarCartonManual(Carton carton) {
        //OBTENER DATOS Y ESTRUCTURA DE VALIDACIÓN
        int[][] numeros = carton.getNumerosCarton();
        GestorMemoria gestor = GestorMemoria.obtenerInstancia();

        Set<Integer> numerosUsados = new HashSet<>();

        // VALIDACIÓN DE COMPLETITUD Y REGLAS (24 CELDAS)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // Omitir la celda central (2, 2)
                if (i == 2 && j == 2) {
                    continue;
                }

                int valorCelda = numeros[i][j];

                // Verificar Completitud (que no haya valores vacíos/cero)
                if (valorCelda <= 0) {
                    throw new IllegalArgumentException("El cartón con ID " + carton.getId() + " está incompleto. Faltan números.");
                }

                // Verificar Rango por Columna (j=0 es Columna B, j=1 es Columna I, etc.)
                if (!validarNumeroEnRangoParaColumna(valorCelda, j)) {
                    throw new IllegalArgumentException("El número " + valorCelda + " está fuera del rango permitido para la columna " + getLetraColumna(j) + ".");
                }

                if (numerosUsados.contains(valorCelda)) {
                    throw new IllegalArgumentException("El número " + valorCelda + " está repetido en el cartón. Todos los números deben ser únicos.");
                }
                numerosUsados.add(valorCelda);
            }
        }
    }

    public boolean eliminarCarton(String id) {
        Carton cartonAEliminar = gestor.obtenerCarton(id);
        if (cartonAEliminar != null) {
            gestor.eliminarCarton(id);
            return true;
        }
        return false;
    }

    public boolean validarNumeroCarton(int numero, int columna) {
        int rangoMinimo = columna * 15 + 1;
        int rangoMaximo = columna * 15 + 15;
        return numero >= rangoMinimo && numero <= rangoMaximo;
    }
    
    public boolean numeroExisteEnCarton(Carton carton, int numero){
        int[][] numeros = carton.getNumerosCarton();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(numeros[i][j] == numero){
                    return true;
                }
            }
        }
        return false;
    }
    
    public void marcarNumero(int numero) {
        // Lógica del Modelo: Usa el patrón Comando para marcar el número 
        IComando comando = new ComandoMarcarNumero(numero);
        comando.ejecutar();
        this.historial.push(comando);

        // Patrón Observer: Notifica a todos los observadores
        notificarNumeroMarcado(numero);

        // Verifica si alguien ganó después de la marca
        verificarGanadores();
    }
    
    public void desmarcarUltimoNumero(){
        if(!historial.isEmpty()){
            IComando comando = historial.pop();
            comando.deshacer();
        }
    }
    
    private void verificarGanadores(){
        for(Carton carton : gestor.obtenerCartones()) {
            if(estrategia.esGanador(carton)) {
                String tipo = estrategia.obtenerTipoVictoria(carton);
                notificarCartonGanador(carton.getId(), tipo);
            }
        }
    }

    public void reiniciar() {
        gestor.reiniciarJuego();
        historial.clear();
        notificarJuegoReiniciado();
    }

    public int generarNumerosAleatorio() {
        Tombola tombola = gestor.obtenerTombola();
        List<Integer> disponibles = tombola.getNumerosDisponibles();
        if (disponibles.isEmpty()) {
            return -1;
        }
        Random random = new Random();
        int index = random.nextInt(disponibles.size());
        return disponibles.get(index);
    }

    public boolean numeroExisteEnOtraCelda(Carton carton, int numero, int filaActual, int columnaActual) {
        // Si se está borrando (numero=0), no hay riesgo de duplicado.
        if (numero <= 0) {
            return false;
        }

        int[][] numeros = carton.getNumerosCarton();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (i == filaActual && j == columnaActual) {
                    continue;
                }

                if (numeros[i][j] == numero) {
                    return true;
                }
            }
        }
        return false;
    }

    // Métodos auxiliares que usaría la clase Servicio
    private boolean validarNumeroEnRangoParaColumna(int numero, int columna) {
        // Implementación de rangos: B(1-15), I(16-30), N(31-45), G(46-60), O(61-75)
        return switch (columna) {
            case 0 ->
                numero >= 1 && numero <= 15;
            case 1 ->
                numero >= 16 && numero <= 30;
            case 2 ->
                numero >= 31 && numero <= 45;
            case 3 ->
                numero >= 46 && numero <= 60;
            case 4 ->
                numero >= 61 && numero <= 75;
            default ->
                false; 
        };
    }

    private String getLetraColumna(int columna) {
        return switch (columna) {
            case 0 ->
                "B";
            case 1 ->
                "I";
            case 2 ->
                "N";
            case 3 ->
                "G";
            case 4 ->
                "O";
            default ->
                "";
        };
    }
}
