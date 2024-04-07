package com.example.network_builder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class slepian_methods
{
    //main method for calculating slepian network
    public double[] slepian_Enu(int stage_count, double N, int switchSize, boolean optimise)
    {
        //variables to be used
        final long[] Cxmin = {0};
        final double[] mopt = {0};
        final double[] ropt = {0};


        //optimise based on switch size
        if (optimise && switchSize != 0)
        {
            //finds next power if input and switch size are not a valid base/power
            if (!isPower(N, switchSize))
            {
                N = findNextPower(N, switchSize);
            }
            stage_count = optimum_stage_count((int)N, switchSize);
        }

        //alt optimisation if no switch size is specified
        if (optimise && switchSize == 0)
        {
            double closest = 0;
            double current = 0;
            double base = 2;
            //goes through and finds closest power and its base to input N
            for(int i = 2; i < Math.log(N); i++)
            {
                if (!isPower(N, i))
                {
                    current = findNextPower(N, i);
                    closest = findNextPower(N, base);
                }

                if(Math.abs(N - current) > Math.abs(N - closest))
                {
                    base = i;
                }
            }
            stage_count = optimum_stage_count((int)N, (int)base);
        }

        //create executors for parallel processing during exectuion
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //runs if a switch size was specified, as it does not need to enumerate through values
        if (switchSize != 0)
        {
            int finalStage_count = stage_count;
            final double[] currentR = {switchSize}; // Make it an array
            double finalN = N;
            //submits to processor for execution
            executor.submit(() ->
            {
                //calculate m based on r (switch size)
                final double[] m = {(finalN % currentR[0] == 0) ? finalN / currentR[0] : Math.ceil(finalN / currentR[0])};
                long currentCx;

                //runs different calculations based on stage count
                if (finalStage_count == 3)
                {
                    currentCx = (long) (2 * (currentR[0] * currentR[0] * m[0]) + (m[0] * m[0] * currentR[0]));
                }
                else {
                    currentCx = (long) (2 * (currentR[0] * currentR[0] * m[0]) + (currentR[0] * (slepian_min_edit(finalStage_count - 2, m[0]))));
                }
                //synchronises threads for final calculations
                synchronized (this) {
                    if (currentCx < Cxmin[0] || Cxmin[0] == 0)
                        //sets as new lowest only if it satisfied the below
                        if (currentCx == (currentR[0] * currentR[0] * m[0] * finalStage_count))
                        {
                            Cxmin[0] = currentCx;
                            ropt[0] = currentR[0];
                            mopt[0] = m[0];
                        }
                }
            });
        }

        //runs if no switch size was specified
        else if (switchSize == 0)
        {
            //based off rob's original c code, enumerates through values up to half of input (values will repeat after)
            for (double r = 1; r <= Math.ceil(N / 2); r++) {
                final double[] currentR = {r}; //makes an array so computer wont complain
                double m = (N % currentR[0] == 0) ? N / currentR[0] : Math.ceil(N / currentR[0]);

                //finds next power if m/r arent valid base/denominator/whatever you call it
                if (!isPower(m, currentR[0]))
                {
                    m = findNextPower(m, currentR[0]);
                }

                int finalStage_count1 = stage_count;
                double finalM = m;
                //submits for parallel processing
                executor.submit(() ->
                {
                    long currentCx;
                    if (finalStage_count1 == 3) {
                        currentCx = (long) (2 * (currentR[0] * currentR[0] * finalM) + (finalM * finalM * currentR[0]));
                    } else {
                        currentCx = (long) (2 * (currentR[0] * currentR[0] * finalM) + (currentR[0] * (slepian_min_edit(finalStage_count1 - 2, finalM))));
                    }
                    //syncs threads
                    synchronized (this)
                    {
                        if (currentCx < Cxmin[0] || Cxmin[0] == 0)
                        { //sets value only if below is true
                            if (currentCx == (currentR[0] * currentR[0] * finalM * finalStage_count1)) {
                                Cxmin[0] = currentCx;
                                ropt[0] = currentR[0];
                                mopt[0] = finalM;
                            }
                        }
                    }
                });
            }

        }

        //stops parallelisation, ensures stopping it doesn't make the computer explode (i hope)
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //sets relevant values in an array which is then returned
        double[] ans = {(ropt[0] * mopt[0]), stage_count, Cxmin[0], mopt[0], ropt[0]};

        return ans;

    }


    //a smaller version of the above, more closely resembling the clos methods
    //helps to calculate subnetworks of larger networks (ie 3 and 5 stage networks of a 7 stage, for example)
    //private since its only used within this class
    private long slepian_min_edit(int stage_count, double N) {
        long Cxmin = 0;
        double r;

        //enumerates
        for (r = 1; r <= Math.ceil(N / 2); r++) {
            double m = (N % r == 0) ? N / r : Math.ceil(N / r);
            if (!isPower(m, r)) {
                m = findNextPower(m, r);
            }

            //runs different calculations based on stage count
            long Cx = 0;
            if (stage_count == 3) {
                Cx = (long) (2 * (r * r * m) + (m * m * r));
            } else {
                double subValue = slepian_min_edit(stage_count - 2, m);
                Cx = (long) (2 * (r * r * m) + (r * subValue));
            }

            //sets newest min only if below is satisfied
            if (Cx < Cxmin || Cxmin == 0) {
                if (Cx == (r * r * m * stage_count)) {
                    Cxmin = Cx;
                }
            }
        }
        //returns
        return Cxmin;
    }


    //finds next power based on input and a base for the power
    private static double findNextPower(double number, double base) {
        // Check if the number is a power of the base
        if (isPower(number, base)) {
            return number;
        } else {
            // Find the next power of the base greater than the number
            int nextPower = (int) Math.pow(base, Math.ceil(Math.log(number) / Math.log(base)));
            return nextPower;
        }
    }

    //checks if values are valid powers
    private static boolean isPower(double number, double base) {
        double exponent = Math.log(number) / Math.log(base);
        return Math.abs(exponent - Math.round(exponent)) < 1e-10; // Tolerance for double precision
    }

    private int optimum_stage_count(int N, double base)
    {
        double exponent = 0;

        exponent = Math.log(N) / Math.log(base);

        return (int) (exponent + (exponent - 1));
    }
}
