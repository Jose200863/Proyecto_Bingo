/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public class ComandoMarcarNumero implements IComando{
    private GestorMemoria gestor;
    private int numero;

    public ComandoMarcarNumero( int numero) {
        this.gestor = GestorMemoria.obtenerInstancia();
        this.numero = numero;
    }
    
    @Override
    public void ejecutar() {
      marcarEnCartones();
        marcarEnTablero();
        actualizarTombola();
    }

    @Override
    public void deshacer() {
        desmarcarEnCartones();
        desmarcarEnTablero();
    }
    
      private void marcarEnCartones() {
        for(Carton carton : gestor.obtenerCartones()){
            carton.marcarNumero(numero);
        }
    }
  
    private void marcarEnTablero() {
        Tablero tablero = gestor.obtenerTablero();
        tablero.marcarNumero(numero);
    }
    
    private void actualizarTombola() {
        Tombola tombola = gestor.obtenerTombola();
        tombola.setUltimoNumero(numero);
        if (!tombola.getNumerosSalidos().contains(numero)) {
            tombola.getNumerosSalidos().add(numero);
        }
        tombola.getNumerosDisponibles().remove(Integer.valueOf(numero));
    }
    
        private void desmarcarEnCartones() {
        for(Carton carton : gestor.obtenerCartones()) {
            carton.desmarcarNumero(numero);
        }
    }
    
    
    private void desmarcarEnTablero() {
        Tablero tablero = gestor.obtenerTablero();
        tablero.desmarcarNumero(numero);
    }
}