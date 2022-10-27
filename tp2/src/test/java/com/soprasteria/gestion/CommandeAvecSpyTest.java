package com.soprasteria.gestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.soprasteria.paiement.IPaiement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soprasteria.paiement.RouteurPaiement;
import com.soprasteria.panier.model.Article;
import com.soprasteria.panier.model.Panier;
import com.soprasteria.panier.model.exceptions.MontantTropEleveException;
import com.soprasteria.panier.model.exceptions.QuantiteArticleTropGrandeException;
import com.soprasteria.panier.model.exceptions.TropDeReferencesException;

@ExtendWith(MockitoExtension.class)
public class CommandeAvecSpyTest {

	private static Client client;
	private static Panier pan;
	private static Article art1;
	@Spy
	private static RouteurPaiement routeurPaiement=new RouteurPaiement();
	@BeforeAll
	public static void init()
			throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		// Panier
		pan = new Panier();
		art1 = new Article(100.00, "REF001", "LIBELLE01", 9.99);
		pan.ajouterArticle(art1, 5);
		client = new Client();
	}

	@Test
	public void testValiderPaiementSpyInstance() {
		// TODO : instancier un spy de RouteurPaiement
		RouteurPaiement routeurPaiement= Mockito.spy(new RouteurPaiement());
		// preparation
		Commande commande = new Commande(/* TODO : params à renseigner */client, pan, routeurPaiement);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification spy
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande

		verify(routeurPaiement).transaction("ENSIM_COMMERCE","4444555551666666","01","2017","345",549.9499999999999);

		// vérification système testé
		assertThat(resultat).isTrue();
	}

	@Test
	public void testValiderPaiementSpyAnnotation() {
		// TODO : instancier un spy par annotation (à définir) de
		// RouteurPaiement


		// preparation
		Commande commande = new Commande(/* params à renseigner */client, pan, routeurPaiement);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification spy
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande

		verify(routeurPaiement).transaction("ENSIM_COMMERCE","4444555551666666","01","2017","345",549.9499999999999 );

		// TODO : vérifier que la méthode ping n'a pas été invoqué.
		//verify(/*spy(new RouteurPaiement())*/, times(1)).ping();
         verify(routeurPaiement,times(0)).ping();
		// vérification système testé
		assertThat(resultat).isTrue();
	}

}
