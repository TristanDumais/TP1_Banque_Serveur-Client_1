package com.atoudeft.banque;
import java.io.Serializable;

public class CompteEpargne extends CompteBancaire implements Serializable {
    private final String numero;
    private double solde;
    private double tauxInteret;

    private static final double LIMITE_SOLDE = 1000.0;
    private static final double FRAIS_RETRAIT = 2.0;

    public CompteEpargne(String numero, double solde, double tauxInteret) {
        super(numero, TypeCompte.EPARGNE);
        this.numero = numero;
        this.solde = solde;
        this.tauxInteret = tauxInteret;
    }

    /**
     * Crédite un montant sur le compte bancaire.
     *
     * @param montant Le montant à créditer sur le compte. Doit être strictement supérieur à 0.
     * @return true si l'opération a été réalisée avec succès, false sinon.
     */
    @Override
    public boolean crediter(double montant){
        if (montant > 0){
            this.solde  += montant;
            return true;
        }
        return false;
    }

    /**
     * Débite un montant du compte épargne, en appliquant des frais si le solde est inférieur à une limite.
     *
     * @param montant Le montant à débiter. Doit être strictement positif et ne pas dépasser le solde disponible
     * @return true si le montant a été débité avec succès, false sinon.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde - montant > 0){
            if (this.solde < LIMITE_SOLDE){
                montant += FRAIS_RETRAIT;
            }
            this.solde  -= montant;
            return true;
        }
        return false;
    }

    /**
     * Effectue le paiement d'une facture à partir du compte épargne.
     * Pour le moment, cette méthode retourne false et ne réalise aucune action.
     *
     * @param numeroFacture Le numéro unique de la facture à payer.
     * @param montant Le montant de la facture à payer. Doit être strictement positif.
     * @param description Une description de la facture, par exemple le fournisseur
     * @return false, car la méthode n'est pas encore implémentée.
     */
    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    /**
     * Effectue un transfert d'argent vers un autre compte bancaire.
     * Pour le moment, cette méthode retourne false et ne réalise aucune action.
     *
     * @param montant Le montant à transférer. Doit être strictement positif.
     * @param numeroCompteDestinataire Le numéro du compte destinataire du transfert.
     * @return false, car la méthode n'est pas encore implémentée.
     */
    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }

    /**
     * Ajoute les intérêts au solde du compte épargne.
     *
     * @return true si les intérêts ont été ajoutés avec succès, false sinon.
     */
    public boolean ajouterInterets() {
        //Verifie si l'ont peut rajouter des interets
        if (this.solde > 0) {
            this.solde += this.solde * this.tauxInteret; //Ajout de l'interet
            return true;
        }
        return false; //Retourne false si le solde est insuffisant (fonctionne pas)
    }
}