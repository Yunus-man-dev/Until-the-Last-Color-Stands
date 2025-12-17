package com.gameonjava.utlcs;

import java.util.Random;

import com.gameonjava.utlcs.Enum.TerrainType;
import com.gameonjava.utlcs.civilization.Brown;
import com.gameonjava.utlcs.civilization.Red;

public class WarManager {
    // This variable indicates attacker army.
   private  Army attackerArmy ;
   // This variable indicates defender army.
    private Army defenderArmy ;

    // This variable indicates the tile that happens battle on.
    private Tile battleTile ;

    // This variable ind[cates attacker’s dice number.
    private int attackerDice ;

    //// This variable indicates defender’s dice number.
    private int defenderDice ;

    // This variable indicates attacker’s attack point. (AP = dice *
    // TP * numberOfSoldiers * attackMultiplier. Also, TP = technologyPoint *
    // uniqueMultiplier, which is a special multiplier for that civilization)
   private int attackerAP ;

    // This variable indicates defender’s attack point. (AP = dice *
    //TP * numberOfSoldiers * defenceMultiplier. Also, TP = technologyPoint *
    //uniqueMultiplier, which is a special multiplier for that civilization)
    private  int defenderAP ;

    private Army winner;

    public static boolean isValidWar(Army attackerArmy ,Army defenderArmy, Tile battleTile){

        // check for attacker's MP
        if(attackerArmy.player.getMp().getValue() < attackerArmy.player.getMp().ATTACK){

            // I assume ATTACK is same for any army. If we are planning to compose it according the army's size
            // we may need a update
            return false;

        }

        // check for attecker's Army size
        else if(attackerArmy.getSoldiers() == 0){
            return false;
        }


        // if battle tile is
        //check whether the tile's are neighbors of each other
        else if(!attackerArmy.tile.isNeighboor(battleTile))
            return false;

        else{
            return true;
        }



    }




    public WarManager(Army attackerArmy ,Army defenderArmy, Tile battleTile ){
        this.attackerArmy = attackerArmy;
        this.defenderArmy = defenderArmy;
        this.battleTile = battleTile;
        startBattle();

    }

    private void startBattle(){
        defenderDice = rollDice();
        attackerDice = rollDice();
        winner = decideWinner();
        // kayiplari uygula;
        applyCasulties();
        if(attackerArmy == winner){

            // tile degisimi sagla
            changeTile();
        }

    }
    private int rollDice(){
        Random rand = new Random();
        return rand.nextInt(1, 7);
    }

    // Calculates and decides the winner. Returns the winner army.
    private Army decideWinner(){

        double attackMultiplier = 0;
        double defenseMultiplier = 0;
        if(attackerArmy.getPlayer().getCivilization() instanceof Brown && defenderArmy.getPlayer().getCivilization() instanceof Red){
            attackMultiplier = attackerArmy.getPlayer().getCivilization().getAttackMultiplier() * Brown.getRedAttackBonus();
        }else if(attackerArmy.getPlayer().getCivilization() instanceof Red && defenderArmy.getPlayer().getCivilization() instanceof Brown) {
            defenseMultiplier = defenderArmy.getPlayer().getCivilization().getDefenseMultiplier() * Brown.getRedDefenseBonus();
        }else{
            attackMultiplier = attackerArmy.getPlayer().getCivilization().getAttackMultiplier();
            defenseMultiplier = defenderArmy.getPlayer().getCivilization().getDefenseMultiplier();
        }
        attackerAP =(int) calculateAP(attackerDice,attackMultiplier, attackerArmy);
        defenderAP = (int) calculateAP(defenderDice,defenseMultiplier, defenderArmy);

        if(attackerAP > defenderAP){
            return attackerArmy;

        }
        else if (attackerAP < defenderAP){
            return defenderArmy;
        }
        else{
            if(attackerArmy.getSoldiers()> defenderArmy.getSoldiers() ){
                return attackerArmy;
            }
            else if( attackerArmy.getSoldiers() < defenderArmy.getSoldiers() ){
                return defenderArmy;
            }
            else{

                int result = rollDiceTillNight(1, 2);
                if(result == 1){
                    return attackerArmy;
                }
                else{
                    return defenderArmy;
                }

            }
        }

    }

    private int rollDiceTillNight(int attack, int defend){
        defenderDice = 0;
        attackerDice = 1;
        while(defenderDice != attackerDice){

            defenderDice = rollDice();
            attackerDice = rollDice();
        }
        if(attackerDice > defenderDice)
            return attack;
        else{
            return defend;
        }

    }


    private double calculateAP(int dice, double multiplier, Army army){

    // This variable indicates attacker’s attack point. (AP = dice *
    // TP * numberOfSoldiers * attackMultiplier. Also, TP = technologyPoint *
    // uniqueMultiplier, which is a special multiplier for that civilization)

        int TP = army.player.getTechnologyPoint();
        int soldiets = army.getSoldiers();
        return dice * TP * soldiets * army.player.getCivilization().getAttackMultiplier();

    }
    public void applyCasulties(){


        if(winner == attackerArmy){

            defenderArmy.removeSoldiers(defenderArmy.getSoldiers());

            int difference = (attackerAP - defenderAP);
            int winerCauslties = findCasulties(attackerArmy, difference);
            attackerArmy.removeSoldiers(winerCauslties);

        }
        else{

            attackerArmy.removeSoldiers(attackerArmy.getSoldiers());

            int difference = ( defenderAP-attackerAP );
            int winerCauslties = findCasulties(defenderArmy, difference);
            defenderArmy.removeSoldiers(winerCauslties);


        }


    }

    private int findCasulties(Army army ,int difference){


        //fark 10 dan fazla ise yuzde 80
        // fark 20 den fazla ise yuzde 60
        // fark 40 dan fazla ise yuzde 40
        // fark 100 den fazla ise yuzde 20


            int casultie = 0 ;
            if(difference < 10 ){
                casultie = (army.getSoldiers() *8)/10;
            }
            else if(difference < 20 ){
                casultie = (army.getSoldiers() *6)/10;
            }
            else if(difference < 30 ){
                casultie = (army.getSoldiers() *4)/10;
            }
            else if(difference < 100){
                casultie = (army.getSoldiers() *2)/10;
            }
            else{
                 casultie = (army.getSoldiers() *1)/10;
            }
            return casultie;

    }




    private void changeTile(){

        battleTile.removeArmy();
        battleTile.setArmy(winner);

        if(!battleTile.getTerrainType().equals(TerrainType.WATER)){

            defenderArmy.player.getOwnedTiles().remove(battleTile);
            attackerArmy.player.getOwnedTiles().add(battleTile);

        }


    }

}
