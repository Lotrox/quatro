/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quatro;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Ontología de comunicación que para jugar a las Cuatro en Raya
 * 
 * @author pedroj
 */
public class OntologiaQuatro extends BeanOntology {
    
    	private static final long serialVersionUID = 1L;

	// NOMBRE
	public static final String ONTOLOGY_NAME = "Ontologia_QUATRO!";
        
        //VOCABULARIO
        public static final String REGISTRO_TABLERO = "Tablero QUATRO!";
        public static final String REGISTRO_JUGADOR = "Jugador QUATRO!";
        public static final String TIPO_JUEGO = "QUATRO";
        public static final int CARACTERISTICA_BLANCA = 1;
        public static final int CARACTERISTICA_NEGRA = 2;
        public static final int CARACTERISTICA_ALTA = 3;
        public static final int CARACTERISTICA_BAJA = 4;
        public static final int CARACTERISTICA_REDONDA = 5;
        public static final int CARACTERISTICA_CUADRADA = 6;
        public static final int CARACTERISTICA_HUECA = 7;
        public static final int CARACTERISTICA_SOLIDA = 8;
        public static final int LIBRE = 0; 

	// The singleton instance of this ontology
	private static Ontology INSTANCE;

	public synchronized final static Ontology getInstance() throws BeanOntologyException {
		if (INSTANCE == null) {
			INSTANCE = new OntologiaQuatro();
		}
		return INSTANCE;
	}

	/**
	 * Constructor
	 * 
	 * @throws BeanOntologyException
	 */
	private OntologiaQuatro() throws BeanOntologyException {
	
            super(ONTOLOGY_NAME);
        
            add("quatro.elementos");
	}

    
}
