/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Vista;

import Modelo.Carton;

/**
 *
 * @author Braya
 */
public interface IVista {
    void mostrarCarton(Carton carton);
    void actualizarTablero(int numero);
    void insertarNumeroCarton(Carton Carton);
    void eliminarCarton(String id);
    void mostarUltimoNumero(int numero);
    void mostarGanador(String id, String tipoVictoria);
    void mostarMensaje(String mensaje);
    void mostrarError(String mensaje);
    void actualizarVista();
    void reiniciarVista();
}
