package wheel.reactive.lists.impl;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;

public abstract class AbstractListSource<VO> implements ListSource<VO>{
	private int _version;
	
	public Iterator<VO> iterator() {
		return new ListSourceIterator<VO>(this);
	}

	public void setVersion(int version) {
		_version = version;
	}

	public int getVersion() {
		return _version;
	}
	
	public class ListSourceIterator<T> implements Iterator<T> {

		  private int version;
		  private AbstractListSource<T> _source;
		  private int position;

		  ListSourceIterator(AbstractListSource<T> source) {
		    this._source = source;
		    this.version = source.getVersion();
		    this.position = 0;
		  }

		  public boolean hasNext() {
		    if (version != _source.getVersion())
		      throw new ConcurrentModificationException();

		    return (position < _source.output().currentSize());
		  }

		  public T next() {
		    if (version != _source.getVersion())
		      throw new ConcurrentModificationException();
		    if (!hasNext())
		      return null;
		    int last = _source.output().currentSize() - 1;
		    last -= position;
		    T result = null;

		    if (last >= 0) {
		      position++;
		      result = _source.output().currentGet(last);
		    }

		    return result;
		  }

		  public void remove() {
		  }

		}
	
}
