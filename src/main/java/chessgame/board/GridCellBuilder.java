package chessgame.board;

import java.util.Optional;

/**
 * Builder interface for constructing cells used for grid
 */
public interface GridCellBuilder<C extends Cell, D extends Direction<D>> {

    /**
     * @param file String representation of the file, by alphabetic letters, i.e. 'A', 'B','a', 'b', etc.
     * @param rank String representation of the rank, i.e. '1', '2', etc.
     * @throws IllegalStateException if index is out of range of this factory
     * @return cell at this file and rank
     */
    C at(String file, String rank);

    /**
     * @throws IllegalStateException if index is out of range of this factory
     * @return cell at this file and rank
     */
    C at(int fileIndex, int rankIndex);

    Optional<C> moveOnce(C cell, D direction);

    /**
     * @return whether the cell at this file and rank is within the range of this factory
     */
    boolean withinRange(int fileIndex, int rankIndex);

    /**
     * @return whether the cell at this file and rank is within the range of this factory
     */
    boolean withinRange(String file, String rank);

}
