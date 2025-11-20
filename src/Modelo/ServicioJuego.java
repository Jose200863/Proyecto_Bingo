/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Modelo.Carton;
import Modelo.ComandoMarcarNumero;
import Modelo.GestorMemoria;
import Modelo.IComando;
import Modelo.IEstrategiaGanador;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ServicioJuego {
    private GestorMemoria repositorio;
    private IEstrategiaGanador estrategiaGanador;

    public ServicioJuego(GestorMemoria repositorio, IEstrategiaGanador estrategiaGanador) {
        this.repositorio = repositorio;
        this.estrategiaGanador = estrategiaGanador;
    }
    public boolean validarNumero(int numero) {
        return numero >= 1 && numero <= 75;
    }
    
    public void marcarNumero(int numero) {
        if (!validarNumero(numero)) {
            return;
        }
        
        IComando comando = new ComandoMarcarNumero(numero);
        comando.ejecutar();
    }
    
    public void desmarcarNumero(int numero) {
        if (!validarNumero(numero)) {
            return;
        }
        
        IComando comando = new ComandoMarcarNumero(numero);
        comando.deshacer();
    }
        public List<ResultadoGanador> verificarGanadores() {
        List<ResultadoGanador> ganadores = new ArrayList<>();
        List<Carton> cartones = repositorio.obtenerCartones();
        
        for (Carton carton : cartones) {
            if (estrategiaGanador.esGanador(carton)) {
                String tipoVictoria = estrategiaGanador.obtenerTipoVictoria(carton);
                ganadores.add(new ResultadoGanador(carton.getId(), tipoVictoria));
            }
        }
        
        return ganadores;
    }
        
    public void reiniciarJuego() {
        repositorio.reiniciarJuego();
    }
    public void limpiarCartones() {
        repositorio.limpiarCartones();
    }
    public void reiniciarTablero() {
        repositorio.reiniciarTablero();
    }
    
    public static class ResultadoGanador {
        private String idCarton;
        private String tipoVictoria;
        
        public ResultadoGanador(String idCarton, String tipoVictoria) {
            this.idCarton = idCarton;
            this.tipoVictoria = tipoVictoria;
        }
        
        public String getIdCarton() {
            return idCarton;
        }
        
        public String getTipoVictoria() {
            return tipoVictoria;
        }
    }
}
