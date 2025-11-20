/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

import java.util.List;

/**
 *
 * @author Braya
 */
public interface IGestorMemoria {
    void agregarCarton(Carton carton);
    void eliminarCarton(String id);
    List<Carton> obtenerCartones();
    Carton obtenerCarton(String id);
    Tablero obtenerTablero();
    Tombola obtenerTombola();
    void reiniciarJuego();
    void limpiarCartones();
    void reiniciarTablero();
    void reiniciarTombola();
}
