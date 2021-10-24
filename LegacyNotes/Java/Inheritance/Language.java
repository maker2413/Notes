/*
Language Families
Languages, just like people, often have families. You can even map language lineage a bit like a family tree.

Java can help us build a model to track inherited traits across families. In this case, we’ll focus on something that often varies between language families: word order — where the subject, verb, and object would go in a sentence.

For example, in English, you would use subject-verb-object order:

subject verb object
She     eats noodles

But in Japanese, you would use subject-object-verb order:

subject object    verb
(she)   (noodles) (eats)

Your Language Inheritance package has three Java files:

Language.java: for the Language parent class, which serves as the template for all languages.
Mayan.java: for Mayan, a child class of Language modeled after the Mayan language family.
SinoTibetan.java: for SinoTibetan, a child class of Language modeled after the Sino-Tibetan language family.

Build out a Java package with these classes to model real-world language families.
*/

class Language {
    protected String name;
    protected int numSpeakers;
    protected String regionsSpoken;
    protected String wordOrder;

    Language(String name, int numSpeakers, String regionsSpoken, String wordOrder) {
        this.name = name;
        this.numSpeakers = numSpeakers;
        this.regionsSpoken = regionsSpoken;
        this.wordOrder = wordOrder;
    }

    public void getInfo() {
        System.out.println(this.name + " is spoken by " + this.numSpeakers + " people mainly in " + this.regionsSpoken + ".");
        System.out.println("The language follows the word order: " + this.wordOrder + ".");
    }

    public static void main(String args[]){
        Language spanish = new Language("Spanish", 555000000, "Spain, Latin America, and Equatorial Guinea", "verb-object");
        Mayan kiche = new Mayan("Ki'che", 2330000);
        SinoTibetan mandarin = new SinoTibetan("Mandarin Chinese", 1100000000);
        SinoTibetan burmese = new SinoTibetan("Burmese", 32000000);

        spanish.getInfo();
        System.out.println("");
        kiche.getInfo();
        System.out.println("");
        mandarin.getInfo();
        System.out.println("");
        burmese.getInfo();
    }
}
