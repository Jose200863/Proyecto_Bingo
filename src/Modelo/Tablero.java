/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Arrays;

/**
 *
 * @author Braya
 */
public class Tablero {
    private boolean[] numeroMarcados;

    public boolean[] getNumeroMarcados() {
        return numeroMarcados;
    }

    public void setNumeroMarcados(boolean[] numeroMarcados) {
        this.numeroMarcados = numeroMarcados;
    }
    
    public Tablero() {
        this.numeroMarcados = new boolean[76];
    }  
     public void reiniciar() {
        Arrays.fill(numeroMarcados, false);
    }
    public void marcarNumero(int numero) {
        if (numero >= 1 && numero <= 75) {
            numeroMarcados[numero] = true;
        }
    }
       public void desmarcarNumero(int numero) {
        if (numero >= 1 && numero <= 75) {
            numeroMarcados[numero] = false;
        }
    }
}
