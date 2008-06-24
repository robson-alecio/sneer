package spikes.danielsantos.things;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.LockObtainFailedException;

public class Finder {

	private static final String TEXT_FIELD = "text";
	private static final String INDEX_DIRECTORY = "tmp";
	private IndexWriter _index;

	public Collection<Thing> find(Collection<Thing> things, String tags) {
		try {
			return tryToFind(things, tags);
		} catch (Exception e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement
																	// Handle
																	// this
																	// exception.
		}
	}

	private Collection<Thing> tryToFind(Collection<Thing> things, String tags)
			throws CorruptIndexException, LockObtainFailedException,
			IOException, ParseException {
		createNewIndex();

		for (Thing thing : things)
			addToIndex(thing);
		_index.optimize();
		_index.close();


		return findInIndex(tags);
	}

	private Collection<Thing> findInIndex(String tags)
			throws CorruptIndexException, IOException, ParseException {
		IndexReader reader = IndexReader.open(INDEX_DIRECTORY);

		Searcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		QueryParser parser = new QueryParser(TEXT_FIELD, analyzer);

		Query query = parser.parse(tags);

		Hits hits = searcher.search(query);

		Collection<Thing> result = new ArrayList<Thing>(hits.length());
		for (int i = 0; i < hits.length(); i++) {
			Document doc = hits.doc(i);
			String name = doc.getFields(TEXT_FIELD)[0].stringValue();
			String description = doc.getFields(TEXT_FIELD)[1].stringValue();

			Thing foundThing = new Thing(name, description);
			result.add(foundThing);
		}

		return result;

	}

	private void addToIndex(Thing thing) throws CorruptIndexException,
			IOException {
		Document doc = new Document();
		doc.add(toField(thing.name()));
		doc.add(toField(thing.description()));

		_index.addDocument(doc);
	}

	private Field toField(String string) {
		return new Field(TEXT_FIELD, string, Store.YES, Field.Index.TOKENIZED);
	}

	private void createNewIndex() throws CorruptIndexException,
			LockObtainFailedException, IOException {
		_index = new IndexWriter(INDEX_DIRECTORY, new StandardAnalyzer(), true);
	}

}
