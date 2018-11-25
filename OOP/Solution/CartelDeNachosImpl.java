package OOP.Solution;

import OOP.Provided.CartelDeNachos;
import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CartelDeNachosImpl implements CartelDeNachos {
    private Set<Profesor> profesors;
    private Set<CasaDeBurrito> rests;

    public CartelDeNachosImpl() {
        this.profesors = new HashSet<>();
        this.rests = new HashSet<>();
    }

    @Override
    public Profesor joinCartel(int id, String name) throws Profesor.ProfesorAlreadyInSystemException {
        Profesor p = new ProfesorImpl(id, name);
        if (profesors.contains(p)) { //same here.. we need to check ID instead of OBJ
            throw new Profesor.ProfesorAlreadyInSystemException();
        }
            profesors.add(p);
            return p;
    }

    @Override
    public CasaDeBurrito addCasaDeBurrito(int id, String name, int dist, Set<String> menu) throws CasaDeBurrito.CasaDeBurritoAlreadyInSystemException {
        CasaDeBurrito c = new CasaDeBurritoImpl(id, name, dist, menu);
        if (rests.contains(c)) {
            throw new CasaDeBurrito.CasaDeBurritoAlreadyInSystemException();
        }
        rests.add(c);
        return c;    
    }

    @Override
    public Collection<Profesor> registeredProfesores() {
        Collection<Profesor> copy = profesors.stream().collect(Collectors.toSet());
        return copy; //returns a copy of profesores so future actions won't change out original one
    }


    @Override
    public Collection<CasaDeBurrito> registeredCasasDeBurrito() {
        Collection<CasaDeBurrito> copy = rests.stream().collect(Collectors.toSet());
        return copy; //returns a copy of favorites so future actions won't change out original one
    }

    @Override
    public Profesor getProfesor(int id) throws Profesor.ProfesorNotInSystemException {
        for (Profesor p : this.profesors) {
            if(p.getId()==id) {
                return p;
            }
        }
        throw new Profesor.ProfesorNotInSystemException();
    }

    @Override
    public CasaDeBurrito getCasaDeBurrito(int id) throws CasaDeBurrito.CasaDeBurritoNotInSystemException {
        for (CasaDeBurrito c : this.rests) {
            if(c.getId()==id) {
                return c;
            }
        }
        throw new CasaDeBurrito.CasaDeBurritoNotInSystemException();
    }

    @Override
    public CartelDeNachos addConnection(Profesor p1, Profesor p2) throws Profesor.ProfesorNotInSystemException, Profesor.ConnectionAlreadyExistsException, Profesor.SameProfesorException {
        if (p1.equals(p2)) {
            throw new Profesor.SameProfesorException();
        }
        if (!this.profesors.contains(p1) || !this.profesors.contains(p2)) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        if (p1.getFriends().contains(p2) || p2.getFriends().contains(p1)) {
            throw new Profesor.ConnectionAlreadyExistsException();
        }
        p1.addFriend(p2);
        p2.addFriend(p1);
        return this;
    }

    //TODO: fix!
    @Override
    public Collection<CasaDeBurrito> favoritesByRating(Profesor p) throws Profesor.ProfesorNotInSystemException {
        if (!this.profesors.contains(p) ) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        TreeSet<Profesor> sorted = new TreeSet<>(Comparator.comparingInt(Profesor::getId));
        sorted.addAll(p.getFriends()); //all friends sorted by their ID's

        Comparator<CasaDeBurrito> byRating = (casa1, casa2) -> {
            return (int)(casa2.averageRating() - casa1.averageRating());
        };
        Comparator<CasaDeBurrito> byDist = (casa1, casa2) -> {
            return (int)(casa1.distance() - casa2.distance());
        };
        Comparator<CasaDeBurrito> byId = (casa1, casa2) -> {
            return (int)(casa1.getId() - casa2.getId());
        };

        List<CasaDeBurrito> burritos = new ArrayList<>();

        for (Profesor friend : sorted) {
            friend.favorites().stream().sorted(byRating.thenComparing(byDist.thenComparing(byId))).
                    forEach(burritos::add);
        }
        return burritos.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(Profesor p) throws Profesor.ProfesorNotInSystemException {
        if (!this.profesors.contains(p) ) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        TreeSet<Profesor> sorted = new TreeSet<>(Comparator.comparingInt(Profesor::getId));
        sorted.addAll(p.getFriends());
        Comparator<CasaDeBurrito> byRating = (casa1, casa2) -> {
            return (int)(casa2.averageRating() - casa1.averageRating());
        };
        Comparator<CasaDeBurrito> byDist = (casa1, casa2) -> {
            return (int)(casa1.distance() - casa2.distance());
        };
        Comparator<CasaDeBurrito> byId = (casa1, casa2) -> {
            return (int)(casa1.getId() - casa2.getId());
        };
//        for (TreeSet<Profesor> friend : sorted)
        List<CasaDeBurrito> burritos = new ArrayList<>();
        for (Profesor friend : sorted) {
            friend.favorites().stream().sorted(byDist.thenComparing(byRating.thenComparing(byId))).forEach(burritos::add);
        }
        return burritos.stream().distinct().collect(Collectors.toList());
    }

    //helper for getRecommendation
    private boolean aux(Profesor p, CasaDeBurrito c, int t) {
        if (t<0) {
            return false;
        }
        if (p.favorites().contains(c)) {
            return true;
        } else {
            for (Profesor s : p.getFriends()) {
                if (!aux(s, c, t-1)) {
                    continue;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean getRecommendation(Profesor p, CasaDeBurrito c, int t) throws Profesor.ProfesorNotInSystemException, CasaDeBurrito.CasaDeBurritoNotInSystemException, ImpossibleConnectionException {
        if (!this.profesors.contains(p) ) {
            throw new Profesor.ProfesorNotInSystemException();
        }
        if (!this.rests.contains(c) ) {
            throw new CasaDeBurrito.CasaDeBurritoNotInSystemException();
        }
        if (t<0) {
            throw new ImpossibleConnectionException();
        }
        return aux(p,c,t);
    }

    //TODO: fails for empty system!
    @Override
    public List<Integer> getMostPopularRestaurantsIds() {
        Map<Integer, Integer> ranks = new HashMap<>();
        for(CasaDeBurrito rest : rests) {
            ranks.put(rest.getId(), 0);
        }
        for (Profesor p : profesors) {
            for (Profesor friend : p.getFriends()) {
                for(CasaDeBurrito rest : friend.favorites()) {
                    int newRank = ranks.get(rest.getId());
                    ranks.put(rest.getId(), newRank+1);
                }
            }
        }
        List<Integer> ranking = new ArrayList<>();
        ranking = ranks.values().stream().sorted().collect(Collectors.toList());
        int max = ranking.get(ranking.size()-1);

        List<Integer> maxRanking = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : ranks.entrySet()) {
            if (entry.getValue()==max) {
                maxRanking.add(entry.getKey());
            }
        }
        maxRanking.sort((s1,s2) -> s1-s2);
        return maxRanking;
    }

    @Override
    public String toString() {
//  * OOP.Provided.Profesor: <name>.
        List<Integer> pIds= new ArrayList<>();
        pIds = this.profesors.stream().map(p -> p.getId()).sorted((i1,i2) -> i1-i2).collect(Collectors.toList());
        String names = pIds.toString();
        String profs = "Registered profesores: " + names.substring(names.indexOf("[")+1, names.indexOf("]")) + ".\n";
//  * Id: <id>.
        List<Integer> cIds= new ArrayList<>();
        cIds = this.rests.stream().map(c -> c.getId()).sorted((i1,i2) -> i1-i2).collect(Collectors.toList());
        names = cIds.toString();
        String casas = "Registered casas de burrito: " + names.substring(names.indexOf("[")+1, names.indexOf("]")) + ".\n";
        //
        String friends = "Profesores:\n";

        for (Integer i : pIds) {
            Profesor prof=null;
            try {
                prof=getProfesor(i);
            } catch (Exception ProfesorNotInSystemException) {

            }
            friends += i + " -> " + prof.getFriends().stream().map(Profesor::getId).sorted((i1,i2) -> i1-i2).collect(Collectors.toList()) +".\n";
        }

        return profs + casas + friends + "End profesores.";
    }
}
