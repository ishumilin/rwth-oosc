package oosc.assignment01.reuse;

/**
 * Reuse ROI calculator based on relative cost of reuse and
 * relative cost of writing for reuse.
 */
public class ReuseRoiCalculator {
    private final double relativeCostReuse;
    private final double relativeCostWriteForReuse;
    private final double reusedKloc;
    private final double newKloc;
    private final double costPerLoc;

    public ReuseRoiCalculator(double relativeCostReuse,
                              double relativeCostWriteForReuse,
                              double reusedKloc,
                              double newKloc,
                              double costPerLoc) {
        this.relativeCostReuse = relativeCostReuse;
        this.relativeCostWriteForReuse = relativeCostWriteForReuse;
        this.reusedKloc = reusedKloc;
        this.newKloc = newKloc;
        this.costPerLoc = costPerLoc;
    }

    public double baselineCost() {
        return (reusedKloc + newKloc) * 1000.0 * costPerLoc;
    }

    public double reuseCost() {
        double reusePart = reusedKloc * 1000.0 * costPerLoc * relativeCostReuse;
        double newPart = newKloc * 1000.0 * costPerLoc * relativeCostWriteForReuse;
        return reusePart + newPart;
    }

    public double roi() {
        return (baselineCost() - reuseCost()) / reuseCost();
    }

    public static void main(String[] args) {
        ReuseRoiCalculator calc = new ReuseRoiCalculator(0.2, 1.7, 55, 4, 100);
        System.out.println("Baseline cost: " + calc.baselineCost());
        System.out.println("Reuse cost: " + calc.reuseCost());
        System.out.println("ROI: " + calc.roi());
    }
}