package com.example.iot;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

import static com.example.iot.registraroiniciarActivity.consumoD;
import static com.example.iot.registraroiniciarActivity.consumoJ;
import static com.example.iot.registraroiniciarActivity.consumoL;
import static com.example.iot.registraroiniciarActivity.consumoMi;
import static com.example.iot.registraroiniciarActivity.consumoS;
import static com.example.iot.registraroiniciarActivity.consumoV;
import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link graph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class graph extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String correogg;

    private LineGraphSeries<DataPoint> series;
   // long consumoL,consumoM,consumoMi,consumoJ,consumoV,consumoS,consumoD;
    private String mParam2;
    View vista;
    public graph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment graph.
     */
    // TODO: Rename and change types and number of parameters
    public static graph newInstance(String param1, String param2) {
        graph fragment = new graph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           /* consumoL = getArguments().getLong("consumoL");
            consumoM = getArguments().getLong("consumoM");
            consumoMi = getArguments().getLong("consumoMi");
            consumoJ = getArguments().getLong("consumoJ");
            consumoV = getArguments().getLong("consumoV");
            consumoS = getArguments().getLong("consumoS");
            consumoD = getArguments().getLong("consumoD");
            correogg = getArguments().getString("correo");*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_graph, container, false);


        //Se crean primero 5 puntos iniciales en la grafica

        GraphView graph1 = (GraphView) vista.findViewById(R.id.grafica); //Se asigna la variable a donde se mostrara el grafico en XML
        series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, consumoL),
                new DataPoint(2, registraroiniciarActivity.consumoM),
                new DataPoint(3, consumoMi),
                new DataPoint(4, consumoJ),
                new DataPoint(5, consumoV),
                new DataPoint(6, consumoS),
                new DataPoint(7, consumoD)
        });
        graph1.addSeries(series); //Se agregan los puntos creados al grafico
        //Se establecen las configuraciones en los ejes X y Y que tendra la grafica
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph1);
        graph1.setTitle("Gráfica de consumo semanal");
        graph1.setTitleColor(Color.GRAY);
        graph1.getGridLabelRenderer().setVerticalAxisTitle("Cantidad [ml]");
        graph1.getGridLabelRenderer().setHorizontalAxisTitle("Días");
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Lun", "Mar", "Mier","Jue","Vier","Sab","Dom"});
        graph1.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph1.getViewport().setMinY(0);
        graph1.getGridLabelRenderer().setTextSize(20f);
        graph1.getGridLabelRenderer().reloadStyles();

        graph1.getViewport().setMaxY(1);
        graph1.getGridLabelRenderer().setHighlightZeroLines(false);
        graph1.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graph1.getGridLabelRenderer().setLabelVerticalWidth(100);
        graph1.getGridLabelRenderer().setTextSize(30);
        graph1.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph1.getGridLabelRenderer().setHorizontalLabelsAngle(120);
        graph1.getGridLabelRenderer().reloadStyles();
        graph1.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph1.getGridLabelRenderer().setHighlightZeroLines(false);
        graph1.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph1.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graph1.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        graph1.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph1.getGridLabelRenderer().reloadStyles();
        graph1.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
        graph1.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);


        return vista;
    }
}