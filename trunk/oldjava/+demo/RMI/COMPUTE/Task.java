package compute;

import java.io.Serializable;

// ����Ҫ֧��Serializable�����ܽ�object���͵�remote��
public interface Task extends Serializable {
    Object execute();
}
