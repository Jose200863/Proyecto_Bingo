/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public class EstrategiaCartonLleno implements IEstrategiaGanador{

    @Override
    public boolean esGanador(Carton carton) {
        boolean[][] marcados = carton.getNumeroMarcados();
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(!marcados[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String obtenerTipoVictoria(Carton carton) {
        return esGanador(carton) ? "Carton Lleno" : null;
    }
    
}
