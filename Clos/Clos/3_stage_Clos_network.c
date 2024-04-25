//      Program evaluating optimal 3-stage Clos network
//      structure for a given total number of inlets.

#include <stdio.h>
#include <math.h>
#include <conio.h>

main ()
{

long double C3, C3min;          // "long double" for very large numbers
int m, n, r, N, ropt, nopt;

n=0; C3min = 0;                 // Initial values

printf ("\n\nGive total number of inlets: ");
scanf ("%d", &N);               // Getting total number of inlets

while (n++ < N)                 // Count every possibility n = 1,2...N
{
if (N % n == 0) r = N/n; else r = floor(N/n) + 1;
       // Count r as lowest possible ensuring AT LEAST the total N inlets.
m = 2*n-1;                      // Count m = 2n-1

C3 = (long double)2*(long double)r*(long double)m*(long double)n +
(long double)m*(long double)r*(long double)r;
       // Count total number of crosspoints C3 = 2mnr+mr^2
       // Casting (long double) ensures compilator uses this type

if ((C3 < C3min) || (C3min==0))         // If counted C3 is minimal whatsoever
{                                       // save Clos network parameters
        C3min = C3;
        ropt = r;
        nopt = n;
};

};

printf ("\n\n3 - stage Clos network\n\nn - number of inlets per switch in section 1,\nr - number of switches in section 1");
printf ("\n\nThe optimal switching network (n=%d, r=%d) consists of %.0Lf crosspoints. ", nopt, ropt, C3min);

printf ("\n\nPress any key to continue.");

getch();                                // Waiting for a key - to see results
return 0;
};
