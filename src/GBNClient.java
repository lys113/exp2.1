import java.io.IOException;
import java.net.*;


public class GBNClient implements Runnable{
private int receport=9090;
private int expectseq=1;
private DatagramPacket recepacket;
private DatagramSocket recesocket;
private int counter=1;
private String rece;
public GBNClient() throws UnknownHostException, SocketException {
   recesocket=new DatagramSocket(receport,InetAddress.getLocalHost());
    System.out.println("客户端：开始接收");
}

    /**
     * 单个确认
     * @param seq，即待确认的序号
     * @return 对单个序号进行确认
     */
    private boolean ackone(int seq) {
        String temp=rece.substring(rece.indexOf("#:")+2).trim();
        int index;
        if(temp!="-") {
            index = Integer.parseInt(temp);
        }
        else{
            index =-1;
        }
        if (index==seq){
            System.out.println("客户端：接到收序号为："+seq+"的数据报");
            return true;
        }
        return false;
    }

    /**
     * 向服务器端返回ack
     * @param seq 将返回该序号的ack
     */
    public void sendAck( int seq) throws IOException {
    String beensent="ack:"+seq;
    byte[] recedata=beensent.getBytes();
    InetAddress inetAddress=this.recepacket.getAddress();
    int port=this.recepacket.getPort();
    recepacket=new DatagramPacket(recedata,recedata.length,port);
    recesocket.send(recepacket);
}

    @Override
    public void run() {
        while (true){
            if (counter>=8){
                System.out.println("客户端接受数据完成");
                break;
            }
            byte[] recedata= new byte[4096];
            recepacket=new DatagramPacket(recedata,recedata.length);
            try {
                recesocket.receive(recepacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
             rece=new String(recedata,0,recedata.length);
            System.out.println("客户端：接收到数据："+rece);
            if (ackone(expectseq)){
                counter++;
                if(counter!=3){
                    System.out.println("客户端：发送ACK："+expectseq);
                    try {
                        sendAck(expectseq);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    expectseq++;
                }
                /*
                客户端发生超时
                 */
                else {
                    System.out.println("客户端：模拟超时");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
