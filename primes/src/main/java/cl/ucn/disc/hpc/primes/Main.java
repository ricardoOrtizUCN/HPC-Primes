package cl.ucn.disc.hpc.primes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Main.
 *
 * 1. Escribir una funcion que retorne true/false si un numero es primo.
 * 2. Contar la cantidad de numeros primos que existen entre 2 y 10000.
 * 3. Escribir un codigo que resuelva el punto 2, utilizando 1,2,4 .. N nucleos.s
 *
 * @autor Ricardo Ortiz-Hidalgo
 */
public class Main {

    /**
     * The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(Main.class);


    /**
     * The Main.
     *
     * @param args to use.
     */
    public static void main(String[] args) throws InterruptedException {

        // The time now
        //long start = System.currentTimeMillis();

        // Counter of primes
        //long nPrimes = 0;


        // for(long i=1; i<100000;i++) {
        //    if(isPrime(i)) {
        //      nPrimes++;
        //System.out.println( i + " isPrime!!!");
        // }
        //}
        //long time= System.currentTimeMillis() - start;

        //Primes: 9592 in 4440 ms.
        //Primes: 9592 in 4474 ms.
        //Primes: 9592 in 4874 ms.
        //Primes: 9592 in 4485 ms.

        //System.out.println("Primes: " + nPrimes + " in " + time + " ms. ");

        // The max
        final long MAX = 1000000;

        // The Chrono
        final StopWatch stopWatch = StopWatch.createStarted();

        log.debug("Starting the Main ..");

        // One thread.
        //Thread thread = new Thread(new PrimeTask(5754853343L));
        //thread.run();

        //log.debug("Prime? {}.", isPrime(5754853343L));


        //The "Ejecutador"
        final ExecutorService executorService = Executors.newFixedThreadPool(8);

        // Create the MAX runnabless and pass to the executor
        for (long i = 1; i < MAX; i++) {
            executorService.submit(new PrimeTask(i));

        }

        // Don't receive more tasks
        executorService.shutdown();

        // Wait for  some time
        if (executorService.awaitTermination(1, TimeUnit.HOURS)) {
            log.debug("Primes foundes: {} in {}.", PrimeTask.getPrimes(), stopWatch);

        } else {
            //Time:
            log.info("Done in {} ", stopWatch);
        }
    }



    private static class PrimeTask implements Runnable{

        /**
         * The Number
         */

        private final long number;

        /**
         *  The Counter.
         *
         */

        private final static AtomicInteger counter = new AtomicInteger(0);

        /**
         * The Constructor
         * @param number to test
         */
        public PrimeTask(final long number){
            this.number = number;
        }

        /**
         * @return the numbers of primes
         */

        public static int getPrimes() {
            return counter.get();
        }

        /**
         * When an object implementing interface {@code Runnable} is used
         * to create a thread, starting the thread causes the object's
         * {@code run} method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method {@code run} is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {

            //If the number is prime, we count him.
            if (isPrime(this.number)) {
                //log.debug("{} was prime !!!", this.number);
                counter.getAndIncrement();


            }

        }

        /**
         * Funtion to test primality.
         *
         * @param n the number to test.
         * @return true is n is prime.
         */
        public static boolean isPrime(final long n){
            // Can't process negative numbers
            if(n<=0){
                throw new IllegalArgumentException("Error in n : Can't process negative number");
            }
            // "One" is not prime!
            if(n==1){
                return false;
            }

            //Testing primality form 2 to n-1
            for (long i = 2; i < n; i++){

                //If module ==0 -> not prime;
                // TODO: Change to n/2 the upper limit
                if(n % i == 0){
                    return false;

                }

            }
            // true prime
            return true;
        }

    }

}
