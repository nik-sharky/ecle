package org.ploys.eclipse.embed.common;

/**
 * Basic Converter interface
 */
public interface Converter<I, O> {
    /**
     * Convert I to O
     *
     * @param data Input data
     * @return Converted result
     */
    O convert(I data);

    /**
     * Default toString converter
     *
     * @param <I> input data
     */
    public static class ToString<I> implements Converter<I, String> {
        @Override
        public String convert(I data) {
            return String.valueOf(data);
        }
    }
}