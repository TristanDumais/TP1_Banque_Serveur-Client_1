package com.atoudeft.banque;

import java.io.Serializable;

public class CompteCheque extends CompteBancaire implements Serializable {
    private final String numero;
    private double solde;

    public CompteCheque(String numero, double solde) {
        super(numero, TypeCompte.CHEQUE);
        this.numero = numero;
        this.solde = solde;
    }

    /**
     * Crédite un montant sur le compte bancaire.
     *
     * @param montant Le montant à créditer sur le compte. Doit être supérieur à 0.
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
     * Débite un montant du compte bancaire.
     *
     * @param montant Le montant à débiter du compte. Doit être supérieur à 0 et ne doit pas dépasser le solde disponible.
     * @return true si l'opération a été réalisée avec succès, false sinon.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde - montant > 0){
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
}
