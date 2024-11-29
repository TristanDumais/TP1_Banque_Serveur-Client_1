package com.programmes;

import com.atoudeft.client.Client;
import com.atoudeft.client.Config;

import java.util.Scanner;

public class ProgrammeClientTP1 {

	public static void main(String[] args) {
		// Configuration par défaut
		String host = "localhost";
		int port = 8080;

		// Initialisation du client
		Client client = new Client(Config.ADRESSE_SERVEUR, Config.PORT_SERVEUR);

		try {
			// Connexion au serveur
			System.out.println("Connexion au serveur " + host + " sur le port " + port + "...");
			boolean connecte = client.connecter();

			if (connecte) {
				System.out.println("Connexion réussie. Tapez vos commandes. Tapez EXIT pour quitter.");

				// Gestion des commandes utilisateur
				Scanner scanner = new Scanner(System.in);
				String commande;

				do {
					System.out.print("> ");
					commande = scanner.nextLine();

					// Envoi de la commande au serveur
					client.envoyer(commande);

					// Confirmation d'envoi
					System.out.println("Commande envoyée au serveur : " + commande);

				} while (!commande.equalsIgnoreCase("EXIT"));

				System.out.println("Déconnexion...");
			} else {
				System.out.println("Impossible de se connecter au serveur.");
			}
		} catch (Exception e) {
			System.out.println("Erreur : " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Assurer la fermeture de la connexion
			client.deconnecter();
			System.out.println("Client déconnecté.");
		}
	}
}
