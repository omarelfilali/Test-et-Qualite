package com.soprasteria.panier.model;

import static org.junit.Assert.*;

import org.junit.*;

import com.soprasteria.panier.model.Article;
import com.soprasteria.panier.model.Panier;
import com.soprasteria.panier.model.exceptions.ArticleInexistantException;
import com.soprasteria.panier.model.exceptions.MontantTropEleveException;
import com.soprasteria.panier.model.exceptions.QuantiteArticleTropGrandeException;
import com.soprasteria.panier.model.exceptions.TropDeReferencesException;
import com.soprasteria.tools.OutilsChaine;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe de tests unitaires de la classe Panier
 * <P>
 * Teste les fonctions de base non triviales de la classe Panier - Ajout
 * d'articles - Retrait d'un article - Changement de quantit� d'un article -
 * Vidage d'un panier - Impression du ticket - Gestion de la remise
 * <P>
 * Couvre les exigences : EXG_PAN_01, EXG_PAN_02, EXG_PAN_03
 * 
 * @author Olivier
 * @version 2.1
 */
public class TestPanier {
	/**
	 * Les donnees de test
	 */
	static Panier pan;
	static Article art1, art2, art3;
	static ArrayList<Article> liste;

	/**
	 * Initialisation des donnees de test avant l'ensemble des tests
	 */
	@BeforeClass
	public static void initTests() {
		pan = new Panier( );
		art1 = new Article( 100.00 , "REF001" , "LIBELLE01" , 9.99 );
		art2 = new Article( 100.00 , "REF002" , "LIBELLE02" , 10.00 );
		liste = new ArrayList<Article>( );
		for (int i = 0; i < 100000; i++)
			liste.add( new Article( 100.00 , "REF00" + i , "LIB" + i , 10.00 ) );
	}

	/**
	 * Remise a zero du panier avant chaque test
	 */
	@Before
	public void avantTest() {
		pan.vider( );
	}

	/**
	 * Test unitaire couvrant l'exigence EXG_PAN_01
	 */
	@Test
	public void testAjouterArticle() throws TropDeReferencesException,
			QuantiteArticleTropGrandeException, MontantTropEleveException, ArticleInexistantException {
		  pan.ajouterArticle(art1,1);
		  pan.ajouterArticle(art1,1);
		  assertEquals(pan.nbArticles(art1.lireReference()),2);
		  // tester retier article
		  pan.retirerArticle(art1.lireReference());
		assertEquals(pan.nbArticles(art1.lireReference()),0);

		pan.ajouterArticle(art1,2);
		assertEquals(pan.nbArticles(art1.lireReference()),2);
		pan.ajouterArticle(art2,3);
		assertEquals(pan.nbArticles(art2.lireReference()),3);

		pan.modifierQuantiteArticle(art1.lireReference(),1);
		pan.modifierQuantiteArticle(art2.lireReference(),1);
		assertEquals(pan.nbArticles(art1.lireReference()),1);
		assertEquals(pan.nbArticles(art2.lireReference()),1);


	}
	/**
	 * Test unitaire couvrant l'exigence EXG_PAN_02
	 */
	@Test
	public void testerViderPan() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		pan.ajouterArticle(art1,1);
		pan.ajouterArticle(art2,1);
		pan.vider();
		assertEquals(pan.nbReferences(),0);


	}





	/**
	 * Test unitaire couvrant l'exigence EXG_PAN_04
	 * @throws TropDeReferencesException 
	 */
	@Test(expected = TropDeReferencesException.class)
	public void testAjouterArticleEnTrop() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {

		for(int i=0;i<2;i++){
			pan.ajouterArticle(liste.get(i), 1);
		}
	}

	/**
	 * Test unitaire couvrant l'exigence EXG_PAN_05
	 * @throws TropDeReferencesException
	 */

	@Test(expected = QuantiteArticleTropGrandeException.class)
	public void testAjouterQuantiteEnTrop() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		pan.ajouterArticle(art1,11);

	}

		/**
		 * Test unitaire couvrant l'exigence EXG_PAN_06
		 * @throws TropDeReferencesException
		 */
		@Test(expected = MontantTropEleveException.class)
		public void testHTeEnTrop() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
			for(int i=0;i<9;i++){
				pan.ajouterArticle(liste.get(i), 9);
			}


		}


		/**
		 * Test unitaire couvrant l'exigence EXG_REMISE_01
		 */
		@Test
		public void testDERemise() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
			for(int i=0;i<2;i++){
				pan.ajouterArticle(liste.get(i),3);
			}
			pan.ecrireRemiseActive(true);
			pan.ecrirePourcentageRemise(10);
			pan.ecrireMontantSeuil(120);

			assertEquals(pan.montantRemise(),66,0.01);
		}

	/**
	 * Test unitaire couvrant l'exigence EXG_TICKET_01
	 */

	@Test
	public void testDeTICKET() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		for(int i=0;i<2;i++){
			pan.ajouterArticle(liste.get(i),3);
		}
		pan.ecrireRemiseActive(true);
		pan.ecrirePourcentageRemise(10);
		pan.ecrireMontantSeuil(120);
		String ticket = pan.lireTicket();
		Date laDate = new Date();
		String sDate = OutilsChaine.formatDateFrance.format(laDate);
		String sHeure = OutilsChaine.formatHeure.format(laDate);

		assertEquals(ticket,"------------------------------------\n" +
				"| LE BEAU PANIER                   |\n" +
				"| "+sDate+" - "+ sHeure  +"               |\n" +
				"------------------------------------\n" +
				"| LIBELLE      PU_HT   QTE     TTC |\n" +
				"|                                  |\n" +
				"| LIB1        100,00     3  330,00 |\n" +
				"| LIB0        100,00     3  330,00 |\n" +
				"|                                  |\n" +
				"------------------------------------\n" +
				"| TOTAL                     660,00 |\n" +
				"------------------------------------\n" +
				"| REMISE 10,00%              66,00 |\n" +
				"------------------------------------\n" +
				"| TVA                        60,00 |\n" +
				"------------------------------------\n" +
				"| NET A PAYER               594,00 |\n" +
				"------------------------------------\n" +
				"| Les prix s'entendent en Euro     |\n" +
				"------------------------------------");


	}


}
