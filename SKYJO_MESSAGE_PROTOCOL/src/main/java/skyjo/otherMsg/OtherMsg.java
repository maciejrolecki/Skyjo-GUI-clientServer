package skyjo.otherMsg;

import java.io.Serializable;

/**
 * The Object is use when the server or the user have to send to important
 * object for the communication
 *
 * @author
 */
public class OtherMsg implements Serializable {

    private final Object object1;
    private final Object object2;

    /**
     * Constructor for OtherMsg
     *
     * @param object1 object one to send
     * @param object2 object two to send
     */
    public OtherMsg(Object object1, Object object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    /**
     * getter for O1
     * @return Object 
     */
    public Object getObject1() {
        return object1;
    }

    /**
     * getter for O2
     * @return Object 
     */
    public Object getObject2() {
        return object2;
    }
}
