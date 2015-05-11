/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro.elementos;

import jade.content.AgentAction;

/**
 *
 * @author pedroj
 */
public class PedirFicha implements AgentAction {
    
    private Partida partida;
    private Jugador jugadorActivo;
    private Movimiento anterior;
    
    public PedirFicha () {
        
    }

    public PedirFicha(Partida partida, Jugador jugadorActivo, Movimiento anterior) {
        this.partida = partida;
        this.jugadorActivo = jugadorActivo;
        this.anterior = anterior;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Jugador getJugadorActivo() {
        return jugadorActivo;
    }

    public void setJugadorActivo(Jugador jugadorActivo) {
        this.jugadorActivo = jugadorActivo;
    }

    public Movimiento getAnterior() {
        return anterior;
    }

    public void setAnterior(Movimiento anterior) {
        this.anterior = anterior;
    }
}
