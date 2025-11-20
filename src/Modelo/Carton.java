/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Objects;

/**
 *
 * @author Braya
 */
public class Carton {
    private String id;
    private int[][] numerosCarton;
    private boolean[][] numeroMarcados;

    public String getId() {
        return id;
    }

    public int[][] getNumerosCarton() {
        return numerosCarton;
    }

    public boolean[][] getNumeroMarcados() {
        return numeroMarcados;
    }

    public Carton(String id) {
        this.id = Objects.requireNonNull(id,"El id no puede ser un valor nulo");
        this.numerosCarton = new int[5][5];
        this.numeroMarcados = new boolean[5][5];
        // Casilla central marcada por defecto
        this.numeroMarcados[2][2] = true;
    }
    
       public void limpiarMarcas() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                numeroMarcados[i][j] = false;
            }
        }
        numeroMarcados[2][2] = true; // Centro libre
    }
    public void marcarNumero(int numero) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numerosCarton[i][j] == numero) {
                    numeroMarcados[i][j] = true;
                    return;
                }
            }
        }
    }
    
        public void desmarcarNumero(int numero) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numerosCarton[i][j] == numero && !(i == 2 && j == 2)) {
                    numeroMarcados[i][j] = false;
                    return;
                }
            }
        }
    }
}
