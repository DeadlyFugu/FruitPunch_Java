package net.sekien.fruity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: matt Date: 30/09/13 Time: 3:04 PM To change this template use File | Settings |
 * File Templates.
 */
public class SList extends SObject implements Iterable<SObject> {
List<SObject> value;

public SList(Collection<SObject> value) {
	this.value = new ArrayList<SObject>(value.size());
	this.value.addAll(value);
}

@Override public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append('(');
	boolean first = true;
	for (SObject o : value) {
		if (first) first=false; else builder.append(" ");
		builder.append(o.toString());
	}
	builder.append(')');
	return builder.toString();
}

@Override public String toBasicString() {
	StringBuilder builder = new StringBuilder();
	builder.append('(');
	boolean first = true;
	for (SObject o : value) {
		if (first) first=false; else builder.append(", ");
		builder.append(o.toBasicString());
	}
	builder.append(')');
	return builder.toString();
}

@Override public Iterator<SObject> iterator() {
	return value.iterator();
}

public int size() {
	return value.size();
}

	@Override public String getType() {
		return "list";
	}
}
