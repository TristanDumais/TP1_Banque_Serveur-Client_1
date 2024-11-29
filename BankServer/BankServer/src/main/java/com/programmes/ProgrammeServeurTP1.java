package com.programmes;

import com.atoudeft.serveur.Config;
import com.atoudeft.banque.serveur.ServeurBanque;

import java.util.Scanner;

public class ProgrammeServeurTP1 {

    public static void main(String[] args) {
        // Initialisation du serveur
        ServeurBanque serveur = new ServeurBanque(Config.PORT_SERVEUR);

        try {
            // Démarrage du serveur
            System.out.println("Démarrage du serveur sur le port " + Config.PORT_SERVEUR + "...");
            boolean serveurDemarre = serveur.demarrer();

            if (serveurDemarre) {
                System.out.println("Serveur démarré avec succès. Appuyez sur ENTER pour arrêter.");
                // Attend une commande pour arrêter le serveur
                new Scanner(System.in).nextLine();
            } else {
                System.out.println("Échec du démarrage du serveur.");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Arrêt propre du serveur
            serveur.arreter();
            System.out.println("Serveur arrêté.");
        }
    }
}
