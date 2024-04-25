//      Program evaluating optimal 7-stage Clos network
//      structure for a given total number of inlets.

//      For more comments look into 3_stage_Clos_network.c

#include <stdio.h>
#include <math.h>
#include <conio.h>


long double Clos3 (int N)        
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

long double Clos5 (int N)        
{

long double C5, C5min;          
int m, n, r;

n=0; C5min = 0;                 

while (n++ < N)                 
{
if (N % n == 0) r = N/n; else r = floor(N/n) + 1;
       
m = 2*n-1;                      

C5 = (long double)2*(long double)r*(long double)m*(long double)n +
(long double)m*(long double)Clos3(r);
       
if ((C5 < C5min) || (C5min==0))
{
        C5min = C5;
};
};

return C5min;

};


main ()
{

long double C7, C7min;          
int m, n, r, N, ropt, nopt;

n=0; C7min = 0;                 

printf ("\n\nGive total number of inlets: ");
scanf ("%d", &N);               

while (n++ < N)                 
{
if (N % n == 0) r = N/n; else r = floor(N/n) + 1;
       
m = 2*n-1;                      

C7 = (long double)2*(long double)r*(long double)m*(long double)n +
(long double)m*(long double)Clos5(r);
      // Instead of middle section 'r*r' there is a 5-stage structure
      // Function Clos5() returns minimal number of crosspoints for given 'r'
       
if ((C7 < C7min) || (C7min==0))
{
        C7min = C7;
        ropt = r;
        nopt = n;
};
};

printf ("\n\n7 - stage Clos network\n\nn - number of inlets per switch in section 1,\nr - number of switches in section 1");
printf ("\n\nThe optimal switching network (n=%d, r=%d) consists of %.0Lf crosspoints. ", nopt, ropt, C7min);

printf ("\n\nPress any key to continue.");

getch();                                // Waiting for a key - to see the results
return 0;
};
