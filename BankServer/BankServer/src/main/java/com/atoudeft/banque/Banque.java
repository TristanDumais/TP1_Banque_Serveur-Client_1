package com.atoudeft.banque;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Banque implements Serializable {
    private String nom;
    private List<CompteClient> comptes;

    public Banque(String nom) {
        this.nom = nom;
        this.comptes = new ArrayList<>();
    }

    /**
     * Recherche un compte-client à partir de son numéro.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @return le compte-client s'il a été trouvé. Sinon, retourne null
     */
    public CompteClient getCompteClient(String numeroCompteClient) {
        for (CompteClient compte : comptes) {
            if (compte.getNumeroCompteClient().equals(numeroCompteClient)) {
                return compte;
            }
        }
        return null;
    }


    /**
     * Recherche un compte bancaire spécifique pour un client.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @param typeCompte type du compte bancaire (par exemple, "Cheque", "Epargne")
     * @return le compte bancaire correspondant ou null si non trouvé
     */
    public CompteBancaire getCompteBancaire(String numeroCompteClient, String typeCompte) {
        CompteClient compteClient = getCompteClient(numeroCompteClient);
        if (compteClient != null) {
            // Parcours les comptes du client pour trouver celui correspondant au type
            for (CompteBancaire compte : compteClient.getComptes()) {
                if (typeCompte.equals("Cheque") && compte instanceof CompteCheque) {
                    return compte;
                } else if (typeCompte.equals("Epargne") && compte instanceof CompteEpargne) {
                    return compte;
                }
            }
        }
        return null;
    }



    /**
     * Effectue un dépot d'argent dans un compte-bancaire
     *
     * @param montant montant à déposer
     * @param numeroCompte numéro du compte
     * @return true si le dépot s'est effectué correctement
     */
    public boolean deposer(double montant, String numeroCompte){
        //Parcourt les comptes clients pour trouver le compte bancaire
        for (CompteClient compteClient : comptes) {
            for (CompteBancaire compte : compteClient.getComptes()) {
                //S'il trouve le numero correspondant
                if (compte.getNumero().equals(numeroCompte)) {
                    //On effectue la transaction
                    return compte.crediter(montant);
                }
            }
        }
        return false; //Compte introuvable ou montant invalide
    }

    /**
     * Effectue un retrait d'argent d'un compte-bancaire
     *
     * @param montant montant retiré
     * @param numeroCompte numéro du compte
     * @return true si le retrait s'est effectué correctement
     */
    public boolean retirer(double montant, String numeroCompte) {
        //Parcourt les comptes clients pour trouver le compte bancaire
        for (CompteClient compteClient : comptes) {
            for (CompteBancaire compte : compteClient.getComptes()) {
                //S'il trouve le numero correspondant
                if (compte.getNumero().equals(numeroCompte)) {
                    //On effectue la transaction
                    return compte.debiter(montant);
                }
            }
        }
        return false; //Compte introuvable ou montant invalide
    }

    /**
     * Effectue un transfert d'argent entre deux comptes de la banque.
     *
     * @param montant Le montant à transférer. Doit être strictement positif.
     * @param numeroCompteInitial Le numéro du compte source d'où l'argent sera retiré.
     * @param numeroCompteFinal Le numéro du compte destinataire où l'argent sera déposé.
     * @return true si le transfert a été effectué avec succès, false sinon.
     */

    public boolean transferer(double montant, String numeroCompteInitial, String numeroCompteFinal) {
        //Le montant doit etre positif
        if (montant <= 0) {
            return false;
        }
        //Recherche des comptes source et destinataire
        CompteBancaire compteSource = null;
        CompteBancaire compteDestinataire = null;

        for (CompteClient compteClient : comptes) {
            for (CompteBancaire compte : compteClient.getComptes()) {

                //On obtient le compte source
                if (compte.getNumero().equals(numeroCompteInitial)) {
                    compteSource = compte;
                }
                //On obtient le compte destinataire
                if (compte.getNumero().equals(numeroCompteFinal)) {
                    compteDestinataire = compte;
                }
            }
        }

        // Vérifie que les deux comptes existent
        if (compteSource == null || compteDestinataire == null) {
            return false;
        }
        // Débite le compte source et crédite le compte destinataire
        if (compteSource.debiter(montant)) {
            return compteDestinataire.crediter(montant);
        }

        return false;
    }


    /**
     * Effectue un paiement de facture.
     * @param montant montant de la facture
     * @param numeroCompte numéro du compte bancaire d'où va se faire le paiement
     * @param numeroFacture numéro de la facture
     * @param description texte descriptif de la facture
     * @return true si le paiement s'est bien effectuée
     */
    public boolean payerFacture(double montant, String numeroCompte, String numeroFacture, String description) {
        //Parcourt les comptes clients pour trouver le compte bancaire
        for (CompteClient compteClient : comptes) {
            for (CompteBancaire compte : compteClient.getComptes()) {
                //S'il trouve le numero correspondant
                if (compte.getNumero().equals(numeroCompte)) {
                    //On paye la facture
                    return compte.payerFacture(numeroFacture, montant, description);
                }
            }
        }
        //Compte introuvable ou montant invalide
        return false;
    }

    /**
     * Crée un nouveau compte-client avec un numéro et un nip et l'ajoute à la liste des comptes.
     *
     * @param numCompteClient numéro du compte-client à créer
     * @param nip nip du compte-client à créer
     * @return true si le compte a été créé correctement
     */
    public boolean ajouter(String numCompteClient, String nip) {
        //6 a 8 characteres en majuscule (A-Z, 0-9)
        if (!numCompteClient.matches("^[A-Z0-9]{6,8}$")){
            return false;
        }
        //Verifie que le nip est composer de chiffres uniquement et de 4 ou 5 characteres
        if (!nip.matches("^[0-9]{4,5}$")){
            return false;
        }
        //Si le compte n'existe pas
        if (getCompteClient(numCompteClient) != null){
            return false;
        }

        // Vérifie si le compte existe déjà
        for (CompteClient compte : comptes) {
            if (compte.getNumeroCompteClient().equals(numCompteClient)) {
                return false; // Le compte existe déjà
            }
        }

        // Ajoute un nouveau compte à la liste
        comptes.add(new CompteClient(numCompteClient, nip));
        return true;
    }

    /**
     * Retourne le numéro du compte-chèque d'un client à partir de son numéro de compte-client.
     *
     * @param numCompteClient numéro de compte-client
     * @return numéro du compte-chèque du client ayant le numéro de compte-client
     */
    public String getNumeroCompteParDefaut(String numCompteClient) {
        CompteClient compteClient = (CompteClient) getCompteClient(numCompteClient);
        //Verifier qu'il existe
        if (compteClient != null) {

            // Parcourt chaque compte bancaire du compte-client
            for (CompteBancaire compte : compteClient.getComptes()) {

                // Vérifie si le compte est un CompteCheque
                if (compte instanceof CompteCheque) {
                    return compte.getNumero();
                }
            }
        }

        return null;
    }

    /**
     * Permet d'obtenir le numero du compte epargne du client
     * @param numeroCompteClient
     * @return numero du compte epargne
     */
    public String getNumeroCompteEpargne(String numeroCompteClient) {
        CompteClient compteClient = (CompteClient) getCompteClient(numeroCompteClient);
        if (compteClient != null) {

            //Trouver le compte epargne
            for (CompteBancaire compte : compteClient.getComptes()) {
                if (compte instanceof CompteEpargne) {
                    return compte.getNumero();
                }
            }
        }
        //null si le compte n'existe pas
        return null;
    }

    /**
     * Vérifie si un client possède un compte épargne.
     *
     * @param numeroCompteClient Le numéro du compte-client à vérifier.
     * @return true si le client possède un compte épargne, false sinon.
     *
     */
    public boolean clientAEpargne(String numeroCompteClient){
        CompteClient compteClient = (CompteClient) getCompteClient(numeroCompteClient);
        //Verifier qu'il existe
        if (compteClient != null) {

            // Parcourt chaque compte bancaire du compte-client
            for (CompteBancaire compte : compteClient.getComptes()) {

                // Vérifie si le compte est un CompteEpargne
                if (compte instanceof CompteEpargne) {
                    return true;
                }
            }
        }
        return false;
    }
}
