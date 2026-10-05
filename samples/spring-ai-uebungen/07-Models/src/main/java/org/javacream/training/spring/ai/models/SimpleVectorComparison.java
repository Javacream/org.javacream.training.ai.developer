package org.javacream.training.spring.ai.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Vergleicht zwei JSON-Dateien mit den Attributen dimensions und vector.
 * Benötigt Jackson Databind (com.fasterxml.jackson.core:jackson-databind).
 * Programmargumente: vector1.json vector2.json Die Vektoren müssen aus einem
 * kompatiblen Embedding-Vektorraum stammen.
 */
public class SimpleVectorComparison {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		String vector1 = "vector_data/hello_world.json";
		String vector2 = "vector_data/hello_world!.json";
		String vector3 = "vector_data/hugo.json";
		double[] first = readVector(Path.of(vector1));
		double[] second = readVector(Path.of(vector2));
		double[] third = readVector(Path.of(vector3));
		double similarity_12 = cosineSimilarity(first, second);
		double similarity_13 = cosineSimilarity(first, third);

		System.out.printf(Locale.ROOT, "Vector1: %s, Vector2: %s%nDimension: %d%nCosine Similarity: %.6f%n", vector1,
				vector2, first.length, similarity_12);
		System.out.printf(Locale.ROOT, "Vector1: %s, Vector2: %s%nDimension: %d%nCosine Similarity: %.6f%n", vector1,
				vector3, first.length, similarity_13);
	}

	private static double[] readVector(Path file) throws IOException {
		JsonNode root = MAPPER.readTree(file.toFile());

		if (root == null || !root.isObject()) {
			throw new IllegalArgumentException(file + ": Ein JSON-Objekt wird erwartet.");
		}

		JsonNode dimensions = root.get("dimensions");
		JsonNode vector = root.get("vector");

		if (dimensions == null || !dimensions.isIntegralNumber() || !dimensions.canConvertToInt()
				|| dimensions.intValue() <= 0) {
			throw new IllegalArgumentException(file + ": 'dimensions' muss eine positive Ganzzahl sein.");
		}

		if (vector == null || !vector.isArray()) {
			throw new IllegalArgumentException(file + ": 'vector' muss ein Array sein.");
		}

		if (vector.size() != dimensions.intValue()) {
			throw new IllegalArgumentException(file + ": dimensions=" + dimensions.intValue() + ", aber vector enthält "
					+ vector.size() + " Werte.");
		}

		double[] result = new double[vector.size()];

		for (int i = 0; i < result.length; i++) {
			JsonNode value = vector.get(i);

			if (!value.isNumber() || !Double.isFinite(value.doubleValue())) {
				throw new IllegalArgumentException(file + ": vector[" + i + "] muss eine endliche Zahl sein.");
			}

			result[i] = value.doubleValue();
		}

		return result;
	}

	public static double cosineSimilarity(double[] first, double[] second) {
		if (first == null || second == null || first.length == 0 || second.length == 0) {
			throw new IllegalArgumentException("Die Vektoren dürfen nicht null oder leer sein.");
		}

		if (first.length != second.length) {
			throw new IllegalArgumentException(
					"Unterschiedliche Dimensionen: " + first.length + " und " + second.length);
		}

		double firstScale = maxAbsoluteValue(first);
		double secondScale = maxAbsoluteValue(second);

		if (firstScale == 0 || secondScale == 0) {
			throw new IllegalArgumentException("Cosine Similarity ist für Nullvektoren nicht definiert.");
		}

		double dotProduct = 0;
		double firstNormSquared = 0;
		double secondNormSquared = 0;

		// Positive Skalierung verhindert Überlauf und ändert den Kosinus nicht.
		for (int i = 0; i < first.length; i++) {
			double a = first[i] / firstScale;
			double b = second[i] / secondScale;

			dotProduct += a * b;
			firstNormSquared += a * a;
			secondNormSquared += b * b;
		}

		double result = dotProduct / (Math.sqrt(firstNormSquared) * Math.sqrt(secondNormSquared));

		// Rundungsfehler können den Wertebereich minimal überschreiten.
		return Math.max(-1.0, Math.min(1.0, result));
	}

	private static double maxAbsoluteValue(double[] vector) {
		double max = 0;

		for (double value : vector) {
			if (!Double.isFinite(value)) {
				throw new IllegalArgumentException("Alle Vektorwerte müssen endlich sein.");
			}
			max = Math.max(max, Math.abs(value));
		}

		return max;
	}
}
