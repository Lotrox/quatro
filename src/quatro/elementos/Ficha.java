/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro.elementos;

import jade.content.Concept;

/**
 *
 * @author pedroj
 */
public class Ficha implements Concept{
    
    private int color;
    private int forma;
    private int altura;
    private int estado;

    public Ficha () {
  
    }

    public Ficha(int color, int forma, int altura, int estado) {
        this.color = color;
        this.forma = forma;
        this.altura = altura;
        this.estado = estado;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setForma(int forma) {
        this.forma = forma;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public int getColor() {
        return color;
    }

    public int getForma() {
        return forma;
    }

    public int getAltura() {
        return altura;
    }

    public int getEstado() {
        return estado;
    }
     
}
