package inc.util.serialization.token;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListWithPositionWrapper<T> implements List<T>, HasPositionMarkers {

    private final List<T> wrapped;
    private long startPos = -1;
    private long endPos = -1;
 
    public long getStartPos() {
	return startPos;
    }

    public long getEndPos() {
	return endPos;
    }

    public void setStartPos(long pos) {
	this.startPos = pos;
    }

    public void setEndPos(long pos) {
	this.endPos = pos;
    }

    public ListWithPositionWrapper(List<T> list, long startPos, long endPos) {
	this.wrapped = list;
	this.startPos = startPos;
	this.endPos = endPos;

    }

    public int size() {
	return wrapped.size();
    }

    public boolean isEmpty() {
	return wrapped.isEmpty();
    }

    public boolean contains(Object o) {
	return wrapped.contains(o);
    }

    public Iterator<T> iterator() {
	return wrapped.iterator();
    }

    public Object[] toArray() {
	return wrapped.toArray();
    }

    public <T> T[] toArray(T[] a) {
	return wrapped.toArray(a);
    }

    public boolean add(T e) {
	return wrapped.add(e);
    }

    public boolean remove(Object o) {
	return wrapped.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
	return wrapped.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
	return wrapped.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
	return wrapped.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
	return wrapped.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
	return wrapped.retainAll(c);
    }

    public void clear() {
	wrapped.clear();
    }

    public boolean equals(Object o) {
	return wrapped.equals(o);
    }

    public int hashCode() {
	return wrapped.hashCode();
    }

    public T get(int index) {
	return wrapped.get(index);
    }

    public T set(int index, T element) {
	return wrapped.set(index, element);
    }

    public void add(int index, T element) {
	wrapped.add(index, element);
    }

    public T remove(int index) {
	return wrapped.remove(index);
    }

    public int indexOf(Object o) {
	return wrapped.indexOf(o);
    }

    public int lastIndexOf(Object o) {
	return wrapped.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
	return wrapped.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
	return wrapped.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
	return wrapped.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
	return DefaultPositionMarkers.toString("List", startPos, endPos) + " --> " + wrapped.toString();
    }
}
