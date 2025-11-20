/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public class CartonManualFactory implements ICartonFactory {

    @Override
    public Carton crearCarton(String id) {
        return new Carton(id);
    }
    
}
