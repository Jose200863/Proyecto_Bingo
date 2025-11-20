/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Braya
 */
public class GestorMemoria implements IGestorMemoria{
    private static GestorMemoria instancia;
    private List<Carton> cartones;
    private Tablero tablero;
    private Tombola tombola;

    public GestorMemoria() {
        this.cartones = new ArrayList<>();
        this.tablero = new Tablero();
        this.tombola = new Tombola();
    }
    
    //Se creo el patron singleton para tener una unica intancia
    public static GestorMemoria obtenerInstancia(){
        if(instancia==null){
            instancia = new GestorMemoria();
        }
        return instancia;
    }

    @Override
    public void agregarCarton(Carton carton) {
        cartones.add(carton);
    }

    @Override
    public void eliminarCarton(String id) {
        cartones.removeIf(c -> c.getId().equals(id));
    }

    @Override
    public List<Carton> obtenerCartones() {
        return new ArrayList<>(cartones);
    }

    @Override
    public Carton obtenerCarton(String id) {
        return cartones.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Tablero obtenerTablero() {
        return tablero;
    }
    
    @Override
    public Tombola obtenerTombola() {
        return tombola;
    }

    @Override
    public void reiniciarJuego() {
        for (Carton carton : cartones) {
            boolean[][] marcados = carton.getNumeroMarcados();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    marcados[i][j] = false;
                }
            }
            marcados[2][2] = true; // Centro libre
        }
        tablero = new Tablero();
        tombola = new Tombola();
    }

    @Override
    public void limpiarCartones() {
           for (Carton carton : cartones) {
            carton.limpiarMarcas();
        }
    }

    @Override
    public void reiniciarTablero() {
      tablero.reiniciar();   }
    

    @Override
    public void reiniciarTombola() {
     tombola = new Tombola();
    }
}
