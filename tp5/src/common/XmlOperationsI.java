package common;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface XmlOperationsI extends Remote{

	public Boolean pseudoExist(String pseudo) throws RemoteException;
	public Boolean userExist(String pseudo, String mdp) throws RemoteException;
	public String addUser(String pseudo, String mdp) throws IOException, ParserConfigurationException, SAXException, TransformerException;
	public String removeUser(String pseudo) throws RemoteException;
}
