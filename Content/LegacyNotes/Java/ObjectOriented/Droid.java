// I could maybe create a second instance and make a tranfer method that exchanges batteries between them to show how more in depth how classes and methods work.

public class Droid {
    int batteryLevel;
    String name;

    public Droid(String droidName) {
        batteryLevel = 100;
        name = droidName;
    }

    public void performTask(String task) {
        System.out.println(name + " is performing task: " + task);
        batteryLevel -= 10;
    }

    public String energyReport() {
        return "Battery is at: " + batteryLevel;
    }

    public static void main(String[] args) {
        Droid myDroid = new Droid("Codey");
        System.out.println(myDroid);
        System.out.println(myDroid.energyReport());
        myDroid.performTask("dancing");
        System.out.println(myDroid.energyReport());
    }

    public String toString() {
        return "Hello, I'm the droid: " + name;
    }
}
