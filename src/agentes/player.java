/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 */

/*-gui Tablero:quatro.Tablero;Jugador1:agentes.player;Jugador2:agentes.player*/
/*java -cp "Desktop\Quatro 30-4\lib\jade.jar" -jar "Desktop\Quatro 30-4\MULTIAGENTE_QUATRO.jar" -gui*/
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
    Boolean[][] tablero = new Boolean[4][4];
    private Ontology ontologia;
    private Codec codec;
    
    protected void inicializar(){
        fichas[0][0] = "CAHB";
        fichas[1][0] = "CALN";
        fichas[2][0] = "CAHN";
        fichas[3][0] = "CALB";
        fichas[4][0] = "CBHN";
        fichas[5][0] = "CBLB";
        fichas[6][0] = "CBHB";
        fichas[7][0] = "CBLN";
        fichas[8][0] = "RBHN";
        fichas[9][0] = "RBLN";
        fichas[10][0] = "RBHB";
        fichas[11][0] = "RBLB";
        fichas[12][0] = "RAHN";
        fichas[13][0] = "RALB";
        fichas[14][0] = "RAHB";
        fichas[15][0] = "RALN";
        for(int i=0;i<16;i++){
            fichas[i][1] = "0";
        }  
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                tablero[i][j] = false;
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
                jugando = true;
                reset();
                ACLMessage refuse = propuesta.createReply();
                refuse.setPerformative(ACLMessage.REJECT_PROPOSAL);
 
                return refuse;
            }
            
        }
    }
    
    protected String selectFicha(){
        for(int i=0;i<16;i++){
            if(fichas[i][1].equals("0")){
                fichas[i][1] = "1";
                return fichas[i][0];
            }
        }
        return " ";
    }
    protected void registrarFicha(String f){
        for(int i=0;i<16;i++){
            if(fichas[i][0].equals(f)){
                fichas[i][1] = "1";
            }
        }
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
                    tablero[pedir.getAnterior().getPosicion().getFila()][pedir.getAnterior().getPosicion().getColumna()] = true;
                    quatro.elementos.Ficha f = pedir.getAnterior().getFicha();
                    Ficha fich = new Ficha(f.getColor(),f.getForma(),f.getAltura(),f.getEstado());
                    System.out.print("Registrando ficha del movimiento anterior: " + fich.toACL() + "\n");
                    registrarFicha(fich.toACL());
                }
                    
                if(!(pedir.getJugadorActivo().getJugador().equals(myAgent.getAID()))){
                     Ficha fich = new Ficha(selectFicha());
                     quatro.elementos.Ficha f = new quatro.elementos.Ficha(fich.getColor(),fich.getForma(),fich.getAltura(),fich.getEstado());
                     System.out.print(myAgent.getAID() + "[Pasivo] Enviando ficha: " + fich.toACL() + "\n");
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
    
       
       protected String colocarFicha(){
           for(int i=0;i<4;i++){
               for(int j=0;j<4;j++){
                if(!tablero[i][j]){
                    tablero[i][j] = true;
                    return String.valueOf(i)+";"+String.valueOf(j);
                }
               }
           }
           return ";";
       }
        
        @Override
       protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
           /*Aquí ponemos el Movimiento realizado y eso*/     
           System.out.print("PLAYER PREPARE  RESULT NOTIF \n");
           System.out.println("JUGADOR " + myAgent.getLocalName() + "\n");
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
                System.out.println("JUGADOR " + myAgent.getLocalName() + fe.getFicha().getAltura() + "\n");
                String[] posxy = colocarFicha().split(";");
                Jugador j = fe.getJugador();
                Posicion p = new Posicion(Integer.parseInt(posxy[0]),Integer.parseInt(posxy[1]));
                Movimiento m = new Movimiento(fe.getFicha(), p);
                Victoria v = new Victoria(false);
                MovimientoRealizado mr = new MovimientoRealizado(j, m);
                mr.setVictoria(v);
                try {
                    getContentManager().fillContent(inform,mr);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(player.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
            System.out.print("INFORM: " + inform.getContent() + "\n");
            return inform;
        }
    }
     @Override
    protected void takeDown() {
        System.out.print(this.getLocalName()+ " se despide." + "\n");
    }

   
}
