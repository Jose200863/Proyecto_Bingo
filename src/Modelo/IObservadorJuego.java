/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public interface IObservadorJuego {
    void onNumeroMarcado(int numero);
    void onCartonGanador(String id, String tipoVictoria);
    void onJuegoReiniciado();
}
