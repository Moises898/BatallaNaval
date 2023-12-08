package com.example.examenbatallanaval;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TablerosJuego extends AppCompatActivity {
    int[][] tablero1,tablero2;
    String name1,name2;
    TextView tvTurno,Jugador1,Jugador2;
    LinearLayout GUI1,GUI2;
    ImageView[][] ImageGUI1 = new ImageView[6][8];
    ImageView[][] ImageGUI2 = new ImageView[6][8];
    boolean player1 = true;
    int puntaje1 = 0, puntaje2 = 0;
    int disparos1 = 0, disparos2 = 0, partidaCorta;
    MediaPlayer mediaPlayer,laser,bomba;
    SharedPreferences data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableros_juego);
        data = getSharedPreferences("partida",MODE_PRIVATE);
        partidaCorta = data.getInt("partida", 0);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        laser = MediaPlayer.create(this, R.raw.laser);
        bomba = MediaPlayer.create(this, R.raw.disparo);
        Intent intent = getIntent();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        tablero1 = (int[][]) intent.getSerializableExtra("tablero1");
        tablero2 = (int[][]) intent.getSerializableExtra("tablero2");
        name1 = (String) intent.getSerializableExtra("name1");
        name2 = (String) intent.getSerializableExtra("name2");
        tvTurno = findViewById(R.id.tvTurno);
        Jugador1 = findViewById(R.id.Jugador1);
        Jugador2 = findViewById(R.id.Jugador2);
        Jugador1.setText(name1);
        Jugador2.setText(name2);
        tvTurno.setText(name1+" selecciona una casilla");
        GUI1 = findViewById(R.id.tablero1).findViewById(R.id.tablero);
        GUI2 = findViewById(R.id.tablero2).findViewById(R.id.tablero);
        // Guarda los objectos de cada tablero
        for (int i = 0; i < GUI1.getChildCount(); i++) {
            View filas = GUI1.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        ImageGUI1[i][j] = (ImageView) imagen;
                    }
                }
            }
        }
        for (int i = 0; i < GUI2.getChildCount(); i++) {
            View filas = GUI2.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        ImageGUI2[i][j] = (ImageView) imagen;
                    }
                }
            }
        }

        // OnClickListener Tablero 1
        for (int i = 0; i < GUI1.getChildCount(); i++) {
            View filas = GUI1.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        int finalI = i;
                        int finalJ = j;
                        imagen.setOnClickListener(v -> {
                            if (player1){
                                laser.start();
                                if (tablero2[finalI][finalJ] == 0){
                                    ImageGUI1[finalI][finalJ].setImageResource(R.drawable.bomba);
                                    disparos1++;
                                    tablero2[finalI][finalJ] = -1;
                                    player1 = false;
                                    tvTurno.setText(name2+" selecciona una casilla");
                                } else if (tablero2[finalI][finalJ] == -1) {
                                    Toast.makeText(this, "Casilla invalida", Toast.LENGTH_SHORT).show();
                                } else {
                                    bomba.start();
                                    ImageGUI1[finalI][finalJ].setImageResource(R.drawable.explosion);
                                    disparos1++;
                                    tablero2[finalI][finalJ] = -1;
                                    player1 = false;
                                    tvTurno.setText(name2+" selecciona una casilla");
                                    puntaje1++;
                                }
                            }else{
                                Toast.makeText(this, "¡No es tu turno!", Toast.LENGTH_SHORT).show();
                            }
                            if (puntaje1 == 12 || puntaje2 == 12){
                                if (partidaCorta < disparos1 || partidaCorta < disparos2){
                                    int max = Math.max(disparos1,disparos2);
                                    partidaCorta = max;
                                    data.edit().putInt("partida", partidaCorta).apply();
                                }
                                if (puntaje1 > puntaje2){
                                    tvTurno.setText(name1+" ha ganado \n"+"Partida mas corta: "+partidaCorta);
                                }else{
                                    tvTurno.setText(name2+" ha ganado \n"+"Partida mas corta: "+partidaCorta);
                                }
                                GUI1.setVisibility(View.INVISIBLE);
                                GUI2.setVisibility(View.INVISIBLE);
                                mediaPlayer.release();
                                laser.release();
                                bomba.release();

                            }
                        });
                    }
                }
            }
        }
        // OnClickListener Tablero 2
        for (int i = 0; i < GUI2.getChildCount(); i++) {
            View filas = GUI2.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        int finalI = i;
                        int finalJ = j;
                        imagen.setOnClickListener(v -> {
                            if (!player1){
                                laser.start();
                                if (tablero1[finalI][finalJ] == 0){
                                    ImageGUI2[finalI][finalJ].setImageResource(R.drawable.bomba);
                                    disparos2++;
                                    tablero1[finalI][finalJ] = -1;
                                    player1 = true;
                                    tvTurno.setText(name1+" selecciona una casilla");
                                } else if (tablero1[finalI][finalJ] == -1) {
                                    Toast.makeText(this, "Casilla invalida", Toast.LENGTH_SHORT).show();
                                } else {
                                    bomba.start();
                                    ImageGUI2[finalI][finalJ].setImageResource(R.drawable.explosion);
                                    disparos2++;
                                    tablero1[finalI][finalJ] = -1;
                                    player1 = true;
                                    tvTurno.setText(name1+" selecciona una casilla");
                                    puntaje2++;
                                }
                            }else{
                                Toast.makeText(this, "¡No es tu turno!", Toast.LENGTH_SHORT).show();
                            }
                            if (puntaje1 == 12 || puntaje2 == 12){
                                if (partidaCorta < disparos1 || partidaCorta <  disparos2){
                                    int max = Math.max(disparos1,disparos2);
                                    partidaCorta = max;
                                    data.edit().putInt("partida", partidaCorta).apply();
                                }
                                if (puntaje1 > puntaje2){
                                    tvTurno.setText(name1+" ha ganado \n"+"Partida mas corta: "+partidaCorta);
                                }else{
                                    tvTurno.setText(name2+" ha ganado \n"+"Partida mas corta: "+partidaCorta);
                                }
                                GUI1.setVisibility(View.INVISIBLE);
                                GUI2.setVisibility(View.INVISIBLE);
                                mediaPlayer.release();
                                laser.release();
                                bomba.release();

                            }
                        });
                    }
                }
            }
        }
    }


}