package com.example.network_builder;

public class clos_methods
{
    //takes stagecount as well as input number to calculate optimum values
    //for r and n, as well as crosspoints for X-stage network
    public double[] X_Enu(int stage_count, int N, boolean optimise) {
        //initialise values
       if(optimise)
       {
           stage_count = optimum_stage_count(N);
       }
        long Cx = 0, Cxmin = 0; //doubles used as numbers could get very large
        double m, n = 0, r, ropt = 0, nopt = 0; //initial values

        while (n++ < N) {
            if (N % n == 0) r = N / n;
            else r = Math.floor(N / n) + 1;
            // Count r as lowest possible ensuring AT LEAST the total N inputs.
            m = 2 * n - 1;

            //if the stage count is 3, use m * 2r
            if (stage_count == 3) {
                Cx = (long) (2 * r * m * n + m * r * r);
            }
            //otherwise, employ min of previous stage as middle stage
            else {
                Cx = (long) (2 * r * m * n + m * X_min((stage_count - 2), r));
            }

            //sets new values if a lower set of values has been found
            if ((Cx < Cxmin) || (Cxmin == 0)) {
                Cxmin = Cx;
                ropt = r;
                nopt = n;
            }
        }
        //put results in an array before returning
       double[] ans = {(ropt * nopt), stage_count, Cxmin, ropt, nopt, ((2 * nopt) - 1)};

        return ans;
    }

    //method very similar to above, but just returns the crosspoint count
    //used in recursion above
    public long X_min(int stage_count, double N) {
        //initialise variables
        long Cx = 0, Cxmin = 0; //doubles used as numbers could get very large
        double m = 0, n = 0, r = 0, ropt = 0, nopt = 0; //initial values

        while (n++ < N) //counts every possibility N = 1, 2, ... , N
        {
            if (N % n == 0) r = N / n;
            else r = Math.floor(N / n) + 1;
            // Count r as lowest possible ensuring AT LEAST the total N inputs.
            m = 2 * n - 1;

            //if the stage count is 3, use m * 2r
            if (stage_count == 3) {
                Cx = (long) (2 * r * m * n + m * r * r);
            }
            //otherwise, employ min of previous stage as middle stage
            else {
                Cx = (long) (2 * r * m * n + m * X_min((stage_count - 2), r));
            }

            if ((Cx < Cxmin) || (Cxmin == 0)) {
                Cxmin = Cx;
            }
        }
        //return min of stage count X
        return Cxmin;
    }

    //method to find most optimum stage count for an input
    public int optimum_stage_count(int N) {
        //set opt at 5, to compare previous stage
        int opt = 5;
        //breaker acts as the switch; if the opt value has been found, it will be set to true
        boolean breaker = false;

        while (!breaker) {
            //if the previous stage is more optimal for N, set opt to that value and return
            if (X_min((opt - 2), N) < X_min(opt, N)) {
                opt = (opt - 2);
                breaker = true;
            }
            //otherwise, increment opt to next stage and repeat
            else {
                opt = (opt + 2);
            }
        }

        //return opt as an integer for use with below
        return opt;
    }
}
