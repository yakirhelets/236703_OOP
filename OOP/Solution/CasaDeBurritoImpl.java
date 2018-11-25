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


    //constructor
    public CasaDeBurritoImpl(int id, String name, int dist, Set<String> menu){
        this.id = id;
        this.name = name;
        this.distance = dist;
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
        if(p == null){
            return false;
        }
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
        return this.rates.size();
    }

    @Override
    public double averageRating(){
        int numOfRates = this.rates.size();
        int sumOfRates=0;
        if(numOfRates == 0){
            return 0;
        }else{
            for (Map.Entry<Integer,Integer> entry : this.rates.entrySet()){
                sumOfRates+= entry.getValue(); //sum of all values
            }
            return (double)sumOfRates / numOfRates; //return avg
        }
    }

    @Override
    public String toString(){
        String menustr = menu.stream().sorted().reduce("", (m1,m2) -> m1 +", " + m2) + ".";
        String fix = menustr.substring(menustr.indexOf(",")+2); //removes the first "," from the string
        String res = "CasaDeBurrito: " +
                getName() + ".\n" +
                "Id: " +
                getId() + ".\n" +
                "Distance: " +
                distance() + ".\n" +
                "Menu: " + fix;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CasaDeBurrito)) {
            return false;
        } else {
            return ((CasaDeBurrito) this).getId() == ((CasaDeBurrito) o).getId();
        }
    }
    @Override
    public int hashCode(){
        return this.id;
    }


    @Override
    public int compareTo(CasaDeBurrito c) {
        return this.getId() - c.getId();
    }
}





