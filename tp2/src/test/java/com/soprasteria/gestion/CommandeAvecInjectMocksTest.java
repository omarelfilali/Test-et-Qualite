package com.soprasteria.gestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soprasteria.paiement.RouteurPaiement;
import com.soprasteria.panier.model.Panier;

@ExtendWith(MockitoExtension.class)
public class CommandeAvecInjectMocksTest {

	// TODO : Créer un mock par annotation pour Client, Panier et
	// RouteurPaiement.
	@Mock
	Client client;
	@Mock
	Panier pan;
	@Mock
	RouteurPaiement routeurPaiement;


	// TODO : Injecter les mocks créés dans commande avec une annotation
	@InjectMocks
	private Commande commande;


	@Test
	public void testValiderPaiement() {
		// TODO : configurer le mock de RouteurPaiement afin que le test passe
		// l'assertion
		when(routeurPaiement.transaction("ENSIM_COMMERCE", "4444555551666666","01",
				"2017", "345", 0)).thenReturn(true);
		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// vérification système testé
		assertThat(resultat).isTrue();
	}

}
