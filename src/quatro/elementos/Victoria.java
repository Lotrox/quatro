/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro.elementos;

import jade.content.Concept;

/**
 *
 * @author pedroj
 */
public class Victoria implements Concept {
    
    private boolean victoria;

    public Victoria() {
    }

    public Victoria(boolean victoria) {
        this.victoria = victoria;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
    }

}
