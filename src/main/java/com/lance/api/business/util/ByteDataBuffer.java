package com.lance.api.business.util;


import java.io.UnsupportedEncodingException;

/**
 * byte工具类
 *
 * @author lance
 * @since 2018-10-12
 */
public class ByteDataBuffer
{
    /**
     * 缓存大小
     */
    private static int INCREASE_DATA_SIZE = 256;
    /**
     * 数字高低位 标示
     */
    private boolean inBigEndian;
    private byte dataBuffer[];
    private int pos;
    private int dataSize;
    private String encoding;
    private char defaultFillChar;

    public ByteDataBuffer()
    {
        this(INCREASE_DATA_SIZE);
    }

    public ByteDataBuffer(int size)
    {
        inBigEndian = true;
        pos = 0;
        encoding = "UTF-8";
        dataBuffer = new byte[size];
        dataSize = 0;
    }

    public ByteDataBuffer(byte data[])
    {
        inBigEndian = true;
        pos = 0;
        encoding = "UTF-8";
        if (data != null)
        {
            dataBuffer = data;
            dataSize = data.length;
        }
        else
        {
            dataBuffer = new byte[INCREASE_DATA_SIZE];
            dataSize = 0;
        }
    }

    public void ensureCapacity(int minCapacity) throws Exception
    {
        if (dataBuffer.length < minCapacity)
        {
            int nextBufSize = INCREASE_DATA_SIZE * (minCapacity / INCREASE_DATA_SIZE + 1);
            byte data[] = new byte[nextBufSize];
            System.arraycopy(dataBuffer, 0, data, 0, dataBuffer.length);
            dataBuffer = data;
        }
    }

    public void writeBytes(byte data[]) throws Exception
    {
        writeBytes(data, 0, data.length);
    }

    public void writeBytes(byte data[], int srcPos, int length) throws Exception
    {
        if (data == null || length <= 0)
        {
            return;
        }
        if (srcPos + length > data.length)
        {
            length = data.length - srcPos;
        }
        ensureCapacity(pos + length);
        System.arraycopy(data, srcPos, dataBuffer, pos, length);
        pos += length;
    }

    public void writeInt8(byte data) throws Exception
    {
        ensureCapacity(pos + 1);
        dataBuffer[pos] = data;
        pos++;
    }

    public void writeFillerBytes(byte data, int length) throws Exception
    {
        ensureCapacity(pos + length);
        for (int j = 0; j < length; j++)
        {
            dataBuffer[pos] = data;
            pos++;
        }

    }

    public void writeInt16(int i) throws Exception
    {
        ensureCapacity(pos + 2);
        if (inBigEndian)
        {
            dataBuffer[pos] = (byte) (i >>> 8 & 255);
            dataBuffer[pos + 1] = (byte) (i & 255);
        }
        else
        {
            dataBuffer[pos] = (byte) (i & 255);
            dataBuffer[pos + 1] = (byte) (i >>> 8 & 255);
        }
        pos += 2;
    }

    public void writeInt32(int i) throws Exception
    {
        ensureCapacity(pos + 4);
        if (inBigEndian)
        {
            dataBuffer[pos] = (byte) (i >>> 24 & 255);
            dataBuffer[pos + 1] = (byte) (i >>> 16 & 255);
            dataBuffer[pos + 2] = (byte) (i >>> 8 & 255);
            dataBuffer[pos + 3] = (byte) (i & 255);
        }
        else
        {
            dataBuffer[pos] = (byte) (i & 255);
            dataBuffer[pos + 1] = (byte) (i >>> 8 & 255);
            dataBuffer[pos + 2] = (byte) (i >>> 16 & 255);
            dataBuffer[pos + 3] = (byte) (i >>> 24 & 255);
        }
        pos += 4;
    }

    public void writeString(String s) throws Exception
    {
        if (s == null)
        {
            writeInt16(-1);
            return;
        }
        else
        {
            byte data[] = s.getBytes(encoding);
            writeInt16(data.length);
            writeBytes(data, 0, data.length);
            return;
        }
    }

    public void writeString(String s, int size) throws Exception
    {
        writeString(s, size, defaultFillChar);
    }

    public void writeString(String s, int size, char fillChar) throws Exception
    {
        if (s == null)
        {
            s = "";
        }
        byte data[] = s.getBytes(encoding);
        if (data.length >= size)
        {
            writeBytes(data, 0, size);
        }
        else
        {
            writeBytes(data, 0, data.length);
            writeFillerBytes((byte) (fillChar & 255), size - data.length);
        }
    }

    public int readBytes(byte data[]) throws Exception
    {
        return readBytes(data, 0, data.length);
    }

    public int readBytes(byte data[], int destPos, int length) throws Exception
    {
        if (length > dataBuffer.length - pos)
        {
            length = dataBuffer.length - pos;
        }
        if (length > 0)
        {
            System.arraycopy(dataBuffer, pos, data, destPos, length);
        }
        pos += length;
        return length;
    }

    public int readInt32() throws Exception
    {
        int i;
        if (inBigEndian)
        {
            i = ((dataBuffer[pos] & 255) << 24) + ((dataBuffer[pos + 1] & 255) << 16) + ((dataBuffer[pos + 2] & 255) << 8) + (dataBuffer[pos + 3] & 255);
        }
        else
        {
            i = (dataBuffer[pos] & 255) + ((dataBuffer[pos + 1] & 255) << 8) + ((dataBuffer[pos + 2] & 255) << 16) + ((dataBuffer[pos + 3] & 255) << 24);
        }
        pos += 4;
        return i;
    }

    public byte readInt8() throws Exception
    {
        if (dataBuffer.length - pos > 0)
        {
            byte result = dataBuffer[pos];
            pos++;
            return result;
        }
        else
        {
            throw new Exception("no Data");
        }
    }

    public byte[] getBytes()
    {
        byte data[] = new byte[getDataSize()];
        System.arraycopy(dataBuffer, 0, data, 0, data.length);
        return data;
    }

    public short readInt16() throws Exception
    {
        short word0;
        if (inBigEndian)
        {
            word0 = (short) (((dataBuffer[pos] & 255) << 8) + (dataBuffer[pos + 1] & 255));
        }
        else
        {
            word0 = (short) ((dataBuffer[pos] & 255) + ((dataBuffer[pos + 1] & 255) << 8));
        }
        pos += 2;
        return word0;
    }

    public String readString() throws Exception
    {
        String result = null;
        int strLen = readInt16();
        if (strLen > 0)
        {
            result = new String(dataBuffer, pos, strLen, encoding);
            pos += strLen;
        }
        if (strLen == 0)
        {
            return "";
        }
        else
        {
            return result;
        }
    }

    public String readString(int size) throws Exception
    {
        return readString(size, defaultFillChar);
    }

    public String readString(int size, char fillByte) throws Exception
    {
        int strLen = size;
        if (strLen > 0)
        {
            for (; strLen > 0 && dataBuffer[(pos + strLen) - 1] == fillByte; strLen--)
                ;
            String result = new String(dataBuffer, pos, strLen, encoding);
            pos += size;
            return result;
        }
        else
        {
            return null;
        }
    }

    public int getDataSize()
    {
        if (pos > dataSize)
        {
            dataSize = pos;
        }
        return dataSize;
    }

    public char getDefaultFillChar()
    {
        return defaultFillChar;
    }

    public String getEncoding()
    {
        return encoding;
    }

    public boolean isInBigEndian()
    {
        return inBigEndian;
    }

    public int getPos()
    {
        return pos;
    }

    public void setDefaultFillChar(char defaultFillChar)
    {
        this.defaultFillChar = defaultFillChar;
    }

    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }

    public void setInBigEndian(boolean inBigEndian)
    {
        this.inBigEndian = inBigEndian;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    public byte[] getDataBuffer()
    {
        return dataBuffer;
    }

    public void reset()
    {
        dataSize = 0;
        pos = 0;
    }

    public boolean hasData()
    {
        return pos < dataSize;
    }

    public String readCString() throws Exception
    {
        byte data[] = getDataBuffer();
        int endPos = -1;
        int i = pos;
        do
        {
            if (i >= data.length)
            {
                break;
            }
            if (data[i] == 0)
            {
                endPos = i;
                break;
            }
            i++;
        } while (true);
        if (endPos < 0)
        {
            endPos = data.length;
        }
        String s = new String(data, pos, endPos - pos, encoding);
        pos = endPos + 1;
        return s;
    }

    public void writeCString(String cs) throws Exception
    {
        if (cs == null)
        {
            cs = "";
        }
        byte data[] = cs.getBytes(encoding);
        int pos = -1;
        int i = 0;
        do
        {
            if (i >= data.length)
            {
                break;
            }
            if (data[i] == 0)
            {
                pos = i;
                break;
            }
            i++;
        } while (true);
        if (pos >= 0)
        {
            writeBytes(data, 0, pos + 1);
        }
        else
        {
            writeBytes(data, 0, data.length);
            writeInt8((byte) 0);
        }
    }

    /**
     * 计算报文长度，不足四位左补零
     *
     * @param text    报文信息
     * @param needlen 报文长度规定的字符数
     * @return
     */
    public static String getLen(String text, int needlen)
    {
        if (text != null)
        {
            int len;
            try
            {
                // serverCode和token长度
                len = text.getBytes("utf-8").length + 39;
                // 看需要

                String lenStr = String.valueOf(len);
                StringBuffer sb = new StringBuffer(lenStr);
                while (sb.length() < needlen)
                {
                    sb.insert(0, "0");
                }
                return sb.toString();
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
