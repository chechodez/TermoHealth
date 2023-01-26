package com.example.iot;

import android.app.Fragment;
import android.app.Notification;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.os.Handler;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import static java.lang.Integer.parseInt;


public class registraroiniciarActivity extends AppCompatActivity {

    //inicialización de variables
    ImageButton botonhome,botonperfil,botoninfo,botonedit,botongraph;
    private Handler mHandler = new Handler();
    Timer timer=new Timer();

    Timer timer2 =new Timer();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refHome = database.getReference("home");
    DatabaseReference refSensores, refTemperatura, refTouch, refWata,refLed1,refLed2;
    int a=0;
    long edad;
    int dia=-1;
    long tiempoA= System.currentTimeMillis(),tiempoB= System.currentTimeMillis();
    int hola=0;
    double cte = 0;
    long peso;
    private final static String channel_id="notificacion";
    private final static int notification_id=0;
    long estatura;
    long temperatura, touch, wata,wataA,wataB,ref=0;
    public static long consumo=0;
    public static long consumoL,consumoM,consumoMi,consumoJ,consumoV,consumoS,consumoD;
    String sexo;
    //declaración de fragments
    home2 Home2=new home2();
    profiles Profiles= new profiles();
    edit Edit= new edit();
    info Info= new info();
    graph Graph= new graph();
    //conectividad con firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    long x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraroiniciar);

        refSensores = refHome.child("sensores");// Se asocia el tema de sensores, donde estara la lectura de todos los sensores
        //Se asocian los temas que estan dentro de "sensores", donde cada uno de estos es la lectura de un sensor
        refTemperatura = refSensores.child("temperatura");
        refTouch = refSensores.child("touch");
        refWata = refSensores.child("wata");
        refLed1 = refSensores.child("led1");
        refLed2 = refSensores.child("led2");

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                createnotificationchannel();
                createnotification();
            }
        };
        timer.schedule(task,10,10000);
        TimerTask task2= new TimerTask() {
            @Override
            public void run() {
                if(dia==0){
                    a=1;
                    consumoL=consumo;

                }else if(dia==1){
                    a=1;
                    consumoM=consumo;

                }else if(dia==2){
                    a=1;
                    consumoMi=consumo;

                }else if(dia==3){
                    a=1;
                    consumoJ=consumo;

                }else if(dia==4){
                    a=1;
                    consumoV=consumo;

                }else if(dia==5){
                    a=1;
                    consumoS=consumo;
                }else if(6 == dia){
                    a=1;
                    consumoD=consumo;

                }
                if(dia!=6){
                dia=dia+1;
                }else{
                dia=0;
                }
                consumo=0;
            }
        };
        timer2.schedule(task2,10,120000);

        Bundle bundle= new Bundle();//se crea un bundle para pasar variables a un fragment
        Bundle bundle2= new Bundle();//se crea un bundle para pasar variables a un fragment
        Intent intent = getIntent();//recuperación de variables de activity anterior
        String recuperamos_variable_string = getIntent().getStringExtra("correo");//Recuperación de usuario
        bundle.putString("correo",recuperamos_variable_string);//Envío de variable correo a fragment
        if(a==1){
            bundle2.putLong("consumoL",consumoL);
            bundle2.putLong("consumoM",consumoM);
            bundle2.putLong("consumoMi",consumoMi);
            bundle2.putLong("consumoJ",consumoJ);
            bundle2.putLong("consumoV",consumoV);
            bundle2.putLong("consumoS",consumoS);
            bundle2.putLong("consumoD",consumoD);

            Graph.setArguments(bundle2);
            a=0;
        }
        Home2.setArguments(bundle);
        Profiles.setArguments(bundle);
        Edit.setArguments(bundle);
        Graph.setArguments(bundle);

        //Envío de bundle a fragments Home2, profiles y edit

        //Identificación de botones por id
        botonhome=(ImageButton) findViewById(R.id.imageButton);
        botonperfil=(ImageButton) findViewById(R.id.imageButton2);
        botoninfo=(ImageButton) findViewById(R.id.imageButton3);
        botonedit=(ImageButton) findViewById(R.id.imageButton4);
        botongraph=(ImageButton) findViewById(R.id.imageButton5);
        Wata(refWata,refLed1,refLed2);

        //Obtención de datos registrados por el usuario por medio de firestore
        db.collection("users").document(recuperamos_variable_string).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    edad=documentSnapshot.getLong("edad");
                    peso=documentSnapshot.getLong("peso");
                    estatura=documentSnapshot.getLong("estatura");
                    sexo=documentSnapshot.getString("genero");
                    bundle.putLong("edad",edad);
                    bundle.putLong("peso",peso);
                    bundle.putLong("estatura",estatura);
                    bundle.putString("sexo",sexo);
                }
            }
        });

        //las siguientes líneas de código hacen que cuando se entre a la aplicación el primer fragment que se vea sea Home2
        if(x==0){
            FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
            transition1.replace(R.id.fragment11,Home2);
            transition1.commit();
            x=1;
        }

        //lectura de boton home para cambio de fragment
        botonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
                transition1.replace(R.id.fragment11,Home2);
                transition1.commit();


            }
        });
        botongraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
                transition1.replace(R.id.fragment11,Graph);
                transition1.commit();


            }
        });
        //lectura de boton perfil para cambio de fragment
        botonperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
                transition1.replace(R.id.fragment11,Profiles);
                transition1.commit();
            }
        });
        //lectura de boton edit para cambio de fragment
        botonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
                transition1.replace(R.id.fragment11,Edit);
                transition1.commit();
            }
        });
        //lectura de boton info para cambio de fragment
        botoninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transition1= getSupportFragmentManager().beginTransaction();
                transition1.replace(R.id.fragment11,Info);
                transition1.commit();
            }
        });
    }
    //esta función hace que cuando se presione el botón sing out se vaya al inicio de sesión
    public void goMain(View view){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
    private void createnotification( ) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
        builder.setSmallIcon(R.drawable.joamani);

        if(((float)consumo/1000)<(home2.consumouwu2)) {
            builder.setContentTitle("Reporte temporal");
            builder.setContentText("Su consumo ha sido de: "+Long.toString(consumo)+"ml");
        }else{
            builder.setContentTitle("Has cumplido tu meta diara!");
            builder.setContentText("Has tomado: "+ Long.toString(consumo) + " ml.");
        }


        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(notification_id, builder.build());
    }
    private void createnotificationchannel( ) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="notificacion";
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void Wata(final DatabaseReference refWata,final DatabaseReference refLed1,final DatabaseReference refLed2) {
    refWata.addValueEventListener(new ValueEventListener() {//Se evalua si se ha recibido un nuevo dato del circuito medidor de agua
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            long estado_pulsador = (long) (dataSnapshot.getValue());//Se toma el dato leido por el circuito medidor de agua y se asigna a la variable estado_pulsador

            wata = estado_pulsador;//Se actualiza la variable que lleva los datos del touch
            if (ref == 0) {
                wataA = wata;
                ref = 1;
            } else {
                wataB = wataA;
                wataA = wata;
            }
            if ((wataB == 100) & (wataA == 75)) {
                consumo = consumo + 250;
            } else if ((wataB == 100) & (wataA == 50)) {
                consumo = consumo + 500;
            } else if ((wataB == 100) & (wataA == 25)) {
                consumo = consumo + 750;
            } else if ((wataB == 100) & (wataA == 0)) {
                consumo = consumo + 1000;
            } else if ((wataB == 75) & (wataA == 50)) {
                consumo = consumo + 250;
            } else if ((wataB == 75) & (wataA == 25)) {
                consumo = consumo + 500;
            } else if ((wataB == 75) & (wataA == 0)) {
                consumo = consumo + 750;
            } else if ((wataB == 50) & (wataA == 25)) {
                consumo = consumo + 250;
            } else if ((wataB == 50) & (wataA == 0)) {
                consumo = consumo + 500;
            } else if ((wataB == 25) & (wataA == 0)) {
                consumo = consumo + 250;
            }
            if(consumo>(home2.consumouwu2)*1000){
                refLed1.setValue(1);
                refLed2.setValue(0);
            }else{
                refLed1.setValue(0);
                refLed2.setValue(1);
            }


        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    });
    }}
