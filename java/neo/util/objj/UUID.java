package neo.util.objj;

import java.security.SecureRandom;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 	@Class Name	: 	UUID.java
 * 	@파일설명		: 	
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class UUID
{

    public static UUID createUUID()
    {
        return new UUID();
    }

    private UUID()
    {
        mBytes = new byte[16];
        long currentTime = getCurrentTime();
        for(int i = 3; i > -1; i--)
            mBytes[3 - i] = (byte)(int)((currentTime & 255L << i * 8) >> i * 8);

        for(int i = 5; i > 3; i--)
            mBytes[9 - i] = (byte)(int)((currentTime & 255L << i * 8) >> i * 8);

        for(int i = 7; i > 5; i--)
            mBytes[13 - i] = (byte)(int)((currentTime & 255L << i * 8) >> i * 8);

        mBytes[6] &= 0xf;
        mBytes[6] |= 0x10;
        mBytes[8] = (byte)((sClockSequence & 0xbf00) >> 8);
        mBytes[8] |= 0x80;
        mBytes[9] = (byte)(sClockSequence & 0xff);
        System.arraycopy(sNodeID, 0, mBytes, 10, 6);
    }

    public long getMostSigBytes()
    {
        long msb = 0L;
        for(int i = 0; i < 8; i++)
            msb |= ((long)mBytes[i] << 56) >>> 8 * i;

        return msb;
    }

    public long getLeastSigBytes()
    {
        long lsb = 0L;
        for(int i = 8; i < 16; i++)
            lsb |= ((long)mBytes[i] << 56) >>> 8 * (i - 8);

        return lsb;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 4; i++)
        {
            sb.append(sHexChars[(mBytes[i] & 0xf0) >> 4]);
            sb.append(sHexChars[mBytes[i] & 0xf]);
        }

        sb.append(sSeparator);
        for(int i = 4; i < 6; i++)
        {
            sb.append(sHexChars[(mBytes[i] & 0xf0) >> 4]);
            sb.append(sHexChars[mBytes[i] & 0xf]);
        }

        sb.append(sSeparator);
        for(int i = 6; i < 8; i++)
        {
            sb.append(sHexChars[(mBytes[i] & 0xf0) >> 4]);
            sb.append(sHexChars[mBytes[i] & 0xf]);
        }

        sb.append(sSeparator);
        for(int i = 8; i < 10; i++)
        {
            sb.append(sHexChars[(mBytes[i] & 0xf0) >> 4]);
            sb.append(sHexChars[mBytes[i] & 0xf]);
        }

        sb.append(sSeparator);
        for(int i = 10; i < 16; i++)
        {
            sb.append(sHexChars[(mBytes[i] & 0xf0) >> 4]);
            sb.append(sHexChars[mBytes[i] & 0xf]);
        }

        return sb.toString();
    }

    private static synchronized long getCurrentTime()
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime < sLastTime)
        {
            sClockSequence++;
            if(sClockSequence > 16383)
                sClockSequence = 0;
            sNum100nsTicks = 0;
        } else
        if(currentTime == sLastTime)
        {
            sNum100nsTicks++;
            if(sNum100nsTicks >= 10000)
            {
                for(currentTime = System.currentTimeMillis(); currentTime == sLastTime; currentTime = System.currentTimeMillis())
                    try
                    {
                        Thread.sleep(1L);
                    }
                    catch(InterruptedException _ex) { }

                sNum100nsTicks = 0;
            }
        } else
        {
            sNum100nsTicks = 0;
        }
        sLastTime = currentTime;
        currentTime += sEpochOffset;
        currentTime *= 10000L;
        currentTime += sNum100nsTicks;
        return currentTime;
    }

    private static int sClockSequence;
    private static byte sNodeID[];
    private static long sLastTime = System.currentTimeMillis();
    private static int sNum100nsTicks = 0;
    private static long sEpochOffset = (new GregorianCalendar(1970, 0, 1, 0, 0, 0)).getTime().getTime() - (new GregorianCalendar(1582, 9, 15, 0, 0, 0)).getTime().getTime();
    private static char sHexChars[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static char sSeparator = '-';
    private byte mBytes[];

    static 
    {
        SecureRandom rng = new SecureRandom();
        sClockSequence = (short)rng.nextInt(16383);
        sNodeID = new byte[6];
        rng.nextBytes(sNodeID);
        sNodeID[0] |= 0x80;
    }
}
