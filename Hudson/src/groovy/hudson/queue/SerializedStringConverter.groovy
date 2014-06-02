package groovy.hudson.queue

import javax.xml.bind.DatatypeConverter

class SerializedStringConverter<E extends Serializable>
    implements StringConverter<E> {

    private Class<E> klass;

    SerializedStringConverter(Class<E> klass) {
        this.klass = klass
    }

    @Override
    String generateString(E e) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream()
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut)
        objOut.writeObject(e)
        objOut.close()
        byteOut.close()
        byte[] bytes = byteOut.toByteArray()
        return DatatypeConverter.printBase64Binary(bytes)
    }

    @Override
    E parseString(String s) {
        byte[] bytes = DatatypeConverter.parseBase64Binary(s)
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes)
        ObjectInputStream objIn = new ObjectInputStream(byteIn)
        E e = klass.cast(objIn.readObject())
        objIn.close()
        byteIn.close()
        return e
    }
}