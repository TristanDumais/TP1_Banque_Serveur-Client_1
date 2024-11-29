package com.atoudeft.banque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompteClient implements Serializable {
    private String numero;
    private String nip;
    private List<CompteBancaire> comptes;

    /**
     * Crée un compte-client avec un numéro et un nip.
     *
     * @param numero le numéro du compte-client
     * @param nip le nip
     */
    public CompteClient(String numero, String nip) {
        this.numero = numero;
        this.nip = nip;
        comptes = new ArrayList<>();
    }

    /**
     * Getter pour le numéro de compte.
     *
     * @return Le numéro du compte-client
     */
    public String getNumeroCompteClient() {
        return this.numero;
    }
    /**
     * Ajoute un compte bancaire au compte-client.
     *
     * @param compte le compte bancaire
     * @return true si l'ajout est réussi
     */
    public boolean ajouter(CompteBancaire compte) {
        return this.comptes.add(compte);
    }


    /**
     * Retourne la liste des comptes bancaire du clients
     *
     * @return la liste des comptes bancaires
     */
    public List<CompteBancaire> getComptes() {
        return comptes;
    }

    /**
     * Retourne le nip du compte
     *
     * @return nip
     */
    public String getNip() {
        return nip;
    }

    // Redéfinition de equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Même référence
        if (obj == null || getClass() != obj.getClass()) return false; // Classe différente ou null
        CompteClient that = (CompteClient) obj;
        return this.numero.equals(that.numero); // Compare uniquement le numéro de compte
    }

    // Redéfinition de hashCode
    @Override
    public int hashCode() {
        return this.numero.hashCode(); // Utilise le hash du numéro
    }
}


