package test;
import com.sun.source.tree.AssertTree;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import work.Barriere;
import work.Controlleur;
import work.Feu;
import work.FeuColor;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class controllerTest {
    Controlleur controlleur;
    Barriere barriere;
    Feu feu;
    boolean leave=false;
    boolean in=false;
    boolean approche=false;
    boolean error = false;

    {
        feu = new Feu();
        barriere = new Barriere();
        controlleur = new Controlleur(feu, barriere);
        Timer timer;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                error= controlleur.traitement(leave,in,approche);
            }
        }, 1000, 1000);
    }

    @Before
    public void setUp() throws InterruptedException {
        leave=false;
        approche=false;
        in = false;
        Thread.sleep(5000);
    }
    /**
     * •	EXIG 2 : Le feu doit être au rouge chaque fois un train est en position In
     * 1: !leave + In + ! approche
     * 2: leave + In + ! approche
     * 3: !leave + In +  approche
     * 4: leave + In + approche
     */
    @Test
    public void testEXIG2() throws InterruptedException {
        in =true;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ROUGE);

        leave = true;
        approche =false;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ROUGE);

        leave = false;
        approche =true;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ROUGE);

        leave = true;
        approche =true;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ROUGE);



        //      assertTrue(feu.getState()== FeuColor.ROUGE);
    }

    //	EXIG 3 : Le feu doit être à l’orange pour 2 secondes chaque fois un train est en position Approach
    @Test
    public void testEXIG3() throws InterruptedException {

        leave=false;
        approche=true;
        in = false;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ORANGE);
        Thread.sleep(800);
        assertTrue(feu.getState()==FeuColor.ORANGE);
    }

    //	EXIG 4 : A chaque fois après le passage à l’orange pendant 2 secondes le feu passe au rouge quand un train est en position Approach
    @Test
    public void testEXIG4() throws InterruptedException {
        leave=false;
        approche=true;
        in = false;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ORANGE);
        Thread.sleep(2100);
        assertTrue(feu.getState()==FeuColor.ROUGE);
    }

    //	•	EXIG 5 : Dès que le feu est au rouge les barrières s’abaissent pendant 5 secondes chaque fois un train est en position Approach
    @Test
    public void testEXIG5() throws InterruptedException {
        leave=false;
        approche=true;
        in = true;
        Thread.sleep(1100);
        assertTrue(feu.getState()==FeuColor.ROUGE);
        Thread.sleep(5500);
        assertTrue(barriere.isEnBas());
    }

    //	•	EXIG 6 : Après avoir fait baisser les barrières, s’il n’y a pas de signal d’erreur ou s’il n’y a pas un autre train en position Approach, les barrières restent baissées tant que le train est en position In
    @Test
    public void testEXIG6() throws InterruptedException {
        leave=false;
        approche=false;
        in = true;
        Thread.sleep(5000);
        assertTrue(barriere.isEnBas());

        Thread.sleep(1000);
        assertTrue(barriere.isEnBas());
        Thread.sleep(3000);
        assertTrue(barriere.isEnBas());
    }

    //	•	EXIG 7 : Après avoir fait baisser les barrières, s’il n’y a pas de signal d’erreur ou s’il n’y a pas un autre train en position Approach, les barrières restent baissées tant que le train est en position In
    @Test
    public void testEXIG7() throws InterruptedException {
        leave = false;
        approche = false;
        in = true;
        Thread.sleep(1000);
        if(!barriere.isEnBas())
            assertTrue(error);
    }

    //todo
    //	•	EXIG 8 : Après avoir fait baisser les barrières, s’il n’y a pas de signal d’erreur ou s’il n’y a pas un autre train en position Approach, les barrières restent baissées tant que le train est en position In
    @Test
    public void testEXIG8() throws InterruptedException {
        leave = false;
        approche = false;
        in = true;
        Thread.sleep(1000);
        if(!barriere.isEnBas())
            assertTrue(error);
    }



    //	•	EXIG 9 : Les barrières sont relevées pendant 5 secondes et puis le feu passe au vert chaque fois le train est en position Leave
    @Test
    public void testEXIG9() throws InterruptedException {
        leave = true;
        approche = false;
        in = false;
        Thread.sleep(5000);
        assertTrue(feu.getState()==FeuColor.VERT);

    }


    //	•	EXIG 10 : Il y a un capteur de position sur les barrières pour indiquer si elles sont en bas ou en haut ou entre.
    @Test
    public void testEXIG10() throws InterruptedException {
        //no need to test

    }


    //	•	EXIG 11 : Les barrières sont en haut quand le feu est au vert
    @Test
    public void testEXIG11() throws InterruptedException {
        if(barriere.isEnHaut())
            assertTrue(FeuColor.VERT==feu.getState());
        leave = true;
        approche = false;
        in = false;
        Thread.sleep(1000);
        if(barriere.isEnHaut())
            assertTrue(FeuColor.VERT==feu.getState());
        leave = false;
        approche = true;
        in = false;
        Thread.sleep(1000);
        if(barriere.isEnHaut())
            assertTrue(FeuColor.VERT==feu.getState());
        leave = false;
        approche = false;
        in = false;
        Thread.sleep(1000);
        if(barriere.isEnHaut())
            assertTrue(FeuColor.VERT==feu.getState());

    }


    //	EXIG 12 : Les barrières sont en bas quand le feu est au rouge
    @Test
    public void testEXIG12() throws InterruptedException {
        if(FeuColor.ROUGE==feu.getState())
            assertTrue(barriere.isEnBas());
        leave = true;
        approche = false;
        in = false;
        Thread.sleep(1000);
        if(FeuColor.ROUGE==feu.getState())
            assertTrue(barriere.isEnBas());
        leave = false;
        approche = true;
        in = false;
        Thread.sleep(1000);
        if(FeuColor.ROUGE==feu.getState())
            assertTrue(barriere.isEnBas());
        leave = false;
        approche = false;
        in = false;
        Thread.sleep(1000);
        if(FeuColor.ROUGE==feu.getState())
            assertTrue(barriere.isEnBas());
    }

    //todo
    //	•	EXIG13 Si un train est en position Leave et à ce moment-là il y a un second train à la position Approach, les barrières s’abaissent de toute manière pour atteindre la position d’en bas.
    @Test
    public void testEXIG13() throws InterruptedException {
        leave = true;
        approche = true;
        in = false;
        Thread.sleep(7100);
        assertTrue(barriere.isEnBas());
    }






}
