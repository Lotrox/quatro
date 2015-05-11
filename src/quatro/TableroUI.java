/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quatro;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Lotrox
 */
public class TableroUI extends javax.swing.JFrame {

    /**
     * Creates new form TableroUI
     */
    
    Image img;
    String jT = new String();
    String jE = new String();
    JButton[][] pos = new JButton[5][5];
    Ficha[][] fichas = new Ficha[5][5];
    int estado; // 0: Buscando jugadores, 1: jugando, 2: finalizado
    
    public TableroUI() {
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                fichas[i][j] = null;
            }
        }
        initComponents();
        pos[0][0] = pos13;
        pos[0][1] = pos14;
        pos[0][2] = pos15;
        pos[0][3] = pos16;
        pos[1][0] = pos9;
        pos[1][1] = pos10;
        pos[1][2] = pos11;
        pos[1][3] = pos12;
        pos[2][0] = pos5;
        pos[2][1] = pos6;
        pos[2][2] = pos7;
        pos[2][3] = pos8;
        pos[3][0] = pos1;
        pos[3][1] = pos2;
        pos[3][2] = pos3;
        pos[3][3] = pos4;
        estado = 0;
        jLabel1.setText("Buscando jugadores...");  
    }
    
    /**
     * 
     * @param x Posición de la fila del fichero.
     * @param y Posición de la columna del fichero.
     * @param f Ficha que deseamos insertar.
     * @param Quatro Marca de victoria.
     */
    public void movimiento(int x, int y, Ficha f, Boolean Quatro) {
        if(estado==1){
            jLabel1.setText("Jugando");  
            log.setText(jT+ " ha posicionado la pieza: " +f.toACL() + " en la posición (" + x + ", " + y + ") \n" + log.getText());
            
            if ((x < 0 || x > 3) || (y < 0 || y > 3)) { //Posición fuera del tablero.
                try {
                    throw new PosicionIlegal("La posición (" + x + ", " + y + ") en el tablero no es válida.");
                } catch (PosicionIlegal ex) {
                    jLabel1.setText("Ganador " + jT);
                    log.setText( jT+ " ha ganado por posición ilegal de su rival en  (" + x + ", " + y + ")" + "\n" + log.getText());
                    estado = 2;
                    Logger.getLogger(TableroUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (fichas[x][y] != null) { //No exista una ficha en esa posición.
                try {
                    throw new PosicionIlegal("Ya existe una ficha en la posición(" + x + ", " + y + ") en el tablero no es válida.");
                } catch (PosicionIlegal ex) {
                    jLabel1.setText("Ganador " + jT);
                    log.setText( jT+ " ha ganado por posición ilegal de su rival en  (" + x + ", " + y + ")" + "\n" + log.getText());
                    estado = 2;
                    Logger.getLogger(TableroUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for (int i = 0; i < 4; i++) { //Ficha repetida.
                for (int j = 0; j < 4; j++) {
                    if (fichas[i][j] != null) {
                        if (f.toACL().equals(fichas[i][j].toACL())) {
                            try {
                                throw new FichaRepetida("La ficha " + f.toACL() + " ya existe en el tablero.");
                            } catch (FichaRepetida ex) {
                               jLabel1.setText("Ganador " + jT);
                               log.setText(jT+ " ha ganado por posición ilegal de su rival en  (" + x + ", " + y + ")" + "\n" + log.getText());
                               estado = 2;
                               Logger.getLogger(TableroUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }

            try {
                pos[x][y].setIcon(f.pintar()); //Dibujar pieza.
                fichas[x][y] = f;
                turno.setText(jT);
                
            } catch (IOException ex) {
                Logger.getLogger(TableroUI.class.getName()).log(Level.SEVERE, null, ex);
            }    
            }
        if(Quatro) compruebaGanador();
        String aux = jT;
        jT = jE;
        jE = aux;
    }

    /**
     * @return 
     * @brief Metodo encargado de comprobar en nuestro tablera si existe línea y muestra el ganador en caso de serlo.
     */
    public Boolean compruebaGanador() {
        int blancoF, blancoC, negroF, negroC;
        int altaF, altaC, bajaF , bajaC;
        int redondaF, redondaC, cuadradaF, cuadradaC;
        int huecaF, huecaC, rellenaF, rellenaC;
        int alB, alN, alA, alBa, alR, alC, alH, alRe;
        
        JButton[][] posW = new JButton[5][5];
        
        for(int j=0;j<4;j++){
            
            blancoF = 0; blancoC = 0; negroF = 0; negroC = 0;
            altaF = 0; altaC=0; bajaF = 0; bajaC = 0;
            redondaF = 0; redondaC=0; cuadradaF = 0; cuadradaC = 0;
            huecaF = 0; huecaC = 0; rellenaF = 0; rellenaC = 0;
            
            for (int i = 0; i < 4; i++) { //Comprobación de líneas.
                
                alB = 0; alN =0; alA = 0; alBa = 0; alR = 0; alC = 0; alH = 0; alRe = 0;
                if (fichas[j][i] != null) { //Filas
                    if (fichas[j][i].carac[3] == 'B') blancoF++; 
                    if (fichas[j][i].carac[3] == 'N') negroF++;
                    if (fichas[j][i].carac[1] == 'A') altaF++; 
                    if (fichas[j][i].carac[1] == 'B') bajaF++; 
                    if (fichas[j][i].carac[0] == 'R') redondaF++; 
                    if (fichas[j][i].carac[0] == 'C') cuadradaF++; 
                    if (fichas[j][i].carac[2] == 'H') huecaF++; 
                    if (fichas[j][i].carac[2] == 'R') rellenaF++; 
                }
                if (fichas[i][j] != null) { //Columnas
                    if (fichas[i][j].carac[3] == 'B') blancoC++;
                    if (fichas[i][j].carac[3] == 'N') negroC++;
                    if (fichas[i][j].carac[1] == 'A') altaC++;
                    if (fichas[i][j].carac[1] == 'B') bajaC++;
                    if (fichas[i][j].carac[0] == 'R') redondaC++;
                    if (fichas[i][j].carac[0] == 'C') cuadradaC++;
                    if (fichas[i][j].carac[2] == 'H') huecaC++;
                    if (fichas[i][j].carac[2] == 'R') rellenaC++;
                }
                
                /*if(fichas[i][j] != null){ // Victoria en cuadrado <NO NECESARIA>
                    for(int ii=i;ii<i+2;ii++){
                        for(int jj=j;jj<j+2;jj++){
                           //pos[ii][jj].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                            if(ii < 4 && jj < 4 && fichas[ii][jj] != null){
                                if (fichas[ii][jj].carac[3] == 'B') alB++;
                                if (fichas[ii][jj].carac[3] == 'N') alN++;
                                if (fichas[ii][jj].carac[1] == 'A') alA++;
                                if (fichas[ii][jj].carac[1] == 'B') alBa++;
                                if (fichas[ii][jj].carac[0] == 'R') alR++;
                                if (fichas[ii][jj].carac[0] == 'C') alC++; 
                                if (fichas[ii][jj].carac[2] == 'H') alH++;
                                if (fichas[ii][jj].carac[2] == 'R') alRe++;
                            }
                            
                        }
                      if(((alB == 4 || alN == 4) || (alA == 4 || alBa == 4)) || ((alR == 4 || alC == 4) || (alH == 4 || alRe == 4)))  {
                                for(int iii=i;iii<i+2;iii++){
                                    for(int jjj=j;jjj<j+2;jjj++){
                                         pos[iii][jjj].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                                    }
                                }
                                System.out.print("QUATRO!");    
                                jLabel1.setText("QUATRO!");
                                System.out.print("Fin del juego, ganador: " + jT);
                                log.setText(jT+ " ha ganado! Fin del juego" + "\n" + "QUATRO!" + "\n" + log.getText());
                                estado = 2;
                                return true;
                        
                        }  
                    }
                    
                }*/
                //Comprobación de victoria Filas
                if ((((blancoF == 4) || (negroF == 4)) || ((altaF == 4) || (bajaF == 4))) ||
                (((redondaF == 4) || (cuadradaF == 4)) || ((huecaF == 4) || (rellenaF == 4)))){
                    for(int ii=0;ii<4;ii++){
                         pos[j][ii].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                    }
                    System.out.print("QUATRO!");
                    jLabel1.setText("QUATRO!");
                    System.out.print("Fin del juego, ganador: " + jT);
                    log.setText(jT+ " ha ganado! Fin del juego" + "\n" + "QUATRO!" + "\n" + log.getText());          
                    estado = 2;
                    return true;
                } 
                //Comprobación de victoria Columnas
                if ((((blancoC == 4) || (negroC == 4)) || ((altaC == 4) || (bajaC == 4))) ||
                (((redondaC == 4) || (cuadradaC == 4)) || ((huecaC == 4) || (rellenaC == 4)))){
                    for(int ii=1;ii<5;ii++){
                         pos[ii][j].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                    }
                    System.out.print("QUATRO!");
                    jLabel1.setText("QUATRO!");
                    System.out.print("Fin del juego, ganador: " + jT);
                    log.setText(jT+ " ha ganado! Fin del juego" + "\n" + "QUATRO!" + log.getText());          
                    estado = 2;
                    return true;
                }
                
            }
        }
           
            for(int i=0;i<4;i++){ //Comprobación de diagonal.
                if((fichas[1][1] != null) && (fichas[2][2] != null) && (fichas[3][3] != null) && (fichas[0][0] != null)){
                    
                    
                    if  ((fichas[1][1].carac[i] == fichas[2][2].carac[i]) && (fichas[3][3].carac[i] == fichas[0][0].carac[i])){
                        pos[1][1].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[2][2].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[3][3].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[0][0].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        System.out.print("QUATRO!");
                        jLabel1.setText("QUATRO!");
                        System.out.print("Fin del juego, ganador: " + jT);
                        log.setText( jT+ " ha ganado! Fin del juego" + "\n" + "QUATRO!" + log.getText() );          
                        estado = 2;
                        return true; 
                    }       
                }
                if((fichas[3][0] != null) && (fichas[2][1] != null) && (fichas[1][2] != null) && (fichas[0][4] != null)){
                    if(((fichas[3][0].carac[i] == fichas[2][1].carac[i]) && (fichas[1][2].carac[i] == fichas[0][3].carac[i]))){
                        pos[3][0].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[2][1].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[1][2].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                        pos[0][3].setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 4, true));
                         System.out.print("QUATRO!");
                       jLabel1.setText("QUATRO!");
                       System.out.print("Fin del juego, ganador: " + jT);
                       log.setText(jT+ " ha ganado! Fin del juego" + "\n" + "QUATRO!" + log.getText());          
                       estado = 2;
                       return true; 
                    }
                }
            }
            
            jLabel1.setText("QUATRO!");
            log.setText(jT + " ha ganado por falso QUATRO! de su rival." + "\n" + log.getText());
            estado = 2;
            return false;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        pos9 = new javax.swing.JButton();
        pos6 = new javax.swing.JButton();
        pos10 = new javax.swing.JButton();
        pos13 = new javax.swing.JButton();
        pos5 = new javax.swing.JButton();
        pos15 = new javax.swing.JButton();
        pos7 = new javax.swing.JButton();
        pos11 = new javax.swing.JButton();
        pos14 = new javax.swing.JButton();
        pos8 = new javax.swing.JButton();
        pos12 = new javax.swing.JButton();
        pos16 = new javax.swing.JButton();
        pos1 = new javax.swing.JButton();
        pos2 = new javax.swing.JButton();
        pos3 = new javax.swing.JButton();
        pos4 = new javax.swing.JButton();
        nombreJuego = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        log = new javax.swing.JTextArea();
        nombreJuego1 = new javax.swing.JLabel();
        turno = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 153));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pos9.setBackground(new java.awt.Color(255, 255, 255));
        pos9.setMaximumSize(new java.awt.Dimension(60, 60));
        pos9.setMinimumSize(new java.awt.Dimension(60, 60));
        pos9.setPreferredSize(new java.awt.Dimension(60, 60));

        pos6.setBackground(new java.awt.Color(255, 255, 255));
        pos6.setMaximumSize(new java.awt.Dimension(60, 60));
        pos6.setMinimumSize(new java.awt.Dimension(60, 60));
        pos6.setPreferredSize(new java.awt.Dimension(60, 60));

        pos10.setBackground(new java.awt.Color(255, 255, 255));
        pos10.setMaximumSize(new java.awt.Dimension(60, 60));
        pos10.setMinimumSize(new java.awt.Dimension(60, 60));
        pos10.setPreferredSize(new java.awt.Dimension(60, 60));

        pos13.setBackground(new java.awt.Color(255, 255, 255));
        pos13.setMaximumSize(new java.awt.Dimension(60, 60));
        pos13.setMinimumSize(new java.awt.Dimension(60, 60));
        pos13.setPreferredSize(new java.awt.Dimension(60, 60));
        pos13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos13ActionPerformed(evt);
            }
        });

        pos5.setBackground(new java.awt.Color(255, 255, 255));
        pos5.setMaximumSize(new java.awt.Dimension(60, 60));
        pos5.setMinimumSize(new java.awt.Dimension(60, 60));
        pos5.setPreferredSize(new java.awt.Dimension(60, 60));
        pos5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pos5MouseClicked(evt);
            }
        });

        pos15.setBackground(new java.awt.Color(255, 255, 255));
        pos15.setMaximumSize(new java.awt.Dimension(60, 60));
        pos15.setMinimumSize(new java.awt.Dimension(60, 60));
        pos15.setPreferredSize(new java.awt.Dimension(60, 60));

        pos7.setBackground(new java.awt.Color(255, 255, 255));
        pos7.setMaximumSize(new java.awt.Dimension(60, 60));
        pos7.setMinimumSize(new java.awt.Dimension(60, 60));
        pos7.setPreferredSize(new java.awt.Dimension(60, 60));

        pos11.setBackground(new java.awt.Color(255, 255, 255));
        pos11.setMaximumSize(new java.awt.Dimension(60, 60));
        pos11.setMinimumSize(new java.awt.Dimension(60, 60));
        pos11.setPreferredSize(new java.awt.Dimension(60, 60));

        pos14.setBackground(new java.awt.Color(255, 255, 255));
        pos14.setMaximumSize(new java.awt.Dimension(60, 60));
        pos14.setMinimumSize(new java.awt.Dimension(60, 60));
        pos14.setPreferredSize(new java.awt.Dimension(60, 60));

        pos8.setBackground(new java.awt.Color(255, 255, 255));
        pos8.setMaximumSize(new java.awt.Dimension(60, 60));
        pos8.setMinimumSize(new java.awt.Dimension(60, 60));
        pos8.setPreferredSize(new java.awt.Dimension(60, 60));

        pos12.setBackground(new java.awt.Color(255, 255, 255));
        pos12.setMaximumSize(new java.awt.Dimension(60, 60));
        pos12.setMinimumSize(new java.awt.Dimension(60, 60));
        pos12.setPreferredSize(new java.awt.Dimension(60, 60));

        pos16.setBackground(new java.awt.Color(255, 255, 255));
        pos16.setMaximumSize(new java.awt.Dimension(60, 60));
        pos16.setMinimumSize(new java.awt.Dimension(60, 60));
        pos16.setPreferredSize(new java.awt.Dimension(60, 60));

        pos1.setBackground(new java.awt.Color(255, 255, 255));
        pos1.setMaximumSize(new java.awt.Dimension(60, 60));
        pos1.setMinimumSize(new java.awt.Dimension(60, 60));
        pos1.setPreferredSize(new java.awt.Dimension(60, 60));
        pos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pos1ActionPerformed(evt);
            }
        });

        pos2.setBackground(new java.awt.Color(255, 255, 255));
        pos2.setMaximumSize(new java.awt.Dimension(60, 60));
        pos2.setMinimumSize(new java.awt.Dimension(60, 60));
        pos2.setPreferredSize(new java.awt.Dimension(60, 60));

        pos3.setBackground(new java.awt.Color(255, 255, 255));
        pos3.setMaximumSize(new java.awt.Dimension(60, 60));
        pos3.setMinimumSize(new java.awt.Dimension(60, 60));
        pos3.setPreferredSize(new java.awt.Dimension(60, 60));

        pos4.setBackground(new java.awt.Color(255, 255, 255));
        pos4.setMaximumSize(new java.awt.Dimension(60, 60));
        pos4.setMinimumSize(new java.awt.Dimension(60, 60));
        pos4.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(pos13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pos14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pos15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pos16, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pos5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(pos2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pos3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pos4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                            .addComponent(pos6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(162, 162, 162))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(156, 156, 156)
                                                    .addComponent(pos7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(159, 159, 159)
                                                    .addComponent(pos11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pos12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pos8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(pos10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(12, 12, 12))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pos3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pos5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pos7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pos6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pos8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pos9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pos12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pos14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pos13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pos16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pos15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        nombreJuego.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        nombreJuego.setText("ESTADO:");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("IrisUPC", 1, 100)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("QUATRO!");

        log.setColumns(20);
        log.setRows(5);
        jScrollPane1.setViewportView(log);

        nombreJuego1.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        nombreJuego1.setText("Turno:");

        turno.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nombreJuego)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(48, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(nombreJuego1)
                        .addGap(18, 18, 18)
                        .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombreJuego)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombreJuego1)
                            .addComponent(turno, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos1ActionPerformed

    private void pos5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pos5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pos5MouseClicked

    private void pos13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pos13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pos13ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableroUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableroUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableroUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableroUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TableroUI().setVisible(true);
            }
        });  
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea log;
    private javax.swing.JLabel nombreJuego;
    private javax.swing.JLabel nombreJuego1;
    private javax.swing.JButton pos1;
    private javax.swing.JButton pos10;
    private javax.swing.JButton pos11;
    private javax.swing.JButton pos12;
    private javax.swing.JButton pos13;
    private javax.swing.JButton pos14;
    private javax.swing.JButton pos15;
    private javax.swing.JButton pos16;
    private javax.swing.JButton pos2;
    private javax.swing.JButton pos3;
    private javax.swing.JButton pos4;
    private javax.swing.JButton pos5;
    private javax.swing.JButton pos6;
    private javax.swing.JButton pos7;
    private javax.swing.JButton pos8;
    private javax.swing.JButton pos9;
    private javax.swing.JLabel turno;
    // End of variables declaration//GEN-END:variables

    private static class PosicionIlegal extends Exception {

        public PosicionIlegal(String error) {
            super("ERROR!: " + error);
        }
    }

    private static class FichaRepetida extends Exception {

        public FichaRepetida(String error) {
            super("ERROR!: " + error);
        }
    }
}
