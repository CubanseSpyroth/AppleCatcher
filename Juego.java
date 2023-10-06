/**
*@(#)Juego.java 		23/10/06
* AppName: AppleCatcher 
* Descripcion: My primer juego programado en java utilizando librerias graficas
* @version  1.0                  __   __
* @author   CubanseSpyroth      |[ |_|[ |_____ 
* Copyright (c) 2022-2023       |__| |__|-\   \
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Juego extends JFrame implements KeyListener{

	private JLabel label_lives, label_score, label_level, apple, live, player, enemy, enemy1, gpanel;
	private JScrollPane scrollPane;
	private JTextArea gamelog;

	//* CONSTANTES *//
	public static final int DEBUG_LEVEL = 0;
	public static final int UNIT = 32;

	//* VARIABLES GLOBALES *//
	public int lives = 3;  // Cantidad de vidas inicial
	public int score = 0;  // Cantidad de puntuaje inicial
	public int level = 1;  // Nivel inicial
	public int apples = 0; // Manzanas iniciales

	public int randomNum = 0;
	public int ok = 0;
	public int console = 0;
	public String reLog = "";


	public Juego(){

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("AppleCatcher");
		getContentPane().setBackground(new Color(50, 200, 20));
		this.addKeyListener(this);

		ImageIcon splayer = new ImageIcon("images/player.png");
		player = new JLabel(splayer);
		player.setBounds(32 , 64, UNIT, UNIT);
		add(player);

		ImageIcon sapple = new ImageIcon("images/apple.png");
		apple = new JLabel(sapple);
		apple.setBounds(160, 224, UNIT, UNIT);
		add(apple);

		ImageIcon slive = new ImageIcon("images/live.png");
		live = new JLabel(slive);
		live.setBounds(500, 32, UNIT, UNIT);
		add(live);

		ImageIcon senemy = new ImageIcon("images/enemy.png");
		enemy = new JLabel(senemy);
		enemy.setBounds(128, 128, UNIT, UNIT);
		add(enemy);

		gamelog = new JTextArea("");
		gamelog.setBackground(new Color(0,0,0)); // Fondo de color negro
		gamelog.setFont(new Font("DejaVu Sans", 1, 11));
		gamelog.setForeground(new Color(255,255,255)); // Texto en color blanco
		scrollPane = new JScrollPane(gamelog);
		scrollPane.setBounds(0, 292, 324, 128);
		gamelog.setEnabled(false);
		add(scrollPane);

		enemy1 = new JLabel(senemy);
		enemy1.setBounds(500, 128, UNIT, UNIT);
		add(enemy1);	

		label_lives = new JLabel("Lives:" + lives);
		label_lives.setBounds(32, 0, 64, 32);
		add(label_lives);

		label_score = new JLabel("Score:" + score);
		label_score.setBounds(128, 0, 128, 32);
		add(label_score);

		label_level = new JLabel("Level:" + level);
		label_level.setBounds(224, 0, 128, 32);
		add(label_level);

	}

	@Override
	public void keyReleased(KeyEvent e){
		// LOLESSSsd
	}

	@Override
	public void keyPressed(KeyEvent e){
		if (DEBUG_LEVEL == 1) inGameLog("[KEYEVENT]Codigo de tecla presionada :" + e.getKeyCode());

		if(e.getKeyCode() == 39){
			if(player.getX() < 288){
				if (DEBUG_LEVEL >= 2) inGameLog("[INFO]+ Movimiento en X = " + player.getX());
				player.setLocation(player.getX()+UNIT, player.getY());
			}
		}

		if(e.getKeyCode() == 37){
			if(player.getX() > 0){
				if (DEBUG_LEVEL >= 2) inGameLog("[INFO]- Movimiento en X = " + player.getX());
				player.setLocation(player.getX()-UNIT, player.getY());
			}	
		}

		if(e.getKeyCode() == 40){
			if(player.getY() < 256){
				if (DEBUG_LEVEL >= 2) inGameLog("[INFO]+ Movimiento en Y = " + player.getY());
				player.setLocation(player.getX(), player.getY()+UNIT);
			}
		}

		if(e.getKeyCode() == 38){
			if(player.getY() > 32){
				if (DEBUG_LEVEL >= 2) inGameLog("[INFO]- Movimiento en Y = " + player.getY());
				player.setLocation(player.getX(), player.getY()-UNIT);
			}
		}

		if (DEBUG_LEVEL != 0) inGameLog("[MOVE]" + getCoords("Player", player));

		if(e.getKeyCode() == 67){
			if(console == 0){
				inGameLog(">set console 1");
				this.setBounds(0,0,324,448);
				console = 1;
			} else {
				inGameLog(">set console 0");
				this.setBounds(0,0,324,324);
				console = 0;
			}
		}

		if(e.getKeyCode() == 115){
			inGameLog("Tecla clave activada. !!TRAMPOSO!! ");
			++lives;
			label_lives.setText("Lives:" + lives);
		}

		if(e.getKeyCode() == 116){
			inGameLog("Tecla clave activada. !!TRAMPOSO!! ");
			score+=20;
			++apples;
			label_score.setText("Score:" + score);
			inGameLog("Obtienes 1 apple +20 Score con trampa");
		}

		playerCollisions(); //Disparador de coliscion
		levelProperts(); //Propiedades de niveles
	}

	@Override
	public void keyTyped(KeyEvent e){
		// LOLESSSsd
	}

	/* FUNCION DE COLICIONES */
	public void playerCollisions(){

		/* ---- COLISCION ENTRE PLAYER:APPLE ----- */
		if (player.getX() == apple.getX() && player.getY() == apple.getY()){
			apple.setLocation(apple.getX()-UNIT, apple.getY()-UNIT);
			score+=20;
			++apples;
			label_score.setText("Score:" + score);
			randomPosition(apple);
			randomPosition(enemy);
			inGameLog(">Obtienes +1 apple y +20 Score");
			inGameLog(">Apples restantes " + apples + "/25");
			if (DEBUG_LEVEL != 0) inGameLog("[MOVE]" + getCoords("Apple",apple));
			if (DEBUG_LEVEL != 0) inGameLog("[MOVE]" + getCoords("Enemy",enemy));
		}

		/* ---- COLISCION ENTRE PLAYER:LIVE ----- */
		if (player.getX() == live.getX() && player.getY() == live.getY()){
			++lives;
			label_lives.setText("Lives:" + lives);
			inGameLog("Obtienes 1 vida ahora tienes " + lives + " vidas");
			wtf(live);
		}

		/* ---- COLISCION ENTRE PLAYER:ENEMY ----- */
		if (player.getX() == enemy.getX() && player.getY() == enemy.getY()){
			playerKill();
			randomPosition(enemy);
		}

		/* ---- COLISCION ENTRE PLAYER:ENEMY1 ----- */
		if (player.getX() == enemy1.getX() && player.getY() == enemy1.getY()){
			playerKill();
			randomPosition(enemy1);
		}

	}

	/* ---- CUANDO MUERE EL PLAYER ----- */
	public void playerKill(){
		if(lives > 1){
			player.setLocation(32, 64);
			--lives;
			label_lives.setText("Lives:" + lives);
			inGameLog(">Has Muerto te quedan " + lives + " vidas");
		} else {
			JOptionPane.showMessageDialog(null, "Game Over");
			player.setLocation(32, 64);
			inGameLog("Juego Terminado");
			System.exit(0);
		}
	}

	/* ---- DESAPARECER OBJETOS ----- */
	public void wtf(JLabel obj){
		obj.setLocation(500,0);
	}

	/* ---- PIDE LAS CORDENADAS DE LOS OBJETOS ----- */
	public static String getCoords(String name ,JLabel obj){
		int x = 0, y = 0;
		x = obj.getX();
		y = obj.getY();
		String cords = "[" + name + "]| Cordenadas x:" + x + " y:" + y + "|";
		return cords;
	}

	/* ---- RANDOMIZANDO POSISIONES EN EL MAPA ----- */
	public void randomPosition(JLabel obj){
		int x,y;
		Random random = new Random();

		/* randomiza numeros en el eje X */
		randomNum = random.nextInt(10) + 1;
		switch(randomNum){
			case 1: x = 64;
				break;
			case 2: x = 128;
				break;
			case 3: x = 160;
				break;
			case 4: x = 192;
				break;
			case 5: x = 224;
				break;
			case 6: x = 256;
				break;
			case 7: x = 160;
				break;
			case 8: x = 192;
				break;
			case 9: x = 64;
				break;
			default : x = 32;
				break;							
		}

		/* randomiza numeros en el eje Y */
		randomNum = random.nextInt(10) + 1;
		switch(randomNum){
			case 1: y = 64;
				break;
			case 2: y = 128;
				break;
			case 3: y = 160;
				break;
			case 4: y = 192;
				break;
			case 5: y = 224;
				break;
			case 6: y = 256;
				break;
			case 7: y = 192;
				break;
			case 8: y = 128;
				break;
			case 9: y = 64;
				break;
			default : y = 32;
				break;							
		}

		obj.setLocation(x, y);
	}

	/* ---- NIVELES DEL JUEGO ----- */
	public void levelProperts(){

		/* Cuando tienes 25 apples se activa */
		if(apples == 25){
			++level;    //incremento de nivel
			apples = 0; //resetea las apples
			inGameLog(">!!!Pasas al nivel " + level + "!!!");
			label_level.setText("Level:" + level); //upgradea el nivel impreso
		}

		if(level == 2 && ok == 0){
			//Spawnear vida
			randomPosition(live);
			ok=1;
		}

		if(level == 3 && ok == 1){
			randomPosition(enemy1); //Spawnear nuevo enemigo
			randomPosition(live);   //Spawnear vida
			ok=0;
		}

		if(level >= 5){
			//Spawnear una vida 
			if (ok == 0) randomPosition(live);
			ok = 1;

			/* Spawnear nuevo enemigo */
			randomPosition(enemy);
			randomPosition(enemy1);
		}

		if(level==6){
			gameEnd();
		}

	}

	public void inGameLog(String log){
		reLog += log + "\n";
		gamelog.setText(reLog);
	}


	/* -------- MAIN FORM ---------*/
	public static void main(String args[]){
		Juego win = new Juego();
        win.setBounds(0,0,324,324);
        win.setVisible(true);
        win.setResizable(true);
	}

	/* ---- FIN DEL JUEGO ----- */
	public void gameEnd(){
		JOptionPane.showMessageDialog(null, "Felicidades has terminado el juego," + "\n" + "gracias por jugar," + "\n" + "          Saludos Spyroth");
		System.exit(0);

	}

}
