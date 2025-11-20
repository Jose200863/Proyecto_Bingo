/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Braya
 */
public class Tombola {

    private List<Integer> numerosDisponibles;
    private List<Integer> numerosSalidos;
    private Integer ultimoNumero;

    public Tombola() {
        this.numerosDisponibles = new ArrayList<>();
        this.numerosSalidos = new ArrayList<>();
        inicializar();
    }

    private void inicializar() {
        for (int i = 1; i <= 75; i++) {
            numerosDisponibles.add(i);
        }
    }

    public List<Integer> getNumerosDisponibles() {
        return numerosDisponibles;
    }

    public List<Integer> getNumerosSalidos() {
        return numerosSalidos;
    }

    public Integer getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }
       
    
    public Integer sacarNumeroAleatorio() {
        if (numerosDisponibles.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int indice = random.nextInt(numerosDisponibles.size());
        Integer numero = numerosDisponibles.remove(indice);
        numerosSalidos.add(numero);
        ultimoNumero = numero;
        return numero;
    }
    
    public boolean ingresarNumeroManual(int numero) {
        if (numero < 1 || numero > 75) {
            return false;
        }
        if (numerosDisponibles.contains(numero)) {
            numerosDisponibles.remove(Integer.valueOf(numero));
            if (!numerosSalidos.contains(numero)) {
                numerosSalidos.add(numero);
            }
            ultimoNumero = numero;  
            return true;
        }
     return false; // El número ya salió o no está disponible
    }
}
   
