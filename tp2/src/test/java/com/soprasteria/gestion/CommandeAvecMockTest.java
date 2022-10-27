package com.soprasteria.gestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.soprasteria.paiement.IPaiement;
import com.soprasteria.paiement.RouteurPaiement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soprasteria.paiement.exception.PaiementException;
import com.soprasteria.panier.model.Article;
import com.soprasteria.panier.model.Panier;
import com.soprasteria.panier.model.exceptions.MontantTropEleveException;
import com.soprasteria.panier.model.exceptions.QuantiteArticleTropGrandeException;
import com.soprasteria.panier.model.exceptions.TropDeReferencesException;

@ExtendWith(MockitoExtension.class)
public class CommandeAvecMockTest {

	private static Client client;
	private static Panier pan;
	private static Article art1;
	@Mock
   private  static IPaiement iPaiement;
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
	public void testValiderPaiementMockInstance() {
		// TODO : instancier un mock de IPaiement et le configurer
		// afin que le test passe l'assertion
		IPaiement iPaiement=mock(IPaiement.class);
		when(iPaiement.transaction("ENSIM_COMMERCE","4444555551666666","01",
				"2017","345",549.9499999999999)).thenReturn(true);

		// preparation
		Commande commande = new Commande(/* TODO : params à renseigner */client, pan,iPaiement );

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification mock
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande

		verify(iPaiement).transaction("ENSIM_COMMERCE","4444555551666666","01",
				"2017","345",549.9499999999999);

		// vérification système testé
		assertThat(resultat).isTrue();
	}

	@Test
	public void testValiderPaiementMockAnnotation() {
		// TODO : configurer le mock par annotation (à définir) de IPaiement
		// afin que le test passe l'assertion
		when(iPaiement.transaction("ENSIM_COMMERCE","4444555551666666","01",
				"2017","345",549.9499999999999)).thenReturn(true);
		// preparation
		Commande commande = new Commande(/* params à renseigner */client, pan, iPaiement);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification mock
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande
		verify(iPaiement).transaction("ENSIM_COMMERCE","4444555551666666","01",
				"2017","345",549.9499999999999);
		// vérification système testé
		assertThat(resultat).isTrue();
	}

	@Test
	public void testValiderPaiementMockException() {
		// TODO : configurer le mock par annotation (à définir) de IPaiement
		// afin que le test passe l'assertion quel que soit les paramètres
		// d'appel de la dépendance
		when(iPaiement.transaction(ArgumentMatchers.any(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString(), AdditionalMatchers.eq(549.94,0.1))).thenThrow(PaiementException.class);
		// preparation
		Commande commande = new Commande(/* params à renseigner */client, pan, iPaiement);
		
		// verification
		assertThrows(PaiementException.class, () -> {			
			// execution
			commande.validerPaiement("4444555551666666", "01/2017", "345");
		});

	}

}
