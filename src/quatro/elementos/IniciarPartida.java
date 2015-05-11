/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quatro.elementos;

import jade.content.AgentAction;
import jade.core.AID;

/**
 *
 * @author pedroj
 */
public class IniciarPartida implements AgentAction {
    
        Partida partida;
        AID tablero;

    public IniciarPartida() {
    }

    public IniciarPartida(Partida partida, AID tablero) {
        this.partida = partida;
        this.tablero = tablero;
    }

    public Partida getPartida() {
        return partida;
    }

    public AID getTablero() {
        return tablero;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public void setTablero(AID tablero) {
        this.tablero = tablero;
    }
    
}
