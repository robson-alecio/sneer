package sneer.bricks.things.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

import sneer.bricks.things.Thing;
import sneer.bricks.things.ThingHome;

public class ThingHomeImpl implements ThingHome {

	private static final String TEXT_FIELD = "text";
	private final Directory _directory = new RAMDirectory();
	private final Analyzer _analyzer = new StandardAnalyzer();
	
	public ThingHomeImpl(){
		try {
			IndexWriter index = openIndex(true);
			index.close();
		} catch (CorruptIndexException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (LockObtainFailedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
		
	}
	
	private Collection<Thing> tryToFind(String tags) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {		
		Query query = new QueryParser(TEXT_FIELD, _analyzer)
			.parse(tags);
		
		IndexSearcher searcher = new IndexSearcher(_directory);
		Hits hits = searcher.search(query);
		
		Collection<Thing> result = new ArrayList<Thing>(hits.length());
		for (int i = 0; i < hits.length(); i++) {
			Document doc = hits.doc(i);
			String name = doc.getFields(TEXT_FIELD)[0].stringValue();
			String description = doc.getFields(TEXT_FIELD)[1].stringValue();
		
			ThingImpl foundThing = new ThingImpl(name, description);
			result.add(foundThing);
		}
		
		return result;
	}

	private void addToIndex(Thing thing)  {
		try {
			tryToAddToIndex(thing);
		} catch (CorruptIndexException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (LockObtainFailedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	private void tryToAddToIndex(Thing thing) throws CorruptIndexException, LockObtainFailedException, IOException {
		Document doc = new Document();
		doc.add(toField(thing.name()));
		doc.add(toField(thing.description()));

		IndexWriter index = openIndex(false);
		try {
			index.addDocument(doc);
			index.optimize();
		} finally {
			index.close();
		}
	}

	private Field toField(String string) {
		return new Field(TEXT_FIELD, string, Store.YES, Field.Index.TOKENIZED);
	}

	private IndexWriter openIndex(boolean createNew) throws CorruptIndexException, IOException {
		while (true) 
			try {
				return tryToOpenIndex(createNew);
			} catch (LockObtainFailedException e) {
				//Simply try again;
			}
	}

	private IndexWriter tryToOpenIndex(boolean createNew) throws CorruptIndexException, LockObtainFailedException, IOException {
		return new IndexWriter(_directory, new StandardAnalyzer(), createNew);
	}

	@Override
	public Collection<Thing> find(String tags) {
		try {
			return tryToFind(tags);
		} catch (CorruptIndexException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (LockObtainFailedException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		} catch (ParseException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}

	@Override
	public Thing create(String name, String description) {
		ThingImpl result = new ThingImpl(name, description);
		addToIndex(result);
		return result;
	}

}