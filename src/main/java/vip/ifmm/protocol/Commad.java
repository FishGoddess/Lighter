package vip.ifmm.protocol;

/**
 * 一次命令
 * 包含具体执行的命令、保存数据的 key 和 value 值等
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 19:44:19
 */
public class Commad {

    private String instruction = null;
    private String key = null;
    private String value = null;

    public Commad() {}

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Commad{" +
                "instruction='" + instruction + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
