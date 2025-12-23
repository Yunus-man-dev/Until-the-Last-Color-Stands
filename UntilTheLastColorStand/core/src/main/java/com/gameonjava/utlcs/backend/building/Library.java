package com.gameonjava.utlcs.backend.building;
import com.gameonjava.utlcs.backend.Player;
import com.gameonjava.utlcs.backend.Tile;

/*Library is a subclass of Building that produces a specified amount of book
each turn. This amount might differ to civilization to civilization. */
public class Library extends Building {
    private double BOOK;

    public Library(Tile tile, double BOOK) {
        super(tile);
        this.BOOK = BOOK;
        name = "Library";
    }
    public Library(){
        super();
    }

    @Override
    public void produce(Player player) {
        double bookProduced = BOOK;
        // if(player.getCivilization() instanceof Blue){
        //    bookProduced = (int) (bookProduced * Blue.getLibraryProductionBonus());
        // }
        if(level == 2){
            bookProduced *= 2;
        }
        if(level == 3){
            bookProduced *= 3;
        }
        player.addScience(bookProduced);
    }

}
