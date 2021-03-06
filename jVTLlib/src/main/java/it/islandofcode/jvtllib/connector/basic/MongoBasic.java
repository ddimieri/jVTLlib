package it.islandofcode.jvtllib.connector.basic;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import it.islandofcode.jvtllib.connector.IConnector;
import it.islandofcode.jvtllib.model.DataPoint;
import it.islandofcode.jvtllib.model.DataSet;
import it.islandofcode.jvtllib.model.DataStructure;
import it.islandofcode.jvtllib.model.Scalar;
import it.islandofcode.jvtllib.model.Scalar.SCALARTYPE;
import it.islandofcode.jvtllib.model.VTLObj;
import it.islandofcode.jvtllib.model.ValueDomain;
import it.islandofcode.jvtllib.model.util.Component;

/**
 * @author Pier Riccardo Monzo
 */
public class MongoBasic implements IConnector {
	private MongoClient MC;
	private String database;
	private String table;

	/*
	 * Si usa uniVocity per il parsing da csv
	 * https://github.com/uniVocity/univocity-parsers
	 */

	public MongoBasic(String IP, int port, String db) {
		String URI = "mongodb://";
		// se IP/porta non specificato o fuori specifica, vai di default
		if (IP == null || IP.isEmpty())
			URI += "127.0.0.1";
		else
			URI += IP;
		URI += ":";
		if (port <= 0 || port > 65535)
			URI += 27017;
		else
			URI += port;

		// mongodb://host1:27017

		MC = MongoClients.create(URI);
		// MC = new MongoClient(IP,port);
		this.database = db;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.islandofcode.jvtllib.connector.IConnector#get(java.lang.String)
	 */
	@Override
	public DataSet get(String location, String[] keep) {
		this.table = location;
		DataSet ds = null;

		MongoDatabase db = MC.getDatabase(this.database);
		MongoCollection<Document> table = db.getCollection(this.table);

		// creiamo qui in DataStructure
		Document first = table.find().first();
		if (first == null) {
			return null; // se ne occupa l'eccezione in NewEval
		}
		DataStructure dstr = new DataStructure(location + "_dstr");
		for (String K : first.keySet()) {
			if ("_id".equals(K))
				continue;
			if (keep != null) {
				for (int i = 0; i < keep.length; i++) {
					if (keep[i].equals(K)) {
						Component C = this.retrive(K);
						if (C == null)
							return null; // se non ho trovato la colonna o non sono riuscito a generarla, torno null.
						dstr.putComponent(C.getId(), C.getDataType(), C.getType());
						break;
					}
				} // fine for su keep
			} else {
				Component C = this.retrive(K);
				if (C == null)
					return null; // se non ho trovato la colonna o non sono riuscito a generarla, torno null.
				dstr.putComponent(C.getId(), C.getDataType(), C.getType());
			}

		} // fine for su first

		ds = new DataSet(location, this.database, dstr, true);
		for (Document D : table.find()) {
			DataPoint dp = new DataPoint();
			for (String I : D.keySet()) {
				if ("_id".equals(I))
					continue;
				VTLObj r = retrive(I).getDataType();
				SCALARTYPE s;
				if (r.getObjType() == VTLObj.OBJTYPE.Scalar) {
					s = ((Scalar) r).getScalarType();
				} else {
					s = ((ValueDomain) r).getScalarType();
				}
				dp.setValue(I, new Scalar(String.valueOf(D.get(I)), s));
			}
			ds.setPoint(dp);
		}

		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.islandofcode.jvtllib.connector.IConnector#set(java.lang.String,
	 * it.islandofcode.jvtllib.model.DataSet)
	 */
	@Override
	public boolean put(String location, DataSet data) {

		return false;
	}

	private final Component retrive(String key) {
		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setHeaderExtractionEnabled(true); // ignora il primo rigo (Header)
		CsvParser p = new CsvParser(parserSettings);
		Component c = null;
		try {
			p.beginParsing(new InputStreamReader(this.getClass().getResourceAsStream("/BIRDmap.csv"), "UTF-8"));
			String[] row;
			VTLObj obtype = null;
			DataStructure.ROLE attr;
			while ((row = p.parseNext()) != null) {
				if (!row[0].equals(key)) {
					continue;
				}
				switch (row[4]) {
				case "string":
					obtype = new Scalar(Scalar.SCALARTYPE.String);
					break;
				case "float":
					obtype = new Scalar(Scalar.SCALARTYPE.Float);
					break;
				case "integer":
					obtype = new Scalar(Scalar.SCALARTYPE.Integer);
					break;
				case "boolean":
					obtype = new Scalar(Scalar.SCALARTYPE.Boolean);
					break;
				case "date":
					obtype = new Scalar(Scalar.SCALARTYPE.Date);
					break;
				default:
					p.stopParsing();
					return null;
				}

				switch (row[1]) {
				case "D":
					attr = DataStructure.ROLE.Identifier;
					break;
				case "O":
					attr = DataStructure.ROLE.Measure;
					break;
				case "A":
					attr = DataStructure.ROLE.Attribute;
					break;
				default:
					p.stopParsing();
					return null;
				}

				c = new Component(row[0], obtype, attr);
			}
		} catch (UnsupportedEncodingException e) {
			p.stopParsing();
			return null;
		}
		p.stopParsing();
		return c;
	}

}
