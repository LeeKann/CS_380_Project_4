/*
*Peter Knight
*Eric Kannampuzha
*Project 4
*Class Ipv6Client.java
*CS 380
*Nima
*/

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.Arrays;
import java.nio.ByteBuffer;



public class Ipv6Client {
     
     public static void main(String[] args) {
        try {
            Socket socket = new Socket("18.221.102.182", 38004);
            System.out.println("Connected to server.");
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            

            
            for(int i = 1; i <= 12; i++) {
                
                ByteBuffer ipv6 = ByteBuffer.allocate((int)(40 + Math.pow(2,i)));
                
                ipv6.putInt((int)0x60000000);           // Version + Traffic Class + Flow Control
                ipv6.putShort((short)Math.pow(2,i));    // Payload Length
                ipv6.put((byte)0x11);                   // Next Header
                ipv6.put((byte)0x14);                   // Hop Limit
                
                ipv6.putInt((int)0x0);                  // 32 bits of zero for source address
                ipv6.putInt((int)0x0);                  // 32 bits of zero for source address
                ipv6.putShort((short)0x0);              // 16 bits of zero for source address
                ipv6.putShort((short)0xffff);           // 16 bits of ones for source address
                ipv6.putInt((int)0x0);                  // 32 bits of zero for source address
                
                ipv6.putInt((int)0x0);                  // 32 bits of zero for dst address
                ipv6.putInt((int)0x0);                  // 32 bits of zero for dst address
                ipv6.putShort((short)0x0);              // 16 bits of zero for dst address
                ipv6.putShort((short)0xffff);           // 16 bits of ones for dst address
                ipv6.put((byte)18);                     // actual destination address
                ipv6.put((byte)221);                    // .
                ipv6.put((byte)102);                    // .
                ipv6.put((byte)182);                    // .
                
                for(int j = 0; j < Math.pow(2,i); j++) { // data is just zeroes
                    ipv6.put((byte)0x1);
                }
                
                System.out.println("data length: " + (int)Math.pow(2,i));
                dos.write(ipv6.array());
                ByteBuffer response = ByteBuffer.allocate(4);
                for(int j = 0; j < 4; j++) {
                    response.put(dis.readByte());
                }
                System.out.printf("Response: 0x" + (Integer.toHexString(response.getInt(0))).toUpperCase() + "\n\n");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
