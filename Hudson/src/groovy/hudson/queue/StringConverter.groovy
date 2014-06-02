package groovy.hudson.queue

/**
 * <code>
 * T t = // ... <br />
 * t.equals(parseString(t.generateString())) // this should be true
 * </code>
 */
interface StringConverter<E> {
    /** 
     * e should not be null
     */
    String generateString(E e)

    /**
     * returns null if fails to parse s
     */
    E parseString(String s)
}
