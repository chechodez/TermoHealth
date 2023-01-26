package com.example.iot;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.os.Handler;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;
import static java.lang.Math.round;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home2#newInstance} factory method to
 * create an instance of this fragment.
 */


/**
 En la funcion home2 se inicializan las variables a utilizar en el codigo y se realiza la conexion con firabase

 */
public class home2 extends Fragment {
    //Variables utilizadas para graficacion en tiempo real
    private Handler mHandler = new Handler();
    private LineGraphSeries<DataPoint> series;
    private double lastXPoint = 0.4;
    // Variables de los textos que se verian en cada boton
    TextView textbienvenido, textToggleButton,textconsumo;
    TextView textEstadoPulsador;
    // Variables de los iconos que se verian en cada boton
    ImageButton botonhome, botonperfil, botoninfo;
    ImageView botella;
    //Variables que obtienen la lectura de cada sensor
    long temperatura, touch, wata,wataA,wataB,consumo,ref=0;
    boolean f;
    long cont;
    //Conexion con firebase y las variables que se quieren leer de firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("home");
    DatabaseReference refSensores, refTemperatura, refTouch, refWata;
    View vista;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Variables creadas por Android al crear el fragmento
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Variables que tomaran los datos ingresados por el usuario en la app
    long edadgg;
    long edad;
    long peso;
    long estatura;
    String sexo,ejercicio;
    long pesogg;
    long estaturagg;
    String sexogg;
    private String correogg;
    public static float consumouwu2;
    public static String[] correofake;

    public home2() {
        // Required empty public constructor
    }


    //
    public static home2 newInstance(String param1, String param2) {
        home2 fragment = new home2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    //* En la funcion onCreate se hace la lectura en este fragmento, de los datos que fueron ingresados por el usuario y se obtuvieron en la actividad registraroiniciarActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { //En caso de que getArguments() no este vacio, se entra al if
            //Haciendo uso de la funcion getArguments().getString("correo"), se obtiene el dato de correo para este caso.
            correogg = getArguments().getString("correo");
            edadgg = getArguments().getLong("edad");
            pesogg = getArguments().getLong("peso");
            estaturagg = getArguments().getLong("estatura");
            sexogg = getArguments().getString("sexo");

        }
    }

    @Override
    //En la funcion onCreateView se realiza la lectura de los sensores y permite la visualizacion de la lectura de estos, junto con la visualizacion de la grafica de temperatura.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_home2, container, false); // Se asocia el codigo de home2 con un layout en XML
        //Lectura de sensores, asociando las variables al tema en firebase correspondiente para cada sensor
        refSensores = refHome.child("sensores");// Se asocia el tema de sensores, donde estara la lectura de todos los sensores
        //Se asocian los temas que estan dentro de "sensores", donde cada uno de estos es la lectura de un sensor
        refTemperatura = refSensores.child("temperatura");
        refTouch = refSensores.child("touch");
        refWata = refSensores.child("wata");

        //Se asigna una variable a cada texto que se mostrara en el boton que se observa en: (R.id."Nombre del Boton")
        textEstadoPulsador = (TextView) vista.findViewById(R.id.textViewPulsador);
        textToggleButton = (TextView) vista.findViewById(R.id.textView);
        textbienvenido = (TextView) vista.findViewById(R.id.textbienvenido);
        textconsumo   = (TextView) vista.findViewById(R.id.textConsumo);
        //Se asigna una variable a la imagen que se mostrara en el image view que se observa en: (R.id."Nombre del Boton")
        ImageView botella = (ImageView) vista.findViewById(R.id.imageView4);
        //Se llama cada una de las funciones que mostraran la lectura de los sensores en la aplicacion
        Temperatura(refTemperatura, textEstadoPulsador);
        Touch(refTouch, textEstadoPulsador);
        Wata(refWata, textToggleButton, botella);
        //Las siguientes lineas de codigo son para la graficacion del sensor de temperatura
        GraphView graph = (GraphView) vista.findViewById(R.id.graph); //Se asigna la variable a donde se mostrara el grafico en XML
        obtenerDatos(correogg);
        //Se crean primero 5 puntos iniciales en la grafica
        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 22),
                new DataPoint(0.1, 22),
                new DataPoint(0.2, 22),
                new DataPoint(0.3, 22),
                new DataPoint(0.4, 22)
        });
        graph.addSeries(series); //Se agregan los puntos creados al grafico
        //Se establecen las configuraciones en los ejes X y Y que tendra la grafica
        graph.setTitle("Temperatura en tiempo real");
        graph.setTitleColor(Color.GRAY);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Temperatura [C°]");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Muestras");
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);
        graph.getGridLabelRenderer().setTextSize(30);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(120);
        graph.getGridLabelRenderer().reloadStyles();
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().reloadStyles();
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);
        addRandomDataPoint(); //Se llama a la funcion encargada de graficar la lectura del sensor de temperatura
         return vista;

    }

    //La funcion addRandomDataPoint se encargara de graficar el sensor de temperatura
    private void addRandomDataPoint() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastXPoint = lastXPoint + 0.1; //Se suma 0.1 al ultimo punto leido en X
                if ((f)) {
                    cont = temperatura;
                }

                series.appendData(new DataPoint(lastXPoint, cont), true, 1000); //Se agrega los puntos de X y Y que se desean graficar
                addRandomDataPoint(); //Se vuelve a llamar a la misma funcion para repetir este mismo proceso varias veces
            }
        }, 100);

    }

    //En la funcion Temperatura se establece cuando se agregara un nuevo punto a la grafica y cuando no
    private void Temperatura(final DatabaseReference refTemperatura, final TextView textEstadoPulsador) {

        refTemperatura.addValueEventListener(new ValueEventListener() { //Se evalua si se ha recibido un nuevo dato del sensor de temperatura
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //
                long estado_pulsador = (long) (dataSnapshot.getValue()); //Se toma el dato leido por temperatura y se asigna a la variable estado_pulsador
                temperatura = estado_pulsador; //Se actualiza la variable que lleva los datos que se grafican en la funcion addRandomDataPoint
                f = true; //Se pone f en true para que grafique el nuevo dato de temperatura


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //En la funcion Touch se muestra en la aplicacion la lectura del sensor touch
    private void Touch(final DatabaseReference refTouch, final TextView textEstadoPulsador) {

        refTouch.addValueEventListener(new ValueEventListener() {//Se evalua si se ha recibido un nuevo dato del sensor de touch
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long estado_pulsador = (long) (dataSnapshot.getValue()); //Se toma el dato leido por el touch y se asigna a la variable estado_pulsador
                touch = estado_pulsador;//Se actualiza la variable que lleva los datos del touch
                if (touch == 1) {
                    textEstadoPulsador.setText("Dispositivo en uso"); //Si el sensor touch esta en alto, se imprime en pantalla touch activo
                } else {
                    textEstadoPulsador.setText("Dispositivo en reposo");//Si el sensor touch esta en bajo, se imprime en pantalla touch inactivo
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void obtenerDatos(String correo){
        db.collection("users").document(correo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { //Se evalua si llega un nuevo dato de los ingresados por el usuario
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) { //Se toman los datos ingresados por el usuario en la aplicacion
                if(documentSnapshot.exists()){ //Si los datos ingresados no esta vacio se entra al if
                    //Se toman las variables long y string ingresadas por el usuario, ademas se pone en field, el dato que se quiere leer
                    edad=documentSnapshot.getLong("edad"); //
                    peso=documentSnapshot.getLong("peso");
                    estatura=documentSnapshot.getLong("estatura");
                    sexo=documentSnapshot.getString("genero");
                    ejercicio=documentSnapshot.getString("frecuencia de ejercicio");
                    correofake=correogg.split("@");
                    double cte = 0;
                    if(ejercicio.equals("Diariamente")){
                        cte = 1;
                    }
                    if(ejercicio.equals("Semanalmente")){
                        cte = 0.5;
                    }
                    consumouwu2 = (float) ((float)peso/30+cte);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String consumouwu = df.format((float)peso/30+cte);
                    String consumouwu3 = df.format(consumo/1000);
                    textbienvenido.setText("Bienvenido" + " " + correofake[0] + "\n"+"La cantidad de agua que debes tomar diariamente es: "+consumouwu+" L"); //Se establece el mensaje mostrado en la aplicacion
                    //Se asignan las variables a cada texto que se mostrara en el boton que se observa en: (R.id."Nombre del Boton")

                    //Se asignan los textos que se mostratan en cada boton



                }
            }
        });
    }

    //En la funcion Wata se realiza el mostrado del porcentaje de la botella tanto en imagen como en texto
    private void Wata(final DatabaseReference refWata, final TextView textToggleButton, final ImageView botella) {

        refWata.addValueEventListener(new ValueEventListener() {//Se evalua si se ha recibido un nuevo dato del circuito medidor de agua
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long estado_pulsador = (long) (dataSnapshot.getValue());//Se toma el dato leido por el circuito medidor de agua y se asigna a la variable estado_pulsador

                wata = estado_pulsador;//Se actualiza la variable que lleva los datos del touch
                if(ref==0){
                    wataA=wata;
                    ref=1;
                }else{
                    wataB=wataA;
                    wataA=wata;
                }
                if((wataB==100)&(wataA==75)){
                    consumo=consumo+250;
                }else if((wataB==100)&(wataA==50)){
                    consumo=consumo+500;
                }else if((wataB==100)&(wataA==25)){
                    consumo=consumo+750;
                }else if((wataB==100)&(wataA==0)){
                    consumo=consumo+1000;
                }else if((wataB==75)&(wataA==50)){
                    consumo=consumo+250;
                }else if((wataB==75)&(wataA==25)){
                    consumo=consumo+500;
                }else if((wataB==75)&(wataA==0)){
                    consumo=consumo+750;
                }else if((wataB==50)&(wataA==25)){
                    consumo=consumo+250;
                }else if((wataB==50)&(wataA==0)){
                    consumo=consumo+500;
                }else if((wataB==25)&(wataA==0)){
                    consumo=consumo+250;
                }
                //En caso de ser wata igual a 0,25,50,75,100. Se muestra en la aplicacion una botella que demuestra el porcentaje de llenado y tambien se muestra en un texto
                if (wata == 0) {
                    botella.setImageResource(R.drawable.cero);
                    textToggleButton.setText("0%");
                } else if (wata == 25) {
                    botella.setImageResource(R.drawable.venticinco);
                    textToggleButton.setText("25%");
                } else if (wata == 50) {
                    botella.setImageResource(R.drawable.cincuenta);
                    textToggleButton.setText("50%");
                } else if (wata == 75) {
                    botella.setImageResource(R.drawable.sietecinco);
                    textToggleButton.setText("75%");
                } else if (wata == 100) {
                    botella.setImageResource(R.drawable.sien);
                    textToggleButton.setText("100%");
                }
                if(registraroiniciarActivity.consumo>consumouwu2*1000){
                    textconsumo.setText("Felicidades ya cumpliste con tu meta diaria, has consumido:"+registraroiniciarActivity.consumo+" ml hoy");
                }else{
                    textconsumo.setText("Su consumo el día de hoy ha sido de:"+registraroiniciarActivity.consumo+ " ml");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
