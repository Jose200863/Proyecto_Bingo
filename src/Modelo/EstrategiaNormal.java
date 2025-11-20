/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Braya
 */
public class EstrategiaNormal implements IEstrategiaGanador {

    @Override
    public boolean esGanador(Carton carton) {
        return verificarHorizontal(carton) || verificarVertical(carton) || 
               verificarDiagonal(carton) ||  verificarCuatroEsquinas(carton);
    }

    @Override
    public String obtenerTipoVictoria(Carton carton) {
        if(verificarHorizontal(carton)){
            return "Horizontal";
        }
        if(verificarVertical(carton)){
            return "vertical";
        }
        if(verificarDiagonal(carton)){
            return "Diagonal";
        }
        if(verificarCuatroEsquinas(carton)){
            return "Cuatro Esquinas";
        }
        return null;
    }
    
    private boolean verificarHorizontal(Carton carton){
        boolean[][] marcados = carton.getNumeroMarcados();
        for (int i= 0; i<5; i++){
            boolean filaCompleta = true;
            for (int j=0; j<5; j++){
                if(!marcados[i][j]){
                    filaCompleta = false;
                    break;
                }
            }
            if(filaCompleta){
                return true;
            }
        }
        return false;
    }
    
    private boolean verificarVertical(Carton carton){
        boolean[][] marcados = carton.getNumeroMarcados();
        for(int col = 0; col < 5; col++){
            boolean columnaCompleta = true;
            for (int fila = 0; fila < 5; fila++){
                if(!marcados[fila][col]){
                    columnaCompleta = false;
                    break;
                }
            }
            if(columnaCompleta)
                return true;
        }
        return false;
    }

    private boolean verificarDiagonal(Carton carton) {
        boolean[][] marcados = carton.getNumeroMarcados();
        boolean diagonal1 = true, diagonal2 = true;
        for (int i = 0; i < 5; i++) {
            if (!marcados[i][i]) {
                diagonal1 = false;
            }
            if (!marcados[i][4 - i]) {
                diagonal2 = false;
            }
        }
        return diagonal1 || diagonal2;
    }
    
    private boolean verificarCuatroEsquinas(Carton carton){
        boolean[][] marcados = carton.getNumeroMarcados();
        return marcados[0][0] && marcados[0][4] && marcados[4][0] && marcados[4][4];
    }
}
