/*
Desert Island Playlist
You’re heading to a desert island, cut off from the world, for the next six months. Luckily, you can bring a playlist of your favorite music ♪♪

Also, this is a chance to put your hard-earned Java skills to the test. Your mission, should you choose to accept it:

Build Playlist.java with the best possible playlist of music using a Java ArrayList. Choose wisely.
*/
import java.util.ArrayList;

class Playlist {
    public static void main(String[] args) {
        // Lets make our ArrayList
        ArrayList<String> desertIslandPlaylist = new ArrayList<String>();

        // Lets add some songs to our playlist
        desertIslandPlaylist.add("She came in through the bathroom window");
        desertIslandPlaylist.add("No one else");
        desertIslandPlaylist.add("Chameleon");
        desertIslandPlaylist.add("Slow jam");
        desertIslandPlaylist.add("I've got you");
        desertIslandPlaylist.add("Party at a rich dude's house");

        // Print our ArrayList
        System.out.println(desertIslandPlaylist);
        // Print how many items are in our ArrayList
        System.out.println(desertIslandPlaylist.size());

        // We can only have 5 songs! Lets remove one
        desertIslandPlaylist.remove(5);
        System.out.println(desertIslandPlaylist);

        // Lets swap two of our songs
        int a = desertIslandPlaylist.indexOf("No one else");
        int b = desertIslandPlaylist.indexOf("Chameleon");
        String tempA = desertIslandPlaylist.get(a);
        String tempB = desertIslandPlaylist.get(b);
        desertIslandPlaylist.set(a, tempB);
        desertIslandPlaylist.set(b, tempA);

        // Now lets see what it is like
        System.out.println(desertIslandPlaylist);
    }
}
