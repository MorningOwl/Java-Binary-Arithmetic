
public class BinaryArithmetic {

	public static void main(String[] args) {
		
		System.out.println("Addition");
		boolean[] augend = convertToUnsigned16Bytes(18);
		System.out.println("  " + getString(augend)+ " (" + convertBytesToInteger(augend) + ")");
		boolean[] addend = convertToUnsigned16Bytes(35);
		System.out.println("+ " + getString(addend)+ " (" + convertBytesToInteger(addend) + ")");
		System.out.println("------------------");
		boolean[] sum = new boolean[16];
		add(augend,addend,sum);
		System.out.println("  " + getString(sum) + " (" + convertBytesToInteger(sum) + ")");
		
		System.out.println("\nSubtraction");
		boolean[] minuend = convertToUnsigned16Bytes(54);
		System.out.println("  " + getString(minuend) + " (" + convertBytesToInteger(minuend) + ")");
		boolean[] subtrahend = convertToUnsigned16Bytes(22);
		System.out.println("- " + getString(subtrahend)+ " (" + convertBytesToInteger(subtrahend) + ")");
		System.out.println("------------------");
		boolean[] difference = new boolean[16];
		subtract(minuend,subtrahend,difference);
		System.out.println("  " + getString(difference)+ " (" + convertBytesToInteger(difference) + ")");
		
		System.out.println("\nMultiplication");
		boolean[] multiplicand = convertToUnsigned16Bytes(13107);
		System.out.println("  " + getString(multiplicand)+ " (" + convertBytesToInteger(multiplicand) + ")");
		boolean[] multiplier = convertToUnsigned16Bytes(5);
		System.out.println("x " + getString(multiplier)+ " (" + convertBytesToInteger(multiplier) + ")");
		System.out.println("------------------");
		boolean[] product = new boolean[16];
		multiply(multiplicand,multiplier,product);
		System.out.println("  " + getString(product) + " (" + convertBytesToInteger(product) + ")");
		
		System.out.println("\nDivision");
		boolean[] dividend = convertToUnsigned16Bytes(567);
		System.out.println("  " + getString(dividend)+ " (" + convertBytesToInteger(dividend) + ")");
		boolean[] divisor = convertToUnsigned16Bytes(87);
		System.out.println("/ " + getString(divisor)+ " (" + convertBytesToInteger(divisor) + ")");
		System.out.println("------------------");
		boolean[] quotient = new boolean[16];
		boolean[] remainder = new boolean[16];
		divide(dividend,divisor,quotient,remainder);
		System.out.println("  " + getString(quotient) + " r" + getString(remainder) + " (" + convertBytesToInteger(quotient) + " r" + convertBytesToInteger(remainder) + ")");
		
	}
	
	/**
	 * Converts a positive integer into an unsigned 16-bit representation.
	 * @param n The integer to convert. Integers less than 0 will be converted to 0 and 
	 *          integers greater than 65535 will be converted to 65535
	 * @return A boolean array with 16 elements
	 */
	public static boolean[] convertToUnsigned16Bytes(int n) {
		if(n < 0) {
			n = 0;
		} else if (n > 65535) {
			n = 65535;
		}
		boolean[] bytes = new boolean[16];
		for(int i = 16; i > 0; i--) {
			if(n >= Math.pow(2, i-1)) {
				n -= Math.pow(2, i-1);
				bytes[i-1] = true;
			} else {
				bytes[i-1] = false;
			}
		}		
		return bytes;
	}
	
	/**
	 * Converts a boolean array representation of bytes to integer.
	 * @param bytes The array of bytes to convert.
	 * @return integer converted from the array
	 */
	public static int convertBytesToInteger(boolean[] bytes) {
		int n = 0;
		for(int i = 0; i < bytes.length; i++) {
			if(bytes[i]){
				n += Math.pow(2,i);
			}
		}
		return n;
	}
	
	/**
	 * Performs a binary addition.
	 * @param augend The first binary array to add.
	 * @param addend The second binary array to add.
	 * @param sum The array to store the answer in.
	 */
	public static void add(boolean[] augend, boolean[] addend, boolean[] sum) {
		if(sum.length < 16) {
			sum = new boolean[16];
		}
	    boolean[] carryOver = new boolean [16];
	    boolean[] tempSum = new boolean[16];
	    boolean temp;
	    
	    for(int i = 0; i < 16; i++) {	    	
	    	temp = false;
	    	if(carryOver[i]) {
	    		temp = flip(temp);
	    	} 
	    	if(augend[i]) {	    		
	    		temp = flip(temp);
	    		if (carryOver[i]) {
	    			if(i+1<16) {
	    				carryOver[i+1] = true;	
	    			}	    			    			
	    		}
	    	}
	    	if(addend[i]) {
	    		temp = flip(temp);
	    		if (augend[i] || carryOver[i]) {
	    			if(i+1<16) {
	    				carryOver[i+1] = true;	
	    			}	    			    			
	    		}
	    	}
	    	tempSum[i] = temp;
	    }
	    
	    for(int i = 0; i < sum.length; i++) {
	    	sum[i] = tempSum[i];
	    }
	}
	
	/**
	 * Performs a binary subtraction.
	 * @param minuend The binary array to be subtracted from.
	 * @param subtrahend The binary array to subtract.
	 * @param difference The array to store the answer in.
	 */
	public static void subtract(boolean[] minuend, boolean[] subtrahend, boolean[] difference) {		
	    //perform 2's Complement on a temporary subtrahend
		boolean[] tempSubtrahend = new boolean[16];
		copyBinaries(subtrahend, tempSubtrahend);
		convertTwosCompliments(tempSubtrahend);				
		add(minuend,tempSubtrahend,difference); //add minuend and converted subtrahend
	}
	
	/**
	 * Performs a binary multiplication.
	 * @param multiplicand The first binary array to multiply.
	 * @param multiplier The second binary array to multiply.
	 * @param product The array to store the answer in.
	 */
	public static void multiply(boolean[] multiplicand, boolean[] multiplier, boolean[] product) {
		resetToZero(product);
		boolean[] tempProduct = new boolean[16];
		
		for(int i = 0; i < multiplier.length; i++) {
			if(multiplier[i]) {
				add(getShiftLeft(multiplicand,i),tempProduct,tempProduct);
			}
		}
		
		for(int i = 0; i < product.length; i++) {
			product[i] = tempProduct[i];
	    }		
	}
	
	/**
	 * Performs a binary division.
	 * @param dividend The binary array to divide from.
	 * @param divisor The binary array to divide with.
	 * @param quotient The array to store the answer in.
	 * @param remainder The array to store the remainder in.
	 */
	public static void divide(boolean[] dividend, boolean[] divisor, boolean[] quotient, boolean[] remainder) {
		resetToZero(quotient);
		boolean[] tempRemainder = new boolean[16];
		
		for(int i = dividend.length - 1; i >= 0; i--) {
			tempRemainder = getShiftLeft(tempRemainder,1);
			tempRemainder[0] = dividend[i];
			
			if(compare(tempRemainder, divisor) != 0) {				
				subtract(tempRemainder,divisor,tempRemainder);
				quotient[i] = true;
			}
		}
		copyBinaries(tempRemainder, remainder);
		//System.out.println("Done:" + "\nQ: " + getString(quotient) + "\nR: " + getString(remainder) + " \nD: " + getString(divisor));
	}
	
	/**
	 * Performs Two's Compliments on the given bytes.
	 * @param bytes A boolean array that represents 16 bytes.
	 */
	public static void convertTwosCompliments(boolean[] bytes) {
		boolean[] one = {true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
		boolean[] sum = new boolean[16];
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = flip(bytes[i]);
		}
		add(bytes,one,sum);
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = sum[i];
		}
	}
	
	/**
	 * Copies one binary array to another.
	 * @param binary1 Binaries to copy from.
	 * @param binary2 Binaries to copy into.
	 */
	public static void copyBinaries(boolean[] binary1, boolean[] binary2) {
		if(binary1.length != binary2.length) {
			binary2 = new boolean[binary1.length];
		}
		for(int i = 0; i < binary1.length; i++) {
			binary2[i] = binary1[i];
		}
	}
	
	/**
	 * Flips a bit to on or off (true or false).
	 * @param bit A boolean true or false.
	 * @return False if bit is true, true if false
	 */
	public static boolean flip(boolean bit) {
		if(bit) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Resets the given binary to all false (0's)
	 * @param binary
	 */
	public static void resetToZero(boolean[] binary) {
		for (int i = 0; i < binary.length; i++) {
			binary[i] = false;
		}
	}
	
	/**
	 * Compares two binaries and returns an integer base on comparison.
	 * 0 if binary1 < binary2
	 * 1 if binary1 == binary2
	 * 2 if binary1 > binary2
	 * @param binary1
	 * @param binary2
	 * @return
	 */
	public static int compare(boolean[] binary1, boolean[] binary2) {
		for(int i = 15; i >= 0; i--) {
			if(binary1[i] == false && binary2[i] == true) {
				return 0;
			} else if(binary1[i] == true && binary2[i] == false) {
				return 2;
			}
		} 
		return 1;
	}

	/**
	 * Shifts the binary to x number of spaces.
	 * @param binary The array of boolean values to perform the shift.
	 * @param spaces The number of left shifts.
	 * @return The result of the shift.
	 */
	public static boolean[] getShiftLeft(boolean[] binary, int spaces) {
		boolean[] result = new boolean[16];
		for(int i = spaces; i < result.length; i++) {
			result[i] = binary[i - spaces];
		}
		return result;
	}
	
	/**
	 * Creats a String representation of the given boolean array.
	 * @param bytes The array of boolean values representing bytes.
	 * @return A string representation of 1's and 0's
	 */
	public static String getString(boolean[] bytes) {
		if(bytes.length == 0) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for(int i = bytes.length-1; i >= 0; i--) {
				if(bytes[i]) {
					sb.append('1');
				} else {
					sb.append('0');
				}
			}
			return sb.toString();
		}
	}

}
