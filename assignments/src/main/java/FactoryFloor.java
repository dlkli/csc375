import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;

public class FactoryFloor {

    public FactoryStation[][] floor;
    private int size, numStation;

    public ArrayList<FactoryStation> stations;
    private ArrayList<int[]> toSwap;

    public FactoryFloor(int x, int numStations) {
        this.size = x;
        this.numStation = numStations;
        floor = new FactoryStation[x][x];
        stations = new ArrayList<>();
        toSwap = new ArrayList<>();
    }

    void generateLayout() {
        for (int i = 0; i < numStation;) {
            int spotX = ThreadLocalRandom.current().nextInt(size);
            int spotY = ThreadLocalRandom.current().nextInt(size);

            if (floor[spotX][spotY] == null) {
                floor[spotX][spotY] = new FactoryStation();
                floor[spotX][spotY].setLocation(spotX, spotY);
                stations.add(floor[spotX][spotY]);
                i++;
            }
        }
        for (FactoryStation fs : stations) {
            fs.setNeighbors(floor);
        }
    }

    void modifyFloor() {
        for(int i = 0; i < stations.size(); i++) {
            int r = ThreadLocalRandom.current().nextInt(0, stations.size());
            FactoryStation a = stations.get(i);
            FactoryStation b = stations.get(r);
            FactoryStation temp = a;
            floor[a.getRow()][a.getColumn()] = floor[b.getRow()][b.getColumn()];
            floor[b.getRow()][b.getColumn()] = floor[temp.getRow()][temp.getColumn()];
            a.setLocation(b.getRow(), b.getColumn());
            b.setLocation(temp.getRow(), temp.getColumn());
        }

    }

    public int getSize() {
        return size;
    }

    public int getNumStations() {
        return numStation;
    }

    public double calculateFitness() {
        double fitness = 0;
        for(FactoryStation fs : stations){
            fs.setNeighbors(floor);
            fitness += fs.measureAffinity();
        }
        return fitness;
    }
}
