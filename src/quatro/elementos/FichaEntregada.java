/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quatro.elementos;

import jade.content.Predicate;

/**
 *
 * @author pedroj
 */
public class FichaEntregada implements Predicate {
    
    Ficha ficha;
    Victoria victoria;

    public FichaEntregada() {
    }

    public FichaEntregada(Ficha ficha, Victoria victoria) {
        this.ficha = ficha;
        this.victoria = victoria;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public Victoria getVictoria() {
        return victoria;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public void setVictoria(Victoria victoria) {
        this.victoria = victoria;
    }
    
}
