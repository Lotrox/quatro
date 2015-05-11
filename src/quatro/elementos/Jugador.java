/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro.elementos;

import jade.content.Concept;
import jade.core.AID;

/**
 *
 * @author pedroj
 */
public class Jugador implements Concept {
    
    private AID jugador;
    
    public Jugador () {
        
    }

    public Jugador(AID jugador) {
        this.jugador = jugador;
    }
    
    public void setJugador(AID jugador) {
        this.jugador = jugador;
    }

    public AID getJugador() {
        return jugador;
    }

}
