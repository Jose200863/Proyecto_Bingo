/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Braya
 */
public class CartonAutomaticoFactory implements ICartonFactory{

    @Override
    public Carton crearCarton(String id) {
        Carton carton = new Carton(id);
        generarNumerosAutomaticos(carton);
        return carton;
    }

    private void generarNumerosAutomaticos(Carton carton) {
        int[][] numeros = carton.getNumerosCarton();
        Random random = new Random();

        for (int col = 0; col < 5; col++) {
            Set<Integer> numerosUsados = new HashSet<>();
            int rangoMin = col * 15 + 1;
            int rangoMax = col * 15 + 15;

            for (int fila = 0; fila < 5; fila++) {
                if (fila == 2 && col == 2) {
                    numeros[fila][col] = 0; // Centro libre
                    continue;
                }
                
                int numero;
                do {
                    numero = random.nextInt(rangoMax - rangoMin + 1) + rangoMin;
                } while (numerosUsados.contains(numero));

                numerosUsados.add(numero);
                numeros[fila][col] = numero;
            }
        }
    }

}
