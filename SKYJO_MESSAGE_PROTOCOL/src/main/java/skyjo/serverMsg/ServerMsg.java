package skyjo.serverMsg;

import skyjo.msgProtocol.Message;


public class ServerMsg implements Message {

    private SrvMsgType type;
    private Object msg;

    /**
     * Constructor of ServerType
     * @param type server message type
     */
    public ServerMsg(SrvMsgType type) {
        this.type = type;
    }

    /**
     * Overload constructor
     * @param type server message type
     * @param msg the object message
     */
    public ServerMsg(SrvMsgType type, Object msg) {
        this.type = type;
        this.msg = msg;
    }

    /**
     * setter of message
     * @param msg new message Object 
     */
    public void setMsg(Object msg) {
        this.msg = msg;
    }

    /**
     * getter of server Type
     * @return server type
     */
    @Override
    public SrvMsgType getType() {
        return type;
    }

    /**
     * getter of Object message
     * @return Object message
     */
    public Object getMsg() {
        return msg;
    }

    /**
     *Override Object content 
     * @return Object 
     */
    @Override
    public Object getContent() {
        return this.msg;
    }
}
