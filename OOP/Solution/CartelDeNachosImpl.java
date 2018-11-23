package OOP.Solution;

import OOP.Provided.CartelDeNachos;
import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (profesors.contains(p)) {
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
        return null;
    }

    @Override
    public Collection<CasaDeBurrito> registeredCasasDeBurrito() {
        return null;
    }

    @Override
    public Profesor getProfesor(int id) {
        return null;
    }

    @Override
    public CasaDeBurrito getCasaDeBurrito(int id) throws CasaDeBurrito.CasaDeBurritoNotInSystemException {
        return null;
    }

    @Override
    public CartelDeNachos addConnection(Profesor p1, Profesor p2) throws Profesor.ProfesorNotInSystemException, Profesor.ConnectionAlreadyExistsException, Profesor.SameProfesorException {
        return null;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByRating(Profesor p) throws Profesor.ProfesorNotInSystemException {
        return null;
    }

    @Override
    public Collection<CasaDeBurrito> favoritesByDist(Profesor p) throws Profesor.ProfesorNotInSystemException {
        return null;
    }

    @Override
    public boolean getRecommendation(Profesor p, CasaDeBurrito c, int t) throws Profesor.ProfesorNotInSystemException, CasaDeBurrito.CasaDeBurritoNotInSystemException, ImpossibleConnectionException {
        return false;
    }

    @Override
    public List<Integer> getMostPopularRestaurantsIds() {
        return null;
    }
}
