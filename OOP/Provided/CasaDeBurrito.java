package OOP.Provided;

/**
 * Each instance of the OOP.Provided.CasaDeBurrito class has an id, a name, a distance
 * from the Technion, and holds a collection of menu items.
 * The id is unique for every OOP.Provided.CasaDeBurrito.
 * */
public interface CasaDeBurrito extends Comparable<CasaDeBurrito> {

    class CasaDeBurritoAlreadyInSystemException    extends Exception {}
    class CasaDeBurritoNotInSystemException        extends Exception {}
    class RateRangeException                       extends Exception {}

    /**
     * @return the id of the casa de burrito.
     * */
    int getId();

    /**
     * @return the name of the casa de burrito.
     * */
    String getName();

    /**
     * @return the distance from the Technion.*/
    int distance();

    /**
     * @return true iff the profesor rated this OOP.Provided.CasaDeBurrito
     * @param p - a profesor
     * */
    boolean isRatedBy(Profesor p);

    /**
     * rate the OOP.Provided.CasaDeBurrito by a profesor
     * @return the object to allow concatenation of function calls.
     * @param p - the profesor rating the OOP.Provided.CasaDeBurrito
     * @param r - the rating
     * */
    CasaDeBurrito rate(Profesor p, int r)
            throws RateRangeException;

    /**
     * @return the number of rating the OOP.Provided.CasaDeBurrito has received
     * */
    int numberOfRates();

    /**
     * @return the OOP.Provided.CasaDeBurrito's average rating
     * */
    double averageRating();

    /**
     * @return the OOP.Provided.CasaDeBurrito's description as a string in the following format:
     * <format>
     * OOP.Provided.CasaDeBurrito: <name>.
     * Id: <id>.
     * Distance: <dist>.
     * Menu: <menuItem1, menuItem2, menuItem3...>.
     * </format>
     * Note: Menu items are ordered by lexicographical order, asc.
     *
     * Example:
     *
     * OOP.Provided.CasaDeBurrito: BBB.
     * Id: 1.
     * Distance: 5.
     * Menu: Cola, French Fries, Steak.
     *
     * */
    String toString();
}
