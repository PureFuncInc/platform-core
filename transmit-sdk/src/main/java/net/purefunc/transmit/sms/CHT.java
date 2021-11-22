package net.purefunc.transmit.sms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class CHT {

    private Socket sock;
    private DataInputStream din;
    private DataOutputStream dout;
    private String ret_message = "";
    private String ret_msisdn = "";

    private String host = "202.39.54.130";
    private int port = 8000;

    public CHT() {
    }

    public int HexToDec(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9')
                sum = sum * 16 + input.charAt(i) - 48;
            else if (input.charAt(i) >= 'A' && input.charAt(i) <= 'F')
                sum = sum * 16 + input.charAt(i) - 55;
        }
        return sum;
    }

    public int create_conn(String user, String passwd) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            this.sock = new Socket(this.host, this.port);
            this.din = new DataInputStream(this.sock.getInputStream());
            this.dout = new DataOutputStream(this.sock.getOutputStream());

            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String acc_pwd_str = user.trim() + "\0" + passwd.trim() + "\0";
            byte acc_pwd_byte[] = acc_pwd_str.getBytes();
            byte acc_pwd_size = (byte) acc_pwd_byte.length;

            out_buffer[0] = 0;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = acc_pwd_size;
            out_buffer[5] = 0;
            for (i = 0; i < acc_pwd_size; i++)
                out_buffer[i + 6] = acc_pwd_byte[i];

            this.dout.write(out_buffer);
            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            return ret_code;
        } catch (UnknownHostException e) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = "Socket Error: " + ex.getMessage();
            return 71;
        }

    }

    public void close_conn() {
        try {
            if (this.din != null) this.din.close();
            if (this.dout != null) this.dout.close();
            if (this.sock != null) this.sock.close();

            this.din = null;
            this.dout = null;
            this.sock = null;
        } catch (UnknownHostException e) {
            this.ret_message = "Cannot find the host!";
        } catch (IOException ex) {
            this.ret_message = "Socket Error: " + ex.getMessage();
        }
    }

    public int send_text_message(String sms_tel, String message) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String msg_set = sms_tel.trim() + "\0" + "01" + "\0";
            byte msg_set_byte[] = msg_set.getBytes();
            int msg_set_size = msg_set_byte.length;

            String msg_content = message.trim() + "\0";
            byte msg_content_byte[] = msg_content.getBytes("Big5");
            int msg_content_size = msg_content_byte.length - 1;

            if (msg_set_size > 80) {
                this.ret_message = "msg_set > max limit!";
                return 80;
            }
            if (msg_content_size > 159) {
                this.ret_message = "msg_content > max limit!";
                return 81;
            }

            if (sms_tel.startsWith("+"))
                out_buffer[0] = 15;
            else
                out_buffer[0] = 1;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = (byte) msg_set_size;
            out_buffer[5] = (byte) msg_content_size;

            for (i = 0; i < msg_set_size; i++)
                out_buffer[i + 6] = msg_set_byte[i];

            for (i = 0; i < msg_content_size; i++)
                out_buffer[i + 106] = msg_content_byte[i];

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            this.ret_message = this.ret_message.trim();
            return ret_code;
        } catch (UnknownHostException eu) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = " Socket Error: " + ex.getMessage();
            return 71;
        }
    }

    public int send_text_message(String sms_tel, String message, String order_time) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String msg_set = sms_tel.trim() + "\0" + "03" + "\0" + order_time.trim();
            byte msg_set_byte[] = msg_set.getBytes();
            int msg_set_size = msg_set_byte.length;

            String msg_content = message.trim() + "\0";
            byte msg_content_byte[] = msg_content.getBytes("Big5");
            int msg_content_size = msg_content_byte.length - 1;

            if (msg_set_size > 80) {
                this.ret_message = "msg_set > max limit!";
                return 80;
            }
            if (msg_content_size > 159) {
                this.ret_message = "msg_content > max limit!";
                return 81;
            }

            if (sms_tel.startsWith("+"))
                out_buffer[0] = 15;
            else
                out_buffer[0] = 1;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = (byte) msg_set_size;
            out_buffer[5] = (byte) msg_content_size;

            for (i = 0; i < msg_set_size; i++)
                out_buffer[i + 6] = msg_set_byte[i];

            for (i = 0; i < msg_content_size; i++)
                out_buffer[i + 106] = msg_content_byte[i];

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            this.ret_message = this.ret_message.trim();
            return ret_code;
        } catch (UnknownHostException eu) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = " Socket Error: " + ex.getMessage();
            return 71;
        }
    }

    public int query_message(int type, String messageid) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String msg_set = messageid.trim() + "\0";
            byte msg_set_byte[] = msg_set.getBytes();
            int msg_set_size = msg_set_byte.length;

            if (msg_set_size > 80) {
                this.ret_message = "msg_set > max limit!";
                return 80;
            }

            out_buffer[0] = (byte) type;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = (byte) msg_set_size;
            out_buffer[5] = 0;

            for (i = 0; i < msg_set_size; i++)
                out_buffer[i + 6] = msg_set_byte[i];

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            this.ret_message = this.ret_message.trim();
            return ret_code;
        } catch (UnknownHostException eu) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = " Socket Error: " + ex.getMessage();
            return 71;
        }
    }

    public int recv_text_message() {
        byte out_buffer[] = new byte[266]; //�ǰe���׬�266
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            out_buffer[0] = 3;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = 0;
            out_buffer[5] = 0;

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content, "big5");
            this.ret_message = this.ret_message.trim();

            this.ret_msisdn = "";
            if (ret_code == 0) {
                String ret_set_msg = new String(ret_set);
                //�Nstring��'\0'���}�A
                StringTokenizer tok = new StringTokenizer(ret_set_msg, "\0");
                if (tok.hasMoreTokens()) {
                    this.ret_msisdn = tok.nextToken();
                }
            }

            return ret_code;
        } catch (UnknownHostException eu) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = " Socket Error: " + ex.getMessage();
            return 71;
        }
    }

    public int cancel_text_message(String messageid) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String msg_set = messageid.trim() + "\0";
            byte msg_set_byte[] = msg_set.getBytes();
            int msg_set_size = msg_set_byte.length;

            if (msg_set_size > 80) {
                this.ret_message = "msg_set > max limit!";
                return 80;
            }

            out_buffer[0] = 16;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = (byte) msg_set_size;
            out_buffer[5] = 0;

            for (i = 0; i < msg_set_size; i++)
                out_buffer[i + 6] = msg_set_byte[i];

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            this.ret_message = this.ret_message.trim();
            return ret_code;
        } catch (UnknownHostException eu) {
            this.ret_message = "Cannot find the host!";
            return 70;
        } catch (IOException ex) {
            this.ret_message = " Socket Error: " + ex.getMessage();
            return 71;
        }
    }

    public int send_wappush_message(String sms_tel, String sms_url, String message) {
        byte out_buffer[] = new byte[266];
        byte ret_code = 99;
        byte ret_coding = 0;
        byte ret_set_len = 0;
        byte ret_content_len = 0;
        byte ret_set[] = new byte[80];
        byte ret_content[] = new byte[160];

        try {
            int i;
            for (i = 0; i < 266; i++) out_buffer[i] = 0;
            for (i = 0; i < 80; i++) ret_set[i] = 0;
            for (i = 0; i < 160; i++) ret_content[i] = 0;

            String msg_set = sms_tel.trim() + "\0" + "01" + "\0";
            byte msg_set_byte[] = msg_set.getBytes();
            int msg_set_size = msg_set_byte.length;

            String msg_content = sms_url.trim() + "\0" + message.trim() + "\0";
            byte msg_content_byte[] = msg_content.getBytes("Big5");
            int msg_content_size = msg_content_byte.length;

            out_buffer[0] = 13;
            out_buffer[1] = 1;
            out_buffer[2] = 0;
            out_buffer[3] = 0;
            out_buffer[4] = (byte) msg_set_size;
            out_buffer[5] = (byte) msg_content_size;

            for (i = 0; i < msg_set_size; i++)
                out_buffer[i + 6] = msg_set_byte[i];

            for (i = 0; i < msg_content_size; i++)
                out_buffer[i + 106] = msg_content_byte[i];

            this.dout.write(out_buffer);

            ret_code = this.din.readByte();
            ret_coding = this.din.readByte();
            ret_set_len = this.din.readByte();
            ret_content_len = this.din.readByte();

            this.din.read(ret_set, 0, 80);
            this.din.read(ret_content, 0, 160);
            this.ret_message = new String(ret_content);
            this.ret_message = this.ret_message.trim();
            return ret_code;
        } catch (UnknownHostException eu) {
            System.out.println(" Cannot find the host ");
            return 70;
        } catch (IOException ex) {
            System.out.println(" Socket Error: " + ex.getMessage());
            return 71;
        }
    }

    public String get_message() {
        return ret_message;
    }

    public String get_msisdn() {
        return ret_msisdn;
    }

//    public static void main(String[] args) throws Exception {
//        try {
//            String server = "202.39.54.130";
//            int port = 8000;
//
//            if (args.length < 4) {
//                System.out.println("Use: java sms2 id passwd tel message");
//                System.out.println(" Ex: java sms2 test test123 0910123xxx HiNet²�T!");
//                return;
//            }
//            String user = args[0];
//            String passwd = args[1];
//            String tel = args[2];
//            String message = new String(args[3].getBytes(), "big5");
//
//            CHT mysms = new CHT();
//            int k = mysms.create_conn(server, port, user, passwd);
//            if (k == 0) {
//                System.out.println("check ok!");
//            } else {
//                System.out.println(mysms.get_message());
//                mysms.close_conn();
//                return;
//            }
//
//            k = mysms.send_text_message(tel, message);
//            if (k == 0) {
//                System.out.println("MessageID=" + mysms.get_message());
//            } else {
//                System.out.print("ret_code=" + k + ",");
//                System.out.println("ret_content=" + mysms.get_message());
//                mysms.close_conn();
//                return;
//            }
//
//            mysms.close_conn();
//        } catch (Exception e) {
//            System.out.println("I/O Exception : " + e);
//        }
//    }
}

