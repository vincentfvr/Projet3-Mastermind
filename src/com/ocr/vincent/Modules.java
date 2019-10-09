package com.ocr.vincent;

import java.util.*;

public class Modules {
    Scanner sc = new Scanner(System.in);

    int i; /** compteur boucles*/
    int combLen = 4; /** nbr de chiffres de la combinaison par défaut à charger depuis les paramètres xml */

    boolean devMode = false; /** false Normal - true Mode Développeur*/
    boolean helpMode = false; /** false Normal - true Mode Aide mémoire*/
    int nbTry = 0; /** Compteur d'essais */
    int nbTryLimit = 4; /** nombre d'essais autorisés avant de trouver la bonne solution*/
    int victory = 0; /** 0 combinaison pas trouvée - 1 combinaison trouvée */

    String winReply= ""; /** Résultat de la combinaison à obtenir (=) x combLen */

    List<Integer> minValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs min*/
    List<Integer> maxValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs max*/
    List<Integer> theValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs justes*/

    /**
     * LANCEMENT DU JEU
     */
    public void runGame() {
        boolean rPlay ;
        int selectedMode;
        do {
        /** Caclul des variables*/
        for (i=0; i<combLen; i++) {
            winReply =  winReply + "=";
            theValues.add(0);
            maxValues.add(9);
            minValues.add(1);
        }
        /** Choix du mode */
        selectedMode = this.displayModesMenu();
        /** Lancement du mode de jeu choisi */

        switch (selectedMode) {
            case 1: /** MODE CHALLENGER */
                this.runChallengerMode(); break;
            case 2: /** MODE DEFENSEUR */
                this.runDefenderMode(); break;
            case 3: /** MODE DUEL */
                this.runDualMode(); break;
            default: break;
        }
        rPlay = this.replayRequest();
        } while (rPlay);
    }

    /**
     * LANCEMENT DU MODE CHALLENGER --> MODE 1
     */
    private void runChallengerMode() {
        String genCombi = this.generateCombination(); // combinaison générée aléatoirement
        String askCombi = "";  // Suggestion saisie par l'utilisateur
        String reply=""; // Résultat après comparaison (+ - =)
        String joueur ="USER";
        do {
            nbTry += 1;
            askCombi = this.askCombination();
            this.displayCombination(genCombi, askCombi, joueur);
            reply = this.compareCombination(genCombi, askCombi, joueur);
            this.displayResult(reply, joueur);
            if (nbTry==nbTryLimit) {
                victory = 1;
                System.out.println("[PARTIE PERDUE] Nombre d'essais dépassé (" + nbTryLimit + ")");
                System.out.println("La combinaison à trouver était : " + genCombi);
            }
        } while (victory == 0);
    }

    /**
     * LANCEMENT DU MODE DEFENSEUR --> MODE 2
     */
    private void runDefenderMode() {
        String askCombi = this.askCombination();  // Suggestion saisie par l'utilisateur
        String genCombi = ""; // combinaison générée aléatoirement
        String reply=""; // Résultat après comparaison (+ - =)
        String joueur ="CPU";
        do {
            nbTry += 1;
            genCombi = this.findCombination();
            this.displayCombination(askCombi, genCombi, joueur);
            reply = this.compareCombination(askCombi, genCombi, joueur);
            this.displayResult(reply, joueur);
        } while (victory == 0);
    }


    /**
     * Recherche par le CPU d'une combinaison en fonction des suggestions précédentes
     * @return cpuComb : Suggestion faite par le CPU
     */
    private String findCombination() {
        String cpuComb="";
        int nb = 0;
        for (i = 0; i < combLen; i++) {
            do {
                if (devMode) {
                    System.out.println("valeur n° " + i + " essai n° " + nbTry);
                }
                if (theValues.get(i) > 0) {
                    nb = theValues.get(i);
                } else {
                    if (maxValues.get(i) - minValues.get(i) == 1) {
                        nb = minValues.get(i) + (int) (Math.random() * maxValues.get(i)) -1;
                        if (nb >= maxValues.get(i)) {
                            nb = maxValues.get(i);
                            if (devMode) System.out.println("nb as max : " + nb);
                        } else if (nb <= minValues.get(i)) {
                            nb = minValues.get(i);
                            if (devMode) System.out.println("nb as min : " + nb);
                        }
                    } else {
                        nb = (minValues.get(i) + maxValues.get(i)) / 2;
                    }
                }
                cpuComb = cpuComb + nb;
            } while (nb <= 0);
            if (devMode) {
                System.out.println("DEV_MODE ------> : " + cpuComb);
                System.out.println("nb " + nb + " max " + maxValues.get(i) + " min " + minValues.get(i));
            }
        }
        return cpuComb;
    }

    /**
     * LANCEMENT DU MODE DUEL --> MODE 3
     */
    private void runDualMode() {
        System.out.println("Création de ma combinaison :");
        System.out.println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");
        String userCombi = this.askCombination();  // Suggestion saisie par l'utilisateur
        String cpuCombi = this.generateCombination(); // combinaison générée aléatoirement
        String userReply=""; // Résultat après comparaison (+ - =)
        String cpuReply=""; // Résultat après comparaison (+ - =)
        String joueur=""; // Nom du joueur
        System.out.println("Joueur CPU a créé sa combinaison :");
        System.out.println("----------------------------------");
        // System.out.println(userCombi); // A SUPPRIMER
        // System.out.println(cpuCombi); // A SUPPRIMER
        do {
            nbTry += 1;
            System.out.println("Saisir votre suggestion :");
            String userSug = this.askCombination();
            String cpuSug = this.findCombination();

            if (victory==0) {
                /** TOUR ORDINATEUR*/
                System.out.println("----------------------------------");
                joueur = "CPU";
                this.displayCombination(userCombi, cpuSug, joueur);
                cpuReply = this.compareCombination(userCombi, cpuSug, joueur);
                this.displayResult(cpuReply, joueur);
            }

            if (victory==0){
                /** TOUR UTILISATEUR*/
                System.out.println("----------------------------------");
                joueur ="USER";
                this.displayCombination(cpuCombi, userSug, joueur);
                userReply = this.compareCombination(cpuCombi, userSug, joueur);
                this.displayResult(userReply, joueur);
                System.out.println("----------------------------------");
            }

            /** if (nbTry==nbTryLimit) {
                victory = 1;
                System.out.println("[PARTIE PERDUE] Nombre d'essais dépassé (" + nbTryLimit + ")");
            } */
        } while (victory == 0);
    }

    /**
     * Affichage du choix du mode et control de saisie
     */
    public Integer displayModesMenu() {
        int mode; /** Choix du mode de jeu  1 ou 2 ou 3 */
        do {
            System.out.println("CHOISIR UN MODE DE JEU : 1 - CHALLENGER, 2 - DÉFENSEUR, 3 - DUEL");
            mode = sc.nextInt();
            switch (mode) {
                case 1:
                    System.out.println("[MODE CHALLENGER]");
                    break;
                case 2:
                    System.out.println("[MODE DÉFENSEUR]");
                    break;
                case 3:
                    System.out.println("[MODE DUEL]");
                    break;
                case 123: /** Permet d'activer le mode Développeur */
                    System.out.println("[MODE DÉVELOPPEUR ACTIF]");
                    devMode=true;
                    break;
                case 456: /** Permet d'activer le mode Aide mémoire */
                    System.out.println("[MODE AIDE MEMOIRE ACTIF]");
                    helpMode=true;
                    break;
                default:
                    System.out.println("Taper 1,2 ou 3");
                    break;
            }
        } while (mode < 1 || mode > 4);
        return mode;
    }

    /**
     *  Génere de manière aléatoire une combinaison à X chiffres
     */
    public String generateCombination() {
        int number = 0;
        String randomCode ="";
            for (i = 0; i < combLen; i++) {
                do {
                    number = (int) (Math.random() * 10);     /** avec 0 < nb < 9 */
                    if (number >= 1) {
                        randomCode = randomCode + number;
                    }
                } while (number == 0);
            }
            return randomCode;
    }

    /**
     * Demande de saisir une suggestion de combinaison générée (avec numérotation counter/combLen)
     * + Procédure de controle de saisie : nombre de caractère +
     * charControl : True si le controle doit être effectué, false si le controle n'est plus à faire
     * @return userComb = Combinaison saisie par l'utilisateur
     */
    public String askCombination() {
        String userComb ="";
        boolean charControl = true;
        do {
            userComb = sc.nextLine(); // scanner
            if (userComb.length() != combLen) { // CONTROLE DU NOMBRE DE CHARACTÈRES SAISI
                System.out.println("Saisir une combinaison de " + combLen + " chiffre(s) (entre 1 et 9) et valider avec Entrée");
                charControl = true;
            } else if (userComb.length() == combLen) {
                charControl = this.inputControl(userComb); // CONTROLE QUE TOUS LES CHARACTERES SOIENT DES CHIFFRES
            }
        } while (charControl);
        return userComb;
    }

    /**
     * Permet de tester si chaque charactère est bien un chiffre
     * @param testComb -> Chaine de caratères à tester
     * @return b1 True si le controle est à refaire. False si le controle est terminé
     */
    private boolean inputControl(String testComb) {
        boolean b1 = false; // état du controle d'entrée. False par défaut. Renvoie True si une erreur est trouvée
        boolean b2; // variable True/false du test isDigit()
        char temp; // variable temporaire pour la boucle. Stocke caractère par caractère de la chaine testComb
        //for (i=0 ; i < combLen; i++) {
        for (i=0 ; i < testComb.length(); i++) {
            temp = testComb.charAt(i);
            b2 = Character.isDigit(temp);
            if (!b2){
                b1 = true;
            }
        }
        if (b1) System.out.println("Saisir uniquement des chiffres");
        return b1;
    }

    /**
     * Affiche la proposition faite avec + le nombre d'essais
     */
    public void displayCombination(String aaa, String bbb, String joueur) {
        if (devMode) {
            System.out.println("DEV-MODE   : " + aaa);
            }
        System.out.println("Essai n°" + nbTry + " [" + joueur + "]");
        System.out.println("Suggestion --> " + bbb);
    }

    /**
     * COMPARATEUR DES DEUX COMBINAISONS
     * @param comb1 : Combinaison à trouver
     * @param comb2 : Combinaison suggerée
     * @return theReply : Contient le résultat de la comparaison comb1/comb2 (+, -, =)
     */
    public String compareCombination(String comb1, String comb2, String player) {
        String theReply = "";
        int secretComb=0 ;
        int suggestComb=0 ;
        /** Comparaison des valeurs*/
        for ( i = 0 ; i<combLen; i++) {
            secretComb = Character.getNumericValue(comb1.charAt(i));
            suggestComb = Character.getNumericValue(comb2.charAt(i));
            if (secretComb == suggestComb) {
                if (player == "CPU") {
                    theValues.set(i, secretComb);
                    maxValues.set(i, 0);
                    minValues.set(i, 0);
                }
                theReply = theReply + "=";
            } else if (secretComb < suggestComb) {
                maxValues.set(i, suggestComb);
                theReply = theReply + "-";
            } else if (secretComb > suggestComb) {
                minValues.set(i, suggestComb);
                theReply = theReply + "+";
            }
        }
        return theReply;
    }

    /**
     * Affichage du résultat
     * @param theReply : Contient le résultat de la comparaison comb1/comb2 (+, -, =)
     */
    public void displayResult(String theReply, String player) {
        /** Affichage des indications */
        System.out.println("Indication --> " + theReply);
        if (helpMode) {
            System.out.println("theValues : " + theValues);
            System.out.println("maxValues : " + maxValues);
            System.out.println("minValues : " + minValues);
        }
        if (nbTry<=nbTryLimit) {
            if (theReply.contains(winReply)) {
                victory = 1;
                System.out.println("**********************************************************");
                System.out.println("   Joueur " + player + " A GAGNÉ! Combinaison trouvée en " + nbTry + " coups.");
                System.out.println("**********************************************************");
            }
        } else {
            victory = 1;
            System.out.println("[PARTIE PERDUE] Nombre d'essais dépassé (" + nbTryLimit + ")");
        }
    }

    /**
     *
     * Méthode permettant de relancer une partie ou quitter
     * @return b1 True -> Rejouer, False = Quitter le jeu
     */

    public boolean replayRequest () {
        boolean b1 = true; // rue -> Rejouer, False = Quitter le jeu
        boolean b2 = false; // Controle de saisie True -> erreur, false -> pas d'erreur
        int myChoise; // Choix 1 ou 2 pour rejouer ou quitter
        String myChoiseStr = ""; // Choix au format string pour le control de saisie
        do {
            System.out.println("Rejouer une partie ? : Taper : 1 -> oui ou 2 -> non");
            myChoiseStr = sc.nextLine();
            b2 = this.inputControl(myChoiseStr); // Controle de saisie : True ; erreur trouvée (char et non int)
            if (b2) {

            } else {
                myChoise = Integer.parseInt(myChoiseStr);
                if (myChoise < 1 || myChoise >2){
                    b2 = true;
                }
                switch (myChoise) {
                    case 1:
                        b1 = true;
                        System.out.println("[NOUVELLE PARTIE]");
                        /** Réinitialisation des variables initiales */
                        winReply = "";
                        nbTry = 0;
                        victory = 0;
                        theValues.clear();
                        maxValues.clear();
                        minValues.clear();
                    break;
                    case 2:
                        b1 = false;
                        System.out.println("[FIN DE PARTIE]");
                    break;
                }
            }
        } while (b2);
        return b1;
    }
}