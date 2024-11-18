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
    public Object getCompteClient(String numeroCompteClient) {
        CompteClient cpt = new CompteClient(numeroCompteClient,"");
        int index = this.comptes.indexOf(cpt);
        if (index != -1)
            return this.comptes.get(index);
        else
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

        CompteClient compteClient = new CompteClient(numCompteClient, nip);
        String numeroCompteBancaire = CompteBancaire.genereNouveauNumero();
        CompteCheque compteCheque = new CompteCheque(numeroCompteBancaire, 0);

        //Ajoute le compte cheque au compte du client
        compteClient.ajouter(compteCheque);
        //Ne pas oublier de l'ajouter a la liste
        comptes.add(compteClient);

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