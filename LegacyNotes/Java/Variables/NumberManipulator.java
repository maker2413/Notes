public class NumberManipulator {
    public static void main(String[] args) {
        // This is our original number so we are making it static
        final int inputNumber = 6;

        // This is the variable we are going to end with
        // Lets make this static as well to prove we aren't cheating
        final int outputNumber = 3;

        // Print out numbers
        System.out.println("Input number was: " + inputNumber);
        System.out.println("Output will be: " + outputNumber);

        // Set our mathNumber to inputNumber
        int mathNumber = inputNumber;

        // Now lets do some mathmatical manipulation on our outputNumber
        System.out.println("Now doing math on input number...");
        mathNumber *= inputNumber;
        mathNumber += inputNumber;
        mathNumber /= inputNumber;
        mathNumber += 17;
        mathNumber -= inputNumber;
        mathNumber /= 6;

        // Print out mathNumber
        System.out.println("Input number is now: " + mathNumber);

        // Check if they were equal
        boolean same = mathNumber == outputNumber;
        System.out.println("Input number is now equal to Output number: " + same);
    }
}
