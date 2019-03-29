package cn.com.fishin.protocol;

import java.util.Arrays;

/**
 * 一次命令
 * 包含具体执行的命令、保存数据的 key 和 value 值等
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 19:44:19
 */
public class Command {

    private String instruction = null;

    // 包括 key 和 value，这其实是为了方便协议的解析和方法的调用
    private String[] allArgs = null;

    public Command() {}

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String[] getAllArgs() {
        return allArgs;
    }

    public void setAllArgs(String[] allArgs) {
        this.allArgs = allArgs;
    }

    @Override
    public String toString() {
        return "Command{" +
                "instruction='" + instruction + '\'' +
                ", allArgs=" + Arrays.toString(allArgs) +
                '}';
    }
}
