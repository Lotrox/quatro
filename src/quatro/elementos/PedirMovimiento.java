/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quatro.elementos;

import jade.content.AgentAction;

/**
 *
 * @author macosx
 */
public class PedirMovimiento implements AgentAction {
    
    private Jugador jugador;
    private Partida partida;
    private Ficha ficha;

    public PedirMovimiento() {
    }

    public PedirMovimiento(Jugador jugador, Partida partida, Ficha ficha) {
        this.jugador = jugador;
        this.partida = partida;
        this.ficha = ficha;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Partida getPartida() {
        return partida;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }
    
}
