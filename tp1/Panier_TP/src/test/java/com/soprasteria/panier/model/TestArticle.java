package com.soprasteria.panier.model;

import static org.junit.Assert.*;

import org.junit.*;

import com.soprasteria.panier.model.Article;

/**
 * Classe de tests unitaires de la classe Article
 * <P>
 * Teste les fonctions de base non triviales de la classe Article - Le calcul de
 * prix TTC
 * <P>
 * 
 * @author Olivier
 * @version 2.0
 */
public class TestArticle {
	private static Article art;

	@BeforeClass
	public static void initTests() {
		art = new Article( 100.00 , "ref1" , "lib1" , 19.6 );
	}

	/**
	 * Test unitaire couvrant les exigences EXG_ART_01 , EXG_ART_02
	 */
	@Test
	public void testPrixTTCArticle() {
	}

	/*EXG_ART_01
	Le système devra permettre de créer des articles*/
	@Test
	public void testCreatArticl(){
		assertNotEquals(art,null);
	}

	/*EXG_ART_02
     Un article est un « objet » informatique possédant un libellé,
     un prix unitaire HORS TAXE et un taux de TVA applicable.
	*/

	@Test
	public void test2(){
		assertEquals(art.lireLibelle(),"lib1");
		assertEquals(art.lirePrixHT(),100.00,2);
		assertEquals(art.lireReference(),"ref1");
		assertEquals(art.lireTva(),19.6,0.001);
	}





}
