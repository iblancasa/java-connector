/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package rticonnector;


class RTIDDSConnector {

    private long pointer;

    public RTIDDSConnector() {
        pointer = RTIDDSConnector_new();
    }

    static {
        System.loadLibrary("connectorjvm");
    }

    private static native long RTIDDSConnector_new();


    public static void main(String[] args) {
        RTIDDSConnector a = new RTIDDSConnector();
        System.out.println("Hello World!"); // Display the string.
    }
}
