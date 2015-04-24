package io.hbar.fx.data.series;

import io.hbar.protobuf.serializer.ProtoBufSerializer;

import java.util.SortedSet;

import org.vertx.java.core.json.JsonObject;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

public class FieldSeries<T extends Enum<T>> {
	public Table<Integer, T, Double> table = TreeBasedTable.create();
	protected String typeString;
	protected T[] fields;
	protected JsonObject options;

	public FieldSeries(Class<T> type, JsonObject options) {
		this.typeString = type.getSimpleName();
		this.fields = type.getEnumConstants();
		this.options = options;
	}

	public void addRow(int timestamp, double... values) throws Exception {
		addRowArray(timestamp, values);
	}

	public void addRowArray(int timestamp, double[] values) throws Exception {
		if (values.length != fields.length) {
			throw new Exception("Arguments don't match fields");
		}

		for (int i = 0; i < fields.length; i++) {
			table.put(timestamp, fields[i], values[i]);
		}
	}

	public byte[] serialize(int startTime, int endTime) {
		return ProtoBufSerializer.serialize(this, startTime, endTime);
	}

	public double[] getSeries(T field) {
		return Doubles.toArray(table.column(field).values());
	}

	public int[] getTimestamps() {
		return Ints.toArray(table.rowKeySet());
	}
	
	public SortedSet<Integer> getTimestampSet() {
		return (SortedSet<Integer>) table.rowKeySet();
	}
	
	public T[] getFields() {
		return fields;
	}
	
	public double getValue(int timestamp, T field) {
		return table.get(timestamp, field);
	}
	
	public String getTypeString() {
		return typeString;
	}
	
	public JsonObject getOptions() {
		return options;
	}
	
	public String toString() {
		return table.toString();
	}
}
