/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public class EstrategiaCuatroEsquinas implements IEstrategiaGanador{

    @Override
    public boolean esGanador(Carton carton) {
        boolean[][] marcados = carton.getNumeroMarcados();
        return marcados[0][0] && marcados[0][4] && marcados[4][0] && marcados[4][4];
    }

    @Override
    public String obtenerTipoVictoria(Carton carton) {
        return esGanador(carton) ? "Cuatro Esquinas" : null;
    }

}
