/*
Specific conditional statements have the following behavior:

if-then:
code block runs if condition is true
if-then-else:
one block runs if conditions is true
another block runs if condition is false
if-then-else chained:
same as if-then but an arbitrary number of conditions
switch:
switch block runs if condition value matches case value
 */
public class Order {
    boolean isFilled;
    double billAmount;
    String shipping;

    public Order(boolean filled, double cost, String shippingMethod) {
        if (cost > 24.00) {
            System.out.println("High value item!");
        } else {
            System.out.println("Low value item!");
        }
        isFilled = filled;
        billAmount = cost;
        shipping = shippingMethod;
    }

    public void ship() {
        if (isFilled) {
            System.out.println("Shipping");
        } else {
            System.out.println("Order not ready");
        }

        double shippingCost = calculateShipping();

        System.out.println("Shipping cost: ");
        System.out.println(shippingCost);
    }

    public double calculateShipping() {
        double shippingCost;
        switch (shipping) {
        case "Regular":
            shippingCost = 0;
            break;
        case "Express":
            shippingCost = 1.75;
            break;
        default:
            shippingCost = .50;
        }
        return shippingCost;
    }

    public static void main(String[] args) {
        // create instances and call methods here!
    }
}
