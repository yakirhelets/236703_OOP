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
        return favorites.stream().filter(s -> s.averageRating() >= rLimit).
                sorted((Comparator.comparingDouble(CasaDeBurrito::averageRating).reversed()).
                        thenComparing(CasaDeBurrito::distance).
                        thenComparing(CasaDeBurrito::getId)).
                    collect(Collectors.toList());
    }
    @Override
    public Collection<CasaDeBurrito> favoritesByDist(int dLimit) {
        return favorites.stream().filter(s -> s.distance() <= dLimit).
                sorted((Comparator.comparingDouble(CasaDeBurrito::distance)).
                        thenComparing((Comparator.comparingDouble(CasaDeBurrito::averageRating).reversed())).
                        thenComparing(CasaDeBurrito::getId)).
                collect(Collectors.toList());
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
    public int hashCode(){
        return this.id;
    }

    @Override
    public int compareTo(Profesor o) {
        return this.id - o.getId();
    }

    @Override
    public String toString() {
//  * OOP.Provided.Profesor: <name>.
        String name = "Profesor: " + this.name + ".\n";
//  * Id: <id>.
        String idStr = "Id: " + Integer.toString(this.id) + ".\n";
//     * Favorites:
        String fixed;
        if (this.favorites.isEmpty()) {
            fixed = "Favorites: .";
        } else {
            String casas = this.favorites.stream().sorted((c1,c2) -> c1.getName().compareTo(c2.getName()))
                    .map(CasaDeBurrito::getName).reduce("", (m1,m2) -> m1 +", " + m2) + ".";
            fixed = "Favorites: " + casas.substring(casas.indexOf(",")+2); //removes the first "," from the string
        }


        return name + idStr + fixed;
    }
}
