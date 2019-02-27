package com.sslsocketclient;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sslsocketclient.DeSplitDemo.createSocketNote;

/**
 * Created by me on 2018/6/7.
 */
public class 查询
{

    public static void main(String[] args) throws Exception
    {
        Map map = search("2", "1");
        ComposeDemo cd = new ComposeDemo();
        Socket ss = null;

        String serverCode = (String) map.get("serverCode");
        Map mapQuery = (Map) map.get("mapQuery");
        ss = createSocketNote();
        BufferedOutputStream bfo = new BufferedOutputStream(ss.getOutputStream());
        bfo.write(cd.doCanProcess(mapQuery, serverCode));
        bfo.flush();

        DeSplitDemo split = new DeSplitDemo();

        final byte[] buf = new byte[36000];

        // 报文帧拆分的处理类
        ss.getInputStream().read(buf);

        ByteDataBuffer bdb = new ByteDataBuffer(buf);
        List tmpList = split.doCanProcess(bdb);
        // 收到的数据
        System.out.println("机构返回的报文:" + tmpList.toString());
        ss.close();

    }

    /**
     * 获取数据
     *
     * @return
     */
    private static Map search(String key, String type)
    {
        String serverCode = "200001";
        Map<String, String> mapQuery = new HashMap<>();

        //查询条件的类别
        mapQuery.put("queryType", type);
        //对应查询类别的查询条件
        mapQuery.put("queryValue", key);
        //预留
        mapQuery.put("extend", "666");
        Map map = new HashMap();
        map.put("serverCode", serverCode);
        map.put("mapQuery", mapQuery);
        return map;
    }
}
