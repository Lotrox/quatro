 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*-container -host 192.168.38.100 Dani:agentes.player*/
package quatro;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.ProposeInitiator;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import quatro.elementos.FichaEntregada;
import quatro.elementos.IniciarPartida;
import quatro.elementos.Juego;
import quatro.elementos.Jugador;
import quatro.elementos.Movimiento;
import quatro.elementos.MovimientoRealizado;
import quatro.elementos.Partida;
import quatro.elementos.PedirFicha;
import quatro.elementos.PedirMovimiento;

/**
 *
 * @author Lotrox
 */
public class Quatro extends Agent{

    /**
     * @param args the command line arguments
     */
    private Ontology ontologia;
    private final Codec codec = new SLCodec();
    private final TableroUI gui = new TableroUI();
    private Boolean j1, j2;
    private Jugador[] participantes = new Jugador[2];
    private quatro.elementos.Ficha saveFicha;
    private quatro.elementos.Ficha ficha;
    private int saveX, saveY;
    private Partida partida;
    private Movimiento movAnterior = null;
    private final ContentManager manager = (ContentManager) getContentManager();
    int turno;


    
    @Override
    protected void setup(){
        // Registra el lenguaje de contenido y la ontologia utilizada
        gui.setVisible(true);
        try {
            OntologiaQuatro.getInstance();
        } catch (BeanOntologyException ex) {
            Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ontologia = OntologiaQuatro.getInstance();
        } catch (BeanOntologyException ex) {
            Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
        }
        manager.registerLanguage(codec);
	manager.registerOntology(ontologia);
        
        saveFicha = null;
        j1 = false;
        j2 = false;
        
        DFAgentDescription descripcion = new DFAgentDescription();
        //jugadores = new DFAgentDescription[2];
        descripcion.setName(getAID()); //Establecemos un nombre al agente con el identificador.

        ServiceDescription servicio = new ServiceDescription(); 
        servicio.setType(OntologiaQuatro.REGISTRO_TABLERO); //Establecemos nombre al tipo de servicio.
        servicio.setName(getLocalName()); //Establecemos nombre del servicio.
        servicio.addOntologies(OntologiaQuatro.ONTOLOGY_NAME);
        servicio.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
        
        descripcion.addServices(servicio); //Añade el servicio a la descripción. 
        //Registro páginas amarillas
        try {
            DFService.register(this, descripcion); //
        } catch (FIPAException ex) {
            Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.addBehaviour(new BuscarJugadores());
           
    }

    /**
     * Método que se invoca antes de que el agente termine su ejecución. Se da
     * de baja en las páginas amarillas
     */
    @Override
    protected void takeDown() {
        //Se da de baja en las páginas amarillas
        try {
            DFService.deregister(this);
            System.out.println("Agente " + getAID().getName() + " finalizado." + "\n");
        } catch (FIPAException ex) {
            Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void comenzarTurno(){ 
        gui.log.setText("--------------------------------------------------------------------------------------------" +"\n" + gui.log.getText()); 
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(gui.fichas[i][j] == null) break;
                if(i == 3 && j == 3){
                    gui.estado = 2;
                    gui.log.setText("A modo de prueba se comprobará ganador... \nPartida finalizada con empate. \n "+ gui.log.getText());      
                    try {
                        Thread.sleep(1800);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    gui.compruebaGanador();
                    this.blockingReceive();
                }
            }
        }
        ACLMessage mensajeCFP = new ACLMessage(ACLMessage.CFP);
       
        mensajeCFP.addReceiver(participantes[0].getJugador());
        mensajeCFP.addReceiver(participantes[1].getJugador());
        //Protocolo que vamos a utilizar
        
        mensajeCFP.setSender(getAID());
        mensajeCFP.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        mensajeCFP.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
         
        mensajeCFP.setLanguage(codec.getName());
        mensajeCFP.setOntology(ontologia.getName());
        gui.turno.setText(participantes[turno].getJugador().getLocalName());
        if(turno == 0) turno = 1;
        else turno = 0; 
        System.out.println("Jugador activo: " + participantes[turno].getJugador() + "\n");
       PedirFicha pedirFicha = new PedirFicha(partida, participantes[turno], movAnterior);   
       Action a = new Action(getAID(), pedirFicha);
       
        try {
            manager.fillContent(mensajeCFP, a);
        } catch (Codec.CodecException | OntologyException ex) {
            Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Se añade el comportamiento que manejará las ofertas.
        this.addBehaviour(new ManejoOpciones(this, mensajeCFP, partida));
    }
    
    private class ManejoOpciones extends ContractNetInitiator{
        
        private Partida partida; 
        
        public ManejoOpciones(Agent agente, ACLMessage plantilla, Partida partida){
            super(agente,plantilla);
            this.partida = partida;
        }
        
        @Override /*Manejador de respuestas de los agentes.*/
        protected void handleAllResponses(Vector respuestas, Vector aceptados) {
            System.out.println("Timeout agotado: perdidos " + (respuestas.size()) + " participantes");
            ACLMessage ficha = null, movimiento = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Object resp : respuestas) {
                ACLMessage mensaje = (ACLMessage) resp;

                if (mensaje.getPerformative() == ACLMessage.PROPOSE) {
                    try {
                        System.out.print("Handle all responsees PROPOSE \n");
                        FichaEntregada fe = (FichaEntregada) manager.extractContent(mensaje);
                        if (fe.getVictoria() == null) {
                            System.out.print("Guardando ficha\n");
                            //saveFicha = new quatro.elementos.Ficha(fe.getFicha().getColor(), fe.getFicha().getForma(), fe.getFicha().getAltura(), fe.getFicha().getEstado());
                            saveFicha = fe.getFicha();
                             quatro.Ficha f = new quatro.Ficha(fe.getFicha().getColor(), fe.getFicha().getForma(), fe.getFicha().getAltura(), fe.getFicha().getEstado());
                            gui.log.setText(gui.jE  + " ha enviado la pieza "+ f.toACL() +"\n" + gui.log.getText());
                            System.out.print("Ficha: " + fe.getFicha().getColor());
                            ficha = mensaje.createReply();
                            System.out.print(mensaje.getContent() + "\n");
                            PedirMovimiento pm = new PedirMovimiento(participantes[turno], partida, saveFicha);
                            Action a = new Action();
                            a.setAction(pm);
                            a.setActor(pm.getJugador().getJugador());
                            manager.fillContent(ficha, a);
                            ficha.setPerformative(ACLMessage.REJECT_PROPOSAL); /*Aqui enviar peticion de movimiento*/
                            aceptados.add(ficha);
                        } else {
                            movimiento = mensaje.createReply(); 
                        }

                    } catch (Codec.CodecException | OntologyException ex) {
                        Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            
            if(saveFicha != null){
                try {
                    movimiento.setSender(getAID());
                    movimiento.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
                    PedirMovimiento pm = new PedirMovimiento(participantes[turno], partida, saveFicha);
                    System.out.println("Pidiendo ficha" + saveFicha.getColor() + "\n");
                    Action a = new Action(participantes[turno].getJugador(), pm);
                    manager.fillContent(movimiento, a);
                    System.out.println("ASD: " + pm.getJugador().getJugador() + "\n");
                    System.out.print(movimiento.getContent() + "\n");
                    movimiento.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    aceptados.add(movimiento);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

//             if(saveFicha != null){
//                 System.out.println("Añadiendo contenido de ficha\n");
//                        ACLMessage respuesta = movimiento.createReply();
//                        respuesta.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
//                        
//                        respuesta.setContent(ficha.getContent());
//                        aceptados.add(respuesta);
//                        
//                        respuesta = ficha.createReply();
//                        respuesta.setPerformative(ACLMessage.REJECT_PROPOSAL);
//                        aceptados.add(respuesta);
//                }
        }

        @Override
        protected void handleInform(ACLMessage inform) {
            gui.estado = 1;
            System.out.print("HOLAAA \n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*if(cadenas[4].equals("1")){
                gui.log.setText(cadenas[0] + " ha pedido la comprobación de victoria! \n" + gui.log.getText() );
                win = true;
            }*/
            MovimientoRealizado fe = null;
            AbsPredicate cs = null;
            try {
                cs = (AbsPredicate)myAgent.getContentManager().extractAbsContent(inform);
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
            Ontology o = myAgent.getContentManager().lookupOntology(OntologiaQuatro.ONTOLOGY_NAME);
            try {
                fe = (MovimientoRealizado)o.toObject((AbsObject)cs);
                movAnterior = fe.getMovimiento();
                System.out.println(fe.getMovimiento() + "\n");
            } catch (OntologyException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(fe.getMovimiento() != null){
                quatro.elementos.Ficha fi = fe.getMovimiento().getFicha();
                quatro.Ficha f = new quatro.Ficha(fi.getColor(), fi.getForma(), fi.getAltura(), fi.getEstado());
                gui.movimiento(fe.getMovimiento().getPosicion().getFila(), fe.getMovimiento().getPosicion().getColumna(), f, false);
                
            }
            
            //saveX = Integer.parseInt(cadenas[1]); saveY = Integer.parseInt(cadenas[2]);
           
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(gui.estado != 2) comenzarTurno();          
        }
        
        
        
    }
    
    public class ProponerJuego extends ProposeInitiator{
        public ProponerJuego(Agent agente, ACLMessage mensaje){
            super(agente, mensaje);
        }
        
        /**
         * Maneja la respuesta en caso de aceptar: ACCEPT_PROPOSAL.
         * @param aceptacion 
         */
        @Override
        protected void handleAcceptProposal(ACLMessage aceptacion){
            gui.log.setText(aceptacion.getSender().getLocalName() + " está preparado para jugar. \n" + gui.log.getText() );
            if(j1){
                j2=true;
                 gui.jT = aceptacion.getSender().getLocalName();
            }
            else{
                j1=true;
                gui.jE = aceptacion.getSender().getLocalName();
            }
            
            if(j1 && j2){
                Juego juego = new Juego(OntologiaQuatro.TIPO_JUEGO);
                partida = new Partida("PARTIDA DE PRUEBA",juego);
                IniciarPartida jugar = new IniciarPartida(partida, getAID());
                gui.log.setText("\n¡COMIENZA PARTIDA ENTRE " + gui.jE + " y " + gui.jT + "!\n\n" + gui.log.getText());          
                comenzarTurno();
            }
        }
        
        @Override
        protected void handleRejectProposal(ACLMessage rechazo) {
            if(participantes[0].getJugador() == rechazo.getSender() || participantes[1].getJugador() == rechazo.getSender() ) System.out.print("Un jugador ha rechazado partida. \n");
            participantes = new Jugador[2];
        }
    }
    /**
     * Comportamiento cíclico.
     */
    public class BuscarJugadores extends SimpleBehaviour {

        private DFAgentDescription[] jugadores;
        @Override
        public void action() {
            try {
                Thread.sleep(2000);
                //Buscamos jugadores.
                DFAgentDescription receptor = new DFAgentDescription();
                ServiceDescription servicio = new ServiceDescription();
                servicio.setType(OntologiaQuatro.REGISTRO_JUGADOR);
               
                receptor.addServices(servicio);
                
                SearchConstraints sc = new SearchConstraints();
                //sc.setMaxResults(new Long(4)); /*Busca como máximo 4*/
            
                try {
                    jugadores = DFService.search(this.myAgent, receptor);
                    if (jugadores.length == 0)  System.out.println("No se ha encontrado ningún jugador! \n");
                    else gui.log.setText("Se ha encontrado " + jugadores.length + " jugador/es.\n" + gui.log.getText());          
                } catch (FIPAException ex) {
                    Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public boolean done(){
            return jugadores.length > 1;
        }
        
        @Override
        public int onEnd(){
            
            // Hace que el comportamiento se reinicie al finalizar.
            if(jugadores.length < 2){
                reset();
                myAgent.addBehaviour(this);
            }else{
                System.out.print("FIN busqueda jugadores \n");
                ACLMessage mensaje = new ACLMessage(ACLMessage.PROPOSE);
                mensaje.setOntology(ontologia.getName());
                mensaje.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
                mensaje.setLanguage(codec.getName());
                mensaje.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
                //Se añade el destinatario.
                participantes[0] = new Jugador(jugadores[0].getName());
                participantes[1] = new Jugador(jugadores[1].getName());
                System.out.print("J1: " + participantes[0].getJugador().getLocalName() + " J2: " + participantes[1].getJugador().getLocalName() + "\n");
                mensaje.addReceiver(participantes[0].getJugador());
                mensaje.addReceiver(participantes[1].getJugador());
                try {
                    Thread.sleep(1200);
                    if(jugadores.length < 2) reset();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Quatro.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Añadir el comportamiento
                myAgent.addBehaviour(new ProponerJuego(myAgent, mensaje));
                System.out.print("Proponiendo juego \n");
                
            }
            return 0;
        }
        
    }

}
