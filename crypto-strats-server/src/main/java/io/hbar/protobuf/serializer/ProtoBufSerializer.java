package io.hbar.protobuf.serializer;

import io.hbar.fx.data.series.FieldSeries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProtoBufSerializer {

	public static <T extends Enum<T>> byte[] serialize(FieldSeries<T> series, int startTime, int endTime) {
		Object seriesBuilder = getBuilder(getSeriesClass(series.getTypeString()));

		for (int timestamp : series.getTimestampSet().subSet(startTime, endTime)) {
			Object rowBuilder = getBuilder(getRowClass(series.getTypeString()));

			try {
				rowBuilder.getClass().getMethod("setTimestamp", int.class).invoke(rowBuilder, timestamp);

				for (T field : series.getFields()) {
					Method setField = rowBuilder.getClass().getMethod("set" + getSetterMethodString(field), double.class);
					setField.invoke(rowBuilder, series.getValue(timestamp, field));
				}

				Object built = rowBuilder.getClass().getMethod("build").invoke(rowBuilder);
				seriesBuilder.getClass().getMethod("addSeries", built.getClass()).invoke(seriesBuilder, built);

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		try {
			Object built = seriesBuilder.getClass().getMethod("build").invoke(seriesBuilder);
			return (byte[]) built.getClass().getMethod("toByteArray").invoke(built);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Class<?> getSeriesClass(String typeString) {
		try {
			return Class.forName("io.hbar.protobuf.schema." + typeString + "SeriesSchema$" + typeString + "Series");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Class<?> getRowClass(String typeString) {
		try {
			return Class.forName("io.hbar.protobuf.schema." + typeString + "SeriesSchema$" + typeString + "Series$" + typeString);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Object getBuilder(Class<?> protoClass) {
		try {
			return protoClass.getMethod("newBuilder").invoke(protoClass);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static <T> String getSetterMethodString(T field) {
		String fieldString = field.toString().toLowerCase();
		return fieldString.substring(0, 1).toUpperCase() + fieldString.substring(1);
	}

}
