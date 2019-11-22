import java.io.IOException;
import java.net.*;

/**
 * 服务器端
 */
public class GBNServer implements Runnable {
private int sendport=9090;
private DatagramSocket sendSocket;
private DatagramPacket sendPacket;
private InetAddress inetAddress = InetAddress.getLocalHost();
private int base =1;
private int nextseq=1;
private int windowsize=5;
private int count =0;
private int live=0;
private String databeensent ="hello";
private String fromClient;

    public GBNServer() throws UnknownHostException {
    }

    /**
     * 发送待发送序号的数据报(仅一段)
     * 无返回值
     * @param seq 待发送序号
     */
   public void SendData(int seq) throws IOException {
       System.out.println("服务器端发送编号为："+seq+"的分组，分组内容"+databeensent);
       byte[] send=new String("#:"+seq).getBytes();
       sendPacket=new DatagramPacket(send,send.length,sendport);
       sendSocket.send(sendPacket);
   }

    @Override
    public void run(){
        System.out.println("服务器：窗口大小："+this.windowsize);
        System.out.println("服务器：开始发送文件");
        int ack=-1;
        while (true){
        /*
        当还有空间剩余时，循环发送，直到无空间剩余
         */
            live++;
            if (live>=10){
                break;
            }
            while (nextseq<=base+windowsize&& nextseq<15){
                try {
                    SendData(nextseq);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nextseq++;
            }
            base=nextseq;
            System.out.println("服务器：接收ACK");
            byte[] bytes = new byte[4096];
            sendPacket=new DatagramPacket(bytes,bytes.length);
            long start = System.currentTimeMillis();
            while (true){
                if(System.currentTimeMillis()-start>=2000) {
                    break;
                }
                try {
                    sendSocket.setSoTimeout(1);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                try {
                    sendSocket.receive(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fromClient = new String(bytes, 0, bytes.length);
            }
            fromClient = new String(bytes, 0, bytes.length);
            if (fromClient.indexOf("ack:")!=-1){
                int temp_ack = Integer.parseInt(fromClient.substring(fromClient.indexOf("ack:") + 4).trim());
                if(temp_ack>ack) {
                    ack = temp_ack;
                    System.out.println("最高ACK："+temp_ack);
                }
            }
            base=ack+1;
            if (ack==8){
                System.out.println("文件传输完成");
                break;
            }
            if (base!=nextseq+1){
                System.out.println("服务器：当前最高ACK："+ack+"期望ack为:"+(nextseq)+"，重新发送");
                base=ack+1;
                nextseq=base;
                while (nextseq<=base+windowsize&& nextseq<15){
                    try {
                        SendData(nextseq);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    nextseq++;
                }

            }

        }

    }
}