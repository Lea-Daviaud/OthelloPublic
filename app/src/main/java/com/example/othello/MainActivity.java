package com.example.othello;

import static com.example.othello.GameView.joueur1;
import static com.example.othello.GameView.joueur2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.othello.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    static TextView textView;
    private final Strategy STRATEGY = Strategy.P2P_STAR;
    private ConnectionsClient connectionsClient;
    private Player opponent;
    private String opponentEndpointId = null;
    private int opponentScore = 0;

    private Player currentPlayer;  // Joueur actuel (moi)
    private Player opponentPlayer;  // Joueur adverse
    private int myCodeName;
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
          //  opponent = new Player(info.getEndpointName()); // Le nom de l'adversaire vient de l'info de la connexion
            opponentPlayer = new Player(Player.WHITE);
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
            // Convertit myCodeName en String avant de l'utiliser dans requestConnection
            connectionsClient.requestConnection(String.valueOf(myCodeName), endpointId, connectionLifecycleCallback);
        }

        @Override
        public void onEndpointLost(String endpointId) {
            // Rien à faire ici pour l'instant
        }
    };


    private void setGameControllerEnabled(boolean enabled) {
        // Exemple pour activer/désactiver des boutons ou des vues de ton jeu
        binding.findOpponent.setEnabled(enabled); // Si tu as un bouton "findOpponent"
        // Active ou désactive d'autres vues de ton jeu ici, par exemple :
        // binding.otherButton.setEnabled(enabled);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_main);
        textView = findViewById(R.id.currentPl);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectionsClient = Nearby.getConnectionsClient(this);

        // Initialisation des joueurs (le joueur local est joueur 1)
        currentPlayer = new Player(Player.BLACK); // Le joueur local est toujours noir
        myCodeName = currentPlayer.getColor();

        // Lorsque l'utilisateur clique sur "findOpponent"
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
                String.valueOf(myCodeName),
                getPackageName(),
                connectionLifecycleCallback,
                options
        );
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Affiche une explication à l'utilisateur
            new AlertDialog.Builder(this)
                    .setMessage("Cette permission est nécessaire pour trouver des joueurs à proximité.")
                    .setPositiveButton("OK", (dialog, which) -> requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_REQUIRED_PERMISSIONS
                    ))
                    .show();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_REQUIRED_PERMISSIONS);
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
        opponent = null;
        opponentScore = 0;
        myScore = 0;
        currentPlayer = new Player(Player.BLACK); // Recommence avec le joueur local comme joueur noir
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