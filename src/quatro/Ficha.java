/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quatro;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Lotrox
 */

public class Ficha {
    
    char[] carac = new char[4];
    
    /**
     * @brief Constructor principal de la clase Ficha.
     * @param forma: cuadrado 'C', redondo 'R'.
     * @param altura: alto 'A', bajo 'B'.
     * @param relleno: hueco 'H', lleno, 'L'.
     * @param color: blanco 'B', negro 'N'.
     * @throws FichaIlegal 
     */
    public Ficha(char forma, char altura, char relleno, char color) throws FichaIlegal {
        carac[0] = forma;
        carac[1] = altura;
        carac[2] = relleno;
        carac[3] = color;
        
        //Excepción en caso de intentar construir una ficha que no tenga las características legales.
        if( (((carac[0] != 'C') && (carac[0] != 'R')) || ((carac[1] != 'A') && (carac[1] != 'B'))) || 
            (((carac[2] != 'H') && (carac[2] != 'L')) || ((carac[3] != 'B') && (carac[3] != 'N'))) ){
            throw new FichaIlegal("La forma de la ficha no es válida.");
        } 
    };
    
    public Ficha(int color, int forma, int altura, int estado){
        if(color == OntologiaQuatro.CARACTERISTICA_BLANCA)      carac[3] = 'B';
        else carac[3] = 'N';
        if(forma == OntologiaQuatro.CARACTERISTICA_CUADRADA)    carac[0] = 'C';
        else carac[0] = 'R';
        if(altura == OntologiaQuatro.CARACTERISTICA_ALTA)       carac[1] = 'A';
        else carac[1] = 'B';
        if(estado == OntologiaQuatro.CARACTERISTICA_HUECA)      carac[2] = 'H';
        else carac[2] = 'L';
    }
    
    
    
    /**
     * @brief Construir una ficha a partir de un mensaje ACL.
     * @param fromACL 
     */
    public Ficha(String fromACL){
        for (int i = 0; i < 4; i++) carac[i] = fromACL.charAt(i);
    }

    /**
     * 
     * @return Ficha convertida a string.
     */
    public String toACL(){  
        return String.valueOf(carac);
    }

    
    /**
     * 
     * @param ficha
     * @return Devuelve la imagen de la ficha solicitada.
     * @throws IOException 
     */
    public ImageIcon pintar() throws IOException {
        try {
            Image img = ImageIO.read(new File("img/" + this.toACL() + ".png"));
            return new ImageIcon(img);
        } catch (IOException ex) {
            System.out.println("Error al cargar textura de ficha img/" + this.toACL() + ".png");
        }
       return null;
    }
    
    public int getColor() {
        if(carac[3]=='B') return OntologiaQuatro.CARACTERISTICA_BLANCA;
        return OntologiaQuatro.CARACTERISTICA_NEGRA;
    }

    public int getForma() {
        if(carac[0]=='C') return OntologiaQuatro.CARACTERISTICA_CUADRADA;
        return OntologiaQuatro.CARACTERISTICA_REDONDA;
    }

    public int getAltura() {
        if(carac[1]=='A') return OntologiaQuatro.CARACTERISTICA_ALTA;
        return OntologiaQuatro.CARACTERISTICA_BAJA;
    }

    public int getEstado() {
        if(carac[2]=='H') return OntologiaQuatro.CARACTERISTICA_HUECA;
        return OntologiaQuatro.CARACTERISTICA_SOLIDA;
    }

    class FichaIlegal extends Exception {

    /**
     * 
     * @param error 
     */
    public FichaIlegal(String error) {
        super("ERROR!: " + error);
    }
        
    
}
}


