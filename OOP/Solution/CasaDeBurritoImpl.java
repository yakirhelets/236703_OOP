package OOP.Solution;

import OOP.Provided.CasaDeBurrito;
import OOP.Provided.Profesor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class CasaDeBurritoImpl implements CasaDeBurrito {
    private int id;
    private String name;
    private int distance;
    private Set<String> menu;
    private Map<Integer, Integer> rates; //added new set here! (profesorID,profesorRate)
    private int numOfRates;
    private int sumOfRates;

    //constructor
    public CasaDeBurritoImpl(int id, String name, int dist, Set<String> menu){
        this.id = id;
        this.name = name;
        this.distance = dist;
        numOfRates =0;
        sumOfRates=0;
        this.menu = new HashSet<>();
        this.rates = new HashMap<>();
        this.menu.addAll(menu);
    }

    @Override
    public int getId(){
        return id;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public int distance(){
        return distance;
    }

    @Override
    public boolean isRatedBy(Profesor p){
        return rates.containsKey(p.getId());
    }

    @Override
    public CasaDeBurrito rate (Profesor p, int r) throws RateRangeException {
        if(r < 0 || r > 5){ //illigal rating
            throw new RateRangeException();
        }
        int profesorID = p.getId();
        if(isRatedBy(p)){   //already rated by profesor p
            rates.remove(profesorID);
        }
        rates.put(profesorID,r);
        return this;
    }

    @Override
    public int numberOfRates(){
        return numOfRates;
    }

    @Override
    public double averageRating(){
        if(numOfRates == 0){
            return 0;
        }else{
            return (double)sumOfRates / numOfRates; //return avg
        }
    }

    @Override
    public String toString(){
        return "OOP.Provided.CasaDeBurrito: " +
                getName() + "./n" +
                "Id: " +
                getId() + "./n" +
                "Distance" +
                distance() + "./n" +
                menu.stream().sorted().reduce("", (m1,m2) -> m1 +", " + m2) + "./n";
    }

    @Override
    public int compareTo(CasaDeBurrito c) {
        return this.getId() - c.getId();
    }
}





