/*
Written by sarker.
Written at 1/25/20.
*/

import org.dase.residue.kgcreation.wikipedia.WikiCatsCyclic;

public class TestWikiCat {

    public void testBeautifyName() {
        WikiCatsCyclic wikiCatsCyclic = new WikiCatsCyclic();
        String testStr = " <>&;#,(){}!@#$%^&*()-{}`~[]<>;,.? Zam\\||a/n{}[]&&(){} `~!@#$%^&*()-{}[]|;'\"<>,.?+=";
        System.out.println(wikiCatsCyclic.beautifyName(testStr));
//        assert "Zaman".equals(wikiCatsCyclic.beautifyName(testStr));
    }

    public static void main(String [] args) {
        TestWikiCat testWikiCat = new TestWikiCat();
        testWikiCat.testBeautifyName();
    }
}
