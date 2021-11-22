/*
DNA Sequencing
The genome of an organism stores all the genetic information necessary to build and maintain that organism. It is an organism’s complete set of DNA.

DNA is composed of a series of nucleotides abbreviated as:

A: Adenine
C: Cytosine
G: Guanine
T: Thymine

So a strand of DNA could look something like ACGAATTCCG.

Write a DNA.java program that determines whether there is a protein in a strand of DNA.

A protein has the following qualities:

It begins with a “start codon”: ATG.
It ends with a “stop codon”: TGA.
In between, each additional codon is a sequence of three nucleotides.
So for example:

ATGCGATACTGA is a protein because it has the start codon ATG, the stop codon TGA, and the length is divisible by 3.
ATGCGATAGA is not a protein because the sequence length is not divisible by 3, so the third condition is not satisfied.
*/

public class DNA {
    public boolean isProtein(String dna) {
        System.out.println("String provided: " + dna);
        int startCodon = dna.indexOf("ATG");
        int stopCodon = dna.indexOf("TGA");

        if (startCodon == -1) {
          System.out.println("Protein does not have a start codon: ATG!");
          return false;
        } else if (stopCodon == -1) {
          System.out.println("Protein does not have a stop codon: TGA!");
          return false;
        } else if ((stopCodon - startCodon) % 3 != 0) {
          System.out.println("Protein is not divisible by 3!");
          return false;
        }

        System.out.println(dna + " is a protein!");
        return true;
    }

    public static void main(String[] args) {
        //  -. .-.   .-. .-.   .
        //    \   \ /   \   \ / 
        //   / \   \   / \   \  
        //  ~   `-~ `-`   `-~ `-
        DNA dna = new DNA();
        String dna1 = "ATGCGATACGCTTGA";
        String dna2 = "ATGCGATACGTGA";
        String dna3 = "ATTAATATGTACTGA";
        String dna4 = "ACGAATTCCG";

        dna.isProtein(dna1);
        System.out.println("");
        dna.isProtein(dna2);
        System.out.println("");
        dna.isProtein(dna3);
        System.out.println("");
        dna.isProtein(dna4);
        System.out.println("");
    }
}
