package cn.edu.ytkj;

/**
 * 工具类
 *
 * @author 李响
 */
public class Util {
    /**
     * 格式化时间
     *
     * @param time 秒
     * @return 字符串格式化后的数据
     */
    public static String formatTime(int time) {
        int hh = time / 3600;
        int mm = (time % 3600) / 60;
        int ss = (time % 3600) % 60;
        return (hh < 10 ? ("0" + hh) : hh) + ":" +
                (mm < 10 ? ("0" + mm) : mm) + ":" +
                (ss < 10 ? ("0" + ss) : ss);
    }
}
