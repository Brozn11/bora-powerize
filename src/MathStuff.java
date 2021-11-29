//# BEGIN SKELETON
import java.util.ArrayList;
import java.util.List;

/**
 * Library with static mathematical functions.
 *
<!--//# BEGIN TODO Name, student id, and date-->
<p><b>
 * @author Bora Ozen
 * @id 1555685
 * @date 27/11/2021
</b></p>
<!--//# END TODO-->
*/
// -----8<----- cut line -----8<-----
public abstract class MathStuff {

    /**
     * Returns exponentiation, taking care of overflow.
     *
     * @param a  the base
     * @param b  the exponent
     * @pre {@code 0 <= a && 0 <= b}
     * @return {@code a ^ b} if {@code a ^ b <= Integer.MAX_VALUE}
     *      else {@code Long.MAX_VALUE}
     * @throws IllegalArgumentException  if precondition violated
     */
    public static long power(int a, int b) throws IllegalArgumentException {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("power: negative argument");
        }
        // 0 <= a && 0 <= b
        long x = a; // see invariant
        int k = b; // see invariant
        long result = 1L; // see invariant

        // invariant: 0 <= k <= b && result * x^k == a^b
        while (k != 0) {
            if (k % 2 == 0) { // even exponent
                if (x <= Integer.MAX_VALUE) {
                    x *= x;
                } else {
                    x = Long.MAX_VALUE;
                }
                k /= 2;
            } else { // odd exponent
                if (result <= Integer.MAX_VALUE && x <= Integer.MAX_VALUE) {
                    result *= x;
                } else {
                    result = Long.MAX_VALUE;
                }
                k -= 1;
            }
            // invariant holds again, k has decreased
        }
        // k == 0, hence result == a^b

        if (result > Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return result;
    }

    /**
     * Record containing a base and an exponent.
     *
     * @inv {@code 0 <= base && 0 <= exponent}
     */
    public static class Power { // BEGIN RECORD TYPE

        /** The base. */
        public int base;

        /** The exponent. */
        public int exponent;

        /**
         * Constructs a Power with given base and exponent.
         *
         * @param base  the base
         * @param exponent  the exponent
         * @pre {@code 0 <= base && 0 <= exponent}
         * @post {@code \result.base == base && \result.exponent == exponent}
         */
        public Power(int base, int exponent) {
            this.base = base;
            this.exponent = exponent;
        }

    } // END RECORD TYPE

    /**
     * Returns exponentiation.
     *
     * @param p  the base and exponent
     * @pre {@code p != null}
     * @return {@code power(p.base, p.exponent)}
     * @throws IllegalArgumentException  if precondition violated
     */
    public static long power(Power p) throws IllegalArgumentException {
        return power(p.base, p.exponent);
    }

    /**
     * Writes a number as a power with maximal exponent.
     *
     * @param n  the number to 'powerize'
     * @return  power decomposition of {@code n} with maximal exponent
     * @throws IllegalArgumentException  if precondition violated
     * @pre {@code 2 <= n}
     * @post {@code n == power(\result) &&
     *     (\forall int b, int e;
     *      2 <= b && 1 <= e && n == b ^ e;
     *      e <= \result.exponent)}
     */
    public static Power powerize(int n) throws IllegalArgumentException {
//# BEGIN TODO Implementation of powerize
        if (n < 2) {
            throw new IllegalArgumentException("n: smaller than 2");
        }
        ArrayList<Power> powerList = getPowerList(n);
        
       
        
        int[] exponentArray = new int[powerList.size()];
        for (int i = 0; i < powerList.size(); i++) {
            exponentArray[i] = powerList.get(i).exponent;
        }
        
        // Step three: get greatest common divisor for our exponent list
        int g_gcdOfExponentList = findGCD(exponentArray);
        
        // Step four: reduce the powers (divide each exponent of the power list by g)
        for (int i = 0; i < powerList.size(); i++) {
            powerList.get(i).exponent /= g_gcdOfExponentList;
        }
        
        // Step five: Calculate the output base "b"
        long b_outputBase = 1;
        for (Power po : powerList) {
            b_outputBase *= power(po);
        }
       
        return new Power((int) b_outputBase, g_gcdOfExponentList);
    }
        
//# END TODO
    

//# BEGIN TODO Contracts and implementations of auxiliary functions.
    /**
     * Returns prime factorization of an integer as powerlist.
     *
     * @param n  the number to return the prime factorization (powerlist of)
     * @return  power decomposition of {@code n} as a list of Power objects
     * @pre {@code 2 <= n}
     * @post {@code (\forall int b, int e; b^e % n ==0)
     *      
     */
public static ArrayList<Power> getPowerList(int n){
        // return list of Power objects for each factor
        // Step one: factorize
        ArrayList<Integer> factors = primeFactorization(n);
        
        
        // create new List of Power objects
        ArrayList<Power> returnPowerList = new ArrayList<Power>();
        
        //Create new Power object for each base in factors list
        int last_value = 0;
        int exponent_count = 0;
        for (int i = 0; i < factors.size(); i++) {
            if (i==0){
                // we are at the first object
                last_value = factors.get(i);
            }
            // if the current item is the same as the previous one, we increment the counter
            if (factors.get(i) == last_value){
                exponent_count++;
                if (i == factors.size()-1){
                    returnPowerList.add(new Power(last_value, exponent_count));
                    return returnPowerList;
                }
              
            } 
            
            else {
                // the current item is the first of the next base
                returnPowerList.add(new Power(last_value, exponent_count));
                exponent_count = 1;
                last_value = factors.get(i);

                // If we are at the last item, create a Power object in every case
                if (i == factors.size()-1){
                    returnPowerList.add(new Power(last_value, exponent_count));
                }
            }

            
        }
        return returnPowerList;
    }
/**
     * Returns gcd of 2 integers
     *
     * @param a,b integers to get the gcd of
     * @return gcd of a and b
     * @pre {@code 2 <= a && 2<= b}
     * @post {@code result == gcd(a,b))}
     */
static int gcdEuc(int a, int b){
        if (a == 0)
            return b;
        return gcdEuc(b % a, a);
    }
 
    // Function to find gcd of array of
    // numbers
    // in one case: [6,3]
    // It is the list of exponents from the PowerList
    static int findGCD(int[] a){
        int result = 0;
        for (int element: a){
            result = gcdEuc(result, element);
 
            if(result == 1)
            {
               return 1;
            }
        }
 
        return result;
    }
    /**
     * Returns ArrayList<Integer> of prime factors of number
     * @param number to get the factorization of
     * @return prime factorization of number as ArrayList<Integer>
     * @pre {@code 2 <= nubmer}
     * @post {@code factorlist(\forall i;0<= i<factorlist.size-1; factorlist.has(i); multiplication of all a(i) = number}
     */
    public static ArrayList<Integer> primeFactorization(int number) {
        int n = number;
        ArrayList<Integer> factorlist = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factorlist.add(i);
                n /= i;
            }
        }
        return factorlist;
    }


//# END TODO
}


//# END SKELETON
