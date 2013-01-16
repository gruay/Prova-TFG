package com.david;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String C_USUARIS = "users";
	private static String C_DOCS = "docs";
	private static String A_MOTDEPAS = "motDePas";
	private static String A_USUARI = "nomDUsuari";
	private static String A_DOC = "nomDoc";
	private static String DB = "TFG";
	private static DB db;
	private static BufferedReader br;
	private static boolean connectat;
	private static String nomUsuari;
	
    public static void main( String[] args )
    {
    	MongoClient m;
		try {
			connectat = false;
			m = new MongoClient();
	    	db = m.getDB(DB);
	    	int opt = 0;
		    br = new BufferedReader(new InputStreamReader(System.in));
	    	while (opt != 999) {
	    		if (!connectat) {
		    		System.out.println("Què vols fer? \n\t1.Registrar-te\n\t2.Entrar\n\t3.Llistar Usuaris\n\t4.Tancar");
		    		opt = Integer.parseInt(br.readLine());
		    		switch (opt) {
					case 1:
						registrarUsuari();
						break;
					case 2:
						login();
						break;
					case 3:
						llistaUsers();
						break;
					case 4:
						opt = 999;
						break;
					default:
						System.out.println("Introdueix un número entre 1 i 4");
						break;
					}
	    		}
	    		else {
		    		System.out.println("Hola " + nomUsuari + ", què vols fer? \n\t1.Registrar un document\n\t2.Eliminar un document\n\t3.Llistar els teus documents\n\t4.Sortir");
		    		opt = Integer.parseInt(br.readLine());
		    		switch (opt) {
					case 1:
						registrarDoc();
						break;
					case 2:
						eliminarDoc();
						break;
					case 3:
						llistaDocs();
						break;
					case 4:
						connectat = false;
						break;
					default:
						System.out.println("Introdueix un número entre 1 i 4");
						break;
					}	    			
	    		}
	    	}
	    	br.close();
	    } catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

	private static void llistaDocs() {
		DBCollection coll = db.getCollection(C_DOCS);
		BasicDBObject q = new BasicDBObject(A_USUARI, nomUsuari);
		DBCursor cursor = coll.find(q);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		cursor.close();
	}

	private static void eliminarDoc() throws IOException {
		llistaDocs();
		int primer = 0;
		String doc = new String();
		DBCollection coll = db.getCollection(C_DOCS);
		BasicDBObject q = new BasicDBObject();
		do {
			if (primer == 0) {
				System.out.println("Introdueix el nom del document que vols eliminar");
				primer = 1;
			}
			else System.out.println("Aquest document no és teu o no existeix, introdueix el nom del document");	
			doc = br.readLine();
			q = new BasicDBObject(A_USUARI, nomUsuari).append(A_DOC, doc);
		} while (!coll.find(q).hasNext());
		coll.remove(q);
		System.out.println("S'ha esborrat correctament");		
	}

	private static void registrarDoc() throws IOException {
		int primer = 0;
		String doc = new String();
		DBCollection coll = db.getCollection(C_DOCS);
		do {
			if (primer == 0) {
				System.out.println("Introdueix el nom del document");
				primer = 1;
			}
			else System.out.println("Introdueix algun document");	
			doc = br.readLine();
		} while (doc.isEmpty()) ;
		coll.insert(new BasicDBObject(A_USUARI, nomUsuari).append(A_DOC, doc));
		System.out.println("Inserció completada");
	}

	private static void llistaUsers() {
		DBCollection coll = db.getCollection(C_USUARIS);
		DBCursor cursor = coll.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		cursor.close();
	}

	private static void login() throws IOException {
		String nom = new String();
		String mdp = new String();
		DBCollection coll = db.getCollection(C_USUARIS);
		BasicDBObject q = new BasicDBObject();
		int primer = 0;
		do {
			if (primer == 0) {
				System.out.println("Introdueix l'usuari i el mot de pas un a cada línia (no introdueixis nom d'usuari per sortir)");
				primer = 1;
			}
			else System.out.println("Combinació incorrecta, introdueix-ho de nou. (Cada cosa en una línia) (no introdueixis nom d'usuari per sortir)");
			nom = br.readLine();
			if (nom.isEmpty()) break;
			mdp = br.readLine();
			q = new BasicDBObject(A_USUARI, nom).append(A_MOTDEPAS, mdp);
		} while (!coll.find(q).hasNext());
		if (!nom.isEmpty()) {
			System.out.println("T'has connectat correctament");
			nomUsuari = nom;
			connectat = true;
		}
	}

	private static void registrarUsuari() throws IOException {
		int primer = 0;
		String nom = new String();
		DBCollection coll = db.getCollection(C_USUARIS);
		BasicDBObject q = new BasicDBObject();
		do {
			if (primer == 0) {
				System.out.println("Introdueix un nom d'usuari");
				primer = 1;
			}
			else System.out.println("Aquest nom ja està agafat, si us plau, introdueix-ne un altre");	
			nom = br.readLine();
			q = new BasicDBObject(A_USUARI, nom);
		} while (coll.find(q).hasNext()) ;
		System.out.println("Introdueix el mot de pas");
		String mdp = br.readLine();
		coll.insert(new BasicDBObject(A_USUARI, nom).append(A_MOTDEPAS, mdp));
		System.out.println("Inserció completada");
		nomUsuari = nom;
		connectat = true;
	}
}
