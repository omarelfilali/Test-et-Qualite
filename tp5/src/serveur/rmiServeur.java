package serveur;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmiServeur {

	public static void main(String[] args) {

		try {
			XmlOperations xmlOD = new XmlOperations();
			Registry reg = LocateRegistry.createRegistry(2019);
			reg.bind("xmlOP", xmlOD);
			System.out.println("C'est bon " + xmlOD);
		} catch (Exception e) {
			System.out.println("ERREUR");
			e.printStackTrace();
		}
	}

}