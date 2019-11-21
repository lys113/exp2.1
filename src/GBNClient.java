import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class GBNClient{
private int receport=9090;
private int expectseq=1;
private DatagramPacket recepacket;
private DatagramSocket recesocket;

public GBNClient(){
while (true){
    
}
}

    /**
     * 单个确认
     * @param seq，即待确认的序号
     * @return 对单个序号进行确认
     */
    private boolean ackone(int seq) {
        return true;

    }

    /**
     * 累计确认
     * @param seq 即待确认的序号
     * @return 输出累计确认的最高值
     */
    public boolean ackall(int seq){
    boolean flag=true;
    for (int i=0;i<=seq;i++){
        if (!ackone(i)){
            flag=false;
        }
    }
    return flag;
    }

    /**
     * 向服务器端返回ack
     * @param seq 将返回该序号的ack
     */
    public void sendAck( int seq) {

}

}
