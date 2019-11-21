import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 服务器端
 */
public class GBNServer extends Thread {
private int sendport=8080;
private DatagramSocket senfSocket;
private DatagramPacket sendPacket;
private int base =1;
private int nextseq=1;
private int windowsize=5;
private int count =0;
private String databeensent ="hello";
public GBNServer(){
    while (true){
        /*
        当还有空间剩余时，循环发送，直到无空间剩余
         */
        while (nextseq<=base+windowsize){
            SendData(nextseq);
            nextseq++;

        }
        /*
        对发送的结果进行确认，若确认成功，则改变base
         */
        if (Ensure(base)){
            base++;
            count++;

        }

    }
   }

    /**
     * 发送待发送序号的数据报
     * 无返回值
     * @param seq 待发送序号
     */
   public void SendData(int seq){
       System.out.println("服务器端发送编号为："+seq+"的分组，分组内容"+databeensent);


   }

    /**
     * 确认待确认序号是否被确认
     * @param seq 待确认序号
     * @return 该序号的确认结果
     */
    public boolean Ensure(int seq){
        return true;

    }
}