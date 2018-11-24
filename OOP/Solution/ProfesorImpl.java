package OOP.Solution;

import OOP.Provided.Profesor;
import OOP.Provided.CasaDeBurrito;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfesorImpl implements Profesor {
    private int id;
    private String name;
    private Set<Profesor> friends;
    private Set<CasaDeBurrito> favorites;

    //C'tor
    public ProfesorImpl(int id, String name) {
        this.id = id;
        this.name = name;
        this.friends = new HashSet<>();
        this.favorites = new TreeSet<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Profesor favorite(CasaDeBurrito c) throws UnratedFavoriteCasaDeBurritoException {
        if (!c.isRatedBy(this)) { //profesor didn't rate this rest yet!
            throw new UnratedFavoriteCasaDeBurritoException();
        } else {
            favorites.add(c); //TODO:in favoriteTest - they are comparing two different rest objects with the same id
            //we are not passing it because we count them as different objects and not by their Id's..
            //--will need to change the implemnt(Set of Id's maybe..) or check if its currect to test this case.
        }
        return this;
    }

    @Override
    public Collection<CasaDeBurrito> favorites() {
        Collection<CasaDeBurrito> copy = favorites.stream().collect(Collectors.toSet());
        return copy; //returns a copy of favorites so future actions won't change out original one
    }

    @Override
    public Profesor addFriend(Profesor p) throws SameProfesorException, ConnectionAlreadyExistsException {
        if (this.equals(p)) {
            throw new SameProfesorException();
        }
        if (friends.contains(p)) {
            throw new ConnectionAlreadyExistsException();
        } else {
            friends.add(p);
        }
        return this;
    }

    @Override
    public Set<Profesor> getFriends() {
        Set<Profesor> copy = friends.stream().collect(Collectors.toSet());
        return copy; //returns a copy of friends so future actions won't change out original one
    }

    @Override
    public Set<Profesor> filteredFriends(Predicate<Profesor> p) {
        Set<Profesor> filtered = new HashSet<>();
        for (Profesor prof : this.friends) {
            if (p.test(prof)) {
                filtered.add(prof);
            }
        }
        return filtered;
    }

    @Override
    public Collection<CasaDeBurrito> filterAndSortFavorites(Comparator<CasaDeBurrito> comp, Predicate<CasaDeBurrito> p) {
        Collection<CasaDeBurrito> filtered = new TreeSet<>(comp); //initialize TreeSet with the given comparator
        for (CasaDeBurrito restaurant : this.favorites) {
            if (p.test(restaurant)) {
                filtered.add(restaurant);
            }
        }
        return filtered;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(int rLimit) {
        return filterAndSortFavorites(
                Comparator.comparingDouble(CasaDeBurrito::averageRating).reversed(),
                casa -> casa.averageRating() >= rLimit
        );
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(int dLimit) {
        return filterAndSortFavorites(
                Comparator.comparingInt(CasaDeBurrito::distance),
                casa -> casa.distance() <= dLimit
        );
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profesor)) {
            return false;
        } else {
            return ((Profesor) this).getId() == ((Profesor) o).getId();
        }

    }

    @Override
    public int compareTo(Profesor o) {
        return this.id - o.getId();
    }

    @Override
    public String toString() {
//  * OOP.Provided.Profesor: <name>.
        String name = "OOP.Provided.Profesor: " + this.name + "\n";
//  * Id: <id>.
        String idStr = "Id: " + Integer.toString(this.id) + "\n";
//     * Favorites: <casaName1, casaName2, casaName3...>
        String casas = this.filterAndSortFavorites(//sort by lexicographic order
                (casa1, casa2) -> (casa1.getName().compareTo(casa2.getName())),
                casa -> true
        ).toString();
        String favs = "Favorites: " + casas.substring(casas.indexOf("[") + 1, casas.indexOf("]")) + "\n";//removing the brackets from the string

        return name + idStr + favs;
    }
}
