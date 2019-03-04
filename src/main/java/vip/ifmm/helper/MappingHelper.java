package vip.ifmm.helper;

import org.springframework.stereotype.Component;
import vip.ifmm.annotation.MethodMapping;
import vip.ifmm.core.NodeManageable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责映射协议名称和方法名的帮助类
 *
 * @author Fish
 * ------> 1149062639@qq.com
 * created by 2019/03/03 20:34:34
 */
@Component
public class MappingHelper {

    // 节点管理器
    @Resource(name = "nodeManager")
    private NodeManageable nodeManager = null;

    // 映射表，将协议指令和方法映射起来
    private Map<String, Method> mappingTable = null;

    // 映射注解
    private static final Class<MethodMapping> MAPPING_ANNOTATION = MethodMapping.class;

    public MappingHelper() {}

    public NodeManageable getNodeManager() {
        return nodeManager;
    }

    public void setNodeManager(NodeManageable nodeManager) {
        this.nodeManager = nodeManager;
    }

    // 初始化
    @PostConstruct
    public void init() {
        // 从节点实现类上读取对应注解，解析生成一个对应表
        Class nodeCLass = nodeManager.getNodeClass();
        Method[] methods = nodeCLass.getDeclaredMethods();

        // 构建映射表
        buildMappingTable(methods);
    }

    // 构建映射表
    private void buildMappingTable(Method[] methods) {
        // 初始值设为方法数的两倍，防止扩容
        mappingTable = new HashMap<>(methods.length * 2);

        Method method = null;
        MethodMapping mappingAnnotation = null;
        for (int i = 0; i < methods.length; i++) {
            method = methods[i];

            // 读取注解信息，存入信息
            mappingAnnotation = method.getAnnotation(MAPPING_ANNOTATION);
            mappingTable.put(mappingAnnotation.instruction(), method);
        }
    }

    /**
     * 根据指令返回映射的方法
     *
     * @param instruction 指令
     * @return 返回映射的方法
     */
    public Method mappingTo(String instruction) {
        return mappingTable.get(instruction);
    }
}
