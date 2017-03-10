package a_Socket;



public class Socket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static boolean HasKeyword(String content, boolean log)
    {
        String receive = "";
        String sendData = "";
        try
        {
            content = content.replace(" ", "").replace("\0", "").replace("\r\n", "").toLowerCase();
            sendData = "CheckPost~全国~ask~" + content + "\0";
            //输入:CheckPost~全国~ask~你可以发布58同城赶集等等都可以  *输出：1~0~58同城  *时间:2015/6/12 8:58:25
            receive = java.net.Socket.Send("192.168.7.119", 3636, sendData, true);
            String[] receiveDatas = receive.split("~");
            if (receiveDatas.length == 3)
            {
                if (receiveDatas[0] == "1")
                {
                    return true;
                }
            }
            return false;
        }
        catch (Exception ex)
        {
            //lock (SlSyncObject.IO_SlLog_Lock)
            //{
            //    SlFile.Write(String.Format("E:\\SouFun\\asklog\\log\\HasKeyword_{0}.txt", DateTime.Now.ToString("yyyy-MM-dd")), String.Format("输入:{0} *异常：{1}  *时间:{2}  * 输出{3}", sendData, ex.Message, DateTime.Now.ToString(), receive));
            //}
            return true;
        }
    }
}
