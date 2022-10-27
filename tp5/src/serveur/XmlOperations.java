package serveur;

import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;

import common.XmlOperationsI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlOperations extends UnicastRemoteObject implements XmlOperationsI {
	//list d'objet client (on creer l'objet pour stocké les donnees d'un client existe dans le fichier xml)
     List<ClientObjet> Listclients;

	 public void remplirListClient() throws ParserConfigurationException, SAXException, IOException {
		 Listclients=new ArrayList<ClientObjet>();
		 //Obtenir la configuration du sax parser
		 SAXParserFactory spfactory = SAXParserFactory.newInstance();

		 //Obtenir une instance de l'objet parser
		 SAXParser saxParser = spfactory.newSAXParser();
		 /*les trois méthodes sont déclarées dans le
         corp du DefaltHandler*/
		 DefaultHandler handler = new DefaultHandler() {
			 boolean bpseudo = false;
			 boolean bmdp= false;


			 /*cette méthode est invoquée à chaque fois que parser rencontre
                       une balise ouvrante '<' */
			 public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException{
				 if (qName.equalsIgnoreCase("client")) {
					 Listclients.add(new ClientObjet());
				 }

				 if (qName.equalsIgnoreCase("pseudo")) {
					 bpseudo=true;
				 }

				 if (qName.equalsIgnoreCase("mdp")) {
					 bmdp = true;
				 }

			 }

		/*cette méthode est invoquée à chaque fois que parser rencontre
          une balise fermante '>' */

			 public void endElement(String uri, String localName,
									String qName) throws SAXException {

				 if (qName.equalsIgnoreCase("pseudo")) {
					 bpseudo = false;
				 }

				 if (qName.equalsIgnoreCase("mdp")) {
					 bmdp = false;
				 }
			 }
			 /*stocker les données stockées entre '<' et '>' */

			 public void characters(char ch[], int start,
									int length) throws SAXException {
				 String s = new String(ch, start, length);
				 if (bpseudo) {
					 Listclients.get(Listclients.size()-1).setPseudo(s);

				 }

				 if (bmdp) {
					 Listclients.get(Listclients.size()-1).setMtp(s);

				 }


			 }


		 };
		 saxParser.parse("..\\tp5\\src\\serveur\\clients.xml", handler);
		 System.out.println(Listclients.get(0).getPseudo());
	 }
	protected XmlOperations() throws ParserConfigurationException, IOException, SAXException {
		remplirListClient();
	}

	public Boolean pseudoExist(String pseudo) throws RemoteException {
		 //on test si le pseudo existe dans la liste des clients
		for(int i=0;i<Listclients.size();i++){
			if(Listclients.get(i).getPseudo().equals(pseudo)){
				return true;
			}
		}
		return  false;
	}
	public Boolean userExist(String pseudo, String mdp) throws RemoteException {

		for(int i=0;i<Listclients.size();i++){
			if(Listclients.get(i).getPseudo().equals(pseudo)){
				if(Listclients.get(i).getMtp().equals(mdp)){
					return true;
				}

			}
		}
		return false;
	}

	public String addUser(String pseudo, String mdp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		 if(!pseudoExist(pseudo)){
			 String file = "..\\tp5\\src\\serveur\\clients.xml";
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document doc = db.parse(file);

			 // Récupérer l'élément racine
			 Node clients = doc.getFirstChild();

			 //creer l'element client
			 Element client = doc.createElement("client");
			 Element pseudo1 = doc.createElement("pseudo");
			 pseudo1.appendChild(doc.createTextNode(pseudo));
			 Element mdp1 = doc.createElement("mdp");
			 mdp1.appendChild(doc.createTextNode(mdp));
			 client.appendChild(pseudo1);
			 client.appendChild(mdp1);
			 clients.appendChild(client);

		// écrire le contenu dans le fichier xml spécifié
			 TransformerFactory tf = TransformerFactory.newInstance();
			 Transformer transformer = tf.newTransformer();
			 DOMSource src = new DOMSource(doc);
			 StreamResult res = new StreamResult(new File(file));
			 transformer.transform(src, res);
			 remplirListClient();
			 return " Le client "+ pseudo +" est bien inséré ";
		 }
		 return " Le client "+ pseudo +" existe déja";

	}
	public String removeUser(String pseudo) throws RemoteException {
		 if(pseudoExist(pseudo)){
			 String xmlFile = "..\\tp5\\src\\serveur\\clients.xml";
			 try {
				 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				 DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				 Document document = documentBuilder.parse(xmlFile);
				 NodeList clients = document.getElementsByTagName("client");
				 //on cherche l'element client qui'a le pseudo qu'on a passé en parametre
				 for (int i = 0; i < clients.getLength(); i++) {
					 Element product = (Element) clients.item(i);
					 Element idTag = (Element) product.getElementsByTagName("pseudo").item(0);
					 if (idTag.getTextContent().equalsIgnoreCase(pseudo)) {
						 idTag.getParentNode().getParentNode().removeChild(clients.item(i));
						 break;
					 }
				 }
				 // écrire le contenu dans le fichier xml spécifié
				 TransformerFactory transformerFactory = TransformerFactory.newInstance();
				 Transformer transformer = transformerFactory.newTransformer();
				 transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				 DOMSource domSource = new DOMSource(document);
				 StreamResult streamResult = new StreamResult(xmlFile);
				 transformer.transform(domSource, streamResult);
					remplirListClient();
			 } catch (Exception e) {
				 System.err.println(e.getMessage());
			 }
			 return "Le client est bien supprimé";
		 }
		return "Le client n'existe pas ";
	}

}