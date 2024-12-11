package com.example.othello;

import static com.example.othello.GameView.joueur1;
import static com.example.othello.GameView.joueur2;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

//import com.example.pfciseaux.databinding.ActivityMainBinding;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final Strategy STRATEGY = Strategy.P2P_STAR;
    private ConnectionsClient connectionsClient;
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private String opponentName = null;
    private String opponentEndpointId = null;
    private int opponentScore = 0;
    private String myCodeName;
    private int myScore = 0;

    private ActivityMainBinding binding;

    private final PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            byte[] bytes = payload.asBytes();
            if (bytes != null) {
            }
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            // Détermine le gagnant et met à jour l'état du jeu/l'interface utilisateur une fois que les deux joueurs ont choisi.
            //// N'hésitez pas à refactoriser et à extraire ce code dans une méthode différente

                setGameControllerEnabled(true);
            }
    };

    // Rappels pour les connexions à d'autres appareils
    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String endpointId, ConnectionInfo info) {
            // Accepter une connexion signifie que vous souhaitez recevoir des messages. Par conséquent, l'API s'attend à ce que
            // vous attachiez un PayloadCall à l'acceptation
            connectionsClient.acceptConnection(endpointId, payloadCallback);
            opponentName = "Opponent\n(" + info.getEndpointName() + ")";
        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            if (result.getStatus().isSuccess()) {
                connectionsClient.stopAdvertising();
                connectionsClient.stopDiscovery();
                opponentEndpointId = endpointId;
                setGameControllerEnabled(true); // we can start playing
            }
        }

        @Override
        public void onDisconnected(String endpointId) {
            resetGame();
        }
    };

    // Rappels pour trouver d'autres appareils
    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
            connectionsClient.requestConnection(myCodeName, endpointId, connectionLifecycleCallback);
        }

        @Override
        public void onEndpointLost(String endpointId) {

        }
    };


    static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_main);
        textView = findViewById(R.id.currentPl);
        if (opponentName == joueur2.getNom()) {
            myCodeName = joueur1.getNom();
        } else {
            myCodeName = joueur2.getNom();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        connectionsClient = Nearby.getConnectionsClient(this);


        binding.findOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdvertising();
                startDiscovery();

            }
        });

        resetGame(); // we are about to start a new game
    }

    private void startAdvertising() {
        AdvertisingOptions options = new AdvertisingOptions.Builder()
                .setStrategy(STRATEGY)
                .build();

        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                myCodeName,
                getPackageName(),
                connectionLifecycleCallback,
                options
        );
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_REQUIRED_PERMISSIONS
            );
        }
    }

    @Override
    @CallSuper
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String errMsg = "Cannot start without required permissions";
        if (requestCode == REQUEST_CODE_REQUIRED_PERMISSIONS) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            recreate();
        }
    }

    /**
     * Efface tout l’état du jeu et met à jour l’interface utilisateur en conséquence.
     */
    private void resetGame() {
        // reset data
        opponentEndpointId = null;
        opponentName = null;
        opponentScore = 0;
        myScore = 0;

    }

    private void startDiscovery() {
        DiscoveryOptions options = new DiscoveryOptions.Builder()
                .setStrategy(STRATEGY)
                .build();

        connectionsClient.startDiscovery(getPackageName(), endpointDiscoveryCallback, options);
    }

    @Override
    protected void onStop() {
        super.onStop();

        connectionsClient.stopAdvertising();
        connectionsClient.stopDiscovery();
        connectionsClient.stopAllEndpoints();

        resetGame();
    }
}