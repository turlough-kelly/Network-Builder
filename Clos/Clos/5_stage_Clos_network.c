//      Program evaluating optimal 5-stage Clos network
//      structure for a given total number of inlets.

//      For more comments look into 3_stage_Clos_network.c

#include <stdio.h>
#include <math.h>
#include <conio.h>

long double Clos3 (int N)        // Almost the same as in 3_stage_Clos_network.c
{

long double C3, C3min;          
int m, n, r;

n=0; C3min = 0;                 

while (n++ < N)                 
{
if (N % n == 0) r = N/n; else r = floor(N/n) + 1;
       
m = 2*n-1;                      

C3 = (long double)2*(long double)r*(long double)m*(long double)n +
(long double)m*(long double)r*(long double)r;
       
if ((C3 < C3min) || (C3min==0))
{
        C3min = C3;
};
};

return C3min;

};


main ()
{

long double C5, C5min;          
int m, n, r, N, ropt, nopt;

n=0; C5min = 0;                 

printf ("\n\nGive total number of inlets: ");
scanf ("%d", &N);               

while (n++ < N)                 
{
if (N % n == 0) r = N/n; else r = floor(N/n) + 1;
       
m = 2*n-1;                      

C5 = (long double)2*(long double)r*(long double)m*(long double)n +
(long double)m*(long double)Clos3(r);
      // Instead of middle section 'r*r' there is a 3-stage structure
      // Function Clos3() returns minimal number of crosspoints for given 'r'
       
if ((C5 < C5min) || (C5min==0))
{
        C5min = C5;
        ropt = r;
        nopt = n;
};
};

printf ("\n\n5 - stage Clos network\n\nn - number of inlets per switch in section 1,\nr - number of switches in section 1");
printf ("\n\nThe optimal switching network (n=%d, r=%d) consists of %.0Lf crosspoints. ", nopt, ropt, C5min);

printf ("\n\nPress any key to continue.");

getch();                                // Waiting for a key - to see the results
return 0;
};
