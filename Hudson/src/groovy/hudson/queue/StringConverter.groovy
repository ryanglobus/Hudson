package hudson.queue

/**
 * <code>
 * T t = // ... <br />
 * t.equals(parseString(t.generateString())) // this should be true
 * </code>
 */
interface StringConverter<E> {
    String generateString(E e)
    E parseString(String s)
}