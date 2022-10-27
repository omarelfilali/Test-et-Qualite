package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import common.XmlOperationsI;




public class rmiClient {

	public static void main(String[] args) {

		try {

			Registry reg = LocateRegistry.getRegistry("localhost", 2019);
			XmlOperationsI xmlOD = (XmlOperationsI) reg.lookup("xmlOP");
			System.out.println("OD=" + xmlOD);
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			String str="";
			while(true) {
				System.out.println("Entrez la requete : ");
				str = inFromUser.readLine();
				if(str.startsWith("P_EXIST")) {
					String pseudo=str.replaceAll("P_EXIST", "");
					pseudo=pseudo.replaceAll(" ", "");
					if(xmlOD.pseudoExist(pseudo)){
						System.out.println("pseudo existe");
					}
					else{
						System.out.println("pseudo n'existe pas");
					}
				}
				if(str.startsWith("U_EXIST")){
					final String SEPARATEUR = " ";
					String mots[] = str.split(SEPARATEUR);
					if(xmlOD.userExist(mots[1],mots[2])){
						System.out.println("L’accès est accepté");
					}else{
						System.out.println("L’accès est refusé, le pseudo et/ou le mot de passe sont incorrects");
					}
				}
				if(str.startsWith("ADD")){
					final String SEPARATEUR = " ";
					String mots[] = str.split(SEPARATEUR);
						System.out.println(xmlOD.addUser(mots[1],mots[2]));

				}
				if(str.startsWith("REMOVE")){
					final String SEPARATEUR = " ";
					String mots[] = str.split(SEPARATEUR);
					System.out.println(xmlOD.removeUser(mots[1]));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}