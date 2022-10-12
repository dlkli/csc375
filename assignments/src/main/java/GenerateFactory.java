import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class GenerateFactory {

    private int threads, spots, stations;
    private final int ITERATIONS = 100;
    private double factoryFitness;
    private ExecutorService pool;
    private JFrame jFrame;
    private ReentrantLock lock = new ReentrantLock();
    public ArrayList<FactoryFloor> solutions;
    private volatile CountDownLatch countdown;
    public volatile FactoryFloor initialFactory;
    private Exchanger<FactoryStation> exchanger = new Exchanger<>();
    public GenerateFactory(int floorSize, int numStations, int numThreads, JFrame jFrame) {
        this.spots = floorSize;
        this.stations = numStations;
        this.threads = numThreads;
        this.jFrame = jFrame;

        solutions = new ArrayList<>();
        this.countdown = new CountDownLatch(threads);
        initialFactory = new FactoryFloor(spots, stations);
        initialFactory.generateLayout();
        factoryFitness = initialFactory.calculateFitness();
        exchanger = new Exchanger<>();
    }

    public void run() {
        Runnable[] threadsRunning = new Runnable[threads];
        pool = Executors.newFixedThreadPool(threads);

        for (int j = 0; j < ITERATIONS; j ++) {
            for (int i = 0; i < threads; i ++) {
                threadsRunning[i] = modifyFactory();
            }
            countdown = new CountDownLatch(threads);
            for (int k = 0; k < threads; k ++) {
                pool.execute(threadsRunning[k]);
            }
            try {
                countdown.await();
                for(FactoryFloor factory : solutions) {
                    if(factory.calculateFitness() > factoryFitness) {
                        initialFactory = factory;
                        factoryFitness = factory.calculateFitness();
                        draw(initialFactory);
                    }
                }
                solutions.clear();
            } catch (InterruptedException e) {
                System.err.println("Countdown Latch Interrupted.");
            }
        }
    }

    private Runnable modifyFactory() {
        Runnable runnable = () -> {
            FactoryFloor f = new FactoryFloor(initialFactory.getSize(), initialFactory.getNumStations());
            f.generateLayout();
            for (int i = 0; i < 100; i++) {
                if ((i + 1) % 25 == 0) {
                    try {
                        int toSwap = ThreadLocalRandom.current().nextInt(0, f.stations.size());
                        FactoryStation swapped = exchanger.exchange(f.stations.get(toSwap), 10,
                                TimeUnit.MILLISECONDS);
                        f.stations.remove(toSwap);
                        f.stations.add(toSwap, swapped);
                    } catch (InterruptedException | TimeoutException ex) {
                        ex.printStackTrace();
                    }
                }
                f.modifyFloor();
                if (f.calculateFitness() > initialFactory.calculateFitness()) {
                    addToList(f);
                }
            }

            countdown.countDown();
        };
        return runnable;
    }

    private void addToList(FactoryFloor factoryFloor) {
        lock.lock();
        try {
            solutions.add(factoryFloor);
        }
        finally {
            lock.unlock();
        }
    }

    public void draw (FactoryFloor factoryFloor) {
        SwingUtilities.invokeLater(() -> {
            int delay = 2000;
            final Timer timer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    {
                        DrawFactory df = new DrawFactory(spots, 600 / spots, factoryFloor, jFrame);
                        jFrame.setLayout(new BorderLayout());
                        df.add(new JLabel("Factory Fitness: " + factoryFloor.calculateFitness()));
                        jFrame.getContentPane().add(df);
                        jFrame.pack();
                        jFrame.setVisible(true);
                        }
                    }
                }); timer.setRepeats(true);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        timer.start();
                    }
                });
                t.start();
        });
    }

    public static void main(String[] args) {
        FactoryGUI gui = new FactoryGUI();
    }
}
