package com.atoudeft.serveur;

import com.atoudeft.banque.Banque;
import com.atoudeft.banque.CompteBancaire;
import com.atoudeft.banque.CompteClient;
import com.atoudeft.banque.CompteEpargne;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    private List<ConnexionBanque> activeConnections = new ArrayList<>();

    /**
     * Verifie si le client est deja connecter a une session
     *
     * @param numCompteClient Numero du compte du client
     * @return true si deja connecter
     */
    public boolean isAccountConnected(String numCompteClient) {

        //Verifie a travers une liste de connexions
        for (ConnexionBanque connexion : activeConnections) {

            //Verifie que la connexion actuelle utilise le bon numero de compte
            if (numCompteClient.equals(connexion.getNumeroCompteClient())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque)serveur;
        Banque banque;
        ConnexionBanque cnx;
        String msg, typeEvenement, argument, numCompteClient, nip;
        String[] t;

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;
                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "NOUVEAU": //Crée un nouveau compte-client :
                    if (cnx.getNumeroCompteClient()!=null) {
                        cnx.envoyer("NOUVEAU NO deja connecte");
                        break;
                    }
                    //On separe l'argument dans une liste, afin d'utliser la partie qu'on veut
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];
                        banque = serveurBanque.getBanque();
                        if (banque.ajouter(numCompteClient,nip)) {
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");
                        }
                        else
                            cnx.envoyer("NOUVEAU NO "+t[0]+" existe");
                    }
                    break;
                case "EPARGNE": //Creer un compte epargne :
                    banque = serveurBanque.getBanque();
                    numCompteClient = cnx.getNumeroCompteClient();

                    //Si le client n'est pas connecter
                    if (cnx.getNumeroCompteClient() == null){
                        cnx.envoyer("EPARGNE NO");
                        break;
                    }
                    //Si le client a deja un compte epargne
                    if (banque.clientAEpargne(numCompteClient)){
                        cnx.envoyer("EPARGNE NO");
                        break;
                    }
                    // Generation d'un numero de compte unique
                    String numeroCompteEpargne;
                    do {
                        numeroCompteEpargne = CompteBancaire.genereNouveauNumero();
                    } while (banque.getCompteClient(numeroCompteEpargne) != null);

                    // Creation du compte Epargne avec un taux d'interet de 5%
                    CompteEpargne compteEpargne = new CompteEpargne(numeroCompteEpargne, 0, 0.05);

                    //Le link au bon client
                    CompteClient compteClient = (CompteClient) banque.getCompteClient(numCompteClient);
                    compteClient.ajouter(compteEpargne);

                    //Confirmation
                    cnx.envoyer("EPARGNE OK compte epargne cree");
                    break;

                case "SELECT":
                    banque = serveurBanque.getBanque();
                    numCompteClient = cnx.getNumeroCompteClient();

                    //Si le client n'est pas connecter
                    if (numCompteClient == null) {
                        cnx.envoyer("SELECT NO non connecte");
                        break;
                    }
                    //Va mettre cheque  ou epargne en minuscule
                    argument = evenement.getArgument().toLowerCase();
                    String compteSelectionner = null;

                    //Verifie si le compte est un compte cheque ou epargne
                    if ("cheque".equals(argument)){
                        compteSelectionner = banque.getNumeroCompteParDefaut(numCompteClient);
                    }
                    if ("epargne".equals(argument)){
                        compteSelectionner = banque.getNumeroCompteEpargne(numCompteClient);
                    }

                    //Verifie si le compte existe
                    if (compteSelectionner != null){
                        cnx.setNumeroCompteActuel(compteSelectionner);
                        cnx.envoyer("SELECT OK");
                    }
                    else{
                        cnx.envoyer("SELECT NO");
                    }
                    break;

                case "DEPOT":   //Depose de l'argent dans le compte actuel
                    banque = serveurBanque.getBanque();
                    String compteActuel = cnx.getNumeroCompteActuel();

                    //Verifie que le compte existe
                    if (compteActuel == null) {
                        cnx.envoyer("DEPOT NO");
                        break;
                    }

                    try {
                        //Conversion String Double afin d'avoir un double comme montant a deposer
                        double montantDepot = Double.parseDouble(evenement.getArgument());

                        //Si l'operation a fonctionner, sa retourne true et sa envoids ... OK
                        if (banque.deposer(montantDepot, compteActuel)) {
                            cnx.envoyer("DEPOT OK");
                        } else {
                            cnx.envoyer("DEPOT NO");
                        }
                        //Si le format n'est pas valide
                    } catch (NumberFormatException e) {
                        cnx.envoyer("DEPOT NO");
                    }
                    break;

                case "RETRAIT":  //Retire de l'argent du compte actuel
                    compteActuel = cnx.getNumeroCompteActuel();
                    banque = serveurBanque.getBanque();

                    //Verifie que le client existe
                    if (compteActuel == null) {
                        cnx.envoyer("RETRAIT NO");
                        break;
                    }

                    try {
                        //Conversion String Double afin d'avoir un double comme montant a retirer
                        double montantRetrait = Double.parseDouble(evenement.getArgument());

                        //Si l'operation a fonctionner, sa retourne true et sa envoids ... OK
                        if (banque.retirer(montantRetrait, compteActuel)) {
                            cnx.envoyer("RETRAIT OK");
                        } else {
                            cnx.envoyer("RETRAIT NO");
                        }
                        //Si le format n'est pas valide
                    } catch (NumberFormatException e) {
                        cnx.envoyer("RETRAIT NO");
                    }
                    break;

                case "FACTURE": //Payer une facture
                    compteActuel = cnx.getNumeroCompteActuel();
                    banque = serveurBanque.getBanque();

                    //Verifie que le client existe
                    if (compteActuel == null) {
                        cnx.envoyer("FACTURE NO");
                        break;
                    }
                    //Separe l'information en 3 partie
                    String[] factureArgs = evenement.getArgument().split(" ", 3);
                    if (factureArgs.length < 3) {
                        //Car il manque d'information
                        cnx.envoyer("FACTURE NO");
                        break;
                    }

                    try {
                        double montantFacture = Double.parseDouble(factureArgs[0]);
                        String numeroFacture = factureArgs[1];
                        String description = factureArgs[2];

                        //Si l'operation a fonctionner, sa retourne true et sa envoids ... OK
                        if (banque.payerFacture(montantFacture, compteActuel, numeroFacture, description)) {
                            cnx.envoyer("FACTURE OK");
                        } else {
                            cnx.envoyer("FACTURE NO");
                        }
                        //Si le format n'est pas valide
                    } catch (NumberFormatException e) {
                        cnx.envoyer("FACTURE NO");
                    }
                    break;

                case "TRANSFER": //Transfer de l'argent a un autre compte
                    banque = serveurBanque.getBanque();
                    compteActuel = cnx.getNumeroCompteActuel();

                    //Verifie que le client existe
                    if (compteActuel == null) {
                        cnx.envoyer("TRANSFER NO");
                        break;
                    }

                    //Separe l'information en 2 partie
                    String[] transferArgs = evenement.getArgument().split(" ", 2);
                    if (transferArgs.length < 2) {
                        cnx.envoyer("TRANSFER NO format invalide");
                        break;
                    }

                    try {
                        //Convertis en double pour obtenir le montant
                        double montantTransfer = Double.parseDouble(transferArgs[0]);
                        String numeroCompteDestinataire = transferArgs[1];

                        //Essaie d'effectuer le transfer
                        if (banque.transferer(montantTransfer, compteActuel, numeroCompteDestinataire)) {
                            cnx.envoyer("TRANSFER OK");
                        } else {
                            cnx.envoyer("TRANSFER NO");
                        }
                    } catch (NumberFormatException e) {
                        //Le format n'etait pas valide
                        cnx.envoyer("TRANSFER NO format invalide");
                    }
                    break;
                case "CONNECT":
                    banque = serveurBanque.getBanque();

                    //On separe l'argument dans une liste, afin d'utliser la partie qu'on veut
                    argument = evenement.getArgument();
                    t = argument.split(":");

                    //Verifie qu'on a assez d'information (numCompteClient et nip)
                    if (t.length < 2) {
                        cnx.envoyer("CONNECT NO"); //Sinon c'est un format invalide
                        break;
                    }


                    numCompteClient = t[0];
                    nip = t[1];

                    //Verifie si le client est deja connecter a une session
                    if (isAccountConnected(numCompteClient)) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }

                    //Le link au bon client
                    CompteClient compteClient = (CompteClient) banque.getCompteClient(numCompteClient);

                    //verifie que le compte client existe et qu'on a le bon nip
                    if (compteClient == null || !compteClient.getNip().equals(nip)) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }

                    //Permet de storer les connections actives
                    activeConnections.add(cnx);

                    //Set le numero de compte du client dans l'objet
                    cnx.setNumeroCompteClient(numCompteClient);

                    //On prend le numero de compte cheque afin de la retourner dans l'objet ConnexionBanque
                    String numeroCompteCheque = banque.getNumeroCompteParDefaut(numCompteClient);

                    //Verifie que le client a un compte cheque
                    if (numeroCompteCheque != null) {
                        cnx.setNumeroCompteActuel(numeroCompteCheque); //On retourne le numero de compte cheque dans l'objet ConnexionBanque
                        cnx.envoyer("CONNECT OK");
                    } else {
                        cnx.envoyer("CONNECT NO");
                    }
                    break;




                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}