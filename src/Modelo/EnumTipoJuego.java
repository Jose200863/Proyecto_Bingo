/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public enum EnumTipoJuego {
    NORMAL("Juego Norma"),
    CUATRO_ESQUINAS("Juego Cuatro Esquinas"),
    CARTON_LLENO("Juego Carton Lleno");
    
    private final String tipoJuego;

    private EnumTipoJuego(String tipoJuego) {
        this.tipoJuego = tipoJuego;
    }

    public String getTipoJuego() {
        return tipoJuego;
    }

    @Override
    public String toString() {
        return "EnumTipoJuego{" + "tipoJuego=" + tipoJuego + '}';
    }

}
