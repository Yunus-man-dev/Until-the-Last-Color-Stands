package io.github.some_example_name.building;
/*Library is a subclass of Building that produces a specified amount of book
each turn. This amount might differ to civilization to civilization. */
public class Library extends Building {
    private final int BOOK;

    public Library(Tile tile, int BOOK) {
        super(tile);
        this.BOOK = BOOK;
    }

    @Override
    public void produce(Player player) {
        int bookProduced = BOOK;
        player.addScience(bookProduced);
    }
    
}
