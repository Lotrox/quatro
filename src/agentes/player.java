/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 */

/*-gui Tablero:quatro.Tablero;Jugador1:agentes.player;Jugador2:agentes.player*/
/*java -cp "Desktop\lib\jade.jar" -jar "Desktop\MULTIAGENTE_QUATRO.jar" -gui*/
/*Esto es una prueba*/

package agentes;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.proto.ProposeResponder;
import java.util.logging.Level;
import java.util.logging.Logger;
import quatro.Ficha;
import quatro.OntologiaQuatro;
import quatro.elementos.FichaEntregada;
import quatro.elementos.Jugador;
import quatro.elementos.Movimiento;
import quatro.elementos.MovimientoRealizado;
import quatro.elementos.PedirFicha;
import quatro.elementos.PedirMovimiento;
import quatro.elementos.Posicion;
import quatro.elementos.Victoria;

/**
 *
 * @author Lotrox
 */
public class player extends Agent  {
    private ContentManager manager = (ContentManager) getContentManager();
    boolean jugando;
    String[][] fichas = new String[16][2];
    Ficha[][] tablero = new Ficha[5][5];
    private Ontology ontologia;
    private Codec codec;
    
    protected void inicializar(){
        fichas[0][0] = "CAHB";
        fichas[1][0] = "CAHN";
        fichas[2][0] = "CALB";
        fichas[3][0] = "CALN";
        fichas[4][0] = "CBHB";
        fichas[5][0] = "CBHN";
        fichas[6][0] = "CBLB";
        fichas[7][0] = "CBLN";
        fichas[8][0] = "RBHB";
        fichas[9][0] = "RBHN";
        fichas[10][0] = "RBLB";
        fichas[11][0] = "RBLN";
        fichas[12][0] = "RBHB";
        fichas[13][0] = "RBHN";
        fichas[14][0] = "RBLB";
        fichas[15][0] = "RBLN";
        for(int i=0;i<16;i++){
            fichas[i][1] = "0";
        }  
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                tablero[i][j] = null;
            }
        }
    }
    
    @Override
    protected void setup(){
        try{
            ontologia = OntologiaQuatro.getInstance();
        }catch (BeanOntologyException ex){
            Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
        }
        codec = new SLCodec();
        manager.registerLanguage(codec);
	manager.registerOntology(ontologia);
        
        jugando = false;
        inicializar();
        System.out.print("Creando Jugador \n");
        
        System.out.printf("%s: Esperando propuestas...\n", this.getLocalName());
        //Descripción para el agente.
        DFAgentDescription descripcion = new DFAgentDescription();
        descripcion.setName(getAID());

        //Tipo y nombre del servicio del agente.
        ServiceDescription servicio = new ServiceDescription();
        servicio.setName(getLocalName());
        servicio.setType(OntologiaQuatro.REGISTRO_JUGADOR);
        servicio.addOntologies(OntologiaQuatro.ONTOLOGY_NAME);
        servicio.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
        
        //Añade servicio al agente.
        descripcion.addServices(servicio);

        //Registro en las páginas amarillas.
        try {
            DFService.register(this, descripcion);
        } catch (FIPAException ex) {
        }
        
        
       
        //Creamos la plantilla a emplear, para solo recibir mensajes con el protocolo FIPA_PROPOSE y la performativa PROPOSE
        MessageTemplate plantilla = ProposeResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        this.addBehaviour(new ResponderProponerJuego(this, plantilla));
         // Plantilla de mensaje contract-net.
         MessageTemplate template = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
            MessageTemplate.MatchPerformative(ACLMessage.CFP) );

        this.addBehaviour(new ContractNet(this, template));
    }
    
 
    private class ResponderProponerJuego extends ProposeResponder {
 
        public ResponderProponerJuego(Agent agente, MessageTemplate plantilla) {
            super(agente, plantilla);
        }
 
        //Preparación de la respuesta. Recibe un mensaje PROPOSE y, según su contenido, acepta o no.
 
        @Override
        protected ACLMessage prepareResponse(ACLMessage propuesta)
                throws NotUnderstoodException {
            jugando = false;
            inicializar();
            //Comprueba los datos de la propuesta
                //Aceptación de la propuesta.
                //Se crea la respuesta al mensaje con la performativa ACCEPT_PROPOSAL, pues se acepta.
            if(!jugando){
                ACLMessage agree = propuesta.createReply();
                agree.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                inicializar();
                return agree;
            }else{
                //Rechazo de la propuesta.
                //Se crea la respuesta al mensaje con la performativa REJECT_PROPOSAL, pues se rechaza.
                reset();
                ACLMessage refuse = propuesta.createReply();
                refuse.setPerformative(ACLMessage.REJECT_PROPOSAL);
 
                return refuse;
            }
            
        }
    }
    
    protected String selectFicha(){
        int i;
        do{
            i = 0 + (int)(Math.random()*15);
            if(fichas[i][1].equals("0")){
                 fichas[i][1] = "1";
                return fichas[i][0];
            }
        }while(fichas[i][1].equals("1"));
        return "";
    }

    
    private class ContractNet extends ContractNetResponder {
        public ContractNet(Agent agente, MessageTemplate plantilla) {
            super(agente, plantilla);
        }
 
        @Override
        protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
            ACLMessage resp = cfp.createReply();
            resp.setPerformative(ACLMessage.PROPOSE);  
            try {
                FichaEntregada fe = null;
                Action a = (Action) manager.extractContent(cfp);
                PedirFicha pedir = (PedirFicha)a.getAction();
                
                if(pedir.getAnterior() != null){
                    
                    quatro.elementos.Ficha f = pedir.getAnterior().getFicha();

                    Ficha fich = new Ficha(f.getColor(),f.getForma(),f.getAltura(),f.getEstado());
                   // System.out.print("Registrando ficha del movimiento anterior: " + fich.toACL() + "\n");
                    tablero[pedir.getAnterior().getPosicion().getFila()][pedir.getAnterior().getPosicion().getColumna()] = fich;
                    registrarFicha(fich);
                }
                    
                if(!(pedir.getJugadorActivo().getJugador().equals(myAgent.getAID()))){
                     Ficha fich = new Ficha(selectFicha());
                     quatro.elementos.Ficha f = new quatro.elementos.Ficha(fich.getColor(),fich.getForma(),fich.getAltura(),fich.getEstado());
                     //System.out.print(myAgent.getAID() + "[Pasivo] Enviando ficha: " + fich.toACL() + "\n");
                     Victoria v = new Victoria(false);
                     fe = new FichaEntregada(f, null);
                }else{
                    Victoria v = new Victoria(false);
                    fe = new FichaEntregada(new quatro.elementos.Ficha(), v);
                } 
                 getContentManager().fillContent(resp, fe);

            } catch (Codec.CodecException | OntologyException ex) {
            }
            return resp;
        }
        
        protected void registrarFicha(Ficha fich){
            for(int i=0;i<16;i++){
                if(fichas[i][0].equals(fich.toACL()) && fichas[i][1].equals("0")){
                    fichas[i][1] = "1";
                }
            }    
        }
        
        protected Boolean hayVictoria() {
            int blancoF, blancoC, negroF, negroC;
            int altaF, altaC, bajaF , bajaC;
            int redondaF, redondaC, cuadradaF, cuadradaC;
            int huecaF, huecaC, rellenaF, rellenaC;
            int alB, alN, alA, alBa, alR, alC, alH, alRe;


            for(int j=0;j<4;j++){

                blancoF = 0; blancoC = 0; negroF = 0; negroC = 0;
                altaF = 0; altaC=0; bajaF = 0; bajaC = 0;
                redondaF = 0; redondaC=0; cuadradaF = 0; cuadradaC = 0;
                huecaF = 0; huecaC = 0; rellenaF = 0; rellenaC = 0;

                for (int i = 0; i < 4; i++) { //Comprobación de líneas.

                    alB = 0; alN =0; alA = 0; alBa = 0; alR = 0; alC = 0; alH = 0; alRe = 0;
                    
                    if (tablero[j][i] != null) { //Filas
                        if (tablero[j][i].carac[3] == 'B') blancoF++; 
                        if (tablero[j][i].carac[3] == 'N') negroF++;
                        if (tablero[j][i].carac[1] == 'A') altaF++; 
                        if (tablero[j][i].carac[1] == 'B') bajaF++; 
                        if (tablero[j][i].carac[0] == 'R') redondaF++; 
                        if (tablero[j][i].carac[0] == 'C') cuadradaF++; 
                        if (tablero[j][i].carac[2] == 'H') huecaF++; 
                        if (tablero[j][i].carac[2] == 'R') rellenaF++; 
                    }
                    if (tablero[i][j] != null) { //Columnas
                        if (tablero[i][j].carac[3] == 'B') blancoC++;
                        if (tablero[i][j].carac[3] == 'N') negroC++;
                        if (tablero[i][j].carac[1] == 'A') altaC++;
                        if (tablero[i][j].carac[1] == 'B') bajaC++;
                        if (tablero[i][j].carac[0] == 'R') redondaC++;
                        if (tablero[i][j].carac[0] == 'C') cuadradaC++;
                        if (tablero[i][j].carac[2] == 'H') huecaC++;
                        if (tablero[i][j].carac[2] == 'R') rellenaC++;
                    }
                    //Comprobación de victoria Filas
                    if ((((blancoF == 4) || (negroF == 4)) || ((altaF == 4) || (bajaF == 4))) ||
                    (((redondaF == 4) || (cuadradaF == 4)) || ((huecaF == 4) || (rellenaF == 4)))){
                        System.out.print("QUATRO! JUGADOR\n");
                        return true;
                    } 
                    //Comprobación de victoria Columnas
                    if ((((blancoC == 4) || (negroC == 4)) || ((altaC == 4) || (bajaC == 4))) ||
                    (((redondaC == 4) || (cuadradaC == 4)) || ((huecaC == 4) || (rellenaC == 4)))){
                        System.out.print("QUATRO! JUGADOR\n");
                        return true;
                    }

                }
            }

                for(int i=0;i<4;i++){ //Comprobación de diagonal.
                    if((tablero[1][1] != null) && (tablero[2][2] != null) && (tablero[3][3] != null) && (tablero[0][0] != null)){

                        if  ((tablero[1][1].carac[i] == tablero[2][2].carac[i]) && (tablero[3][3].carac[i] == tablero[0][0].carac[i]) && (tablero[1][1].carac[i] == tablero[3][3].carac[i])){
                            return true; 
                        }       
                    }
                    if((tablero[3][0] != null) && (tablero[2][1] != null) && (tablero[1][2] != null) && (tablero[0][3] != null)){
                        if  ((tablero[3][0].carac[i] == tablero[2][1].carac[i]) && (tablero[1][2].carac[i] == tablero[0][3].carac[i]) && (tablero[3][0].carac[i] == tablero[1][2].carac[i])){
                           return true; 
                        }
                    }
                }

                return false;
        }
    
       
       protected String colocarFicha(Ficha f){
           for(int i=0;i<4;i++){
               for(int j=0;j<4;j++){
                if(tablero[i][j] == null){
                    tablero[i][j] = f;
                    if(hayVictoria()){
                        return String.valueOf(i)+";"+String.valueOf(j)+";WIN";
                    }else tablero[i][j] = null;
                }
               }
           }
           int i = 0, j = 0;
           while(tablero[i][j] != null){
               i = 0 + (int)(Math.random()*4);
               j = 0 + (int)(Math.random()*4);
           }
           return String.valueOf(i)+";"+String.valueOf(j)+"; ";
       }
        
        @Override
       protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
           /*Aquí ponemos el Movimiento realizado y eso*/     
          // System.out.print("PLAYER PREPARE  RESULT NOTIF \n");
           //System.out.println("JUGADOR " + myAgent.getLocalName() + "\n");
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            PedirMovimiento fe = null;
            try {
                Action a = (Action) manager.extractContent(accept);
                fe = (PedirMovimiento)a.getAction();
                
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
            }
            Ontology o = myAgent.getContentManager().lookupOntology(OntologiaQuatro.ONTOLOGY_NAME);
           
            if(fe != null){
                //System.out.println("JUGADOR " + myAgent.getLocalName() + fe.getFicha().getAltura() + "\n");
                Ficha fich = new Ficha(fe.getFicha().getColor(),fe.getFicha().getForma(),fe.getFicha().getAltura(),fe.getFicha().getEstado());
                String[] posxy = colocarFicha(fich).split(";");
                Boolean marcaVictoria = false;
                if(posxy[2].equals("WIN")) marcaVictoria = true;
                Jugador j = fe.getJugador();
                Posicion p = new Posicion(Integer.parseInt(posxy[0]),Integer.parseInt(posxy[1]));
                Movimiento m = new Movimiento(fe.getFicha(), p);
                registrarFicha(fich);
                Victoria v = new Victoria(marcaVictoria);
                MovimientoRealizado mr = new MovimientoRealizado(j, m);
                mr.setVictoria(v);
                try {
                    getContentManager().fillContent(inform,mr);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
            //System.out.print("INFORM: " + inform.getContent() + "\n");
            return inform;
        }
    }
     @Override
    protected void takeDown() {
        System.out.print(this.getLocalName()+ " se despide." + "\n");
    }

   
}
