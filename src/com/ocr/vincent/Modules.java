package com.ocr.vincent;

import java.util.*;

public class Modules {
    Scanner sc = new Scanner(System.in);

    int i; /** compteur boucles*/
    int combLen = 4; /** nbr de chiffres de la combinaison par défaut à charger depuis les paramètres xml */

    int selectedMode; /** Choix du mode de jeu  1 ou 2 ou 3 */
    boolean devMode = false; /** false Normal - true Mode Développeur*/
    boolean helpMode = false; /** false Normal - true Mode Aide mémoire*/
    int nbTry = 0; /** Compteur d'essais */
    int nbTryLimit = 10; /** nombre d'essai autorisé avant de trouver la bonne solution*/
    int victory = 0; /** 0 combinaison pas trouvée - 1 combinaison trouvée */

    String winReply= ""; /** Résultalt de la combinaison à obtenir (=) x combLen */

    List<Integer> usrComb = new ArrayList<Integer> (combLen); /** USER Combinaison Liste Array integer*/
    List<Integer> cpuComb = new ArrayList<Integer>(combLen); /** CPU Combinaison Liste Array integer*/

    List<Integer> minValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs min*/
    List<Integer> maxValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs max*/
    List<Integer> theValues = new ArrayList<Integer>(combLen); /** Mémorise les valeurs justes*/

    /**
     * LANCEMENT DU JEU
     */
    public void runGame() {
        /** Caclul des variables*/
        for (i=0; i<combLen; i++) {
            winReply =  winReply + "=";
            theValues.add(0);
            maxValues.add(9);
            minValues.add(1);
        }
        /** Choix du mode */
        this.displayModesMenu();
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
    }

    /**
     * LANCEMENT DU MODE CHALLENGER --> MODE 1
     */
    private void runChallengerMode() {
        String genCombi = this.generateCombination(); // combinaison générée aléatoirement
        String askCombi = "";  // Suggestion saisie par l'utilisateur
        String reply=""; // Résultat après comparaison (+ - =)
        do {
            nbTry += 1;
            askCombi = this.askCombination();
            this.displayCombination(genCombi, askCombi);
            reply = this.compareCombination(genCombi, askCombi);
            this.displayResult(reply);
        } while (victory == 0);
    }

    /**
     * LANCEMENT DU MODE DEFENSEUR --> MODE 2
     */
    private void runDefenderMode() {
        this.askCombination();

        do {
            nbTry += 1;
            //this.displayCombination();
            //this.findCombination();
            //this.compareCombination();
            //this.displayResult();
            if (nbTry==nbTryLimit) {
                victory = 1;
            }
        } while (victory == 0);
    }

    private void findCombination() {
        int nb = 0;
        cpuComb.clear();
        for (i = 0; i < combLen; i++) {
            do {
                if (devMode) {
                    System.out.println("valeur n° " + i + " essai n° " + nbTry);
                }
                if (theValues.get(i) > 0) {
                    nb = theValues.get(i);
                } else {
                    if (maxValues.get(i) - minValues.get(i) == 1) {
                        nb = minValues.get(i) + (int) (Math.random() * maxValues.get(i)) - 1;
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
                cpuComb.add(nb);
            } while (nb <= 0);
            if (devMode) {
                System.out.println("DEV_MODE ------> : " + cpuComb);
                System.out.println("nb " + nb + " max " + maxValues.get(i) + " min " + minValues.get(i));
            }
        }
    }

    /**
     * LANCEMENT DU MODE DUEL --> MODE 3
     */
    private void runDualMode() {

    }

    /**
     * Affichage du choix du mode et control de saisie
     */
    public void displayModesMenu() {
        do {
            System.out.println("CHOISIR UN MODE DE JEU : 1 - CHALLENGER, 2 - DÉFENSEUR, 3 - DUEL");
            selectedMode = sc.nextInt();
            switch (selectedMode) {
                case 1:
                    System.out.println("MODE CHALLENGER");
                    break;
                case 2:
                    System.out.println("MODE DÉFENSEUR");
                    break;
                case 3:
                    System.out.println("MODE DUEL");
                    break;
                case 123: /** Permet d'activer les mode Développeur */
                    System.out.println("MODE DÉVELOPPEUR ACTIF");
                    devMode=true;
                    break;
                case 456: /** Permet d'activer les mode Aide mémoire */
                    System.out.println("MODE AIDE MEMOIRE ACTIF");
                    helpMode=true;
                    break;
                default:
                    System.out.println("Taper 1,2 ou 3");
                    break;
            }
        } while (selectedMode < 1 || selectedMode > 4);
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
                        cpuComb.add(number); /** ------------- A SUPPRIMER APRES ADAPTATION DU MODE DEFENDER------------ */
                        randomCode = randomCode + number;
                    }
                } while (number == 0);
            }
            return randomCode;
    }

    /**
     * Demande de saisir une suggestion de combinaison générée (avec numérotation counter/combLen)
     * + Procédure de controle de saisie : nombre de caractère +
     * userComb = Combinaison saisie par l'utilisateur
     * charControl : True si le controle doit être effectué, false si le controle n'est plus à faire
     */
    public String askCombination() {
        usrComb.clear(); /** ------------- A SUPPRIMER ------------ */
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
        for (i=0 ; i < combLen; i++) {
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
    public void displayCombination(String aaa, String bbb) {
        if (devMode) {
            System.out.println("DEV-MODE   : " + aaa);
            }
        System.out.println("Essai n°" + nbTry);
        System.out.println("Suggestion --> " + bbb);
    }

    /**
     * COMPARATEUR DES DEUX COMBINAISONS
     */
    public String compareCombination(String aaaa, String bbbb) {
        String theReply = ""; /** Résultat de la suggestion contenant + - ou = */
        int secretComb=0 ;
        int suggestComb=0 ;
        /** Comparaison des valeurs*/
        for ( i = 0 ; i<combLen; i++) {
            secretComb = Character.getNumericValue(aaaa.charAt(i));
            suggestComb = Character.getNumericValue(bbbb.charAt(i));

            if (secretComb == suggestComb) {
                theValues.set(i, secretComb);
                maxValues.set(i, 0);
                minValues.set(i, 0);
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
     */
    public void displayResult(String theReply) {
        /** Affichage des indications */
        System.out.println("Indication --> " + theReply);
        if (helpMode) {
            System.out.println("theValues : " + theValues);
            System.out.println("maxValues : " + maxValues);
            System.out.println("minValues : " + minValues);
        }

        if (theReply.contains(winReply)) {
            victory=1;
            System.out.println("******************************************");
            System.out.println("* GAGNÉ! Combinaison trouvée en " + nbTry + " coups. *");
            System.out.println("******************************************");
        }
        //theReply ="";
    }
}