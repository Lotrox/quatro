/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro.elementos;

import jade.content.Predicate;

/**
 *
 * @author pedroj
 */
public class MovimientoRealizado implements Predicate {

    private Jugador jugador;
    private Movimiento movimiento;
    private Victoria victoria;
    
    public MovimientoRealizado () {
        
    }

    public MovimientoRealizado(Jugador jugador, Movimiento movimiento) {
        this.jugador = jugador;
        this.movimiento = movimiento;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public Victoria getVictoria() {
        return victoria;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public void setVictoria(Victoria victoria) {
        this.victoria = victoria;
    }
}
