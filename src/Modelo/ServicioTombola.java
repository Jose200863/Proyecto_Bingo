/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Modelo.GestorMemoria;
import Modelo.Tombola;

/**
 *
 * @author kevin
 */
public class ServicioTombola {
    private GestorMemoria repositorio;

    public ServicioTombola(GestorMemoria repositorio) {
        this.repositorio = repositorio;
    }
    
    public Integer generarNumeroAleatorio() {
        Tombola tombola = repositorio.obtenerTombola();
        return tombola.sacarNumeroAleatorio();
    }
    
    public boolean ingresarNumeroManual(int numero) {
        Tombola tombola = repositorio.obtenerTombola();
        return tombola.ingresarNumeroManual(numero);
    }
    
    public Integer obtenerUltimoNumero() {
        return repositorio.obtenerTombola().getUltimoNumero();
    }
     
    public void reiniciarTombola() {
        repositorio.reiniciarTombola();
    }
    
    public java.util.List<Integer> obtenerNumerosDisponibles() {
        return repositorio.obtenerTombola().getNumerosDisponibles();
    }
    
    public java.util.List<Integer> obtenerNumerosSalidos() {
        return repositorio.obtenerTombola().getNumerosSalidos();
    }
}
