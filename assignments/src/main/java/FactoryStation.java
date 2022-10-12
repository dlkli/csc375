import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class FactoryStation {

    float MAX_STATION_AFFINITY = 5;
    float MIN_STATION_AFFINITY = 1;

    private enum StationType {
        pinkStation,
        yellowStation,
        cyanStation
    }

    int x;
    int y;
    Color color;
    ArrayList<FactoryStation> surroundingStations = new ArrayList<>();
    StationType type;


    public FactoryStation() {
        this.type = setStationType();

        if (this.type == StationType.pinkStation) {
            this.color = Color.MAGENTA;
        }
        else if (this.type == StationType.yellowStation) {
            this.color = Color.YELLOW;
        }
        else if (this.type == StationType.cyanStation) {
            this.color = Color.CYAN;
        }
        else {
            this.color = Color.WHITE;
        }
    }

    //check the station shape. if it's a square, it's less "happy" the more neighbors it has
    // if circle, it's more "happy" the more neighbors it has
    //if triangle, it's more "happy" if it has odd number of neighbors
    public float measureAffinity() {
        if (type == StationType.pinkStation) {
            if (surroundingStations.size() == 0) {
                return MAX_STATION_AFFINITY;
            }
            else {
                return (surroundingStations.size() + 1) / MAX_STATION_AFFINITY;
            }

        }
        else if (type == StationType.cyanStation){
            return MIN_STATION_AFFINITY * surroundingStations.size();
        }
        else {
            if (surroundingStations.size() == 1 || surroundingStations.size() == 3) {
                return MAX_STATION_AFFINITY;
            }
            else {
                return MAX_STATION_AFFINITY/2;
            }
        }
    }

    private StationType setStationType() {
        return StationType.values()[new Random().nextInt(StationType.values().length)];
    }

    public int getRow() {
        return x;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getColumn() {
        return y;
    }

    public StationType getType() {
        return type;
    }

    public synchronized void setNeighbors(FactoryStation[][] station) {
        //make sure when we set neighbors we don't have old neighbor data
        surroundingStations.clear();

        if (y - 1 >= 0 && station[x][y - 1] != null) {
            surroundingStations.add(station[x][y - 1]);
        } else if (y < station[y].length && station[x][y]!= null) {
            surroundingStations.add(station[x][y]) ;
        } else if (x - 1 >= 0 && station[x - 1][y] != null) {
            surroundingStations.add(station[x - 1][y]);
        } else if (x < station[x].length && station[x][y] != null) {
            surroundingStations.add(station[x + 1][y]);
        }
    }
}