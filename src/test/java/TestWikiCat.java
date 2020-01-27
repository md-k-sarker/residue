/*
Written by sarker.
Written at 1/25/20.
*/

import org.dase.residue.kgcreation.wikipedia.WikiCat;

public class TestWikiCat {

    public void testBeautifyName() {
        WikiCat wikiCat = new WikiCat();
        String testStr = " <>&;#,(){}!@#$%^&*()-{}`~[]<>;,.? Zam\\||a/n{}[]&&(){} `~!@#$%^&*()-{}[]|;'\"<>,.?+=";
        System.out.println(wikiCat.beautifyName(testStr));
//        assert "Zaman".equals(wikiCat.beautifyName(testStr));
    }

    public static void main(String [] args) {
        TestWikiCat testWikiCat = new TestWikiCat();
        testWikiCat.testBeautifyName();
    }
}
