package Modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

/**
 *
 * @author Braya
 */
public enum EnumModoJuego {
    MANUAL("Juego manual"),
    AUTOMATICO("Juego automatico");
    
    private final String modoJuego;

    private EnumModoJuego(String modoJuego) {
        this.modoJuego = modoJuego;
    }

    public String getModoJuego() {
        return modoJuego;
    }

    @Override
    public String toString() {
        return "EnumModoJuego{" + "modoJuego=" + modoJuego + '}';
    }

}
