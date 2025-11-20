/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Modelo.Carton;
import Modelo.CartonAutomaticoFactory;
import Modelo.CartonManualFactory;
import Modelo.EnumModoJuego;
import Modelo.GestorMemoria;
import Modelo.ICartonFactory;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ServicioCartones {
    private GestorMemoria repositorio;

    public ServicioCartones(GestorMemoria repositorio) {
        this.repositorio = repositorio;
    }
    
    public Carton crearCarton(String id, EnumModoJuego modo) {
        ICartonFactory factory = crearFactory(modo);
        Carton carton = factory.crearCarton(id);
        repositorio.agregarCarton(carton);
        return carton;
    }
    
    public void eliminarCarton(String id) {
        repositorio.eliminarCarton(id);
    }
    
    public List<Carton> obtenerCartones() {
        return repositorio.obtenerCartones();
    }
     
    public Carton obtenerCarton(String id) {
        return repositorio.obtenerCarton(id);
    }
    
    public boolean validarNumeroEnColumna(int numero, int columna) {
        switch(columna) {
            case 0: return numero >= 1 && numero <= 15;
            case 1: return numero >= 16 && numero <= 30;
            case 2: return numero >= 31 && numero <= 45;
            case 3: return numero >= 46 && numero <= 60;
            case 4: return numero >= 61 && numero <= 75;
            default: return false;
        }
    }
    
    public boolean validarCartonCompleto(Carton carton) {
        int[][] numeros = carton.getNumerosCarton();
        boolean[][] marcados = carton.getNumeroMarcados();
        java.util.Set<Integer> numerosVistos = new java.util.HashSet<>();
        
        if (!marcados[2][2]) {//verifica el centro
            return false;
        }
        
        // Contar números y verificar rangos por columna
        int contadorNumeros = 0;
        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 5; col++) {
                if (fila == 2 && col == 2) {
                    continue; // Centro libre
                }
                
                int numero = numeros[fila][col];
                if (numero == 0 || numero < 1 || numero > 75) {
                    return false;                 }
                
                
                // rango
                if (!validarNumeroEnColumna(numero, col)) {
                    return false;
                }
                
                // Verificar duplicados
                if (numerosVistos.contains(numero)) {
                    return false;
                }
                numerosVistos.add(numero);
                
                contadorNumeros++;
            }
        }
        
        // exactamente 24
        return contadorNumeros == 24;
    }
        private ICartonFactory crearFactory(EnumModoJuego modo) {
        if (modo == EnumModoJuego.AUTOMATICO) {
            return new CartonAutomaticoFactory();
        } else {
            return new CartonManualFactory();
        }
    }
}
