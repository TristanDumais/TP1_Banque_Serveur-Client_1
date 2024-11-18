package com.atoudeft.serveur;

import java.io.Serializable;

public class PileChainee implements Serializable {
    private Noeud sommet;

    public PileChainee() {
        this.sommet = null; // la pile est vide initialement
    }

    // Ajoute un élément dans la pile
    public void empiler(Operation operation) {
        Noeud nouveauNoeud = new Noeud(operation);
        if (estVide()) {
            sommet = nouveauNoeud;
        }else {
            nouveauNoeud.setNext(sommet);
            sommet = nouveauNoeud;
        }
    }

    // Enlève un élément de la pile et le retourne
    public Operation depiler() {
        if (estVide()) {
            throw new RuntimeException("La pile est vide");  // Empèche de dépiler une pile vide
        }
        Operation operation = sommet.getOperation();
        sommet = sommet.getNext(); // Le prochain élément devient le sommet de la pile, éléminant le vieux sommet
        return operation;
    }

    // retourne true, si il n'y a rien au sommet de la pile (elle est donc vide)
    public boolean estVide() {
        return sommet == null;

    }

    public void afficherHistorique() {
        Noeud current = sommet;
        while (current != null) {
            System.out.println(current.getOperation().toString());
            current = current.getNext();
        }
    }
}
