package com.example.examenbatallanaval;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button btnComenzar,btnSiguiente;
    Switch btnDireccion;
    View selectorNaves;
    LinearLayout tablerodeJuego;
    EditText nombre1,nombre2;
    TextView texto1,texto2,tvTurno;
    String name1,name2;
    int[] naves = { 3, 3, 2, 2, 1, 1};
    int[][] tablero1 = new int[6][8];
    int[][] tablero2 = new int[6][8];
    int[][] tableroTemp = new int[6][8];
    ImageView[][] GUI = new ImageView[6][8];
    int index = 0;
    boolean player1Turn = true,naveColocada= false,gameReady = false;
    MediaPlayer mediaPlayer;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.start();
        btnComenzar = findViewById(R.id.btnComenzar);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnDireccion = findViewById(R.id.orientacion);
        selectorNaves = findViewById(R.id.selector);
        tablerodeJuego = selectorNaves.findViewById(R.id.tablero);
        nombre1 = findViewById(R.id.nombre1);
        nombre2 = findViewById(R.id.nombre2);
        texto1 = findViewById(R.id.jugador1);
        texto2 = findViewById(R.id.jugador2);
        tvTurno = findViewById(R.id.turno);
        for (int i = 0; i < tablerodeJuego.getChildCount(); i++) {
            View filas = tablerodeJuego.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        GUI[i][j] = (ImageView) imagen;
                    }
                }
            }
        }

        // Aplicamos onCLickListener a cada casilla
        for (int i = 0; i < tablerodeJuego.getChildCount(); i++) {
            View filas = tablerodeJuego.getChildAt(i);
            if (filas instanceof LinearLayout) {
                for (int j = 0; j < ((LinearLayout) filas).getChildCount(); j++) {
                    // ImageView dentro de cada LinearLayour
                    View imagen = ((LinearLayout) filas).getChildAt(j);
                    if (imagen instanceof ImageView) {
                        int finalI = i;
                        int finalJ = j;
                        imagen.setOnClickListener(l -> {
                            if (!naveColocada){
                                int longitud = naves[index];
                                // Vertical si esta activo, Horizontal si no lo esta
                                int orientacion = btnDireccion.isChecked() ? 0 : 1;
                                boolean posicion = false;
                                boolean fits = true;
                                if (orientacion == 0 && finalI + longitud  > tableroTemp.length ) {
                                    fits = false;
                                } else if (orientacion == 1 && finalJ + longitud  > tableroTemp[0].length) {
                                    fits = false;
                                }
                                // Checa que no choque con otra nave
                                if (fits) {
                                    boolean overlaps = false;
                                    for (int t = 0; t < longitud; t++) {
                                        if (orientacion == 0 && tableroTemp[finalI +t][finalJ] != 0) {
                                            overlaps = true;
                                        } else if (orientacion == 1 && tableroTemp[finalI][finalJ +t] != 0 ) {
                                            overlaps = true;
                                        }
                                    }
                                    if (!overlaps) {
                                        for (int d = 0; d < longitud; d++) {
                                            if (orientacion == 0) {
                                                tableroTemp[finalI +d][finalJ] = finalI+1;
                                                GUI[finalI +d][finalJ].setImageResource(imagenes(naves[index]));
                                            } else {
                                                tableroTemp[finalI][finalJ +d] = finalI+1;
                                                GUI[finalI][finalJ +d].setImageResource(imagenes(naves[index]));
                                            }
                                        }
                                        posicion = true;
                                        naveColocada = true;
                                    }
                                    else{
                                        Toast.makeText(this, "No hay suficiente espacio", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!posicion) {
                                    Toast.makeText(this, "Selecciona otra casilla", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    // Coloca las naves
    public void comenzar(View view){
        name1 = nombre1.getText().toString();
        name2 = nombre2.getText().toString();
        btnComenzar.setVisibility(View.INVISIBLE);
        btnSiguiente.setVisibility(View.VISIBLE);
        btnDireccion.setVisibility(View.VISIBLE);
        nombre1.setVisibility(View.INVISIBLE);
        nombre2.setVisibility(View.INVISIBLE);
        texto1.setVisibility(View.INVISIBLE);
        texto2.setVisibility(View.INVISIBLE);
        tvTurno.setVisibility(View.VISIBLE);
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.drawable.screen2);
        BitmapDrawable background = new BitmapDrawable(getResources(), bitmap);
        layout.setBackground(background);
        selectorNaves.setVisibility(View.VISIBLE);
        String text = name1 + " coloca la nave de " + naves[index] + " espacios";
        tvTurno.setText(text);
    }
    public void siguiente(View view){
        if (naveColocada){
            String jugador = player1Turn ? name1 : name2;
            index++;
            // Si no se han colocado todas las naves
            if (index <= 5){
                naveColocada = false;
                String text = jugador + " coloca la nave de " + naves[index] + " espacios";
                tvTurno.setText(text);
            } else {
                // Si ya se colocaron todas las naves
                if (player1Turn) {
                    // Jugador 1 coloca todas sus naves
                    for (int i = 0; i < tableroTemp.length; i++) {
                        System.arraycopy(tableroTemp[i], 0, tablero1[i], 0, tableroTemp[i].length);
                    }
                    player1Turn = false;
                    index = 0;
                    naveColocada = false;
                    resetTableroTemp();
                } else {
                    // Ambos jugadores colocan sus naves
                    for (int i = 0; i < tableroTemp.length; i++) {
                        System.arraycopy(tableroTemp[i], 0, tablero2[i], 0, tableroTemp[i].length);
                    }
                    gameReady = true;
                }

                if (!gameReady){
                    jugador = player1Turn ? name1 : name2;
                    String text = jugador + " coloca la nave de " + naves[index] + " espacios";
                    tvTurno.setText(text);
                }
            }
        } else {
            Toast.makeText(this, "Coloca una nave para continuar", Toast.LENGTH_SHORT).show();
        }

        // Si ambos jugadores colocaron sus naves oculta el boton
        if (gameReady){
            btnSiguiente.setVisibility(View.INVISIBLE);
            tvTurno.setVisibility(View.INVISIBLE);
            Intent partida = new Intent(this, TablerosJuego.class);
            partida.putExtra("tablero1",tablero1);
            partida.putExtra("tablero2",tablero2);
            partida.putExtra("name1",name1);
            partida.putExtra("name2",name2);
            // Launch the activity of player 1
            startActivity(partida);
            finish();
        }
    }

    private void resetTableroTemp() {
        for (int i = 0; i < tableroTemp.length; i++) {
            for (int j = 0; j < tableroTemp[0].length; j++) {
                tableroTemp[i][j] = 0;
                GUI[i][j].setImageResource(0);
            }
        }
    }


    public int imagenes(int imagen){
        int nave;
        switch (imagen){
            case 1:
                nave = R.drawable.naveuno;
                break;
            case 2:
                nave = R.drawable.navedos;
                break;
            case 3:
                nave = R.drawable.navetres;
                break;
            default:
                nave = 0;
        }
        return nave;
    }
}